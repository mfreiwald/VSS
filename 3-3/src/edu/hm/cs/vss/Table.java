package edu.hm.cs.vss;

import edu.hm.cs.vss.Philosopher.STATE;

public class Table {
	
	final Seat[] seats;
	final Fork[] forks;
	
	final int maxEatingPhilosophers;
	
	public Table(int seats) {
		this.seats = new Seat[seats];
		this.forks = new Fork[seats];
		
		for(int i=0; i<seats; i++) {
			this.forks[i] = new Fork();
		}
		
		for(int i=0; i<seats; i++) {
			this.seats[i] = new Seat(forks[i], forks[(i+1)%seats]);
		}
		
		maxEatingPhilosophers = seats/2;
		
		
	}
	
	public synchronized boolean hasFreeSeat() {
		for(Seat seat: this.seats) {
			if(!seat.isBusy) {
				return true;
			}
		}
		return false;
	}
	
	public synchronized void sitDown(String name) {
		while(!this.hasFreeSeat()) {
			System.out.println(name+" waits for seat.");
			//this.state = STATE.WAITING;
			try {
				wait();
			} catch (InterruptedException ex){
				System.err.println(ex.toString());
			}
		}
		for(Seat seat: this.seats) {
			if(!seat.isBusy) {
				seat.isBusy = true;
				return;
			}
		}
	}
	
	public synchronized void standUp() {
		for(Seat seat: this.seats) {
			if(seat.isBusy) {
				seat.isBusy = false;
				notifyAll();
				return;
			}
		}
	}
	
}
