package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Master extends UnicastRemoteObject implements IMaster {

	private static final long serialVersionUID = 1L;

	protected Master() throws RemoteException {
		super();
	}

	
	
}
