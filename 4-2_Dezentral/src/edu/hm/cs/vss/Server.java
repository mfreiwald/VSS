package edu.hm.cs.vss;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	public void run(final Client client, final Master master) {

		try {

			Registry registry = LocateRegistry.createRegistry(Config.RMI_PORT);

			registry.bind("Client", client);
			registry.bind("Master", master);

		} catch (AlreadyBoundException e) {
			Logging.log(Logger.Server, e.getMessage());
		} catch (RemoteException e) {
			Logging.log(Logger.Server, e.getMessage());
		}
	}
}
