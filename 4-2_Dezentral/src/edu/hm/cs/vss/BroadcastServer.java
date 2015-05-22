package edu.hm.cs.vss;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Wartet auf Konfigurierten Port auf UDP-Broadcasts. Sollte sich ein Client melden, dann??
 * @author michael
 *
 */
public class BroadcastServer extends Thread {
	
	private DatagramSocket socket;
	private boolean delay = false;
	
	BroadcastServer() {
	    try {
			this.socket = new DatagramSocket(Config.BROADCAST_PORT);

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		Logging.log(Logger.BroadcastServer, "Starting Broadcast Server. Listening on Port " + Config.BROADCAST_PORT);
		
		DatagramPacket packet = new DatagramPacket (new byte[36], 36);
		while(true) {
			try {
				
				// Warte auf ankommende Broadcasts
				socket.receive(packet);
				
				byte[] data = packet.getData();
	            String uuid = new String(data);
	            
	            //Logging.log(Logger.BroadcastServer, "R UUID: "+uuid + " Size: " + uuid.getBytes().length);
	            //Logging.log(Logger.BroadcastServer, "M UUID: "+Config.SERIAL_UUID + " Size: " + Config.SERIAL_UUID.getBytes().length);
	            
	            
	            //Logging.log(Logger.BroadcastServer, Config.SERIAL_UUID + " == "+uuid);
	            //Logging.log(Logger.BroadcastServer, Main.broadcastSender.getSenderPort() + " == "+packet.getPort());
	            
	            
	            if(uuid.equals(Config.SERIAL_UUID) && Main.getBroadcastSender().getSenderPort() == packet.getPort() ) {
	            	//Logging.log(Logger.BroadcastServer, "Same UUID");
	            	continue;
	            }
	            
	            if(delay) {
	            	try {
						sleep(Config.BROADCAST_DELAY);
					} catch (InterruptedException e) {
						Logging.log(Logger.BroadcastServer, "Delay Sleep Exception: "+e.getMessage());
					}
	            }
				
				// Sende Nachricht zurück, dass er mein Parnter sein darf
				Logging.log(Logger.BroadcastServer, "Received Broadcast from " + packet.getSocketAddress());
                
                // Es ist uns egal ob die Nachricht ankommt oder nicht
				
				// Sende die Anzahl der Partner
				/*
				Random random = new Random();
				Integer nrPartners = random.nextInt(5);
				
				byte[] outBuffer = ByteBuffer.allocate(4).putInt(nrPartners).array();
	            packet.setData(outBuffer);
	            packet.setLength(outBuffer.length);
	            */
				socket.send(packet);
	            
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void enableDelay() {
		this.delay = true;
	}
	
	public void disableDelay() {
		this.delay = false;
	}
	
	public boolean hasDelay() {
		return this.delay;
	}
}
