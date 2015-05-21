package edu.hm.cs.vss.init;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRegistryServer extends Remote {

	public void addClient(Client client) throws RemoteException;
	public List<Client> getClients() throws RemoteException;
	
	public Client heyDerClientIstAusgefallenGibMirDenNaechsten(Client ich, Client ausgefallenerClient) throws RemoteException;
}
