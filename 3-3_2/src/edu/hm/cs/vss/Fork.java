package edu.hm.cs.vss;

import java.util.concurrent.Semaphore;

public class Fork extends Semaphore {

	private static final long serialVersionUID = 1L;
	private final int nr;

	Fork(int nr) {
		super(1, true);
		this.nr = nr;
	}

	public String toString() {
		return "Fork " + nr;
	}
}
