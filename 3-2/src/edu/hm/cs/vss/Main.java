package edu.hm.cs.vss;

public class Main {

	final static int PRODUCERS = 5;
	final static int CONSUMERS = 12;
	final static int PRODUCER_SLEEP = 1;
	final static int CONSUMER_SLEEP = 1;
	
	public static void main(String[] args) {
		Data data = new Data();
		
		for(int i=0; i<PRODUCERS; i++) {
			Producer pro = new Producer(i, data);
			pro.start();
		}
		
		for(int i=0; i<CONSUMERS; i++) {
			Consumer con = new Consumer(i, data);
			con.start();
		}
	}
}
