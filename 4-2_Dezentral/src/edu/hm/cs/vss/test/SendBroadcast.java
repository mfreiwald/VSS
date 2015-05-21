package edu.hm.cs.vss.test;

import edu.hm.cs.vss.BroadcastServer;

public class SendBroadcast {

	public static void main(String[] args) {
		String broadcastAdress = args[0];
		 BroadcastServer.sendBroadcast(broadcastAdress);
	}

}
