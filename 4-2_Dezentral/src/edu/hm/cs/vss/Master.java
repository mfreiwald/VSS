package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vss.philosophe.Philosopher;
import edu.hm.cs.vss.seat.Seat;

public class Master extends UnicastRemoteObject implements IMaster {

	private static final long serialVersionUID = 1L;
	private List<Thread> philosophers = new ArrayList<>();

	protected Master() throws RemoteException {
		super();
	}
	
	@Override
	public void addPhilosopher(boolean isHungry) throws RemoteException {
		Thread thr = Philosopher.createPhilosopher(isHungry);
		thr.start();
		this.philosophers.add(thr);
	}
	
	@Override
	public void addSeat() throws RemoteException {
		Seat seat = new Seat();
		Main.getTable().addSeat(seat);
	}
	
	@Override
	public String status() throws RemoteException {
		String s = "";
		s += "Philosophers: " + this.philosophers.size() + "\n";
		s += "Seats: "+Main.getTable().nrSeats() + "\n";
		
		List<Seat> seats = Main.getTable().getSeats();
		for(int i=0; i<seats.size(); i++) {
			s += "Seat "+i+"\t";
		}
		s += "\n";
		for(Seat seat: seats) {
			Philosopher p = seat.getSittingPhilosopher();
			String name;
			if(p == null) {
				name = "null";
			} else {
				name = p.toString();
			}
			s += name+"\t";
		}
		
		
		return s;
	}

}
