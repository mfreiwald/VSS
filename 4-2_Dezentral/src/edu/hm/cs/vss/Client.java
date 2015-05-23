package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Client extends UnicastRemoteObject implements IClient {

	private static final long serialVersionUID = System.currentTimeMillis();
		
	 IClient left1;
	 IClient left2;
	 IClient right1;
	 IClient right2;

	public Client() throws RemoteException {
		super();
	}

	
	public boolean tryToConnectToClient(IClient newLeft) {
		
		try {
			IClient newRight = newLeft.setRight(this);
			this.left1 = newLeft;
			if(newRight == null) {
				this.right1 = this.left1;
			} else {
				this.right1 = newRight;
			}
			this.right1.setLeft(this);
			
			return true;
		} catch (RemoteException e) {
			Logging.log(Logger.Client, "Can't connect to newLeft Client. " + e.getMessage());
			return false;
		}
	}
	
	
	
	@Override
	public String getUUID() {
		return Config.SERIAL_UUID;
	}
	
	@Override
	public boolean isAlive() throws RemoteException {
		return true;
	}


	/*
	@Override
	public synchronized boolean tryToConnect(IClient i) throws RemoteException {
		
		// i.getUUID() !!! Nur so viel Methodenaufrufe wie notwenig!!!
		Logging.log(Logger.Client, i.getUUID() + " möchte mit mir eine Verbindung aufbauen.");
		
		
		// Es möchte ein neuer Client an meine rechte Seite..
		// Alles anhalten bzw. den Sitzplatz ganz rechts anhalten
		// ggf. muss gewartet werden, bis speisender Philosoph fertig ist.
		
		// Philosoph ist fertig
		
		// Platz kann gesperrt werden
		
		
		
		return true;
	}
	*/
	
	public synchronized IClient setRight(IClient newRight) throws RemoteException {
		
		
		//Main.getBroadcastServer().enableDelay();
		
		
		IClient oldRight = this.right1;
		this.right1 = newRight;
		
		newRight.setLeft(this);
		
		return oldRight;
	}
	
	

	
	public void setLeft(IClient newLeft) throws RemoteException {
		this.left1 = newLeft;
	}
	

	
	private void setLeft1(IClient client) {
		this.left1 = client;
		new ClientAliveRecognizer(this.left1).start();
	}
	
	private void setRight1(IClient client) {
		this.right1 = client;
		new ClientAliveRecognizer(this.right1).start();
	}
	
	
	
	
	
	
	public List<String> iterate(List<String> clients) throws RemoteException {
		
		List<String> tmp = new ArrayList<>();
		tmp.addAll(clients);
		
		IClient ich = this;
		if(!tmp.contains(ich.getUUID())) {
			tmp = this.left1.iterate(tmp);
		}
		tmp.add(ich.getUUID());
		return tmp;
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
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
	*/
	
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
			left2 = left1.getLeft();

		if (left2 == this) {
			left2 = null;
		}

		if (right1 != null)
			right2 = right1.getRight();

		if (right2 == this) {
			right2 = null;
		}

	}

	public IClient getLeft() {
		return this.left1;
	}

	public IClient getRight() {
		return this.right1;
	}
	

}
