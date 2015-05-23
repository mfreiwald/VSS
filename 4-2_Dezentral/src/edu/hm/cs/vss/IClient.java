package edu.hm.cs.vss;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IClient extends Remote {
			
	/**
	 * Gibt die eindeutige UUID eines Clients zurück
	 * @return
	 * @throws RemoteException
	 */
	public String getUUID() throws RemoteException;
	
	/**
	 * Client kann angefragt werden, ob eine Verbindung zurstande kommen kann.
	 * @param i Die anzufragende Client
	 * @return Ergebnis ob Anfrage erfolgreich war.
	 * @throws RemoteException
	 */
	//public boolean tryToConnect(IClient i) throws RemoteException;
	
	public IClient setRight(IClient newRight) throws RemoteException;
	public void setLeft(IClient newLeft) throws RemoteException;

	
	public boolean isAlive() throws RemoteException;
	
	
	public List<String> iterate(List<String> clients) throws RemoteException;
	
	
	
	
	public void sayHello() throws RemoteException;
	public void makeLongCalculation(IClient remote) throws RemoteException;
	
	
	IClient ichBinDeinNeuerRechter(IClient newRight) throws RemoteException;
	IClient ichBinDeinNeuerLinker(IClient newLeft) throws RemoteException;
	void findNeighbours() throws RemoteException;
	
	IClient getLeft() throws RemoteException;
	IClient getRight() throws RemoteException;
}
