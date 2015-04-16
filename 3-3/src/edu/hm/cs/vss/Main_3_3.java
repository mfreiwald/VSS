package edu.hm.cs.vss;

import java.util.TreeSet;

public class Main_3_3 {

	static int NUMBER_PHILOSOPHERS = 6;
	static int NUMBER_SEATS = 5;
	
	public static void main(String[] args) {
			
		//Table table = new Table(NUMBER_SEATS);
		Tab table = new Tab();
		for(int i=0; i<NUMBER_PHILOSOPHERS; i++) {
			Phi p = new Phi(i, table);
			p.start();
		}
		
	}

}

class Tab {
	TreeSet<Sea> freeSeats = new TreeSet<>();
	
	public Tab() {
		freeSeats.add(new Sea(1));
		freeSeats.add(new Sea(2));
		freeSeats.add(new Sea(3));
	}
	
	synchronized Sea sitDown() {
		if(freeSeats.isEmpty()) {
			return null;
		} else {
			Sea seat = freeSeats.first();
			freeSeats.remove(seat);
			return seat;
		}
	}
	
	synchronized void standUp(Sea seat) {
		freeSeats.add(seat);
			notifyAll();
		
	}
}

class Phi extends Thread {
	private int nr;
	private Tab table;
	Phi(int nr, Tab table) {
		this.nr = nr;
		this.table = table;
	}
	public void run() {
		Sea seat = null;
		System.out.println("Phi "+nr+" will sit down.");
		while((seat = table.sitDown()) == null) {
			System.out.println("Phi "+nr+" has no seat. Waiting...");
			try {
				synchronized(table) {
					table.wait();
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Phi "+nr+" eats on seat "+seat.nr);
		try {
			sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Phi "+nr+" stands up");
		table.standUp(seat);
	}
}

class Sea implements Comparable<Sea>{
	
	int nr;
	Sea(int nr) {
		this.nr = nr;
	}
	
	public String toString() {
		return ""+nr;
	}

	@Override
	public int compareTo(Sea o) {
		return this.toString().compareTo(o.toString());
	}
	
}
