package edu.hm.cs.vss;

public class Fork {

	private final int nr;
	private boolean inUse = false;
	
	Fork(int nr) {
		this.nr = nr;
	}
	
	public synchronized boolean isInUse() {
		return inUse;
	}
	
	public synchronized void take() {
		this.inUse = true;
	}
	
	public synchronized void release() {
		this.inUse = false;
		this.notifyAll();
	}
	
	public String toString() {
		return "Fork "+nr;
	}
}
