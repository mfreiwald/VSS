package edu.hm.cs.vss;

import java.net.SocketAddress;

public class Main {

	public static void main(String[] args) {
		final String subnet;
		if (args.length == 1)
			subnet = args[0];
		else {
			System.err.println("Please add a subnet address as an argument.");  
			return;
		}
		
		BroadcastServer bcs = new BroadcastServer();
		bcs.start();
		
		BroadcastSender sender = new BroadcastSender(subnet);
		
		SocketAddress leftPartner = sender.sendBroadcast(1);
		if(leftPartner == null) {
			// you are alone
		} else {
			// find out your new right partner
		}
	
	}

}
