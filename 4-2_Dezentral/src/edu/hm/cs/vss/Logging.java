package edu.hm.cs.vss;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logging {

	private static List<Logger> disabledLoggers = new ArrayList<>();
	
	public static void log(Logger logger, String message) {
		if(!disabledLoggers.contains(logger)) {
			System.out.println(timestamp() + " [" + logger + "] " + message);
		}
	}
	
	public static void log(String logger, String message) {
		System.out.println(timestamp() + " [(" + logger + ")] " + message);
	}
	
	public static String timestamp() {		
		return new SimpleDateFormat("HH:mm:ss.S").format(new Date());
	}
	
	public static String timestampMinutes() {		
		return new SimpleDateFormat("mm:ss").format(new Date());
	}
	
	public static void disableLogger(Logger logger) {
		disabledLoggers.add(logger);
	}
	
	public static void enableLogger(Logger logger) {
		disabledLoggers.remove(logger);
	}
	
}
