package edu.hm.cs.vss;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class BroadcastSender {

	private InetAddress broadcastAdress;
	private DatagramSocket socket;

	private List<InetAddress> receivedPartners = new ArrayList<>();

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
			Logging.log(Logger.BroadcastSender, "Socket Exception "+e.getMessage());
		}
	}

	
	/**
	 * Sender einen Broadcast aus. Sammelt alle zurück kommenden Antworten und gibt dessen Sender in einer Liste zurück.
	 * @param times Anzahl zum wie vielten male der Broadcast ausgeführt wird.
	 * @return Liste alle Sender, die eine Antwort geschickt haben.
	 */
	public List<InetAddress> sendBroadcast(int times) {
		Logging.log(Logger.BroadcastSender, "Send a new broadcast to " + broadcastAdress);
		
		DatagramPacket packet = new DatagramPacket(new byte[36], 36,
				this.broadcastAdress, Config.BROADCAST_PORT);
		try {
			
			//Logging.log(Logger.BroadcastSender, "Sending a Broadcast with UUID: " + Config.SERIAL_UUID + " Size: "+Config.SERIAL_UUID.getBytes().length);
			byte[] outBuffer = Config.SERIAL_UUID.getBytes();
            packet.setData(outBuffer);
            packet.setLength(outBuffer.length);
			socket.send(packet);

			
			// Warte auf Antworten
			
			// Sammle alle Antworten innerhalb von x Sekunden
			// Anschließend such einen Client auf, mit dem eine Verbindung aufgebaut werden soll. Am besten wäre der Client, der am meisten Clients kennt. 
			// (Damit wird die Wahrscheinlichkeit verringert, sich mit ebenso neu hinzugekommenen Clients zu verbinden, obwohl bereits welche vorhanden sind)
			
			long time = System.currentTimeMillis() + Config.WAIT_FOR_BROADCAST_ANSWER_TIMEOUT;
			
			while(time > System.currentTimeMillis()) {
				
				socket.receive(packet);
				
				//byte[] data = packet.getData();
				//int nrPartners = ByteBuffer.wrap(data).getInt();
				
				//this.receivedPartners.add(packet.getAddress()+" Partners: "+nrPartners);
				this.receivedPartners.add(packet.getAddress());
				Logging.log(Logger.BroadcastSender, "Yeah, you found someone.. "+packet.getSocketAddress());
			}
			
			// Es wurde min. einen Client gefunden. Als linken Partner speichern
			
			
			
				
		} catch (SocketTimeoutException e) {
			Logging.log(Logger.BroadcastSender, "SocketTimeout "+e.getMessage());
			if(this.receivedPartners.isEmpty()) {
				if(times == Config.TIMES_REPEAT_BROADCAST) {
					Logging.log(Logger.BroadcastSender, "No one there. You are alone..");
				} else {
					return this.sendBroadcast(times+1);
				}
			}
		} catch (IOException e) {
			Logging.log(Logger.BroadcastSender, "IOException sending broadcast"+e.getMessage());
		}
		
		return this.receivedPartners;
	}

	public int getSenderPort() {
		return socket.getLocalPort();
	}
}
