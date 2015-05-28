package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.hm.cs.vss.philosophe.Philosopher;
import edu.hm.cs.vss.seat.ISeat;
import edu.hm.cs.vss.seat.Seat;

public class Table {

	private List<Seat> seats = new ArrayList<>();
	
	public Table() {
		// Add one Seat
		
		try {
			Seat firstSeat = new Seat();
			this.seats.add(firstSeat);
		} catch (RemoteException e) {
			Logging.log(Logger.Table, "Error creating first seat. "+e.getMessage());
		}
		
	}
	
	public Seat getSeat(int i) {
		return this.seats.get(i);
	}

	/*
	public ISeat rightSeatOf(int i) {
		int nextSeatIndex = i + 1;
		if (seats.size() > nextSeatIndex) {
			return this.seats.get(nextSeatIndex);
		} else {

			do {
				// First seat of right client
				IClient rightClient = Main.getClient().getRight();

				// Einziger Client, also nächster rechter Sitz ist wieder der
				// erste.
				if (rightClient == null) {
					return this.seats.get(0);
				} else {
					try {
						return rightClient.getFirstSeat();
					} catch (RemoteException e) {
						// retry
					}
				}
			} while (true);
		}
	}
	*/
	
	public void addSeat(Seat seat) {
		// get last Seat first
		Seat lastSeat = this.seats.get(this.seats.size()-1);
		lastSeat.setRightSeat(seat);
		this.seats.add(seat);
	}
	
	public Seat sitDown(Philosopher p) {
		Random random = new Random();
		int index = random.nextInt(this.seats.size());
		Seat seat = this.seats.get(index);
		
		// Blocking while waiting for the seat
		seat.sitDown(p);
		return seat;
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
