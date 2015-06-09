package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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

					if (nrMovePhilosophers > 0) {
						Logging.log(Logger.SpeadPhilosopherThread, "Move "
								+ nrMovePhilosophers + " Philosophers to right");

						// Remove Philophers
						// Backup
						List<PhilosopherBackup> movingPhilosophers = new ArrayList<>();

						for (int i = 0; i < nrMovePhilosophers; i++) {

							try {
								PhilosopherBackup backup = Main.getMaster()
										.removePhilosopher();
								if (backup == null) {
									i--;
								} else {
									movingPhilosophers.add(backup);
								}
							} catch (RemoteException e) {
								i--;
							}

						}

						// Move backup

						try {
							rightClient.importPhilosophers(movingPhilosophers);

							Logging.log(Logger.SpeadPhilosopherThread, "Moved "
									+ movingPhilosophers.size()
									+ " Philosophers to right");

						} catch (RemoteException e) {
							Logging.log(
									Logger.SpeadPhilosopherThread,
									"Remote Exception while moving "
											+ e.getMessage());
						}

					}
					// -> Restore this backups
				}

			}

			try {
				Thread.sleep(Config.SPREAD_PHILOSOPHERS_INTERVAL);
			} catch (InterruptedException e) {
				Logging.log(Logger.SpeadPhilosopherThread,
						"Crashed.." + e.getMessage());
			}
		}
	}
}
