package edu.hm.cs.vss.test;

public class Client {

	public Client left1;
	public Client left2;
	public Client right1;
	public Client right2;
	
	public final int nr;
	
	public Client(int nr) {
		this.nr = nr;
	}
	
	
	// setzte den linken client auf den neuen
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
	
	public void leftIsBroken() {
		if(this.left2 == null) {
			this.left1 = null;
		} else {
			this.setLeft(left2);
		}
	}
	
	public void rightIsBroken() {
		if(this.right2 == null) {
			this.right1 = null;
		} else {
			
		}
	}
	
	public void print() {
		System.out.println("        " + this.toString() + "   ");
		System.out.println(left1 + "       " + right1);
		System.out.println(left2 + "       " + right2);
	}
	
	public void printCircle() {
		this.printCircle(this);
	}
	
	private void printCircle(Client c) {
		System.out.println(c);
		if(c.left1 != null && c.left1 != this)
			printCircle(c.left1);
	}
	
	@Override
	public String toString() {
		return "Client " + this.nr;
	}
}
