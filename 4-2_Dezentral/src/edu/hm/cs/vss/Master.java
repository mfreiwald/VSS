package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import edu.hm.cs.vss.philosophe.Philosopher;
import edu.hm.cs.vss.philosophe.PhilosopherBackup;
import edu.hm.cs.vss.seat.Seat;

public class Master extends UnicastRemoteObject implements IMaster {

	private static final long serialVersionUID = 1L;
	private List<Philosopher> philosophers = new ArrayList<>();
	public final BackupRightThread backupThread = new BackupRightThread();
	public final CheckPhilosophersThread checkPhilosophersThread = new CheckPhilosophersThread();

	protected Master() throws RemoteException {
		super();

		// Start Backup Thread
		backupThread.start();
		checkPhilosophersThread.start();
	}

	@Override
	public void addPhilosopher(boolean isHungry) throws RemoteException {
		synchronized (philosophers) {
			Philosopher p = Philosopher.createPhilosopher(isHungry);
			Thread thr = new Thread(p);
			thr.start();
			this.philosophers.add(p);
		}
	}

	@Override
	public void addSeat() throws RemoteException {
		Seat seat = new Seat();
		Main.getTable().addSeat(seat);
	}

	@Override
	public void removePhilosopher() throws RemoteException {
		synchronized (philosophers) {
			if (this.philosophers.size() > 0) {
				Philosopher first = this.philosophers.get(0);
				first.stopPhilosopher();
				this.philosophers.remove(0);
				Logging.log(Logger.Master, "Philosopher " + first.toString()
						+ " removed.");
			} else {
				Logging.log(Logger.Master,
						"No Philosopher available to remove.");
			}
		}
	}

	@Override
	public boolean removeSeat() throws RemoteException {
		return Main.getTable().removeSeat();
	}

	@Override
	public String statusPhilosophers() throws RemoteException {
		String s = ""; // "\n\n\n";
		try {

			s += Logging.timestamp() + "\n";
			s += "Philosophers:\t" + this.philosophers.size() + "\n";
			s += "Seats:\t\t" + Main.getTable().nrSeats() + "\n";

			long sumEating = 0;
			for (Philosopher p : this.philosophers) {
				sumEating += p.getTimesEating();
			}
			s += "Sum Eating:\t" + sumEating + "\n";
			s += "\n";

			for (Philosopher p : this.philosophers) {
				s += p.toString() + "\t";
			}
			s += "\n";

			for (Philosopher p : this.philosophers) {
				s += p.getTimesEating() + "\t";
			}
			s += "\n";

			for (Philosopher p : this.philosophers) {
				s += p.getRuntimeString() + "\t";
			}
			s += "\n";

			for (Philosopher p : this.philosophers) {
				s += p.getStatus() + "\t";
			}
			s += "\n";
		} catch (ConcurrentModificationException e) {
			s = "";
		}

		return s;
	}

	@Override
	public String statusSeats() throws RemoteException {
		String s = ""; // "\n\n\n";

		try {
			s += Logging.timestamp() + "\n";
			s += "Philosophers:\t" + this.philosophers.size() + "\n";
			s += "Seats:\t\t" + Main.getTable().nrSeats() + "\n";
			long sumEating = 0;
			for (Philosopher p : this.philosophers) {
				sumEating += p.getTimesEating();
			}
			s += "Sum Eating:\t" + sumEating + "\n";
			s += "\n";

			List<Seat> seats = Main.getTable().getSeats();
			for (int i = 0; i < seats.size(); i++) {
				s += "Seat " + i + " [" + seats.get(i).waitingPhilosophers()
						+ "]" + "\t";
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

		} catch (ConcurrentModificationException e) {
			s = "";
		}
		return s;
	}

	@Override
	public void enableLogging(Logger logger) throws RemoteException {
		Logging.enableLogger(logger);
	}

	@Override
	public void disableLogging(Logger logger) throws RemoteException {
		Logging.disableLogger(logger);
	}

	@Override
	public void stopClient() throws RemoteException {
		for(Philosopher p: this.philosophers) {
			p.stopPhilosopher();
		}
		
		
	}
	
	public List<Philosopher> getPhilosophers() {
		return this.philosophers;
	}

	public void restoreBackup() {
		Logging.log(Logger.Master, "Restore Backup");
		List<PhilosopherBackup> ps = this.backupThread.getBackup();

		for (PhilosopherBackup backup : ps) {
			Philosopher p = Philosopher.restorePhilosopher(backup);

			synchronized (philosophers) {
				Thread thr = new Thread(p);
				this.philosophers.add(p);
				thr.start();
			}
		}
		
		this.backupThread.clearBackup();
	}
}
