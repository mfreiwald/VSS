package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vss.philosophe.Philosopher;
import edu.hm.cs.vss.seat.Seat;

public class Master extends UnicastRemoteObject implements IMaster {

	private static final long serialVersionUID = 1L;
	private List<Philosopher> philosophers = new ArrayList<>();
	public final BackupRightThread backupThread = new BackupRightThread();

	protected Master() throws RemoteException {
		super();
		
		
		// Start Backup Thread
		backupThread.start();
	}

	@Override
	public void addPhilosopher(boolean isHungry) throws RemoteException {
		Philosopher p = Philosopher.createPhilosopher(isHungry);
		Thread thr = new Thread(p);
		thr.start();
		this.philosophers.add(p);
	}

	@Override
	public void addSeat() throws RemoteException {
		Seat seat = new Seat();
		Main.getTable().addSeat(seat);
	}

	@Override
	public String status() throws RemoteException {
		String s = ""; // "\n\n\n";
		s += Logging.timestamp() + "\n";
		s += "Philosophers:\t" + this.philosophers.size() + "\n";
		s += "Seats:\t" + Main.getTable().nrSeats() + "\n";
		s += "\n";
		List<Seat> seats = Main.getTable().getSeats();
		for (int i = 0; i < seats.size(); i++) {
			s += "Seat " + i + " [" + seats.get(i).waitingPhilosophers() + "]" + "\t";
		}
		s += "\n";
		for (Seat seat : seats) {
			Philosopher p = seat.getSittingPhilosopher();
			String name;
			if (p == null) {
				name = "null";
			} else {
				name = p.toString();
			}
			s += name + "\t\t";
		}
		s += "\n";

		for (Seat seat : seats) {
			Philosopher p = seat.getSittingPhilosopher();
			String name;
			if (p == null) {
				name = "";
			} else {
				name = p.getStatus() + "";
			}
			s += name + "\t\t";
		}
		if (!seats.isEmpty())
			s += "\n";

		// for(int i=0; i<10; i++)
		// s += "\n";

		return s;
	}
	
	public void createBackupOfRightClient() {
		
	}
	
	public void exportPhilosopher(Philosopher p) {
		
	}

	public List<Philosopher> getPhilosophers() {
		return this.philosophers;
	}

	public void restoreBackup(List<Philosopher> philosophers) {
		Logging.log(Logger.Master, "Restore Backup");
		
		for(Philosopher p: philosophers) {
			Thread thr = new Thread(p);
			thr.start();
			this.philosophers.add(p);
		}
	}
}
