package edu.hm.cs.vss;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Wartet auf Konfigurierten Port auf UDP-Broadcasts. Sollte sich ein Client melden, dann??
 * @author michael
 *
 */
public class BroadcastServer extends Thread {
	
	private DatagramSocket socket;
	BroadcastServer() {
	    try {
			this.socket = new DatagramSocket(Config.BROADCAST_PORT);

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		DatagramPacket packet = new DatagramPacket (new byte[1], 1);
		while(true) {
			try {
				socket.receive(packet);

				if(packet.getPort() == BroadcastSender.getInstance().getSenderPort()) {
					continue;
				}
				
				System.out.println("Received from: " + packet.getAddress () + ":" +
                        packet.getPort ());

				byte[] outBuffer = new java.util.Date().toString().getBytes();
                packet.setData (outBuffer);
                packet.setLength (outBuffer.length);
                socket.send (packet);
	            
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
