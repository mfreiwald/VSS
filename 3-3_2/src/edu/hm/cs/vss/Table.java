package edu.hm.cs.vss;

import java.util.concurrent.Semaphore;

public class Table {

	//final TreeSet<Seat> freeSeats = new TreeSet<>();
	final Seat[] seats;
	final Fork[] forks;
	private final Semaphore available;
	
	final int maxEatingPhilosophers;

	public Table(int seats) {
		this.forks = new Fork[seats];
		this.seats = new Seat[seats];
		this.available = new Semaphore(seats, true);
		
		
		for(int i=0; i<seats; i++) {
			this.forks[i] = new Fork(1, true, i);
		}
		
		for(int i=0; i<seats; i++) {
			Seat s = new Seat(i, forks[i], forks[(i+1)%seats]);
			//this.freeSeats.add(s);
			this.seats[i] = s;
		}
		
		maxEatingPhilosophers = seats/2;
		
	}
	
	public Seat sitDown(Philosopher p) {
		try {
			available.acquire();
			Logger.log(p+" can search for a seat.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// find seat
		//try {
			Seat s = findSeat(p);
			return s;
		//} catch (NoSeatAvailableException e) {
		//	e.printStackTrace();
		//	available.release();
		//	return this.sitDown(p);
		//}
	}
	
	private Seat findSeat(Philosopher p) {// throws NoSeatAvailableException {
			
		int rounds = 0;
		while(true) {
			rounds++;
			for(int i=0; i<seats.length; i++) {
				int index = (i+p.nr) % (seats.length-1);
				Seat s = seats[index];
				if(!s.sitDown()) {
					Logger.log(p+" sit down on seat "+s.nr);
					return s;
				}
			}
			Logger.log(p.toString() + " found no seat in round "+rounds);
		}
		// darf hier nie ankommen
		//throw new NoSeatAvailableException();
	}
	
	public void standUp(Seat s) {
		s.standUp();
		available.release();
	}
	
	/*
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
	*/
}
