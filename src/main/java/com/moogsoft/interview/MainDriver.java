package com.moogsoft.interview;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * MainDriver
 * @author wayne
 * @date Jun/10/2017
 * @version 1.0.0
 */
public class MainDriver {
	public static void main(String[] args) throws IOException {

		RelevantWords hc = new RelevantWords();
		System.out.println("Entry the number: 1 ~ 20");
		Scanner sc = new Scanner(System. in );
		int X = 1;

		while (true) {
			try {
				X = Integer.parseInt(sc.nextLine());
				if (X >= 1 && X <= 20) {
					break;
				} else {
					System.out.println("Please entry the number between 1 ~ 20:");
				}
			} catch(NumberFormatException nfe) {
				System.out.println("Please entry the number between 1 ~ 20:");
			}
		}
		hc.getReleventWords(X);
	}
}