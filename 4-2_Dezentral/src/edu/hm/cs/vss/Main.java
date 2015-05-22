package edu.hm.cs.vss;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

	private static Client client;
	private static Server server;
	
	public static void main(String[] args) throws RemoteException {
		final String subnet;
		if (args.length == 1)
			subnet = args[0];
		else {
			System.err.println("Please add a subnet address as an argument.");  
			return;
		}
		
		Main.client = new Client();
		
		Main.server = new Server();
		Main.server.run(Main.client);
		
		
		
		BroadcastServer bcs = new BroadcastServer();
		bcs.start();
		
		BroadcastSender sender = new BroadcastSender(subnet);
		
		InetAddress leftPartner = sender.sendBroadcast(1);
		if(leftPartner == null) {
			// you are alone
		} else {
			//
			Logging.log(Logger.Main, "Left Partner Address: "+leftPartner.toString());
			
			
			// Objekt vom Remote Partner holen
			try {
				System.out.println("Probiers mal");
				IClient leftClient = (IClient) Naming.lookup ("rmi:/"+leftPartner.toString()+":" + Config.RMI_PORT + "/" + "Client");
				
				System.out.println("hastn?");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			
			// linken Parnter mitteilen, dass du der neue bist
			
			
			// find out your new right partner
		}
	
	}

}
