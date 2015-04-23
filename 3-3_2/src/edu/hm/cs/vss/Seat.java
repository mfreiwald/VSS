package edu.hm.cs.vss;

public class Seat implements Comparable<Seat> {
	
	public final int nr;
	private final Fork leftFork;
	private final Fork rightFork;
	
	Seat(int nr, Fork left, Fork right) {
		this.nr = nr;
		this.leftFork = left;
		this.rightFork = right;
	}
	
	
	public synchronized void takeForks() {
		
		boolean hasLeft = false;
		boolean hasRight = false;
		
		// wir brauchen false in der schleife zum weiter machen
		// hasLeft = true && hasRight = true => false
		// !true || !false => false || true => true
		// false || false => true || true => true
		// !true || !true => false || false => false
		
		while(!hasLeft || !hasRight) {
			while(leftFork.isInUse()) {
				try {
					if(hasRight) {
						rightFork.release();
						hasRight = false;
					}
					synchronized(leftFork) {
						leftFork.wait();
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(!hasLeft) {
				leftFork.take();
				hasLeft = true;
			}
			
			while(rightFork.isInUse()) {
				try {
					if(hasLeft) {
						leftFork.release();
						hasLeft = false;
					}
					synchronized(rightFork) {
						rightFork.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(!hasRight) {
				rightFork.take();
				hasRight = true;
			}
		
			
		}
		
	}
	
	public synchronized void releaseForks() {
		leftFork.release();
		rightFork.release();
		
		// notify left and right seat
		
		
		// 		1			2
		// 	schaut AA	
		// 	schaut BB
		//	nimmt AA
		//					schaut BB
		//					schaut CC
		// 					nimmt BB
	}
	
	
	public String toString() {
		return "Seat "+nr;
	}

	@Override
	public int compareTo(Seat o) {
		return this.toString().compareTo(o.toString());
	}
}
