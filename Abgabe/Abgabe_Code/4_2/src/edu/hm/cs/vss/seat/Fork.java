package edu.hm.cs.vss.seat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class Fork extends UnicastRemoteObject implements IFork {

	private static final long serialVersionUID = 1L;
	private Semaphore semaphore = new Semaphore(1, true);
	private boolean remoteAcquire = false;
	
	protected Fork() throws RemoteException {
		super();
	}
	
	@Override
	public boolean tryAcquire() {
		// ist das ein Remote Zugriff??
		
		boolean gotIt = this.semaphore.tryAcquire();
		if(!gotIt) {
			//tryRemoteAcquire = false;
		}
		return gotIt;
	}
	
	@Override
	public void release() {
		this.semaphore.release();
		remoteAcquire = false;
	}
	
	@Override
	public void setRemoteAcquire(boolean remote) throws RemoteException {
		this.remoteAcquire = remote;
	}
	
	public boolean isRemoteAcquire() {
		return this.remoteAcquire;
	}
	
	public void newFork() {
		this.semaphore = new Semaphore(1, true);
	}
}
