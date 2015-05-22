package edu.hm.cs.vss;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
	
	public String getUUID() throws RemoteException;
	public void sayHello() throws RemoteException;
	public boolean tryToConnect(IClient i) throws RemoteException;
	
	
	
	
	public void setLeft(IClient newLeft) throws RemoteException;
	IClient ichBinDeinNeuerRechter(IClient newRight) throws RemoteException;
	IClient ichBinDeinNeuerLinker(IClient newLeft) throws RemoteException;
	void findNeighbours() throws RemoteException;
	
	IClient getLeft1() throws RemoteException;
	IClient getRight1() throws RemoteException;
}
