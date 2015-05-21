package edu.hm.cs.vss;

public class Main {

	public static void main(String[] args) {
		
		BroadcastServer bcs = new BroadcastServer();
		bcs.start();
		
		BroadcastSender sender = BroadcastSender.getInstance(args[0]);
		for(int i=0; i<3; i++) {
			System.out.println("Send a new Broadcast..");
			sender.sendBroadcast();
			System.out.println();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
