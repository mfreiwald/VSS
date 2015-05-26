package edu.hm.cs.vss.philosophe;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.cs.vss.Config;
import edu.hm.cs.vss.Logger;
import edu.hm.cs.vss.Logging;
import edu.hm.cs.vss.Main;

public class Philosopher extends UnicastRemoteObject implements IPhilosopher, Runnable {

	private static final long serialVersionUID = 1L;
	private boolean running = true;
	private long timesEating = 0;
	private final boolean isHungry;
	private boolean hasToStop = false;
	private long startTime;

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
		
		while(running) {
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
		Logging.log(Logger.Philosopher, "meditate");

		try {
			if (isHungry) {
				Thread.sleep(Config.TIME_MEDIATE_HUNGRY);
			} else {
				Thread.sleep(Config.TIME_MEDITATE);
			}
		} catch (InterruptedException e) {
			Logging.log(Logger.Philosopher, "meditate exception: "+e.getMessage());
		}
	}
	
	private void eat() {
		Logging.log(Logger.Philosopher, "eat");

		
		//Main.getTable().sitDown(this);
		
		
		timesEating++;
		
		try {
			Thread.sleep(Config.TIME_EAT);
		} catch (InterruptedException e) {
			Logging.log(Logger.Philosopher, "eat exception: "+e.getMessage());
		}
	}
	
	private void sleep() {
		Logging.log(Logger.Philosopher, "sleep");

		try {
			Thread.sleep(Config.TIME_SLEEP);
		} catch (InterruptedException e) {
			Logging.log(Logger.Philosopher, "sleep exception: "+e.getMessage());
		}
	}
	
	@Override
	public long getRuntime() throws RemoteException {
		return System.currentTimeMillis()-this.startTime;
	}
	
	@Override
	public long getTimesEating() throws RemoteException {
		return this.timesEating;
	}
	
	@Override
	public void stopEating() throws RemoteException {
		this.hasToStop = true;
	}
	
	public static Thread createPhilosopher(boolean isHungry) {
		try {
			return new Thread(new Philosopher(isHungry));
		} catch (RemoteException e) {
			Logging.log(Logger.Philosopher, "Can not create Philosopher." + e.getMessage());
			return null;
		}
	}
}
