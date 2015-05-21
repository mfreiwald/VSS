package edu.hm.cs.vss;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import edu.hm.cs.vss.init.Client;
import edu.hm.cs.vss.init.IRegistryServer;
import edu.hm.cs.vss.init.RegistryServer;

public class Server {
		
	public static void main(String[] args) throws RemoteException {
		final int port = (args.length > 0) ? Integer.parseInt(args[0]) : -1;
		if(port == -1) {
			System.exit(-1);
		}
		final String registryServer = (args.length > 1) ? args[1] : "";
		if(registryServer.isEmpty()) {
			System.exit(-2);
		}
		
		final Client client = new Client("localhost", port);
		final Master master = new Master();
		final String masterObjName = "Master";
		
		Registry registry = LocateRegistry.createRegistry(port);
		
		try {
			registry.bind(masterObjName, master);
			System.out.println (masterObjName+" bound to registry, port " + port + ".");  
			
			
		    try {
				IRegistryServer obj = (IRegistryServer) Naming.lookup ("rmi://"+registryServer+":" + RegistryServer.PORT + "/" + "RegistryServer");
				obj.addClient(client);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		    
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
				
	}

}
