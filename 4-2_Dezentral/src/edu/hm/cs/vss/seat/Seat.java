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
	private boolean tmpRightForkIsRemote = false;
	private Philosopher sittingPhilosopher = null;

	public Seat() throws RemoteException {
		super();
		this.leftFork = new Fork();
	}

	public void takeForks() {
		boolean isEating = false;

		IFork tmpRight = null;
		while (!isEating) {
			boolean hasLeft = this.leftFork.tryAcquire();

			tmpRight = getRightFork(); // remote oder nicht?
			boolean hasRight;
			try {
				hasRight = tmpRight.tryAcquire();
				if(hasRight) {
					tmpRight.setRemoteAcquire(tmpRightForkIsRemote);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				hasRight = false;
			}

			if (hasLeft && hasRight) {
				isEating = true;
			}

			if (!isEating) {
				if (hasLeft && !hasRight) {
					this.leftFork.release();
				}
				if (!hasLeft && hasRight) {
					try {
						tmpRight.release();
					} catch (RemoteException e) {
						e.printStackTrace();
						// Kann nicht released werden, weil Client ausgefallen..
						// ist aber uns egal..
					}
				}
			}
		}
		this.tmpRightFork = tmpRight;

	}

	public void releaseForks() {
		leftFork.release();
		try {
			this.tmpRightFork.release();
		} catch (RemoteException e) {
			e.printStackTrace();
			// Kann nicht released werden, weil Client ausgefallen.. ist aber
			// uns egal..
		}
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
					IFork rightFork = Main.getClient().getFirstSeat()
							.getLeftFork();
					tmpRightForkIsRemote = false;
					return rightFork;
				} else {
					IFork rightFork = rightClient.getFirstSeat().getLeftFork();
					tmpRightForkIsRemote = true;
					return rightFork;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			tmpRightForkIsRemote = false;
			return this.rightSeat.getLeftFork();
		}
	}

	public void setRightSeat(Seat rightSeat) {
		this.rightSeat = rightSeat;
	}

	public boolean isInUse() {
		if (this.semaphore.availablePermits() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public Philosopher getSittingPhilosopher() {
		return this.sittingPhilosopher;
	}

	@Override
	public Fork getLeftFork() {
		return this.leftFork;
	}
	
	public void block() {
		try {
			this.semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void unblock() {
		this.semaphore.release();
	}
	
	public int waitingPhilosophers() {
		return this.semaphore.getQueueLength();
	}

}
