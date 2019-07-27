package com.mycompany.parentheses;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

public class Checker {
	private static final Map<Character, Character> PARENTHESIS_MAP =
		new ImmutableMap
			.Builder<Character, Character>()
			.put('(', ')')
			.put('{', '}')
			.put('[', ']')
			.build();

	public boolean isValid(String expressionToEvaluate) {
		if (Strings.isNullOrEmpty(expressionToEvaluate)) {
			return true;
		}
		Deque<Character> tokenStack = new ArrayDeque<>(expressionToEvaluate.length());

		for (char value : expressionToEvaluate.toCharArray()) {
			if (PARENTHESIS_MAP.keySet().contains(value)) {
				tokenStack.push(value);
			} else if (PARENTHESIS_MAP.values().contains(value)) {
				if (!tokenStack.isEmpty() && PARENTHESIS_MAP.get(tokenStack.peek()) == value) {
					tokenStack.pop();
				} else {
					return false;
				}
			}
		}
		return tokenStack.isEmpty();
	}
}
