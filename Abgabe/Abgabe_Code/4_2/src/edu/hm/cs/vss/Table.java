package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.hm.cs.vss.philosophe.Philosopher;
import edu.hm.cs.vss.seat.Seat;

public class Table {

	private List<Seat> seats = new ArrayList<>();
    private final Random rand = new Random();

	public Table() {
		// Add one Seat

		try {
			Seat firstSeat = new Seat();
			this.seats.add(firstSeat);

			Seat secondSeat = new Seat();
			firstSeat.setRightSeat(secondSeat);
			this.seats.add(secondSeat);
		} catch (RemoteException e) {
			Logging.log(Logger.Table,
					"Error creating first seat. " + e.getMessage());
		}

	}

	public Seat getSeat(int i) {
		return this.seats.get(i);
	}

	public void addSeat(Seat seat) {
		// get last Seat first
		Seat lastSeat = this.seats.get(this.seats.size() - 1);
		lastSeat.setRightSeat(seat);
		this.seats.add(seat);
	}

	public boolean removeSeat() {
		if (this.seats.size() > 2) {
			Seat third = this.seats.get(2);
			third.removing();
			this.seats.remove(2);
			
			if(this.seats.size() > 2) {
				this.seats.get(1).setRightSeat(this.seats.get(2));
			}
			
			third.unblock();
			Logging.log(Logger.Table, "Removed Seat");
			return true;
		} else {
			Logging.log(Logger.Table, "Only 2 Seats available");
			return false;
		}
	}

	public Seat sitDown(Philosopher p) {

		
		// get a seat number
		
		// priority for first seat is lowest 
		
		// ask if seat is trying to remove
		

		// index between 1 and size(), seat 0 has lower priority cause remote access
	    int index = rand.nextInt((this.seats.size() - 1) + 1) + 1;

		// get seat with lowest queue
		int minWaiting = Integer.MAX_VALUE;
		Seat minSeat = null;
		for(int i=index; i<this.seats.size()+index; i++) {
			Seat s = this.seats.get(i%this.seats.size());
			if(s.tryToRemoving()) {
				continue;
			}
			if (s.waitingPhilosophers() < minWaiting) {
				minWaiting = s.waitingPhilosophers();
				minSeat = s;
			}
		}
		minSeat.sitDown(p);
		return minSeat;

		/*
		 * // Random Sitdown Random random = new Random(); int index =
		 * random.nextInt(this.seats.size()); Seat seat = this.seats.get(index);
		 * 
		 * // Blocking while waiting for the seat seat.sitDown(p); return seat;
		 */
	}

	public void standUp(Seat s) {
		s.standUp();
	}

	public int nrSeats() {
		return this.seats.size();
	}

	public List<Seat> getSeats() {
		return this.seats;
	}



}
