package edu.hm.cs.vss.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConnectClients {

	public static void main(String[] args) {
		
		List<Client> clients = new ArrayList<>();
		
		for(int i=1; i<=4; i++) {
			Client c = new Client(i);
			clients.add(c);
			if(i>2) {
				c.setLeft(clients.get(1));
			} else if (i>1) {
				c.setLeft(clients.get(0));
			}
		}
		
		Client first = clients.get(0);
		first.printCircle();
		
		
		Client c1 = new Client(1);
		Client c2 = new Client(2);
		Client c3 = new Client(3);
		Client c4 = new Client(4);
		Client c5 = new Client(5);
		Client c6 = new Client(6);
		
		/*
		c1.left1 = c2;
		
		Client c2oldRight = c2.ichBinDeinNeuerRechter(c1);
		if(c2oldRight != null) {
			c1.right1 = c2oldRight;
			c2oldRight.ichBinDeinNeuerLinker(c1);
		} else {
			c1.right1 = c1.left1;
		}
		*/
		
		/*
		// c1 ist vorhanden
		
		// c2 kommt neu dazu
		c2.setLeft(c1);
		
		// c3 kommt neu dazu. linker wird 1
		c3.setLeft(c1);

		// c4 kommt neu dazu. linker wird 1
		c4.setLeft(c1);
		
		c5.setLeft(c1);
		c6.setLeft(c1);
		
		c1.print();
		c2.print();
		c3.print();
		c4.print();
		c5.print();
		c6.print();
		*/
		
		/*
		System.out.println("Broke Client2");
		c2 = null;
		c1.leftIsBroken();
		c1.print();
		c3.print();
		c4.print();
		c5.print();
		c6.print();
		*/
	}

}
