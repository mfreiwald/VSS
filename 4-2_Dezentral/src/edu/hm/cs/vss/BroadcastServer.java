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
	
	
	
	
	public static void sendBroadcast() {
		try {
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(new byte[100], 0, InetAddress.getByName("192.168.1.255"), Config.BROADCAST_PORT);

			System.out.println("getInetAddress: " + socket.getInetAddress());
			System.out.println("getLocalAddress: " + socket.getLocalAddress());
			System.out.println("getLocalSocketAddress: " + socket.getLocalSocketAddress());
			System.out.println("getPort: " + socket.getPort());
			System.out.println("getLocalPort: " + socket.getLocalPort());
            socket.send (packet);
            
			packet.setLength(100);
            socket.receive (packet);
            socket.close ();
            byte[] data = packet.getData ();
            String time=new String(data);  // convert byte array data into string
            System.out.println(time);
			
	 } catch (IOException e) {
			e.printStackTrace();
		}
	}
}
