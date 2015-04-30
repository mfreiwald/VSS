package edu.hm.cs.vss;

public class Seat{
	
	public final int nr;
	private final Fork leftFork;
	private final Fork rightFork;
	private Boolean inUse = false;
	
	Seat(int nr, Fork left, Fork right) {
		this.nr = nr;
		this.leftFork = left;
		this.rightFork = right;
	}
	
	
	public void takeForks() {
		boolean isEating = false;

		while(!isEating) {
			boolean hasLeft = leftFork.tryAcquire();
			boolean hasRight = rightFork.tryAcquire();
			
			if(hasLeft && hasRight) {
				isEating = true;
			}
			
			if(!isEating) {
				if(hasLeft && !hasRight) {
					leftFork.release();
				}
				if(!hasLeft && hasRight) {
					rightFork.release();
				}
			}
		}
		
	}
	
	public synchronized void releaseForks() {
		leftFork.release();
		rightFork.release();
	}
	

	public boolean sitDown() {
		synchronized(inUse) {
			if(inUse) {
				return false;
			} else {
				inUse = true;
				return true;
			}
		}
	}
	
	public void standUp() {
		synchronized(inUse) {
			inUse = false;
		}
	}
	
	public String toString() {
		return "Seat "+nr;
	}

}
