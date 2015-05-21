package edu.hm.cs.vss;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		BroadcastServer bcs = new BroadcastServer();
		bcs.start();
		
		BroadcastSender sender = BroadcastSender.getInstance(args[0]);
		System.out.println("Send a new Broadcast..");
		sender.sendBroadcast();
		
	}

}
