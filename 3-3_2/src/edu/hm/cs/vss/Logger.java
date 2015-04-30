package edu.hm.cs.vss;

import java.util.ArrayList;
import java.util.List;

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
	
	static int errs = 0;
	public static synchronized void err(String s) {
		errs++;
		System.err.println(s);
		if(errs > 5) {
			Main.exit();
		}
	}
	
	
	public static synchronized void showStats(List<Philosopher> pies) {
		
		boolean print = false;
		String output = "";
		for(Philosopher pi: pies) {
			//System.out.print(pi.nr+" "+pi.getSeat()+"\t\t");
			output += pi.nr+" "+pi.getSeat()+"\t\t";
		}
		
		//System.out.println();
		output += "\n";

		for(Philosopher pi: pies) {
			output += pi.state+"\t\t";
			if(pi.getHasToStop()) {
				//System.out.println(pi.state+"\t\t");
				print = true;
			}
		}
		//System.out.println();
		//System.out.println();
		output += "\n\n";
		
		
		//if(print) {
			System.out.println(output);
		//}
	}
	
	
	private ArrayList<Philosopher>  philosophers;

	public Logger(ArrayList<Philosopher> philosophers) {
		this.philosophers = philosophers;
	}
	
	public void run() {
		while(!Main.timeOver) {
			
			Logger.showStats(this.philosophers);
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(Philosopher p: this.philosophers) {
			try {
				p.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Logger.showStats(this.philosophers);
		System.out.println("Ende Logger");
		
		String output = "";
		for(Philosopher p: this.philosophers) {
			output += p.nr+"\t\t";
		}
		output += "\n";
		
		long timesEating = 0;
		for(Philosopher p: this.philosophers) {
			output += p.getTimesEating()+"\t\t";
			timesEating += p.getTimesEating();
		}
		output += "\n";
		System.out.println(output);
		System.out.println("Gesamt: "+timesEating);
		
	}
}
