package edu.hm.cs.vss;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class BroadcastSender {

	
    private static BroadcastSender sender; 
	
    public static BroadcastSender getInstance(String address) {
        if(sender == null) {
        	sender = new BroadcastSender(address);
        }
    	return sender;
    }
    
    public static BroadcastSender getInstance() {
    	if(sender == null) {
    		throw new NullPointerException("First call instance with an address.");
    	}
    	return sender;
    }
    
    
    
    
	private InetAddress broadcastAdress;
	private DatagramSocket socket;

	public BroadcastSender(String broadcastAdress) {
		try {
			this.broadcastAdress = InetAddress.getByName(broadcastAdress);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(Config.WAIT_FOR_BROADCAST_ANSWER_TIMEOUT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void sendBroadcast() {

		DatagramPacket packet = new DatagramPacket(new byte[36], 36,
				this.broadcastAdress, Config.BROADCAST_PORT);
		try {
			
			//Logging.log(Logger.BroadcastSender, "Sending a Broadcast with UUID: " + Config.SERIAL_UUID + " Size: "+Config.SERIAL_UUID.getBytes().length);
			byte[] outBuffer = Config.SERIAL_UUID.getBytes();
            packet.setData(outBuffer);
            packet.setLength(outBuffer.length);
			socket.send(packet);

			socket.receive(packet);
			
			// Es wurde min. einen Client gefunden. Als linken Partner speichern
			
			Logging.log(Logger.BroadcastSender, "Yeah, you found someone.. "+packet.getSocketAddress());
			 
			
		} catch (SocketTimeoutException e) {
			Logging.log(Logger.BroadcastSender, "No one there. You are alone..");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getSenderPort() {
		return socket.getLocalPort(); 
	}
}
