package edu.hm.cs.vss.philosophe;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.cs.vss.Config;
import edu.hm.cs.vss.Logger;
import edu.hm.cs.vss.Logging;

public class Philosopher extends UnicastRemoteObject implements IPhilosopher, Runnable {

	private static final long serialVersionUID = 1L;
	private boolean running = true;
	private int timesEating = 0;
	private final boolean isHungry;


	public Philosopher(boolean isHungry) throws RemoteException {
		super();
		this.isHungry = isHungry;
	}
	
	public Philosopher() throws RemoteException {
		this(false);
	}
	
	public void run() {
		Logging.log(Logger.Philosopher, "Start Philosopher");
		while(running) {
			meditate();
			
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
	
	public static Thread createPhilosopher(boolean isHungry) {
		try {
			return new Thread(new Philosopher(isHungry));
		} catch (RemoteException e) {
			Logging.log(Logger.Philosopher, "Can not create Philosopher." + e.getMessage());
			return null;
		}
	}
}
