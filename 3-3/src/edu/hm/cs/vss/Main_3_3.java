package edu.hm.cs.vss;

public class Main_3_3 {

	static int NUMBER_PHILOSOPHERS = 5;
	static int NUMBER_SEATS = 5;
	
	public static void main(String[] args) {
			
		for(int i=0; i<NUMBER_PHILOSOPHERS; i++) {
			Philosopher p = new Philosopher(i+1);
			p.start();
		}

	}

}
