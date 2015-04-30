package edu.hm.cs.vss;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Logger extends Thread {
	
	public static boolean enabled = false;
	
	public static synchronized void log(String s) {
		if(Logger.enabled) {
			//System.out.println(s);
			showStats(Main.pies);
		}
	}
	
	public static synchronized void log() {
		showStats(Main.pies);
	}
	
	
	public static synchronized void showStats(List<Philosopher> pies) {
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
	
	
	private ArrayList<Philosopher>  philosophers;

	public Logger(ArrayList<Philosopher> philosophers) {
		this.philosophers = philosophers;
	}
	
	public void run() {
		while(true) {
			
			Logger.showStats(this.philosophers);
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
