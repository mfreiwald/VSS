package edu.hm.cs.vss;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
	
	public void setLeft(IClient newLeft) throws RemoteException;
	IClient ichBinDeinNeuerRechter(IClient newRight) throws RemoteException;
	IClient ichBinDeinNeuerLinker(IClient newLeft) throws RemoteException;
	void findNeighbours() throws RemoteException;
	
	IClient getLeft1();
	IClient getRight1();
}
