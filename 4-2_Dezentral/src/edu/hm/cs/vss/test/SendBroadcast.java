package edu.hm.cs.vss.test;

import edu.hm.cs.vss.BroadcastSender;

public class SendBroadcast {

	public static void main(String[] args) {
		String broadcastAdress = args[0];
		 BroadcastSender sender = new BroadcastSender(broadcastAdress);
		 sender.sendBroadcast(1);
	}

}
