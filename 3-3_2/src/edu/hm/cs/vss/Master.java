package edu.hm.cs.vss;

public class Master extends Thread {

	private final Philosopher[] philosophers;
	
	public Master(Philosopher[] philosophers) {
		this.philosophers = philosophers;
	}
	
	
	public void run() {
		while(true) {
			
		}
	}
	
	private void checkPhilosophers() {
		
		Philosopher maxEating = this.philosophers[0];
		Philosopher minEating = this.philosophers[0];
		
		for(Philosopher p: this.philosophers) {
			
			if(maxEating.getTimesEating() < p.getTimesEating()) {
				maxEating = p;
			}
			
			if(minEating.getTimesEating() > p.getTimesEating()) {
				minEating = p;
			}
		}
		
		if(maxEating.getTimesEating() >= minEating.getTimesEating()+10) {
			
		}
		
	}
	
	
}