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
			this.forks[i] = new Fork(i);
		}
		
		for(int i=0; i<seats; i++) {
			this.seats[i] = new Seat(i, forks[i], forks[(i+1)%seats]);
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
	
	public synchronized Seat sitDown(String name) {
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
				System.out.println(name+" sits on "+seat.nr+" with forks ("+seat.leftFork+","+seat.rightFork+")");
				
				return seat;
			}
		}
		return null;
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
	
	public void notifySeats() {
		for(Seat seat: this.seats) {
			seat.notify();
		}
	}
	
}
