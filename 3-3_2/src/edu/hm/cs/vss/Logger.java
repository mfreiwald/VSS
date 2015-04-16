package edu.hm.cs.vss;

import java.util.ArrayList;

public class Logger {
	
	public static boolean enabled = true;
	
	public static void log(String s) {
		if(Logger.enabled) {
			System.out.println(s);
			//showStats(Main.pies);
		}
	}
	
	
	public static synchronized void showStats(ArrayList<Philosopher> pies) {
		for(Philosopher pi: pies) {
			System.out.print(pi+" "+pi.state+"\t");
		}
		System.out.println();
		System.out.println();
	}
}
