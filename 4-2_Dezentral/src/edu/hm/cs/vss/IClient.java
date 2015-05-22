package edu.hm.cs.vss;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
	public boolean tryToConnect(IClient i) throws RemoteException;
	
	
	public void sayHello() throws RemoteException;
	public void makeLongCalculation(IClient remote) throws RemoteException;
	
	
	
	public void setLeft(IClient newLeft) throws RemoteException;
	IClient ichBinDeinNeuerRechter(IClient newRight) throws RemoteException;
	IClient ichBinDeinNeuerLinker(IClient newLeft) throws RemoteException;
	void findNeighbours() throws RemoteException;
	
	IClient getLeft1() throws RemoteException;
	IClient getRight1() throws RemoteException;
}
