package edu.hm.cs.vss.seat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import edu.hm.cs.vss.IClient;
import edu.hm.cs.vss.Main;
import edu.hm.cs.vss.philosophe.Philosopher;

public class Seat extends UnicastRemoteObject implements ISeat {

	private static final long serialVersionUID = 1L;
	private final Fork leftFork;
	private final Semaphore semaphore = new Semaphore(1, true);
	private Seat rightSeat = null;
	private IFork tmpRightFork = null;
	private Philosopher sittingPhilosopher = null;
	
	public Seat() throws RemoteException {
		super();
		this.leftFork = new Fork();
	}

	public void takeForks() throws RemoteException {
		boolean isEating = false;

		IFork tmpRight = null;
		while (!isEating) {
			boolean hasLeft = this.leftFork.tryAcquire();
			
			tmpRight = getRightFork();
			boolean hasRight = tmpRight.tryAcquire();

			if (hasLeft && hasRight) {
				isEating = true;
			}

			if (!isEating) {
				if (hasLeft && !hasRight) {
					this.leftFork.release();
				}
				if (!hasLeft && hasRight) {
					tmpRight.release();
				}
			}
		}
		this.tmpRightFork = tmpRight;

	}

	public void releaseForks() throws RemoteException {
		leftFork.release();
		this.tmpRightFork.release();
		this.tmpRightFork = null;
	}

	public void sitDown(Philosopher p) {
		try {
			this.semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.sittingPhilosopher = p;
	}

	public void standUp() {
		this.sittingPhilosopher = null;
		this.semaphore.release();
	}

	private IFork getRightFork() {
		if (this.rightSeat == null) {
			// If seat is the last one
			try {
				IClient rightClient = Main.getClient().getRight();
				if (rightClient == null) {
					return Main.getClient().getFirstSeat().getLeftFork();
				} else {
					return rightClient.getFirstSeat().getLeftFork();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			try {
				return this.rightSeat.getLeftFork();
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public void setRightSeat(Seat rightSeat) {
		this.rightSeat = rightSeat;
	}
	
	public boolean isInUse() {
		if(this.semaphore.availablePermits() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Philosopher getSittingPhilosopher() {
		return this.sittingPhilosopher;
	}

	@Override
	public IFork getLeftFork() throws RemoteException {
		return this.leftFork;
	}

}
