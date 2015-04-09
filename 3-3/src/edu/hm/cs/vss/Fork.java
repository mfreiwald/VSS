package edu.hm.cs.vss;

public class Fork {

	public final int nr;
	private volatile boolean inUse = false;
	
	public Fork(int nr) {
		this.nr = nr;
	}
	
	public boolean isInUse() {
		return inUse;
	}

	
	public synchronized boolean take() {
		if(inUse) {
			return false;
		} else {
			inUse = true;
			return true;
		}
	}
	
	public synchronized void release() {
		inUse = false;
	}
	
	public String toString() {
		return ""+nr;
	}
}
