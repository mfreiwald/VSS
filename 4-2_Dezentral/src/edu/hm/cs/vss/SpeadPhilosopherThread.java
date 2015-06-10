package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vss.philosophe.PhilosopherBackup;

public class SpeadPhilosopherThread extends Thread {

	public void run() {
		while (true) {

			spreadPhilosophersByCPU();

			try {
				Thread.sleep(Config.SPREAD_PHILOSOPHERS_INTERVAL);
			} catch (InterruptedException e) {
				Logging.log(Logger.SpeadPhilosopherThread,
						"Crashed.." + e.getMessage());
			}
		}
	}

	private void spreadPhilosophersByCPU() {
		Client thisClient = Main.getClient();
		IClient rightClient = Main.getClient().getRight();
		if (rightClient == null) {
			// nothing to do
		} else {

			try {
				ClientInfo thisInfo = thisClient.getClientInfo();
				ClientInfo rightInfo = rightClient.getClientInfo();

				double sumPhilo = thisInfo.nrPhilosophers
						+ rightInfo.nrPhilosophers;
				double sumCPUs = thisInfo.nrCPUs + rightInfo.nrCPUs;

				long nrThisPhilos = Math.round(sumPhilo * (thisInfo.nrCPUs / sumCPUs));
				long nrRightPhilos = Math.round(sumPhilo * (rightInfo.nrCPUs / sumCPUs));

				if (nrThisPhilos + nrRightPhilos != sumPhilo) {
					throw new Exception(
							"Fehler bei der Anzahl an Philosophen: "
									+ "("+nrThisPhilos +" + " + nrRightPhilos + ") != "
									+ sumPhilo
									+ "\t sumCPUs: "+sumCPUs+", thisCPUs: "+thisInfo.nrCPUs + ", rightInfo: "+rightInfo.nrCPUs);
				}

				// this: von 21 auf 14
				// right: von 0 auf 7
				// => this.export => right.import
				if (nrThisPhilos < thisInfo.nrPhilosophers
						&& nrRightPhilos > rightInfo.nrPhilosophers) {

					List<PhilosopherBackup> export = thisClient
							.exportPhilosophers(thisInfo.nrPhilosophers
									- nrThisPhilos);
					if (export.isEmpty()) {
						throw new Exception(
								"Fehler beim Exportieren der Philosophen von thisClient");
					} else {
						rightClient.importPhilosophers(export);
					}

				} else if (nrThisPhilos > thisInfo.nrPhilosophers
						&& nrRightPhilos < rightInfo.nrPhilosophers) {
					// this: von 2 auf 5
					// right: von 14 auf 11
					// => right.export => this.import

					List<PhilosopherBackup> export = rightClient
							.exportPhilosophers(rightInfo.nrPhilosophers
									- nrRightPhilos);

					if (export.isEmpty()) {
						throw new Exception(
								"Fehler beim Exportieren der Philosophen von rightClient");
					} else {
						thisClient.importPhilosophers(export);
					}

				}

			} catch (RemoteException e) {
				Logging.log(Logger.SpeadPhilosopherThread, e.getMessage());
			} catch (Exception e) {
				Logging.log(Logger.SpeadPhilosopherThread, e.getMessage());
			}

		}
	}

	private void spreadPhilosophersAllSame() {
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

						PhilosopherBackup backup = Main.getMaster()
								.removePhilosopher();
						if (backup == null) {
							i--;
						} else {
							movingPhilosophers.add(backup);
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
	}
}
