package com.the_qa_company.qendpoint.core.hdt;

import com.the_qa_company.qendpoint.core.hdt.impl.converter.FSDToMSDConverter;
import com.the_qa_company.qendpoint.core.hdt.impl.converter.MSDToFSDConverter;
import com.the_qa_company.qendpoint.core.listener.ProgressListener;
import com.the_qa_company.qendpoint.core.options.HDTOptions;
import com.the_qa_company.qendpoint.core.options.HDTOptionsKeys;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Class to convert a current HDT on disk to another one with another dict type
 *
 * @author Antoine Willerval
 */
public interface Converter {
	/**
	 * create a Converter for this HDT to a new type
	 *
	 * @param origin  origin HDT
	 * @param newType new type {@link Converter}
	 * @return Converter
	 */
	static Converter newConverter(HDT origin, String newType) {
		Objects.requireNonNull(origin, "origin can't be null!");

		String oldType = origin.getDictionary().getType();

		switch (oldType) {
		case HDTVocabulary.DICTIONARY_TYPE_MULT_SECTION, HDTOptionsKeys.DICTIONARY_TYPE_VALUE_MULTI_OBJECTS -> {
			if (newType.equals(HDTVocabulary.DICTIONARY_TYPE_FOUR_SECTION)) {
				return new MSDToFSDConverter();
			}
		}
		case HDTVocabulary.DICTIONARY_TYPE_FOUR_SECTION -> {
			switch (newType) {
			case HDTVocabulary.DICTIONARY_TYPE_MULT_SECTION, HDTOptionsKeys.DICTIONARY_TYPE_VALUE_MULTI_OBJECTS -> {
				return new FSDToMSDConverter();
			}
			}
		}
		}

		throw new IllegalArgumentException("Can't find Converter for types " + oldType + " => " + newType);
	}

	/**
	 * @return future type
	 */
	String getDestinationType();

	/**
	 * convert the HDT at location origin to a new HDT at location destination
	 *
	 * @param origin      origin HDT
	 * @param destination destination HDT
	 */
	void convertHDTFile(HDT origin, Path destination, ProgressListener listener, HDTOptions options) throws IOException;

	/**
	 * convert the HDT at location origin to a new HDT at location destination
	 *
	 * @param origin      origin HDT
	 * @param destination destination HDT
	 */
	default void convertHDTFile(HDT origin, String destination, ProgressListener listener, HDTOptions options)
			throws IOException {
		convertHDTFile(origin, Path.of(destination), listener, options);
	}
}
