package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.util.List;

import edu.hm.cs.vss.philosophe.Philosopher;

public class BackupRightThread extends Thread {

	private List<Philosopher> backup;
	
	public void run() {
		while (true) {
			IClient rightClient = Main.getClient().getRight();
			if (rightClient == null) {
				// nothing to backup
			} else {
				try {
					this.backup = rightClient.backup();
				} catch (RemoteException e) {
					// no longer available..
				}
			}
			
			try {
				Thread.sleep(Config.BACKUP_INTERVAL);
			} catch (InterruptedException e) {
				Logging.log("BackupRightThread", "Crashed.."+e.getMessage());
			}
		}
	}
	
	public List<Philosopher> getBackup() {
		return this.backup;
	}
}
