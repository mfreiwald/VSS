package edu.hm.cs.vss;

public class Main {

	public static void main(String[] args) {
		
		BroadcastServer bcs = new BroadcastServer();
		bcs.start();
		
		for(int i=0; i<3; i++) {
			System.out.println("Send a new Broadcast..");
			BroadcastServer.sendBroadcast("172.16.43.255");
			System.out.println();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
