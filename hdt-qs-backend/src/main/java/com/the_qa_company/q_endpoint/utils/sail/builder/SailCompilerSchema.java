package com.the_qa_company.q_endpoint.utils.sail.builder;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Schema describing the model compiler nodes,
 * {@literal @prefix mdlc: <http://the-qa-company.com/modelcompiler/>}
 * @author Antoine Willerval
 */
public class SailCompilerSchema {
	private static final ValueFactory VF = SimpleValueFactory.getInstance();
	private static final Map<IRI, String> DESC = new HashMap<>();
	/**
	 * {@literal @prefix mdlc: <http://the-qa-company.com/modelcompiler/>}
	 */
	public static final String COMPILER_NAMESPACE = "http://the-qa-company.com/modelcompiler/";
	/**
	 * mdlc:main
	 */
	public static final IRI MAIN = iri("main", "The main node, the start point of the model");
	/**
	 * mdlc:node
	 */
	public static final IRI NODE = iri("node", "Describe a node");
	/**
	 * mdlc:filter
	 */
	public static final IRI TYPE = iri("type", "Describe the type of a node");
	/**
	 * mdlc:paramKey
	 */
	public static final IRI PARAM_KEY = iri("paramKey", "Describe a parameter key");
	/**
	 * mdlc:paramValue
	 */
	public static final IRI PARAM_VALUE = iri("paramValue", "Describe a parameter value");
	/**
	 * mdlc:dirParam
	 */
	public static final IRI PARSED_STRING_PARAM = iri("parsedStringParam", "Describe a parsed string param");
	/**
	 * mdlc:parsedString
	 */
	public static final IRI PARSED_STRING_DATATYPE = iri("parsedString", "Describe a parsed string datatype");
	/**
	 * mdlc:paramLink
	 */
	public static final IRI PARAM_LINK = iri("paramLink", "Describe a node param linked sails");
	/**
	 * mdlc:paramFilter
	 */
	public static final IRI PARAM_FILTER = iri("paramFilter", "Describe a node param filter");
	/**
	 * mdlc:paramFilterAnd
	 */
	public static final IRI PARAM_FILTER_AND = iri("paramFilterAnd", "Describe a node param filter op AND");
	/**
	 * mdlc:paramFilterOr
	 */
	public static final IRI PARAM_FILTER_OR = iri("paramFilterOr", "Describe a node param filter op OR");
	/**
	 * mdlc:predicateFilter
	 */
	public static final IRI PARAM_FILTER_TYPE_PREDICATE = iri("predicateFilter", "Describe the type predicate Filter");
	/**
	 * mdlc:languageFilter
	 */
	public static final IRI PARAM_FILTER_TYPE_LANGUAGE = iri("languageFilter", "Describe the type language Filter");
	/**
	 * mdlc:languageFilterLang
	 */
	public static final IRI PARAM_FILTER_TYPE_LANGUAGE_LANG = iri("languageFilterLang", "Describe the type language Filter param");
	/**
	 * mdlc:acceptNoLanguageLiterals
	 */
	public static final IRI PARAM_FILTER_TYPE_LANGUAGE_NO_LANG_LIT = iri("acceptNoLanguageLiterals", "Describe the type language Filter param");
	/**
	 * mdlc:typeFilter
	 */
	public static final IRI PARAM_FILTER_TYPE_TYPE = iri("typeFilter", "Describe the predicate type type");
	/**
	 * mdlc:typeFilter
	 */
	public static final IRI PARAM_FILTER_TYPE_TYPE_PREDICATE = iri("typeFilterPredicate", "Describe the predicate type type param type predicate");
	/**
	 * mdlc:typeFilterObject
	 */
	public static final IRI PARAM_FILTER_TYPE_TYPE_OBJECT = iri("typeFilterObject", "Describe the predicate type type param object");
	/**
	 * mdlc:typeFilterLuceneExp
	 */
	public static final IRI PARAM_FILTER_TYPE_LUCENE_EXP = iri("typeFilterLuceneExp", "Describe the lucene exp predicate type");
	/**
	 * mdlc:filterNode
	 */
	public static final IRI FILTER_TYPE = iri("filterNode", "Describe the type Filter");
	/**
	 * mdlc:multiFilterNode
	 */
	public static final IRI MULTI_FILTER_TYPE = iri("multiFilterNode", "Describe the type Multi-Filter");
	/**
	 * mdlc:linkedSailNode
	 */
	public static final IRI LINKED_SAIL_TYPE = iri("linkedSailNode", "Describe the type Linked sail");
	/**
	 * mdlc:luceneNode
	 */
	public static final IRI LUCENE_TYPE = iri("luceneNode", "Describe the type Lucene");
	/**
	 * mdlc:luceneLang
	 */
	public static final IRI LUCENE_TYPE_LANG = iri("luceneLang", "Describe the Lucene language(s)");
	/**
	 * mdlc:luceneEvalMode
	 */
	public static final IRI LUCENE_TYPE_EVAL_MODE = iri("luceneEvalMode", "Describe the Lucene evaluation mode");
	/**
	 * mdlc:luceneParam
	 */
	public static final IRI LUCENE_TYPE_PARAM = iri("luceneParam", "Describe a Lucene param");
	/**
	 * mdlc:luceneReindexQuery
	 */
	public static final IRI LUCENE_TYPE_REINDEX_QUERY = iri("luceneReindexQuery", "Describe the Lucene reindex query");
	/**
	 * mdlc:dirLocation
	 */
	public static final IRI DIR_LOCATION = iri("dirLocation", "Describe a directory");
	/**
	 * mdlc:storageMode
	 */
	public static final IRI STORAGE_MODE = iri("storageMode", "The storage mode");
	/**
	 * mdlc:hybridStoreStorage
	 */
	public static final IRI HYBRIDSTORE_STORAGE = iri("hybridStoreStorage", "The storage mode hybrid store");
	/**
	 * mdlc:nativeStoreStorage
	 */
	public static final IRI NATIVESTORE_STORAGE = iri("nativeStoreStorage", "The storage mode native store");
	/**
	 * mdlc:memoryStoreStorage
	 */
	public static final IRI MEMORYSTORE_STORAGE = iri("memoryStoreStorage", "The storage mode memory store");
	private static final Set<IRI> STORAGES = Set.of(HYBRIDSTORE_STORAGE, NATIVESTORE_STORAGE, MEMORYSTORE_STORAGE);
	/**
	 * mdlc:rdfStoreSplit
	 */
	public static final IRI RDF_STORE_SPLIT_STORAGE = iri("rdfStoreSplit", "The storage load split update count");
	/**
	 * mdlc:hdtPassMode
	 */
	public static final IRI HDT_PASS_MODE = iri("hdtPassMode", "The mode to parse the Triple flux");
	/**
	 * mdlc:hdtOnePassMode
	 */
	public static final IRI HDT_ONE_PASS_MODE = iri("hdtOnePassMode", "The mode to parse the Triple flux in one pass, reduce disk usage");
	/**
	 * mdlc:hdtTwoPassMode
	 */
	public static final IRI HDT_TWO_PASS_MODE = iri("hdtTwoPassMode", "The mode to parse the Triple flux in two passes, reduce time usage");

	private static final Set<IRI> PASS_MODES = Set.of(HDT_ONE_PASS_MODE, HDT_TWO_PASS_MODE);

	private static IRI iri(String name, String desc) {
		IRI iri = VF.createIRI(COMPILER_NAMESPACE + name);
		String old = DESC.put(iri, desc);
		assert old == null : "Iri already registered: " + iri;
		return iri;
	}

	/**
	 * throw an exception if the iri isn't a HDT pass mode
	 * @param mode the iri
	 * @return the iri
	 * @throws SailCompiler.SailCompilerException if the iri isn't a pass mode
	 */
	public static IRI throwIfNotPassMode(IRI mode) throws SailCompiler.SailCompilerException {
		if (PASS_MODES.contains(mode)) {
			return mode;
		}
		throw new SailCompiler.SailCompilerException(mode + " isn't a pass mode!");
	}

	/**
	 * throw an exception if the iri isn't a storage mode
	 * @param mode the iri
	 * @return the iri
	 * @throws SailCompiler.SailCompilerException if the iri isn't a storage mode
	 */
	public static IRI throwIfNotStorageMode(IRI mode) throws SailCompiler.SailCompilerException {
		if (STORAGES.contains(mode)) {
			return mode;
		}
		throw new SailCompiler.SailCompilerException(mode + " isn't a storage mode!");
	}

	/**
	 * @return a description map indexed by IRI
	 */
	public static Map<IRI, String> getDescriptions() {
		return Collections.unmodifiableMap(DESC);
	}

	private SailCompilerSchema() {
	}
}
