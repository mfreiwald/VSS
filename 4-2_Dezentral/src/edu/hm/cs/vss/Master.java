package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vss.philosophe.Philosopher;

public class Master extends UnicastRemoteObject implements IMaster {

	private static final long serialVersionUID = 1L;
	private List<Thread> philosophers = new ArrayList<>();

	protected Master() throws RemoteException {
		super();
	}
	
	@Override
	public void addPhilosopher(boolean isHungry) throws RemoteException {
		Thread thr = Philosopher.createPhilosopher(isHungry);
		thr.start();
		this.philosophers.add(thr);
	}

}
