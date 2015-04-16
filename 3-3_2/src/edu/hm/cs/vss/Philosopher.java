package edu.hm.cs.vss;

public class Philosopher extends Thread {
	
	private final int nr;
	private final Table table;
	private int timesEating = 0;
	
	public States state;
	
	public enum States {
		MEDITATE, SLEEPS, WANTS_TO_EAT, WAITING_FOR_SEAT, WAITING_FOR_FORK, EATS
	}
	
	
	public Philosopher(int nr, Table table) {
		this.nr = nr;
		this.table = table;
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
		Logger.log(this+" is meditateing..");
		state = States.MEDITATE;
		
		try {
			sleep(Main.TIME_MEDITATE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void eat() {
		Logger.log(this+" wants to eat.. Show for a seat.");
		state = States.WANTS_TO_EAT;
		
		Seat seat = null;
		while((seat = table.sitDown()) == null) {
			try {
				synchronized(table) {
					state = States.WAITING_FOR_SEAT;
					table.wait();
				}
			} catch (InterruptedException e) {
					e.printStackTrace();
			}
		}
		
		Logger.log(this+" sits on "+seat);
		
		Logger.log(this+" tries to get the forks..");
		
		state = States.WAITING_FOR_FORK;
		seat.takeForks();
		
		Logger.log(this+" has forks and starts to eat.");
		state = States.EATS;
		timesEating++;

		try {
			sleep(Main.TIME_EAT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Logger.log(this+ " finished eating and return the forks.");
		seat.releaseForks();
		table.notifyAllSeats();
		
		Logger.log(this+" stands up.");
		table.standUp(seat);
		
	}
	
	private void sleep() {
		state = States.SLEEPS;
		
		Logger.log(this+" sleeps.");
		try {
			sleep(Main.TIME_SLEEP);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return "Phiolopher "+nr;
	}
}
