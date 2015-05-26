package edu.hm.cs.vss.seat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import edu.hm.cs.vss.IClient;
import edu.hm.cs.vss.Main;

public class Seat extends UnicastRemoteObject implements ISeat {

	private static final long serialVersionUID = 1L;
	private final Fork leftFork;
	private final Semaphore semaphore = new Semaphore(1, true);
	
	public Seat() throws RemoteException {
		super();
		this.leftFork = new Fork();
	}
	
	public void takeForks() throws RemoteException {
		boolean isEating = false;

		while (!isEating) {
			boolean hasLeft = this.leftFork.tryAcquire();
			boolean hasRight = getRightFork().tryAcquire();

			if (hasLeft && hasRight) {
				isEating = true;
			}

			if (!isEating) {
				if (hasLeft && !hasRight) {
					this.leftFork.release();
				}
				if (!hasLeft && hasRight) {
					this.getRightFork().release();
				}
			}
		}

	}
	
	public void releaseForks() throws RemoteException {
		leftFork.release();
		getRightFork().release();
	}
	
	public void sitDown() {
		try {
			this.semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void standUp() {
		this.semaphore.release();
	}
	
	private IFork getRightFork() {
		// If seat is in the array
		// ToDo
		
		// If seat is the last one
		try {
			IClient rightClient = Main.getClient().getRight();
			if(rightClient == null) {
				return Main.getClient().getFirstSeat().getLeftFork();
			} else {
				return rightClient.getFirstSeat().getLeftFork();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public IFork getLeftFork() throws RemoteException {
		return this.leftFork;
	}
	
	

}
