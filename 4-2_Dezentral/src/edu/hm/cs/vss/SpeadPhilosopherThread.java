package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vss.philosophe.Philosopher;
import edu.hm.cs.vss.philosophe.PhilosopherBackup;

public class SpeadPhilosopherThread extends Thread {

	public void run() {
		while (true) {
			IClient rightClient = Main.getClient().getRight();
			if (rightClient == null) {
				// nothing to do
			} else {

				int thisPhilosophers = Main.getClient().nrRunningPhilosophers();
				int rightPhilosophers = Integer.MAX_VALUE;
				try {
					rightPhilosophers = rightClient.nrRunningPhilosophers();
				} catch (RemoteException e) {
					rightPhilosophers = Integer.MAX_VALUE;
				}

				if (rightPhilosophers < thisPhilosophers) {
					// how many philosophers to move ?

					// 10 - 3 = 7 / 2 = 3.5 = 3 => 10 - 3 & 3 + 3 = 7 & 6
					int diff = thisPhilosophers - rightPhilosophers;
					int nrMovePhilosophers = (int) (diff / 2);

					Logging.log("SpeadPhilosopherThread", "Move "
							+ nrMovePhilosophers + " Philosophers to right");

					// Remove Philophers
					// Backup
					List<PhilosopherBackup> movingPhilosophers = new ArrayList<>();
					List<Philosopher> philosophers = Main.getMaster()
							.getPhilosophers();
					for (int i = 0; i < nrMovePhilosophers; i++) {
						Philosopher piToExport = philosophers.get(thisPhilosophers - 1 - i);
						piToExport.stopPhilosopher();
						movingPhilosophers.add(piToExport.backup());
					}

					// Move backup
					try {
						rightClient.importPhilosophers(movingPhilosophers);
						
						Main.getMaster().removePhilosopher();
						
					} catch (RemoteException e) {
					}
					
					// -> Restore this backups
				}

			}

			try {
				Thread.sleep(Config.SPREAD_PHILOSOPHERS_INTERVAL);
			} catch (InterruptedException e) {
				Logging.log("SpeadPhilosopherThread",
						"Crashed.." + e.getMessage());
			}
		}
	}
}
