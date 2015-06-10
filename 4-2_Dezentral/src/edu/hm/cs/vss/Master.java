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
	public final SpeadPhilosopherThread speadPhilosopherThread = new SpeadPhilosopherThread();
	public final GlobalThread globalThread = new GlobalThread();
	
	protected Master() throws RemoteException {
		super();
	}
	
	public void startThreads() {
		this.backupThread.start();
		this.checkPhilosophersThread.start();
		this.speadPhilosopherThread.start();
		this.globalThread.start();
		
	}

	@Override
	public void addPhilosopher(boolean isHungry) throws RemoteException {
		
		double avg = Main.getClient().searchGlobalEatingAVG(null, 0);
		
		synchronized (philosophers) {
			Philosopher p = Philosopher.createPhilosopher(isHungry, (long)avg);
			p.start();
			this.philosophers.add(p);
		}
	}

	@Override
	public void addSeat() throws RemoteException {
		Seat seat = new Seat();
		Main.getTable().addSeat(seat);
	}

	@Override
	public PhilosopherBackup removePhilosopher() throws RemoteException {
		PhilosopherBackup backup = null;
		try {

			synchronized (philosophers) {
				if (this.philosophers.size() > 0) {
					Philosopher first = this.philosophers.get(0);
					first.stopPhilosopher();
					first.join();
					backup = first.backup();
					this.philosophers.remove(0);
					Logging.log(Logger.Master,
							"Philosopher " + first.toString() + " removed.");
				} else {
					Logging.log(Logger.Master,
							"No Philosopher available to remove.");
				}
			}
		} catch (InterruptedException e) {
			Logging.log(Logger.Master, "Remove Philosopher Interrupt" + e.getMessage());
		}
		return backup;
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
		for (Philosopher p : this.philosophers) {
			p.stopPhilosopher();
		}
		for (Philosopher p : this.philosophers) {
			try {
				p.join();
			} catch (InterruptedException e) {
				Logging.log(Logger.Master, "Stop Client Interrupt" + e.getMessage());
			}
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
				this.philosophers.add(p);
				p.start();
			}
		}

		this.backupThread.clearBackup();
	}

	public void importPhilosophers(List<PhilosopherBackup> philosophers) {
		for (PhilosopherBackup backup : philosophers) {
			Philosopher p = Philosopher.restorePhilosopher(backup);

			synchronized (philosophers) {
				this.philosophers.add(p);
				p.start();
			}
		}
	}
}
