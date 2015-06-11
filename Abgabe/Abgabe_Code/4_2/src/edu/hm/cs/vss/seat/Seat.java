package edu.hm.cs.vss.seat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import edu.hm.cs.vss.IClient;
import edu.hm.cs.vss.Logger;
import edu.hm.cs.vss.Logging;
import edu.hm.cs.vss.Main;
import edu.hm.cs.vss.philosophe.Philosopher;
import edu.hm.cs.vss.philosophe.Philosopher.States;

public class Seat extends UnicastRemoteObject implements ISeat {

	private static final long serialVersionUID = 1L;
	private final Fork leftFork;
	private final Semaphore semaphore = new Semaphore(1, true);
	private Seat rightSeat = null;
	private IFork tmpRightFork = null;
	private boolean tmpRightForkIsRemote = false;
	private Philosopher sittingPhilosopher = null;
	private boolean tryToRemove = false;

	public Seat() throws RemoteException {
		super();
		this.leftFork = new Fork();
	}

	public void takeForks() {
		boolean isEating = false;

		IFork tmpRight = null;
		while (!isEating) {
			if(this.sittingPhilosopher != null) {
				this.sittingPhilosopher.setStatus(States.Left);
			}
			boolean hasLeft = this.leftFork.tryAcquire();

			tmpRight = getRightFork(); // remote oder nicht?
			boolean hasRight;
			try {
				if(this.sittingPhilosopher != null) {
					this.sittingPhilosopher.setStatus(States.Right);
				}
				hasRight = tmpRight.tryAcquire();
				if(hasRight) {
					tmpRight.setRemoteAcquire(tmpRightForkIsRemote);
				}
			} catch (RemoteException e) {
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
			// Kann nicht released werden, weil Client ausgefallen.. ist aber
			// uns egal..
		}
		this.tmpRightFork = null;
	}

	public void sitDown(Philosopher p) {
		try {
			this.semaphore.acquire();
		} catch (InterruptedException e) {
			Logging.log(Logger.Seat, "Interrupt exception on sitDown acquire "+e.getMessage());
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
				IClient rightClient = Main.getClient().getRight();
				if (rightClient == null) {
					IFork rightFork = Main.getClient().getFirstSeat()
							.getLeftFork();
					tmpRightForkIsRemote = false;
					return rightFork;
				} else {
					try {
						IFork rightFork = rightClient.getFirstSeat().getLeftFork();
						tmpRightForkIsRemote = true;
						return rightFork;
					} catch (RemoteException e) {
						// Maybe not good
						return getRightFork();
					}
				
					
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
			Logging.log(Logger.Seat, "Interrupt Exception in block "+e.getMessage());
		}
	}
	
	public void unblock() {
		this.semaphore.release();
	}
	
	public int waitingPhilosophers() {
		return this.semaphore.getQueueLength();
	}

	public void removing() {
		this.tryToRemove = true;
		this.block();
	}
	
	public boolean tryToRemoving() {
		return this.tryToRemove;
	}

}
