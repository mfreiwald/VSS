package edu.hm.cs.vss;

import java.util.ArrayList;

public class Logger {
	
	public static boolean enabled = true;
	
	public static synchronized void log(String s) {
		if(Logger.enabled) {
			System.out.println(s);
			//showStats(Main.pies);
		}
	}
	
	public static synchronized void log() {
		//showStats(Main.pies);
	}
	
	
	public static synchronized void showStats(ArrayList<Philosopher> pies) {
		for(Philosopher pi: pies) {
			System.out.print(pi.nr+" "+pi.getSeat()+"\t\t");
		}
		System.out.println();
		for(Philosopher pi: pies) {
			System.out.print(pi.state+"\t\t");
		}
		System.out.println();
		System.out.println();
	}
}
