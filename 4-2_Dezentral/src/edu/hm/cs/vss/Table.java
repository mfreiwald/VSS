package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vss.seat.ISeat;
import edu.hm.cs.vss.seat.Seat;

public class Table {

	private List<ISeat> seats = new ArrayList<>();
	
	public Table() {
		// Add one Seat
		
		try {
			ISeat firstSeat = new Seat();
			this.seats.add(firstSeat);
		} catch (RemoteException e) {
			Logging.log(Logger.Table, "Error creating first seat. "+e.getMessage());
		}
		
	}

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
}
