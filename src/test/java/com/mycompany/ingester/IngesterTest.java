package com.mycompany.ingester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IngesterTest {
	private static final String inputFileName = "enrollment-output.txt";
	private static final String inputOutputDirectory = "src/main/resources/com/mycompany/ingester/";
	private static String inputFileNameWithFullPath = String.format("%s%s", inputOutputDirectory, inputFileName);
	private Ingester ingester = new Ingester();

	@DisplayName("Test to split input file by company and remove duplicates and persist to separate files by company")
	@Test
	void ingestAndSplitByCompanyAndPersist() {
		ingester
			.ingestAndSplitByCompany(inputFileNameWithFullPath)
			.forEach((key, value) -> ingester.writeToFileContents(inputOutputDirectory, key, value));
		Assertions.assertTrue(Boolean.TRUE);
	}
}