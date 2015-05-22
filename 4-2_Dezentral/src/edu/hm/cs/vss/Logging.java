package edu.hm.cs.vss;

public class Logging {

	
	public static void log(Logger logger, String message) {
		System.out.println("[" + logger + "] " + message);
	}
	
	public static void log(String logger, String message) {
		System.out.println("[(" + logger + ")] " + message);
	}
}
