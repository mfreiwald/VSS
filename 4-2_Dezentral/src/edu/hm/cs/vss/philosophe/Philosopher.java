package edu.hm.cs.vss.philosophe;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

import edu.hm.cs.vss.Config;
import edu.hm.cs.vss.Logger;
import edu.hm.cs.vss.Logging;
import edu.hm.cs.vss.Main;
import edu.hm.cs.vss.seat.Seat;

public class Philosopher extends UnicastRemoteObject implements IPhilosopher,
		Runnable {

	private static final long serialVersionUID = 1L;
	private final String uuid = UUID.randomUUID().toString();
	private boolean running = true;
	private long timesEating = 0;
	private final boolean isHungry;
	private boolean hasToStop = false;
	private long startTime;
	private States status;
	
	public enum States {
		MEDIA, SLEEP, WTEAT, WSEAT, WFORK, EATS, htSTOP,
	} 

	public Philosopher(boolean isHungry) throws RemoteException {
		super();
		this.isHungry = isHungry;
	}

	public Philosopher() throws RemoteException {
		this(false);
	}

	public void run() {
		Logging.log(Logger.Philosopher, "Start Philosopher");
		this.startTime = System.currentTimeMillis();

		while (running) {
			meditate();

			if (hasToStop) {
				try {
					Thread.sleep(Config.TIME_STOP_EATING);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			hasToStop = false;

			eat();
			if (timesEating % 3 == 0) {
				sleep();
			}
		}
	}

	private void meditate() {
		Logging.log(Logger.Philosopher, toString() + " meditate");
		this.status = States.MEDIA;
		
		try {
			if (isHungry) {
				Thread.sleep(Config.TIME_MEDIATE_HUNGRY);
			} else {
				Thread.sleep(Config.TIME_MEDITATE);
			}
		} catch (InterruptedException e) {
			Logging.log(Logger.Philosopher,
					"meditate exception: " + e.getMessage());
		}
	}

	private void eat() {
		Logging.log(Logger.Philosopher, toString() + " try to sit down");

		this.status = States.WSEAT;
		Seat seat = Main.getTable().sitDown(this);
		Logging.log(Logger.Philosopher, toString() + " sit down");

		try {
			Logging.log(Logger.Philosopher, toString() + " try to get forks");
			
			this.status = States.WFORK;
			seat.takeForks();
			
			this.status = States.EATS;
			timesEating++;
			Logging.log(Logger.Philosopher, toString() + " eat");

			try {
				Thread.sleep(Config.TIME_EAT);
			} catch (InterruptedException e) {
				Logging.log(Logger.Philosopher,
						"eat exception: " + e.getMessage());
			}
			Logging.log(Logger.Philosopher, toString() + " release forks");
			seat.releaseForks();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Logging.log(Logger.Philosopher, toString() + " stand up");

		seat.standUp();
	}

	private void sleep() {
		Logging.log(Logger.Philosopher, toString() + " sleep");
		this.status = States.SLEEP;
		
		try {
			Thread.sleep(Config.TIME_SLEEP);
		} catch (InterruptedException e) {
			Logging.log(Logger.Philosopher,
					"sleep exception: " + e.getMessage());
		}
	}
	
	public States getStatus() {
		return this.status;
	}

	@Override
	public long getRuntime() throws RemoteException {
		return System.currentTimeMillis() - this.startTime;
	}

	@Override
	public long getTimesEating() throws RemoteException {
		return this.timesEating;
	}

	@Override
	public void stopEating() throws RemoteException {
		this.hasToStop = true;
	}

	@Override
	public String toString() {
		return uuid.substring(0, 4);
	}
	
	public static Thread createPhilosopher(boolean isHungry) {
		try {
			return new Thread(new Philosopher(isHungry));
		} catch (RemoteException e) {
			Logging.log(Logger.Philosopher,
					"Can not create Philosopher." + e.getMessage());
			return null;
		}
	}

}
