package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements IClient {

	private static final long serialVersionUID = 1L;
	
	
	
	private IClient left1;
	private IClient left2;
	private IClient right1;
	private IClient right2;

	public Client() throws RemoteException {
		super();
	}

	@Override
	public String getUUID() {
		return Config.SERIAL_UUID;
	}

	@Override
	public boolean tryToConnect(IClient i) throws RemoteException {
		Logging.log(Logger.Client, i.getUUID()
				+ " möchte mit mir eine Verbindung aufbauen.");
		
		
		return true;
	}

	
	
	
	@Override
	public void sayHello() throws RemoteException {
		System.out.println("Da hat wohl jemand \"Hallo\" gesagt..");
	}
	
	@Override
	public synchronized void makeLongCalculation(IClient remote) throws RemoteException {
		System.out.println(remote.getUUID() + " möchte etwas berechnen lassen. " + System.currentTimeMillis());
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Hey " + remote.getUUID() + ". Wir sind fertig damit. " + System.currentTimeMillis());
	}

	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void setLeft(IClient newLeft) throws RemoteException {
		this.left1 = newLeft;

		// sag ihm du bist der neue rechte
		IClient newRight = this.left1.ichBinDeinNeuerRechter(this);

		if (newRight != null) {
			this.right1 = newRight;
		} else {
			this.right1 = this.left1;
		}
		this.right1.ichBinDeinNeuerLinker(this);

		findNeighbours();

		if (this.left1 != null)
			this.left1.findNeighbours();

		if (this.right1 != null)
			this.right1.findNeighbours();

		if (this.left2 != null)
			this.left2.findNeighbours();

		if (this.right2 != null)
			this.right2.findNeighbours();
	}

	public IClient ichBinDeinNeuerRechter(IClient newRight) {
		IClient oldRight = this.right1;
		this.right1 = newRight;
		if (this.left1 == null) {
			this.left1 = this.right1;
		}

		return oldRight;
	}

	public IClient ichBinDeinNeuerLinker(IClient newLeft) {
		IClient oldLeft = this.left1;
		this.left1 = newLeft;
		if (this.right1 == null) {
			this.right1 = this.left1;
		}

		return oldLeft;
	}

	public void findNeighbours() throws RemoteException {
		Logging.log(Logger.Client, "Suche deine Nachbarn..");
		if (left1 != null)
			left2 = left1.getLeft1();

		if (left2 == this) {
			left2 = null;
		}

		if (right1 != null)
			right2 = right1.getRight1();

		if (right2 == this) {
			right2 = null;
		}

	}

	public IClient getLeft1() {
		return this.left1;
	}

	public IClient getRight1() {
		return this.right1;
	}

}
