package edu.hm.cs.vss.test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vss.Config;
import edu.hm.cs.vss.IClient;

public class TestClients {

	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.getRegistry(Config.RMI_PORT);
			
			final IClient client = (IClient) registry.lookup("Client");
			
			try {
				
				List<String> clients = new ArrayList<>();
				clients = client.iterate(clients);
				
				System.out.println("All Clients:");
				System.out.println(clients.toString());
				
			} catch (RemoteException re) {
				System.out.println(re.getMessage());
			}
			
		} catch (RemoteException e) {
			System.out.println("No Registy found. Maybe the PhilosopherProblemSolvingUnit not running..");
			System.out.println(e.getMessage());
		} catch (NotBoundException e) {
			System.out.println("No central unit \"" + e.getMessage() + "\" found in the registry. Maybe the PhilosopherProblemSolvingUnit not running..");
		}

	}

}
