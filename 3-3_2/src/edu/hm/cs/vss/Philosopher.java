package edu.hm.cs.vss;

public class Philosopher extends Thread {
	
	public final int nr;
	private final Table table;
	private final boolean isHungry;
	
	private int timesEating = 0;

	public States state;
	
	public enum States {
		MEDIA, 
		SLEEP, 
		WTEAT, 
		WSEAT, 
		WFORK, 
		EATS
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
		while(true) {
			meditate();
			eat();
			if(timesEating % 3 == 0) {
				sleep();
			}
		}
	}
	
	
	private void meditate() {
		state = States.MEDIA;
		Logger.log(this+" is meditateing..");

		try {
			if(isHungry) {
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
		Logger.log(this+" wants to eat.. Looking for a seat.");

		Seat seat = table.sitDown(this);
		
		/*
		while((seat = table.sitDown()) == null) {
			try {
				synchronized(table) {
					state = States.WSEAT;
					Logger.log();
					table.wait();
				}
			} catch (InterruptedException e) {
					e.printStackTrace();
			}
		}
		*/
		Logger.log(this+" sits on "+seat);
		
		
		state = States.WFORK;
		Logger.log(this+" tries to get the forks..");

		seat.takeForks();
		
		state = States.EATS;
		Logger.log(this+" has forks and starts to eat.");
		timesEating++;

		try {
			sleep(Main.TIME_EAT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Logger.log(this+ " finished eating and return the forks.");
		seat.releaseForks();
		
		Logger.log(this+" stands up.");
		table.standUp(seat);

	}
	
	public int getSeat() {
		return -1;
	}
	
	private void sleep() {
		state = States.SLEEP;
		Logger.log(this+" sleeps.");
		
		try {
			sleep(Main.TIME_SLEEP);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getTimesEating() {
		return timesEating;
	}
	
	public String toString() {
		return "Phiolopher "+nr;
	}
}
