package edu.hm.cs.vss;

import java.util.ArrayList;

public class Main {

	static final int NUMBER_PHILOSOPHERS = 7;
	static final int NUMBER_SEATS = 4;
	
	static final int TIME_SLEEP = 8000;
	static final int TIME_EAT = 2000;
	static final int TIME_MEDITATE = 4000;
	
	public static ArrayList<Philosopher> pies = new ArrayList<>();
	
	public static void main(String[] args) {
		
		Table table = new Table(NUMBER_SEATS);
		for(int i=0; i<NUMBER_PHILOSOPHERS; i++) {
			Philosopher p = new Philosopher(i, table);
			pies.add(p);
			p.start();
		}
	}
}
