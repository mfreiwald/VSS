package edu.hm.css.vss;

public class Producer extends Thread {

	final int number;
	final Data data;
	
	Producer(int number, Data data) {
		this.number = number;
		this.data = data;
	}
	
	public void writeData(long nr) {
		data.writeData(this.number, nr);
	}
	
	@Override
	public void run() {
		long i = 0;
		while(true) {
			this.writeData(i);
			i++;
			
		}
	}
}
