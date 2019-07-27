package com.mycompany.ingester;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

public class Ingester {
	private static final Splitter COMMA_SPLITTER = Splitter.on(',');
	private static final Logger log = LoggerFactory.getLogger(Ingester.class);

	public Map<String, List<InsuranceInformation>> ingestAndSplitByCompany(String fileWithFullPath) {
		try {
			List<InsuranceInformation> insuranceInformationList = Files
				.lines(Paths.get(fileWithFullPath))
				.map(this::parse)
				.collect(toList());

			Map<String, List<InsuranceInformation>> groupByCompanyName =
				insuranceInformationList
					.stream()
					.collect(groupingBy(InsuranceInformation::getCompanyName));

			return groupByCompanyName
				.entrySet()
				.stream()
				.map(item -> new SimpleEntry<>(item.getKey(), removeDuplicatesAndSort(item.getValue())))
				.collect(toMap(Entry::getKey, Entry::getValue));
		} catch (IOException e) {
			log.error("Unable to parse file {}", fileWithFullPath);
		}

		return Collections.emptyMap();
	}

	/**
	 * Parse LineContents and split into individual tokens
	 */
	private InsuranceInformation parse(String lineContents) {
		List<String> parsedTokens = COMMA_SPLITTER.splitToList(lineContents);
		return InsuranceInformation
			.newBuilder()
			.withUserId(parsedTokens.get(0))
			.withInsuredName(parsedTokens.get(1))
			.withVersion(Integer.valueOf(parsedTokens.get(2)))
			.withCompanyName(parsedTokens.get(3))
			.build();
	}

	/**
	 * Remove duplicates as identified by UserId and return distinct UserId's that have max version number
	 */
	private List<InsuranceInformation> removeDuplicatesAndSort(List<InsuranceInformation> insuranceInformationList) {
		List<InsuranceInformation> result = insuranceInformationList
			.stream()
			.collect(
				collectingAndThen(
					toMap(
						item -> Collections.singletonList(item.getUserId()),
						item -> item,
						maxBy(comparing(InsuranceInformation::getVersion))),
					item -> new ArrayList<>(item.values()))
			);
		result.sort(comparing(InsuranceInformation::getNameFormattedByLastAndFirst));
		return result;
	}

	public void writeToFileContents(String outputDirectory,
		String companyName,
		List<InsuranceInformation> insuranceInformationList) {
		String outputFileWithFullName = String.format("%s%s", outputDirectory, companyName);
		Paths.get(outputFileWithFullName);

		File outputFile = new File(outputFileWithFullName);
		try (PrintWriter printWriter = new PrintWriter(outputFile)) {
			insuranceInformationList
				.stream()
				.map(item -> String.format(
					"%s,%s,%d,%s",
					item.getUserId(),
					item.getNameFormattedByLastAndFirst(),
					item.getVersion(),
					item.getCompanyName()))
				.forEach(printWriter::println);
		} catch (FileNotFoundException e) {
			log.error("Unable to write to file {}", outputFileWithFullName);
		}
	}
}
