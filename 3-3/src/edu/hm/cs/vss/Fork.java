package edu.hm.cs.vss;

public class Fork {

	private final int nr;
	private volatile boolean inUse = false;
	
	public Fork(int nr) {
		this.nr = nr;
	}
	
	public synchronized boolean isInUse() {
		return inUse;
	}
	
	public synchronized void take() {
		inUse = true;
	}
	
	public synchronized void release() {
		inUse = false;
	}
	
	public String toString() {
		return "Fork "+nr;
	}
}
