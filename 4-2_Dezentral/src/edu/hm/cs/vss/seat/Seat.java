package edu.hm.cs.vss.seat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Seat extends UnicastRemoteObject implements ISeat {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

	}


	protected Seat() throws RemoteException {
		super();
	}
	
	
	

}
