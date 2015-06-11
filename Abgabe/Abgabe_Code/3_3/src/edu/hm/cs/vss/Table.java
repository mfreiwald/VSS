package edu.hm.cs.vss;

import edu.hm.cs.vss.Philosopher.States;

public class Table {

	final Seat[] seats;
	final Fork[] forks;

	final int maxEatingPhilosophers;

	public Table(int seats) {
		this.forks = new Fork[seats];
		this.seats = new Seat[seats];

		for (int i = 0; i < seats; i++) {
			this.forks[i] = new Fork(i);
		}

		for (int i = 0; i < seats; i++) {
			Seat s = new Seat(i, forks[i], forks[(i + 1) % seats]);
			this.seats[i] = s;
		}

		maxEatingPhilosophers = seats / 2;
	}

	public Seat sitDown(Philosopher p) {
		// find seat
		Seat s = findSeat(p);
		return s;
	}

	private Seat findSeat(Philosopher p) {
		for (int i = 0; i < seats.length; i++) {
			int index = (i + p.nr) % seats.length;
			Seat s = seats[index];

			if (s.sitDown()) {
				Logger.log(p + " sit down on seat " + s.nr);
				return s;
			}
		}

		// darf sich beim Startplatz anstellen
		int index = p.nr % seats.length;
		Seat s = seats[index];
		p.state = States.WSEAT;
		return s.waitForSeat();
	}

	public void standUp(Seat s) {
		s.standUp();
	}
}
