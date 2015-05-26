package edu.hm.cs.vss.seat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class Fork extends UnicastRemoteObject implements IFork {

	private static final long serialVersionUID = 1L;
	private final Semaphore semaphore = new Semaphore(1, true);

	protected Fork() throws RemoteException {
		super();
	}

	@Override
	public boolean tryAcquire() {
		return this.semaphore.tryAcquire();
	}
	
	@Override
	public void release() throws RemoteException {
		this.semaphore.release();
	}
	
}
