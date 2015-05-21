package edu.hm.cs.vss.init;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;

public class RegistryServer extends UnicastRemoteObject implements IRegistryServer {

private static final long serialVersionUID = 1L;
	private List<Client> clients = new ArrayList<>();
	
	protected RegistryServer() throws RemoteException {
		super();
	}
	
	@Override
	public void addClient(Client client) {
		this.clients.add(client);
		System.out.println("Client "+ client+" registred.");
	}
	
	@Override
	public List<Client> getClients() {
		return this.clients;
	}
	
	@Override
	public Client heyDerClientIstAusgefallenGibMirDenNaechsten(Client ich, Client ausgefallenerClient) throws RemoteException {
		int ichIndex = this.clients.indexOf(ich);
		int ausgefallenerIndex = this.clients.indexOf(ausgefallenerClient);
		
		if(ichIndex < ausgefallenerIndex) {
			return this.clients.get((ausgefallenerIndex+1) % this.clients.size());
		} else if (ichIndex > ausgefallenerIndex) {
			return this.clients.get((ausgefallenerIndex-1) % this.clients.size());
		} else {
			throw new RemoteException("alles scheise...");
		}
	}
	
	public static final int PORT = 4710; 

	public static void main(String[] args) throws RemoteException {
		
		
		Registry registry = LocateRegistry.createRegistry(PORT);

		
		RegistryServer server = new RegistryServer();
		try {
			registry.bind("RegistryServer", server);
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	


}
