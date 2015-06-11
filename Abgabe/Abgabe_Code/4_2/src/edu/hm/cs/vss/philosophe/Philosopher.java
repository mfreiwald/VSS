package edu.hm.cs.vss.philosophe;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import edu.hm.cs.vss.Config;
import edu.hm.cs.vss.Logger;
import edu.hm.cs.vss.Logging;
import edu.hm.cs.vss.Main;
import edu.hm.cs.vss.seat.Seat;

public class Philosopher extends Thread {

	private final String uuid = UUID.randomUUID().toString();
	private boolean running = true;
	private long timesEating = 0;
	private final boolean isHungry;
	private boolean hasToStop = false;
	private long startTime;
	private States status;

	public enum States {
		MEDIA, SLEEP, WTEAT, WSEAT, WFORK, EATS, htSTOP, Left, Right
	}

	public Philosopher(boolean isHungry, long startEatings) throws RemoteException {
		super();
		this.isHungry = isHungry;
		this.timesEating = startEatings;
	}
	
	public Philosopher(PhilosopherBackup backup) throws RemoteException {
		super();
		this.timesEating = backup.getTimesEating();
		this.isHungry = backup.isHungry();
		this.hasToStop = backup.isHasToStop();
		this.startTime = backup.getStartTime();
	}

	public void run() {
		Logging.log(Logger.Philosopher, "Start Philosopher " + this.toString());
		this.startTime = System.currentTimeMillis();

		while (running) {
			meditate();

			while (hasToStop) {
				this.status = States.htSTOP;
				
				try {
					Thread.sleep(Config.TIME_STOP_EATING);
				} catch (InterruptedException e) {
					Logging.log(Logger.Philosopher, "Sleep Interrupt "+e.getMessage());
				}
			}
			//hasToStop = false;

			eat();
			if (timesEating % 3 == 0) {
				sleep();
			}
		}
		
		Logging.log(Logger.Philosopher, "Stop Philosopher " + this.toString());
	}

	private void meditate() {
		Logging.log(Logger.PhilosopherStatus, toString() + " meditate");
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
		Logging.log(Logger.PhilosopherStatus, toString() + " try to sit down");

		this.status = States.WSEAT;
		Seat seat = Main.getTable().sitDown(this);
		Logging.log(Logger.PhilosopherStatus, toString() + " sit down");

		Logging.log(Logger.PhilosopherStatus, toString() + " try to get forks");

		this.status = States.WFORK;
		seat.takeForks();

		this.status = States.EATS;
		timesEating++;
		Logging.log(Logger.PhilosopherStatus, toString() + " eat");

		try {
			Thread.sleep(Config.TIME_EAT);
		} catch (InterruptedException e) {
			Logging.log(Logger.Philosopher, "eat exception: " + e.getMessage());
		}
		Logging.log(Logger.PhilosopherStatus, toString() + " release forks");
		seat.releaseForks();

		Logging.log(Logger.PhilosopherStatus, toString() + " stand up");

		seat.standUp();
	}

	private void sleep() {
		Logging.log(Logger.PhilosopherStatus, toString() + " sleep");
		this.status = States.SLEEP;

		try {
			Thread.sleep(Config.TIME_SLEEP);
		} catch (InterruptedException e) {
			Logging.log(Logger.Philosopher,
					"sleep exception: " + e.getMessage());
		}
	}
	
	public void setStatus(States status) {
		this.status = status;
	}
	
	public void stopEating() {
		this.hasToStop = true;
	}
	
	public void continueEating() {
		this.hasToStop = false;
	}
	
	public boolean hasToStop() {
		return this.hasToStop;
	}
	
	public void stopPhilosopher() {
		this.running = false;
	}

	public States getStatus() {
		return this.status;
	}
	
	public boolean isHungry() {
		return this.isHungry;
	}
	
	public long getRuntime() {
		return System.currentTimeMillis() - this.startTime;
	}
	
	public String getRuntimeString() {
		return new SimpleDateFormat("mm:ss").format(new Date(this.getRuntime()));
	}
	
	public long getTimesEating() {
		return this.timesEating;
	}
	
	public PhilosopherBackup backup() {
		
		return new PhilosopherBackup(this.timesEating, this.isHungry, this.hasToStop, this.startTime);
		
	}
	
	@Override
	public String toString() {
		String name = uuid.substring(0, 4);
		if(this.isHungry) {
			name += "-H";
		}
		return name; 
	}

	public static Philosopher createPhilosopher(boolean isHungry, long startEatings) {
		try {
			return new Philosopher(isHungry, startEatings);
		} catch (RemoteException e) {
			Logging.log(Logger.Philosopher,
					"Can not create Philosopher." + e.getMessage());
			return null;
		}
	}
	
	public static Philosopher restorePhilosopher(PhilosopherBackup p) {
		try {
			return new Philosopher(p);
		} catch (RemoteException e) {
			Logging.log(Logger.Philosopher,
					"Can not restore Philosopher." + e.getMessage());
			return null;
		}
	}

}
