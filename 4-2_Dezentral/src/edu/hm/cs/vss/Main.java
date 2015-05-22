package edu.hm.cs.vss;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

	private static BroadcastServer broadcastServer;
	private static BroadcastSender broadcastSender;
	private static Client client;
	private static Server server;
	
	public static void main(String[] args) {
		final String subnet;
		if (args.length == 1)
			subnet = args[0];
		else {
			System.err.println("Please add a subnet address as an argument.");  
			return;
		}
		
		Main.server				= new Server();
		Main.broadcastServer 	= new BroadcastServer();		
		Main.broadcastSender 	= new BroadcastSender(subnet);
		try {
			Main.client 		= new Client();
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
		
		if(Main.client != null) {
			Main.server.run(Main.client);
		} else {
			System.err.println("Main.client is null");
			return;
		}
		
		// Broadcast Server erst starten, wenn Client komplett ins Netzwerk eingebunden wurde.
		Main.broadcastServer.start();

		
		InetAddress leftPartner = Main.broadcastSender.sendBroadcast(1);
		if(leftPartner == null) {
			// you are alone
			
			
		} else {
			//
			Logging.log(Logger.Main, "Left Partner Address: "+leftPartner.toString());
			
			
			// Objekt vom Remote Partner holen
			try {
				
				String rmiURL = "rmi:/"+leftPartner.toString()+":" + Config.RMI_PORT + "/" + "Client";
				Logging.log(Logger.Main, "Connect to "+rmiURL);
				
				IClient leftClient = (IClient) Naming.lookup (rmiURL);
				
				boolean isNewLeft = leftClient.tryToConnect(Main.client);
				if(isNewLeft) {
					Logging.log(Logger.Main, "Verbindung erfolgreich aufgebaut.");
				} else {
					Logging.log(Logger.Main, "Leider mag er mich nicht.");
				}
				
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			// linken Parnter mitteilen, dass du der neue bist
			
			
			// find out your new right partner
		}
	
		System.out.println("Main beendet...");
	}

	public static BroadcastServer getBroadcastServer() {
		return broadcastServer;
	}

	public static BroadcastSender getBroadcastSender() {
		return broadcastSender;
	}

	public static Client getClient() {
		return client;
	}

	public static Server getServer() {
		return server;
	}
	
	

}
