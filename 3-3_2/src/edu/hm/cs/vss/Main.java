package edu.hm.cs.vss;

import java.util.ArrayList;

public class Main {

	static final int NUMBER_PHILOSOPHERS = 9;
	static final int NUMBER_HUNGRY_PHILOSOPHERS = 1;
	static final int NUMBER_SEATS = 4;
	
	static final int TIME_SLEEP = 800;
	static final int TIME_EAT = 200;
	static final int TIME_MEDITATE = 400;
	static final int TIME_MEDIATE_HUNGRY = 300;
		
	public static ArrayList<Philosopher> pies = new ArrayList<>();
	
	public static void main(String[] args) {
		
		
		Table table = new Table(NUMBER_SEATS);
		for(int i=0; i<NUMBER_PHILOSOPHERS; i++) {
			Philosopher p = new Philosopher(i, table);
			pies.add(p);
			p.start();
		}
		for(int i=0; i<NUMBER_HUNGRY_PHILOSOPHERS; i++) {
			Philosopher p = new Philosopher(NUMBER_PHILOSOPHERS+i, table, true);
			pies.add(p);
			p.start();
		}
		new Logger(pies).start();

	}
}
