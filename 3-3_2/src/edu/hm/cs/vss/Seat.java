package edu.hm.cs.vss;

public class Seat implements Comparable<Seat> {
	
	private final int nr;
	private final Fork leftFork;
	private final Fork rightFork;
	
	Seat(int nr, Fork left, Fork right) {
		this.nr = nr;
		this.leftFork = left;
		this.rightFork = right;
	}
	
	
	public synchronized void takeForks() {
		while(leftFork.isInUse() || rightFork.isInUse()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		leftFork.take();
		rightFork.take();
	}
	
	public synchronized void releaseForks() {
		leftFork.release();
		rightFork.release();
		
		// notify left and right seat
	}
	
	
	public String toString() {
		return "Seat "+nr;
	}

	@Override
	public int compareTo(Seat o) {
		if(o.leftFork.isInUse() && o.rightFork.isInUse()) {
			return 1;
		} else if(this.leftFork.isInUse() && this.rightFork.isInUse()) {
			return -1;
		} else {
			return 0;
		}
		
		
		//return this.toString().compareTo(o.toString());
	}
}
