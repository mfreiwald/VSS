package edu.hm.cs.vss;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

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

		Main.server = new Server();
		Main.broadcastServer = new BroadcastServer();
		Main.broadcastSender = new BroadcastSender(subnet);
		try {
			Main.client = new Client();
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}

		if (Main.client != null) {
			Main.server.run(Main.client);
		} else {
			System.err.println("Main.client is null");
			return;
		}

		// ToDo: Broadcast Server erst starten, wenn Client komplett ins
		// Netzwerk eingebunden wurde.
		Main.broadcastServer.start();

		List<InetAddress> potentialLeftPartners = Main.broadcastSender
				.sendBroadcast(1);
		if (potentialLeftPartners.isEmpty()) {
			// you are alone
			Logging.log(Logger.Main, "You are alone.");

		} else {

			// Probiere alle möglichen Partner durch, bis einer annimmt.

			for (int i = 0; i < potentialLeftPartners.size(); i++) {

				InetAddress leftPartner = potentialLeftPartners.get(i);

				//
				Logging.log(Logger.Main,
						"Left Partner Address: " + leftPartner.toString());

				// Objekt vom Remote Partner holen
				try {

					final String rmiURL = "rmi:/" + leftPartner.toString()
							+ ":" + Config.RMI_PORT + "/" + "Client";

					Logging.log(Logger.Main, "Connect to " + rmiURL);

					final IClient leftClient = (IClient) Naming.lookup(rmiURL);

					final boolean isNewLeft = leftClient
							.tryToConnect(Main.client);

					if (isNewLeft) {
						Logging.log(Logger.Main, "Verbindung erfolgreich mit "
								+ rmiURL + " aufgebaut.");

						break;
					} else {
						Logging.log(Logger.Main, "Connection refused by "
								+ rmiURL);
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
