package edu.hm.cs.vss;

import java.util.TreeSet;

public class Table {

	final TreeSet<Seat> freeSeats = new TreeSet<>();
	final Seat[] seats;
	final Fork[] forks;
	
	final int maxEatingPhilosophers;

	public Table(int seats) {
		this.forks = new Fork[seats];
		this.seats = new Seat[seats];
		
		for(int i=0; i<seats; i++) {
			this.forks[i] = new Fork(i);
		}
		
		for(int i=0; i<seats; i++) {
			Seat s = new Seat(i, forks[i], forks[(i+1)%seats]);
			this.freeSeats.add(s);
			this.seats[i] = s;
		}
		
		maxEatingPhilosophers = seats/2;
		
	}
	
	public synchronized Seat sitDown() {
		if(freeSeats.isEmpty()) {
			return null;
		} else {
			Seat seat = freeSeats.first();
			freeSeats.remove(seat);
			return seat;
		}
	}
	
	public synchronized void standUp(Seat seat) {
		freeSeats.add(seat);
		this.notifyAll();
	}
	
	public synchronized void notifyAllSeats() {
		for(Seat s : this.seats) {
			synchronized(s) {
				s.notifyAll();
			}
		}
	}
}
