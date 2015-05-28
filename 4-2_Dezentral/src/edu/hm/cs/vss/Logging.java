package edu.hm.cs.vss;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {

	
	public static void log(Logger logger, String message) {
		System.out.println(timestamp() + " [" + logger + "] " + message);
	}
	
	public static void log(String logger, String message) {
		System.out.println(timestamp() + " [(" + logger + ")] " + message);
	}
	
	public static String timestamp() {		
		return new SimpleDateFormat("HH:mm:ss.S").format(new Date());
	}
	
}
