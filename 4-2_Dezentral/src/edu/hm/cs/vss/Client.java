package edu.hm.cs.vss;

import java.rmi.RemoteException;
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

			IClient newRight = newLeft.newRight(this);

			this.setLeft(newLeft);

			if (newRight == null) {
				this.setRight(this.left1);
			} else {
				this.setRight(newRight);
			}

			this.right1.newLeft(this);

			
			findNeighbours();

			if (this.left1 != null)
				this.left1.findNeighbours();

			if (this.right1 != null)
				this.right1.findNeighbours();

			if (this.left2 != null)
				this.left2.findNeighbours();

			if (this.right2 != null)
				this.right2.findNeighbours();
			
			
			return true;
		} catch (RemoteException e) {
			Logging.log(Logger.Client,
					"Can't connect to newLeft Client. " + e.getMessage());
			return false;
		}
	}

	private void setLeft(IClient newLeft) {
		this.left1 = newLeft;
		try {
			findNeighbours();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setRight(IClient newRight) {
		this.right1 = newRight;
		try {
			findNeighbours();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getUUID() {
		return Config.SERIAL_UUID;
	}

	@Override
	public IClient newRight(IClient newRight) throws RemoteException {
		// Main.getBroadcastServer().enableDelay();

		IClient oldRight = this.right1;

		this.setRight(newRight);

		return oldRight;
	}

	@Override
	public void newLeft(IClient newLeft) throws RemoteException {
		this.setLeft(newLeft);
	}

	@Override
	public IClient getLeft() {
		return this.left1;
	}

	@Override
	public IClient getRight() {
		return this.right1;
	}

	@Override
	public IClient getLeft2() {
		return this.left2;
	}

	@Override
	public IClient getRight2() {
		return this.right2;
	}

	@Override
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

	@Override
	public boolean isAlive() throws RemoteException {
		return true;
	}

	@Override
	public List<String> iterate(List<String> clients) throws RemoteException {

		List<String> tmp = new ArrayList<>();
		tmp.addAll(clients);

		IClient ich = this;
		if (!tmp.contains(ich.getUUID())) {
			tmp = this.left1.iterate(tmp);
		}
		tmp.add(ich.getUUID());
		return tmp;
	}

	@Override
	public String toString() {
		return this.getUUID();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}

}
