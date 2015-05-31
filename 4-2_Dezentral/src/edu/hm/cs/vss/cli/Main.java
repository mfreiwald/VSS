package edu.hm.cs.vss.cli;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import edu.hm.cs.vss.Config;
import edu.hm.cs.vss.IMaster;
import edu.hm.cs.vss.Logger;

public class Main {

	private static boolean running = false;
	private static String[] args;

	private static IMaster master;

	public static void main(String[] args) {
		Main.args = args;
		try {
			Registry registry = LocateRegistry.getRegistry(Config.RMI_PORT);
			try {
				master = (IMaster) registry.lookup("Master");
				running = true;
			} catch (NotBoundException e) {
				running = false;
			}
		} catch (RemoteException e) {
			running = false;
		}

		if (args.length > 0) {
			if (args[0].equals("start")) {
				start();
			} else {
				if (!running) {
					System.out.println("No Client local running.");
					return;
				}
				switch (args[0]) {
				case "status":
					status();
					break;
				case "help":
					help();
					break;
				case "stop":
					stop();
					break;
				case "add":
					add();
					break;
				case "remove":
					remove();
					break;
				case "enable":
					enable();
					break;
				case "disable":
					disable();
					break;
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
		System.out
				.println("java edu.hm.cs.vss.cli.Main add philosopher [hungry]");
		System.out.println("java edu.hm.cs.vss.cli.Main add seat");
	}

	private static void status() {
		try {
			if(args.length > 1) {
				switch(args[1]) {
				case "p":
				case "Philosophers":
					while (true) {
						System.out.println(master.statusPhilosophers());
						Thread.sleep(10);
					}
				case "s":
				case "Seats":
					while (true) {
						System.out.println(master.statusSeats());
						Thread.sleep(10);
					}
				}
			}
			
		} catch (RemoteException | InterruptedException e) {
			System.out.println("Client maybe no longer available");
			System.out.println(e.getMessage());
		}
	}

	private static void start() {
		System.out.println("start...");
	}

	private static void stop() {
		System.out.println("stop...");
	}

	private static void add() {
		try {
			if (args.length > 1) {
				if (args[1].equals("philosopher") || args[1].equals("p")) {
					if (args.length > 2) {
						if (args[2].equals("hungry")) {
							master.addPhilosopher(true);
						} else {
							master.addPhilosopher(false);
						}
					} else {
						master.addPhilosopher(false);
					}
				} else if (args[1].equals("seat") || args[1].equals("s")) {
					master.addSeat();
				}
			} else {
				help();
			}
		} catch (RemoteException e) {
			System.out.println("Client maybe no longer available");
		}
	}
	
	private static void remove() {
		try {
			if (args.length > 1) {
				if (args[1].equals("philosopher") || args[1].equals("p")) {
					master.removePhilosopher();
				} else if (args[1].equals("seat") || args[1].equals("s")) {
					master.removeSeat();
				}
			} else {
				help();
			}
		} catch (RemoteException e) {
			System.out.println("Client maybe no longer available");
		}
	}
	
	private static void enable() {
		try {
			if (args.length > 1) {
				switch(args[1]) {
				case "Philosopher": master.enableLogging(Logger.PhilosopherStatus); break;
				}
			} else {
				help();
			}
		} catch (RemoteException e) {
			System.out.println("Client maybe no longer available");
		}
	}
	
	private static void disable() {
		try {
			if (args.length > 1) {
				switch(args[1]) {
				case "Philosopher": master.disableLogging(Logger.PhilosopherStatus); break;
				}
			} else {
				help();
			}
		} catch (RemoteException e) {
			System.out.println("Client maybe no longer available");
		}
	}

}
