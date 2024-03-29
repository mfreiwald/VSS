package edu.hm.cs.vss;

public class Producer extends Thread {

	final int number;
	final Data data;
	
	Producer(int number, Data data) {
		this.number = number;
		this.data = data;
	}
	
	public void writeData(long nr) {
		data.write(number, nr);
		try {
			sleep(Main.PRODUCER_SLEEP);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		long i = 0;
		while(true) {
			this.writeData(i);
			i++;
			if(i == Long.MAX_VALUE) {
				i = 0;
			}
		}
	}
}
