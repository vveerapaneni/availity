package com.mycompany.ingester;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class TestMain {
	private static final Logger log = getLogger(TestMain.class);

	private static String concatInsuredInformation(List<InsuranceInformation> informationList) {
		return informationList
			.stream()
			.map(InsuranceInformation::toString)
			.collect(Collectors.joining("\n"));
	}

	public static void main(String[] args) {
		Ingester ingester = new Ingester();
		final String inputOutputDirectory = "src/main/resources/com/mycompany/ingester/";
		final String inputFileName = "enrollment-output.txt";
		String inputFileNameWithFullPath = String.format("%s%s", inputOutputDirectory, inputFileName);
		Map<String, List<InsuranceInformation>> result =
			ingester.ingestAndSplitByCompany(inputFileNameWithFullPath);

		result.forEach((key, value) -> log.info("{}", concatInsuredInformation(value)));
		result.forEach((key, value) -> ingester.writeContentsToFile(inputOutputDirectory, key, value));
	}
}
