package edu.hm.cs.vss;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
	
	public void setLeft(Client newLeft) throws RemoteException;
	
}
