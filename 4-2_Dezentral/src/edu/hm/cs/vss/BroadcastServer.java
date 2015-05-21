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
			
			System.out.println("getInetAddress: " + socket.getInetAddress());
			System.out.println("getLocalAddress: " + socket.getLocalAddress());
			System.out.println("getLocalSocketAddress: " + socket.getLocalSocketAddress());
			System.out.println("getPort: " + socket.getPort());
			System.out.println("getLocalPort: " + socket.getLocalPort());

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		DatagramPacket packet = new DatagramPacket (new byte[1], 1);
		while(true) {
			try {
				socket.receive(packet);
				System.out.println("Sender Port: "+BroadcastSender.getInstance().getSenderPort());
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
	
	
	
	private static DatagramSocket senderSocket;
	public static void sendBroadcast(String boradcastAdress) {
		try {
			senderSocket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(new byte[100], 0, InetAddress.getByName(boradcastAdress), Config.BROADCAST_PORT);

			System.out.println("getInetAddress: " + senderSocket.getInetAddress());
			System.out.println("getLocalAddress: " + senderSocket.getLocalAddress());
			System.out.println("getLocalSocketAddress: " + senderSocket.getLocalSocketAddress());
			System.out.println("getPort: " + senderSocket.getPort());
			System.out.println("getLocalPort: " + senderSocket.getLocalPort());
			
			senderSocket.send(packet);
            
			packet.setLength(100);
			//senderSocket.receive (packet);
			senderSocket.close ();
            byte[] data = packet.getData ();
            String time=new String(data);  // convert byte array data into string
            System.out.println(time);
            System.out.println("getAddress: " + packet.getAddress());
            System.out.println("getSocketAddress: " + packet.getSocketAddress());
            System.out.println("getPort: " + packet.getPort());
            
			
	 } catch (IOException e) {
			e.printStackTrace();
		}
	}
}
