package edu.hm.cs.vss;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import edu.hm.cs.vss.philosophe.PhilosopherBackup;
import edu.hm.cs.vss.seat.ISeat;

public interface IClient extends Remote {
			
	/**
	 * Gibt die eindeutige UUID eines Clients zurück
	 * @return
	 * @throws RemoteException
	 */
	public String getUUID() throws RemoteException;
	
	public IClient newRight(IClient newRight) throws RemoteException;
	public void newLeft(IClient newLeft) throws RemoteException;
	
	public IClient getLeft() throws RemoteException;
	//public IClient getRight() throws RemoteException;
	public IClient getLeft2() throws RemoteException;
	public IClient getRight2() throws RemoteException;
	public IClient getRightRemote() throws RemoteException;
	
	public void findNeighbours() throws RemoteException;

	public boolean isAlive() throws RemoteException;
	public ISeat getFirstSeat() throws RemoteException;
	
	public List<PhilosopherBackup> backup() throws RemoteException;
	
	public int nrRunningPhilosophers() throws RemoteException;
	public void importPhilosophers(List<PhilosopherBackup> philosophers) throws RemoteException;
	
	public double searchGlobalEatingAVG(IClient startingClient, double avg) throws RemoteException;
	
	public List<String> iterate(List<String> clients) throws RemoteException;
		
}
