package edu.hm.cs.vss;

public class Consumer extends Thread {

	final int number;
	final Data data;
	
	Consumer(int number, Data data) {
		this.number = number;
		this.data = data;
	}
	
	public void readData() {
		data.read(number);
		try {
			sleep(Main.CONSUMER_SLEEP);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			this.readData();
		}
	}
}
