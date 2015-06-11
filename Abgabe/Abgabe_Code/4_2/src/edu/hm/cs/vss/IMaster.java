package edu.hm.cs.vss;

import java.rmi.Remote;
import java.rmi.RemoteException;

import edu.hm.cs.vss.philosophe.PhilosopherBackup;

public interface IMaster extends Remote {

	public void addPhilosopher(boolean isHungry) throws RemoteException;
	public void addSeat() throws RemoteException;
	
	public PhilosopherBackup removePhilosopher() throws RemoteException;
	public boolean removeSeat() throws RemoteException;
	
	public String statusPhilosophers() throws RemoteException;
	public String statusSeats() throws RemoteException;
	
	public void enableLogging(Logger logger) throws RemoteException;
	public void disableLogging(Logger logger) throws RemoteException;
	
	public void stopClient() throws RemoteException;
}
