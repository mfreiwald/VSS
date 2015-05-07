package edu.hm.cs.vss;

import java.util.ArrayList;
import java.util.List;

public class Logger extends Thread {

	public static boolean enabled = false;

	public static synchronized void log(String s) {
		if (Logger.enabled) {
			showStats(Main.pies);
		}
	}

	public static synchronized void log() {
		showStats(Main.pies);
	}

	public static synchronized void showStats(List<Philosopher> pies) {
		String output = "";
		for (Philosopher pi : pies) {
			output += pi.nr + " " + pi.getSeat() + "\t\t";
		}

		output += "\n";

		for (Philosopher pi : pies) {
			output += pi.state + "\t\t";
		}
		output += "\n\n";

		System.out.println(output);
	}

	private ArrayList<Philosopher> philosophers;

	public Logger(ArrayList<Philosopher> philosophers) {
		this.philosophers = philosophers;
	}

	public void run() {
		while (!Main.timeOver) {

			Logger.showStats(this.philosophers);

			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (Philosopher p : this.philosophers) {
			try {
				p.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Logger.showStats(this.philosophers);
		System.out.println("Ende Logger");

		String output = "";
		for (Philosopher p : this.philosophers) {
			output += p.nr + "\t\t";
		}
		output += "\n";

		long timesEating = 0;
		for (Philosopher p : this.philosophers) {
			output += p.getTimesEating() + "\t\t";
			timesEating += p.getTimesEating();
		}
		output += "\n";
		System.out.println(output);
		System.out.println("Durchschnitt: "
				+ (timesEating / this.philosophers.size()));
		System.out.println("Gesamt: " + timesEating);

	}
}
