package edu.hm.cs.vss.cli;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.hm.cs.vss.Config;
import edu.hm.cs.vss.IMaster;

public class Main {

	private static boolean running = false;
	private static String[] args;
	
	private static IMaster master;
	
	public static void main(String[] args) throws RemoteException {
		Main.args = args;
		try {
			Registry registry = LocateRegistry.getRegistry(Config.RMI_PORT);
			try {
				master = (IMaster)registry.lookup("Master");
				running = true;
			} catch (NotBoundException e) {
				running = false;
			}
		} catch (RemoteException e) {
			running = false;
		}
				
		if(args.length > 0) {
			if(args[0].equals("start")) {
				start();
			} else {
				if(!running) {
					System.out.println("No Client local running.");
					return;
				}
				switch(args[0]) {
				case "status": status(); break;
				case "help": help(); break;
				case "stop": stop(); break;
				case "add": add(); break;
				}
			}
			
		} else {
			help();
		}
		
	}
	
	private static void help() {
		System.out.println("Usage:");
		System.out.println("java edu.hm.cs.vss.cli.Main help");
		System.out.println("java edu.hm.cs.vss.cli.Main start");
		System.out.println("java edu.hm.cs.vss.cli.Main stop");
		System.out.println("java edu.hm.cs.vss.cli.Main status");
		System.out.println("java edu.hm.cs.vss.cli.Main add philosopher [hungry]");
		System.out.println("java edu.hm.cs.vss.cli.Main add seat");
	}
	
	private static void status() {
		try {
			while(true) {
				System.out.println(master.status());
				Thread.sleep(50);
			}
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void start() {
		System.out.println("start...");
	}
	
	private static void stop() {
		System.out.println("stop...");
	}
	
	private static void add() throws RemoteException {
		if(args.length > 1) {
			if(args[1].equals("philosopher")) {
				if(args.length > 2) {
					if(args[2].equals("hungry")) {
						master.addPhilosopher(true);
					} else {
						master.addPhilosopher(false);
					}
				} else {
					master.addPhilosopher(false);
				}
			} else if(args[1].equals("seat")) {
				master.addSeat();
			}
		} else {
			help();
		}
	}
	
	

}
