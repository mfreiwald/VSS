package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements IClient {

	private static final long serialVersionUID = 1L;
	private Client left1;
	private Client left2;
	private Client right1;
	private Client right2;
	
	protected Client() throws RemoteException {
		super();
	}
	
	@Override
	public void setLeft(Client newLeft) {
		this.left1 = newLeft;
		
		// sag ihm du bist der neue rechte
		Client newRight = this.left1.ichBinDeinNeuerRechter(this);
		
		if (newRight != null) {
			this.right1 = newRight;
		} else {
			this.right1 = this.left1;
		}
		this.right1.ichBinDeinNeuerLinker(this);

		findNeighbours();
		
		if(this.left1 != null)
			this.left1.findNeighbours();
		
		if(this.right1 != null)
			this.right1.findNeighbours();
		
		if(this.left2 != null)
			this.left2.findNeighbours();
		
		if(this.right2 != null)
			this.right2.findNeighbours();
	}
	
	private Client ichBinDeinNeuerRechter(Client newRight) {
		Client oldRight = this.right1;
		this.right1 = newRight;
		if(this.left1 == null) {
			this.left1 = this.right1;
		}
		
		return oldRight;
	}
	
	private Client ichBinDeinNeuerLinker(Client newLeft) {
		Client oldLeft = this.left1;
		this.left1 = newLeft;
		if(this.right1 == null) {
			this.right1 = this.left1;
		}
		
		return oldLeft;
	}
	
	
	private void findNeighbours() {
		
		if(left1 != null)
			left2 = left1.left1;
				
		if(left2 == this) {
			left2 = null;
		}
		
		if(right1 != null) 
			right2 = right1.right1;
				
		if(right2 == this) {
			right2 = null;
		}
		
		
	}
	
}
