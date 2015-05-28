package edu.hm.cs.vss.seat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFork extends Remote {

	public boolean tryAcquire() throws RemoteException;
	public void release() throws RemoteException;
	
	public void setRemoteAcquire(boolean remote) throws RemoteException;
}
