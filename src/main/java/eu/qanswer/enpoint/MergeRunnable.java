package eu.qanswer.enpoint;

import org.apache.zookeeper.data.Stat;
import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailRepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.SailConnection;
import org.rdfhdt.hdt.enums.RDFNotation;
import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.exceptions.ParserException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.options.HDTSpecification;
import org.rdfhdt.hdt.rdf4j.HybridQueryPreparer;
import org.rdfhdt.hdt.rdf4j.HybridStore;
import org.rdfhdt.hdt.rdf4j.HybridTripleSource;
import org.rdfhdt.hdt.rdf4j.utility.HDTProps;
import org.rdfhdt.hdt.rdf4j.utility.IRIConverter;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MergeRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MergeRunnable.class);
    private RepositoryConnection nativeStoreConnection;

    private String hdtIndex;
    private String locationHdt;

    private HDT hdt;
    private HybridStore hybridStore;

    public MergeRunnable(String locationHdt, HybridStore hybridStore) {
        this.locationHdt = locationHdt;
        this.hdtIndex = locationHdt+"index.hdt";
        this.nativeStoreConnection = hybridStore.getRepoConnection();
        this.hybridStore = hybridStore;
        this.hdt = hybridStore.getHdt();
    }


    public synchronized void run() {
        hybridStore.isMerging = true;
        try {
            String rdfInput = locationHdt+"temp.nt";
            String hdtOutput = locationHdt+"temp.hdt";

            // diff hdt indexes...
            diffIndexes(hdtIndex,locationHdt+"triples-delete.arr");


            // dump all triples in native store
            writeTempFile(nativeStoreConnection,rdfInput);
            // create the hdt index for the temp dump
            createHDTDump(rdfInput,hdtOutput);
            // cat the original index and the temp index
            catIndexes(hdtIndex,hdtOutput);
            // empty native store
            emptyNativeStore();

            Files.delete(Paths.get(rdfInput));
            Files.delete(Paths.get(hdtOutput));
            Files.deleteIfExists(Paths.get(locationHdt+"triples-delete.arr"));


            this.hdt = HDTManager.mapIndexedHDT(hdtIndex,new HDTSpecification());


            this.hybridStore.setHdtProps(new HDTProps(hdt));
            this.hybridStore.setHdt(this.hdt);
            this.hybridStore.initDeleteArray();
            this.hybridStore.isMerging = false;
            this.nativeStoreConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void diffIndexes(String hdtInput1,String bitArray) {

        String hdtOutput = locationHdt+"new_index.hdt";
        File filex = new File(hdtOutput);
        File theDir = new File(filex.getAbsolutePath() + "_tmp");
        theDir.mkdirs();
        try {
            HDT hdt = HDTManager.diffHDTBit(theDir.getAbsolutePath(),hdtInput1,bitArray,new HDTSpecification(),null);
            hdt.saveToHDT(hdtOutput,null);
            hdt = HDTManager.indexedHDT(hdt,null);

            Files.deleteIfExists(Paths.get(hdtIndex));
            Files.deleteIfExists(Paths.get(hdtIndex+".index.v1-1"));
            File file = new File(hdtOutput);
            boolean renamed = file.renameTo(new File(hdtIndex));
            File indexFile = new File(hdtOutput+".index.v1-1");
            boolean renamedIndex = indexFile.renameTo(new File(hdtIndex+".index.v1-1"));
            if(renamed && renamedIndex) {
                logger.info("Replaced indexes successfully after diff");
                hdt.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void catIndexes(String hdtInput1,String hdtInput2) throws IOException {
        String hdtOutput = locationHdt+"new_index.hdt";
        HDT hdt = null;
        HDTSpecification spec = new HDTSpecification();
        try {
            File file = new File(hdtOutput);
            File theDir = new File(file.getAbsolutePath()+"_tmp");
            theDir.mkdirs();
            String location = theDir.getAbsolutePath()+"/";
            hdt = HDTManager.catHDT(location,hdtInput1, hdtInput2 , spec,null);

            StopWatch sw = new StopWatch();
            hdt.saveToHDT(hdtOutput, null);
            logger.info("HDT saved to file in: "+sw.stopAndShow());
            Files.delete(Paths.get(location+"dictionary"));
            Files.delete(Paths.get(location+"triples"));
            theDir.delete();

            hdt = HDTManager.indexedHDT(hdt,null);

            Files.deleteIfExists(Paths.get(hdtIndex));
            Files.deleteIfExists(Paths.get(hdtIndex+".index.v1-1"));
            boolean renamed = file.renameTo(new File(hdtIndex));
            File indexFile = new File(hdtOutput+".index.v1-1");
            boolean renamedIndex = indexFile.renameTo(new File(hdtIndex+".index.v1-1"));
            if(renamed && renamedIndex) {
                logger.info("Replaced indexes successfully after cat");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(hdt != null)
                hdt.close();
        }

    }
    private void createHDTDump(String rdfInput,String hdtOutput){


        String baseURI = "file://"+rdfInput;
        RDFNotation notation = RDFNotation.guess(rdfInput);
        HDTSpecification spec = new HDTSpecification();

        try {
            StopWatch sw = new StopWatch();
            HDT hdt = HDTManager.generateHDT(new File(rdfInput).getAbsolutePath(), baseURI,RDFNotation.NTRIPLES , spec, null);
            logger.info("File converted in: "+sw.stopAndShow());
            hdt.saveToHDT(hdtOutput, null);
            logger.info("HDT saved to file in: "+sw.stopAndShow());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }
    private void writeTempFile(RepositoryConnection connection,String file){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            RDFWriter writer = Rio.createWriter(RDFFormat.NTRIPLES, out);
            RepositoryResult<Statement> repositoryResult =
                    connection.getStatements(null,null,null,false);
            writer.startRDF();
            IRIConverter iriConverter = new IRIConverter(this.hdt);
            while (repositoryResult.hasNext()) {
                Statement stm = repositoryResult.next();
                Statement stmConverted = this.hybridStore.getValueFactory().createStatement(
                  iriConverter.getIRIHdtSubj(stm.getSubject()),
                  iriConverter.getIRIHdtPred(stm.getPredicate()),
                        iriConverter.getIRIHdtObj(stm.getObject())
                );
                writer.handleStatement(stmConverted);
            }
            writer.endRDF();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void emptyNativeStore(){
        nativeStoreConnection.clear();
    }
}