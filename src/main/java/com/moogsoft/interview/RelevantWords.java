package com.moogsoft.interview;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * RelevantWords class
 * @author wayne
 * @date Jun/10/2017
 * @version 1.0.0
 */
public final class RelevantWords {

	static private final String PREFIX_URL = "http://www.imdb.com/title/tt2788710/reviews?start=";

	/**
	 * END_INDEX, end index of review page 
	 */
	private final int END_INDEX = 86;
	/**
	 * map, key is word or phrase, value is the frequency, the top 1 relevant word has the highest frequency.
	 */
	private ConcurrentHashMap < String,
	Integer > map;

	/**
	 * filter, remove some useless information.
	 */

	private Filter filter;
	/**
	 * Constructor. 
	 */
	public RelevantWords() {
		map = new ConcurrentHashMap < String,
		Integer > ();
		filter = new Filter();
	}
	/**
	 * Print the top 'X' relevant words.
	 * @param X number of relevant words.
	 */
	public void getReleventWords(int X) {
		Thread[] threads = new Thread[END_INDEX];
		for (Integer i = 0; i < END_INDEX; i++) {
			HttpClient reader = new HttpClient(i * 10);
			threads[i] = new Thread(reader);
			try {
				Thread.sleep(80);
				threads[i].start();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}

		}

		for (int i = 0; i < END_INDEX; i++) {
			try {
				threads[i].join();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		print(map, X);
	}
	/**
	 * Associates the specified words of text to the map.
	 * @param line, text get from the Internet.
	 */
	private void put(String line) {
		if (line == null || line.isEmpty()) {
			throw new IllegalArgumentException("The input is null or empty!");
		}
		line = line.replaceAll("[^a-zA-Z]", " ");

		String[] words = line.split("\\s+");
		int len = words.length;
		String[] keys = new String[3];
		Arrays.fill(keys, "");
		for (int i = 0; i < len; i++) {
			//make up the phrase with adjacent two or three words.
			keys[0] = words[i];
			if (i < len - 1) {
				keys[1] = keys[0] + " " + words[i + 1];
			}
			if (i < len - 2) {
				keys[2] = keys[1] + " " + words[i + 2];
			}

			for (int j = 0; j < 3; j++) {
				if (filter.isValid(keys[j].toLowerCase())) {
					map.put(keys[j].toLowerCase(), map.getOrDefault(keys[j], 0) + 1);
				}
				keys[j] = "";
			}

		}
	}
	/**
	 * Print the result in the standard output.
	 * @param map ConcurrentHashMap <Word, Count>
	 * @param X　number of relevant words.
	 */
	private void print(Map < String, Integer > map, int X) {
		if (map == null || map.isEmpty()) {
			throw new IllegalArgumentException("paramter map can not be null or empty");
		}
		List < Entry < String,
		Integer >> list = new LinkedList < Entry < String,
		Integer >> (map.entrySet());

		Collections.sort(list, new Comparator < Entry < String, Integer >> () {
			public int compare(Entry < String, Integer > o1, Entry < String, Integer > o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		int len = list.size();
		X = X < len ? X: len;
		System.out.println("The top " + X + " relevant words or phrase are");
		for (int i = 0; i < X; i++) {
			Entry < String,
			Integer > entry = list.get(i);
			System.out.println((i + 1) + " : " + entry.getKey() + " , Frequency : " + entry.getValue());
		}

	}
	/**
	 * HttpCient class, sent get request to the URL.
	 * @author wayne
	 * @date Jun/10/2017
	 * @version 1.0.0
	 */
	private class HttpClient implements Runnable {
		private String url = "";
		private int id;
		/**
		 * Constructor.
		 * @param i thread id
		 */
		public HttpClient(int i) {
			this.id = i;
			this.url = PREFIX_URL + String.valueOf(i);
		}

		public void run() {
			try {
				System.out.println("Thread " + this.id + " running!");
				Document doc = Jsoup.connect(this.url).userAgent("Mozilla").get();
				Element newHeadlines = doc.getElementById("tn15content");
				Elements p = newHeadlines.getElementsByTag("p");
				for (Element x: p) {
					String v = x.text();
					if (v != null && !v.isEmpty() && !v.startsWith("***")) {
						put(v);
					}
				}
			} catch(Exception e) {

				e.printStackTrace();
			}

		}
	}

}