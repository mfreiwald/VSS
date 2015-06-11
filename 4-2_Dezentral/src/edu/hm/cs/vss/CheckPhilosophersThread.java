package edu.hm.cs.vss;

import java.rmi.RemoteException;
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
			
			double globalAVGEating = Main.getClient().searchGlobalEatingAVG(null, 0);

			for (Philosopher p : philosophers) {
				if (p.getTimesEating() >= globalAVGEating + Config.DIFFERENZ_EATING_PHILOSOPHERS) {
					p.stopEating();
				} else {
					p.continueEating();
				}
			}
		} catch (ConcurrentModificationException e) {
			Logging.log(Logger.CheckPhilosophersThread, e.getMessage());
		} catch (NullPointerException e) {
			Logging.log(Logger.CheckPhilosophersThread, e.getMessage());
		} catch (RemoteException e) {
			Logging.log(Logger.CheckPhilosophersThread, e.getMessage());
		}
	}
}
