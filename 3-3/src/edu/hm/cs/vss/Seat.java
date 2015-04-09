package edu.hm.cs.vss;

public class Seat {

	public boolean isBusy = false;
	public final Fork leftFork;
	public final Fork rightFork;
	public final int nr;
	
	public Seat(int nr, Fork left, Fork right) {
		this.nr = nr;
		this.leftFork = left;
		this.rightFork = right;
	}
	
	public synchronized void takeForks() {
		while(leftFork.isInUse() || rightFork.isInUse()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		leftFork.take();
		rightFork.take();
	}
	
	public synchronized void releaseForks() {
		System.out.println("Relase Forks ("+leftFork+","+rightFork+")");
		leftFork.release();
		rightFork.release();
	}
	
}
