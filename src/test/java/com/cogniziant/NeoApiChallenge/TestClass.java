package com.cogniziant.NeoApiChallenge;

import org.junit.Test;

public class TestClass {
//write a method to display duplicate characters in the string
	


	public static void printDup(String s) {
		String dup = "";
		for (int i = 0; i < s.length() - 1; i++) {
			for (int j = i + 1; j < s.length(); j++) {
				if (s.charAt(j) == s.charAt(i)) {
					dup = dup + s.charAt(j);
				}
			}
		}
		System.out.println(dup);
	}

	public static void main(String[] args) {
		String str = "amazon";
		printDup(str);
	}
}
