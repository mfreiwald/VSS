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
	BroadcastServer() {
	    try {
			this.socket = new DatagramSocket(Config.BROADCAST_PORT);

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		DatagramPacket packet = new DatagramPacket (new byte[36], 36);
		while(true) {
			try {
				
				// Warte auf ankommende Broadcasts
				socket.receive(packet);
				
				byte[] data = packet.getData();
	            String uuid = new String(data);
	            
	            //Logging.log(Logger.BroadcastServer, "R UUID: "+uuid + " Size: " + uuid.getBytes().length);
	            //Logging.log(Logger.BroadcastServer, "M UUID: "+Config.SERIAL_UUID + " Size: " + Config.SERIAL_UUID.getBytes().length);
	            
	            if(uuid.equals(Config.SERIAL_UUID)) {
	            	//Logging.log(Logger.BroadcastServer, "Same UUID");
	            	continue;
	            }
				
				// Sende Nachricht zurück, dass er mein Parnter sein darf
				Logging.log(Logger.BroadcastServer, "Received Broadcast from " + packet.getSocketAddress());
                
                // Es ist uns egal ob die Nachricht ankommt oder nicht
                socket.send (packet);
	            
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
