package edu.hm.cs.vss;

public class Philosopher extends Thread {

	public final int nr;
	private final Table table;
	private final boolean isHungry;

	private int timesEating = 0;
	private boolean hasToStop = false;
	private int timeToWait = 0;
	private Seat seat = null;

	public States state;

	public enum States {
		MEDIA, SLEEP, WTEAT, WSEAT, WFORK, EATS, htSTOP,
	}

	public Philosopher(int nr, Table table) {
		this(nr, table, false);
	}

	public Philosopher(int nr, Table table, boolean isHungry) {
		this.nr = nr;
		this.table = table;
		this.isHungry = isHungry;
	}

	public void run() {
		while (!Main.timeOver) {
			meditate();

			if (hasToStop) {
				try {
					state = States.htSTOP;
					sleep(timeToWait);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			hasToStop = false;
			timeToWait = 0;

			eat();
			if (timesEating % 3 == 0) {
				sleep();
			}
		}
		System.out.println("Ende " + this.toString());

	}

	private void meditate() {
		state = States.MEDIA;
		Logger.log(this + " is meditateing..");

		try {
			if (isHungry) {
				sleep(Main.TIME_MEDIATE_HUNGRY);
			} else {
				sleep(Main.TIME_MEDITATE);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void eat() {
		state = States.WTEAT;
		Logger.log(this + " wants to eat.. Looking for a seat.");

		Seat seat = table.sitDown(this);
		this.seat = seat;
		Logger.log(this + " sits on " + seat);

		state = States.WFORK;
		Logger.log(this + " tries to get the forks..");

		seat.takeForks();

		state = States.EATS;
		Logger.log(this + " has forks and starts to eat.");
		timesEating++;

		try {
			sleep(Main.TIME_EAT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Logger.log(this + " finished eating and return the forks.");
		seat.releaseForks();

		Logger.log(this + " stands up.");
		this.seat = null;
		table.standUp(seat);
	}

	public int getSeat() {
		if (this.seat == null) {
			return -1;
		} else {
			return this.seat.nr;
		}
	}

	private void sleep() {
		state = States.SLEEP;
		Logger.log(this + " sleeps.");

		try {
			sleep(Main.TIME_SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getTimesEating() {
		return timesEating;
	}

	public void stopEating(int time) {
		this.hasToStop = true;
		this.timeToWait = time;
	}

	public boolean getHasToStop() {
		return hasToStop;
	}

	public String toString() {
		return "Phiolopher " + nr;
	}

}
