package edu.hm.cs.vss;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import edu.hm.cs.vss.seat.Seat;

public class Main {

	private static BroadcastServer broadcastServer;
	private static BroadcastSender broadcastSender;
	private static Client client;
	private static Server server;
	private static Table table;
	private static Master master;

	public static void main(String[] args) {
		final String subnet;
		if (args.length == 1)
			subnet = args[0];
		else {
			subnet = "";
			System.err.println("Please add a subnet address as an argument.");
			// System.exit(-1);
		}

		Logging.log(Logger.Main, "Starting Client with UUID "
				+ Config.SERIAL_UUID);

		Main.table = new Table();
		Main.server = new Server();
		Main.broadcastServer = new BroadcastServer();
		Main.broadcastSender = new BroadcastSender(subnet);

		try {
			Main.client = new Client();
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		try {
			Main.master = new Master();
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		if (Main.client != null && Main.master != null) {
			Main.server.run(Main.client, Main.master);
		} else {
			System.err.println("Main.client is null");
			System.exit(-1);
		}

		searchPartners();

	}

	private static void searchPartners() {
		List<InetAddress> potentialLeftPartners = Main.broadcastSender
				.sendBroadcast(1);

		if (potentialLeftPartners.isEmpty()) {

			finishedInitProcess();

		} else {

			while (!tryToGoIntoNet(potentialLeftPartners)) {
				// Keine will eine Verbindung eingehen
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Logging.log(Logger.Main, "Thread.sleep " + e.getMessage());
				}
			}
			finishedInitProcess();

		}
	}

	private static boolean tryToGoIntoNet(List<InetAddress> potentialLeftPartners) {

		// Probiere alle möglichen Partner durch, bis einer annimmt.

		for (int i = 0; i < potentialLeftPartners.size(); i++) {

			InetAddress leftPartner = potentialLeftPartners.get(i);

			//
			Logging.log(Logger.Main,
					"Left Partner Address: " + leftPartner.toString());

			// Objekt vom Remote Partner holen
			try {

				final String rmiURL = "rmi:/" + leftPartner.toString() + ":"
						+ Config.RMI_PORT + "/" + "Client";

				Logging.log(Logger.Main, "Connect to " + rmiURL);

				final IClient leftClient = (IClient) Naming.lookup(rmiURL);

				Logging.log(Logger.Main, "You got the leftClient Remote Object");
				Logging.log(Logger.Main,
						"Call now leftClient.tryToConnect() ... ");

				// Windows -> Mac | Mac -> Windows: tryToConnect funktioniert
				// Debian -> Mac | Mac -> Debian: tryToConnect funktioniert
				// nicht, sayHello schon
				// Es liegt am Argument IClient ... keine Ahnung warum es einmal
				// mit geht und wo anders nicht.

				final boolean connected = Main.client
						.tryToConnectToClient(leftClient);

				if (connected) {
					Logging.log(Logger.Main, "Verbindung erfolgreich mit "
							+ rmiURL + " aufgebaut.");

					return true;
				} else {
					Logging.log(Logger.Main, "Connection refused by " + rmiURL);
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}

		// Keiner ist eine Verbindung eingegegangen.
		return false;
	}

	private static void finishedInitProcess() {
		// Verbindung ist zustande gekommen
		Main.broadcastServer.start();

		// Starte SolvingPhilosopherProblem
		Logging.log(Logger.Main, "Starte SolvingPhilosopherProblem");
		startSolvingPhilosopherProblem();
	}

	private static void startSolvingPhilosopherProblem() {

		
		
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

	public static Table getTable() {
		return table;
	}
}
