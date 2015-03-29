package edu.hm.css.vss;

public class Consumer extends Thread {

	final int number;
	final Data data;
	
	Consumer(int number, Data data) {
		this.number = number;
		this.data = data;
	}
	
	public void readData() {
		data.readData(number);
	}
	
	@Override
	public void run() {
		while(true) {
			this.readData();
		}
	}
}
