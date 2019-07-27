package com.mycompany.parentheses;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParenthesesParserTest {
	private final Checker checker = new Checker();

	private static Stream<Arguments> testData() {
		return Stream.of(
			Arguments.of("", true),
			Arguments.of(null, true),
			Arguments.of("(kk)()()()oii", true),
			Arguments.of("(kk)(()()", false),
			Arguments.of("a*(b+c)-(d*e)", true),
			Arguments.of("(a*(b-c)*{d+e}+[r++1])", true),
			Arguments.of("(a*(b-c)*{d+e}) + (e", false),
			Arguments.of("(a+b*(c-d)-(e-f/(g+h*(k-i)/(l-j+k + (e", false)
		);
	}

	@ParameterizedTest
	@MethodSource("testData")
	void isValid(String expressionToEvaluate,
		boolean result) {
		Assertions.assertEquals(checker.isValid(expressionToEvaluate), result);
	}
}