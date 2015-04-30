package edu.hm.cs.vss;

import java.util.ArrayList;

public class Master extends Thread {

	private final ArrayList<Philosopher> philosophers;
	
	public Master(ArrayList<Philosopher> philosophers) {
		this.philosophers = philosophers;
	}
	
	
	public void run() {
		while(!Main.timeOver) {
			checkPhilosophers();
		}
		
		System.out.println("Ende Master");
	}
	
	private void checkPhilosophers() {
		
		Philosopher minEating = this.philosophers.get(0);
		
		for(Philosopher p: this.philosophers) {
			if(minEating.getTimesEating() > p.getTimesEating()) {
				minEating = p;
			}
		}
		
		for(Philosopher p: this.philosophers) {
			if(p.getTimesEating() >= minEating.getTimesEating()+10) {
				p.stopEating(500);
			}
		}		
	}
	
	
}
