package edu.hm.cs.vss.test;

import java.awt.Button;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.JButton;

import edu.hm.cs.vss.Config;
import edu.hm.cs.vss.IClient;

public class TestClients {

	
	public static void main(String[] args) throws RemoteException {

		String s = args[0];
		System.out.println(s);
		
		
		if (s.equals("left")) {
			clients = leftTurn();
		} else {
			clients = rightTurn();
		}
		
		
		draw();
		
		for(IClient c: clients) {
			printClient(c);
		}
		
	}
	
	private static ArrayList<IClient> clients;
	private static final int nodeWidth = 50;
	private static final int width = 250;
	
	private static void draw() throws HeadlessException, RemoteException {
		
		Frame frame = new Frame();
		frame.setSize(width, 50 + clients.size()*70 + 50);  
		frame.setVisible(true);  
				
		
		double nodes = clients.size();
		int nodesPerSide = (int) Math.ceil(nodes/2);
		System.out.println(Math.ceil(nodes/2));

		
		System.out.println(clients.size());
		
		
		for(int i=0; i<nodesPerSide; i++) {
			IClient client = clients.get(i);
			Button n = new Button(client.getUUID().substring(0, 4));
			n.setBounds(width-100, 40+70*i, nodeWidth, nodeWidth);
			frame.add(n);
		}
		
		for(int i=nodesPerSide; i<clients.size(); i++) {
			IClient client = clients.get(i);
			Button n = new Button(client.getUUID().substring(0, 4));
			n.setBounds(50, 10+70*nodesPerSide - (40+70*(i-nodesPerSide)), nodeWidth, nodeWidth);
			frame.add(n);
		}
	}
	
	
	

	private static ArrayList<IClient> leftTurn() {
		ArrayList<IClient> clients = new ArrayList<>();

		try {
			Registry registry = LocateRegistry.getRegistry(Config.RMI_PORT);

			IClient next = (IClient) registry.lookup("Client");

			while (next != null && !clients.contains(next)) {
				if(next != null) {
					System.out.println("Add "+next.getUUID());
					clients.add(next);
					next = next.getLeft();
				}
			}

		} catch (RemoteException e) {
			System.out
					.println("No Registy found. Maybe the PhilosopherProblemSolvingUnit not running..");
			System.out.println(e.getMessage());
		} catch (NotBoundException e) {
			System.out
					.println("No central unit \""
							+ e.getMessage()
							+ "\" found in the registry. Maybe the PhilosopherProblemSolvingUnit not running..");
		}
		
		return clients;
	}

	private static ArrayList<IClient> rightTurn() {
		ArrayList<IClient> clients = new ArrayList<>();

		try {
			Registry registry = LocateRegistry.getRegistry(Config.RMI_PORT);

			IClient next = (IClient) registry.lookup("Client");

			while (!clients.contains(next)) {
				clients.add(next);
				if(next != null) {
					next = next.getRight();
				}
			}

		} catch (RemoteException e) {
			System.out
					.println("No Registy found. Maybe the PhilosopherProblemSolvingUnit not running..");
			System.out.println(e.getMessage());
		} catch (NotBoundException e) {
			System.out
					.println("No central unit \""
							+ e.getMessage()
							+ "\" found in the registry. Maybe the PhilosopherProblemSolvingUnit not running..");
		}
		
		return clients;
	}

	private static void printClient(IClient client) throws RemoteException {
		if (client == null) {
			return;
		}
		System.out.println("\t" + client.getUUID());

		IClient left = client.getLeft();
		IClient right = client.getRight();
		if (left != null && right != null) {
			System.out.println(left.getUUID() + "  " + right.getUUID());
		} else {
			System.out.println();
		}

		IClient left2 = client.getLeft2();
		IClient right2 = client.getRight2();
		if (left2 != null && right2 != null) {
			System.out.println(left2.getUUID() + "  " + right2.getUUID());
		} else {
			System.out.println();
		}
		
		System.out.println();
	}
}
