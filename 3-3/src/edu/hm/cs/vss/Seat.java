package edu.hm.cs.vss;

public class Seat {

	public boolean isBusy = false;
	private Fork leftFork;
	private Fork rightFork;
	
	public Seat(Fork left, Fork right) {
		this.leftFork = left;
		this.rightFork = right;
	}
	
	
}
