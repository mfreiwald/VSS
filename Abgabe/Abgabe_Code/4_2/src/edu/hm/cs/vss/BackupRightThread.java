package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vss.philosophe.PhilosopherBackup;

public class BackupRightThread extends Thread {

	private List<PhilosopherBackup> backup = new ArrayList<>();
	
	public void run() {
		while (true) {
			IClient rightClient = Main.getClient().getRight();
			if (rightClient == null) {
				// nothing to backup
			} else {
				try {
					Logging.log(Logger.BackupRightThread, "Get right Backup");
					this.backup = rightClient.backup();
				} catch (RemoteException e) {
					// no longer available..
					Logging.log(Logger.BackupRightThread, "Backup right no longer available!? "+e.getMessage());
				}
			}
			
			try {
				Thread.sleep(Config.BACKUP_INTERVAL);
			} catch (InterruptedException e) {
				Logging.log(Logger.BackupRightThread, "Crashed.."+e.getMessage());
			}
		}
	}
	
	public List<PhilosopherBackup> getBackup() {
		return this.backup;
	}
	
	public void clearBackup() {
		this.backup.clear();
	}
}
