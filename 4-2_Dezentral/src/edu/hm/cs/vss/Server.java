package edu.hm.cs.vss;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	
	public void run(final Client client) {

		try {
			Registry registry = LocateRegistry.createRegistry(Config.RMI_PORT);
			registry.bind("Client", client);
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
