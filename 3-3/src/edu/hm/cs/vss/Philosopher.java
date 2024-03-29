package edu.hm.cs.vss;

public class Philosopher extends Thread {
	
	public enum STATE {
		WAITING, EATS, SLEEPS, MEDITATE
	};
	
	final int secToSleep = 8*1000;
	final int secToEat = 1*1000;
	final int secToMeditate = 3*1000;
	
	final String name;
	final Table table;
	int timesEating = 0;
	STATE state = null;
	
	public Philosopher(int nr, Table table) {
		this.name = "Philosopher "+nr;
		this.table = table;
	}
	
	
	@Override
	public void run() {
		while(true) {
			try {
				meditate();
				eat();
				if(timesEating % 3 == 0) {
					sleep();
				}
			} catch (InterruptedException ex){
				
			}
		}
	}
	
	private void eat() throws InterruptedException {
		System.out.println(name+" wants to eat.");
		
		Seat seat = table.sitDown(name);
		System.out.println(name+" sit down on "+seat.toString()+".");

		seat.takeForks();
		System.out.println(name+" is eating.");
		this.state = STATE.EATS;
		timesEating++;
		sleep(secToEat);
		seat.releaseForks();
		
		table.notifySeats();
		table.standUp();
	}
	
	private void meditate() throws InterruptedException {
		System.out.println(name+" is meditateing.");
		this.state = STATE.MEDITATE;
		sleep(secToMeditate);
		
	}
	
	private void sleep() throws InterruptedException {
		System.out.println(name+" is sleeping.");
		this.state = STATE.SLEEPS;

		sleep(secToSleep);

	}
}
