package edu.hm.cs.vss.test;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import edu.hm.cs.vss.BroadcastSender;
import edu.hm.cs.vss.Config;
import edu.hm.cs.vss.IClient;
import edu.hm.cs.vss.Client;
import edu.hm.cs.vss.Logger;
import edu.hm.cs.vss.Logging;

public class SendBroadcast {

	public static void main(String[] args) {
		String broadcastAdress = args[0];
		BroadcastSender sender = new BroadcastSender(broadcastAdress);
		Client client;
		try {
			client = new Client();
			Logging.log(Logger.Main, "Starting Client with UUID " + client.getUUID());

		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		List<InetAddress> potentialLeftPartners = sender.sendBroadcast(1);

		if (potentialLeftPartners.isEmpty()) {
			// you are alone
			Logging.log("SendBroadcast", "You are alone.");

		} else {

			// Probiere alle möglichen Partner durch, bis einer annimmt.

			for (int i = 0; i < potentialLeftPartners.size(); i++) {

				InetAddress leftPartner = potentialLeftPartners.get(i);

				//
				Logging.log("SendBroadcast",
						"Left Partner Address: " + leftPartner.toString());

				// Objekt vom Remote Partner holen
				try {

					final String rmiURL = "rmi:/" + leftPartner.toString()
							+ ":" + Config.RMI_PORT + "/" + "Client";

					Logging.log("SendBroadcast", "Connect to " + rmiURL);

					final IClient leftClient = (IClient) Naming.lookup(rmiURL);

					final boolean connected = client.tryToConnectToClient(leftClient);

					if (connected) {
						Logging.log("SendBroadcast", "Verbindung erfolgreich mit "
								+ rmiURL + " aufgebaut.");

						
						
						//leftClient.makeLongCalculation(client);
						
						
						break;
					} else {
						Logging.log("SendBroadcast", "Connection refused by "
								+ rmiURL);
						closeClient(client);
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
		}
		
		
		
		
		
		/*
		
		*/
		Logging.log("SendBroadcast", "Program finished");

	}

	
	private static void closeClient(Client client) {
		try {
			UnicastRemoteObject.unexportObject(client, false);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}
}
