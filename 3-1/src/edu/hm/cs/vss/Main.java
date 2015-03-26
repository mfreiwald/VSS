package edu.hm.cs.vss;

public class Main extends Thread {

	private static final int MAX_THREADS = 5;
	
	
	public static void main(String[] args) {
		
		for(int i=0; i<MAX_THREADS; i++) {
			Main thread = new Main(i);
			thread.start();
		}
	}
	
	
	private final int nr;
	
	public Main(int nr) {
		this.nr = nr;
	}
	
	public void run() {
		System.out.println("Thread "+nr+". Hello World!");
	}
	
}
