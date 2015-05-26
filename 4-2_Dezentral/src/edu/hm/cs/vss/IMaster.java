package edu.hm.cs.vss;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMaster extends Remote {

	public void addPhilosopher(boolean isHungry) throws RemoteException;
	
}
