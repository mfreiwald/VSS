package edu.hm.cs.vss;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	
	public void run(final Client client) throws AccessException, RemoteException {
		Registry registry = LocateRegistry.createRegistry(Config.RMI_PORT);

		try {
			registry.bind("Client", client);
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
}
