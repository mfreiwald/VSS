package edu.hm.cs.vss;

import java.util.concurrent.Semaphore;

public class Fork extends Semaphore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int nr;
	//private Boolean inUse = false;
	
	Fork(int permits, boolean isFair, int nr) {
		super(permits, isFair);
		this.nr = nr;
	}
	
	
	
	
	
	/*
	public boolean isInUse() {
		synchronized (inUse) {
			return inUse;
		}
	}
	
	public void take() {
		synchronized(inUse) {
			this.inUse = true;
		}
	}
	
	public synchronized void release() {
		synchronized (inUse) {
			this.inUse = false;
			this.notifyAll();
		}
	}
	*/
	
	public String toString() {
		return "Fork "+nr;
	}
}
