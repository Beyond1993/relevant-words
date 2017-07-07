package com.moogsoft.interview;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
/**
 * Filter class 
 * @author wayne
 *　@date Jun/10/2017
 * @version 1.0.0
 */
public class Filter {
	private Set < String > set;
	/**
	 * Constructor.
	 */
	public Filter() {
		set = new HashSet < String > ();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("filter.txt"));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}

		String line = null;

		try {
			while ((line = br.readLine()) != null) {
				set.add(line);
			}
		} catch(IOException e) {

			e.printStackTrace();
		}

	}

	/**
 	 * Returns true if the input word or phrase is valid.
 	 * @param key word or phrase
 	 * @return true if the input word or phrase is valid.
 	 */
	public boolean isValid(String key) {
		if (key.length() <= 2) {
			return false;
		}
		String[] keys = key.split("\\s+");
		for (int i = 0; i < keys.length; i++) {

			if (set.contains(keys[i])) {
				return false;
			}
		}
		return true;
	}
	
}