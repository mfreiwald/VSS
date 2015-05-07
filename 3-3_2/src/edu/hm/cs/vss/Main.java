package edu.hm.cs.vss;

import java.util.ArrayList;

public class Main {

	static final int NUMBER_PHILOSOPHERS = 20;
	static final int NUMBER_HUNGRY_PHILOSOPHERS = 0;
	static final int NUMBER_SEATS = 5;

	static final int TIME_SLEEP = 10;
	static final int TIME_EAT = 1;
	static final int TIME_MEDITATE = 5;
	static final int TIME_MEDIATE_HUNGRY = 5;
	static final int TIME_STOP_EATING = 5;

	static final int RUN_TIME = 10000;

	static boolean timeOver = false;

	public static ArrayList<Philosopher> pies = new ArrayList<>();

	public static void main(String[] args) {
		new Main();

		System.out.println("Ende Main");
	}

	Main() {
		Table table = new Table(NUMBER_SEATS);
		for (int i = 0; i < NUMBER_PHILOSOPHERS; i++) {
			Philosopher p = new Philosopher(i, table);
			pies.add(p);
			p.start();
		}
		for (int i = 0; i < NUMBER_HUNGRY_PHILOSOPHERS; i++) {
			Philosopher p = new Philosopher(NUMBER_PHILOSOPHERS + i, table,
					true);
			pies.add(p);
			p.start();
		}

		new Master(pies).start();
		new Logger(pies).start();

		try {
			Thread.sleep(RUN_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Main.timeOver = true;
	}

	public static void exit() {
		System.exit(-1);
	}
}
