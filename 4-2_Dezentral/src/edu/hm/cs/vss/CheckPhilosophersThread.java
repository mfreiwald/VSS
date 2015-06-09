package edu.hm.cs.vss;

import java.util.ConcurrentModificationException;
import java.util.List;

import edu.hm.cs.vss.philosophe.Philosopher;

public class CheckPhilosophersThread extends Thread {

	public void run() {
		while (true) {
			
			List<Philosopher> philosophers = Main.getMaster().getPhilosophers();
			if(!philosophers.isEmpty()) {
				this.checkPhilosophers(philosophers);
			}

			try {
				sleep(Config.CHECK_PHILOSOPHERS_INTERVAL);
			} catch (InterruptedException e) {

			}

		}
	}

	private void checkPhilosophers(List<Philosopher> philosophers) {
		try {

			Philosopher minEating = philosophers.get(0);

			for (Philosopher p : philosophers) {
				if (minEating.getTimesEating() > p.getTimesEating()) {
					minEating = p;
				}
			}

			for (Philosopher p : philosophers) {
				if (p.getTimesEating() >= minEating.getTimesEating() + Config.DIFFERENZ_EATING_PHILOSOPHERS) {
					p.stopEating();
				}
			}
		} catch (ConcurrentModificationException e) {
			Logging.log(Logger.CheckPhilosophersThread, e.getMessage());
		} catch (NullPointerException e) {
			Logging.log(Logger.CheckPhilosophersThread, e.getMessage());
		}
	}
}
