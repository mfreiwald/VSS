package edu.hm.cs.vss.seat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISeat extends Remote {

	public IFork getLeftFork() throws RemoteException;
	
}
