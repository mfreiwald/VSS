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
		System.out.println("java edu.hm.cs.vss.cli.Main status philosopher | p");
		System.out.println("java edu.hm.cs.vss.cli.Main status seat | s");
		System.out.println("java edu.hm.cs.vss.cli.Main add philosopher | p [hungry]");
		System.out.println("java edu.hm.cs.vss.cli.Main add seat | s");
		System.out.println("java edu.hm.cs.vss.cli.Main enable <Logger>");
		System.out.println("java edu.hm.cs.vss.cli.Main disable <Logger>");
		System.out.println();
		System.out.println("Loggers:");
		System.out.println(" "+Logger.Philosopher);
		System.out.println(" "+Logger.PhilosopherStatus);
		System.out.println(" "+Logger.Seat);
		System.out.println(" "+Logger.Master);
		System.out.println(" "+Logger.Main);
		System.out.println(" "+Logger.BroadcastServer);
		System.out.println(" "+Logger.BroadcastSender);
		System.out.println(" "+Logger.Client);
		System.out.println(" "+Logger.Table);
		System.out.println(" "+Logger.Server);
		System.out.println(" "+Logger.BackupRightThread);
		System.out.println(" "+Logger.CheckPhilosophersThread);
		System.out.println(" "+Logger.SpeadPhilosopherThread);
	}

	private static void status() {
		try {
			if(args.length > 1) {
				switch(args[1]) {
				case "p":
				case "Philosophers":
					while (true) {
						System.out.println(master.statusPhilosophers());
						Thread.sleep(Config.STATUS_UPDATE_INTERVAL);
					}
				case "s":
				case "Seats":
					while (true) {
						System.out.println(master.statusSeats());
						Thread.sleep(Config.STATUS_UPDATE_INTERVAL);
					}
				default: 
					help();
					break;
				}
			} else {
				help();
			}
			
		} catch (RemoteException | InterruptedException e) {
			System.out.println("Client maybe no longer available");
			System.out.println(e.getMessage());
		}
	}

	private static void start() {
		System.out.println("start...");
		System.out.println("Not implemented..");
	}

	private static void stop() {
		System.out.println("stop...");
		System.out.println("Not implemented..");
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
				} else {
					help();
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
				} else {
					help();
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
				case "Philosopher": 
				case "p":
					master.enableLogging(Logger.PhilosopherStatus); break;
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
				Logger log = null;
				switch(args[1]) {
				
				case "BroadcastServer": log = Logger.BroadcastServer; break;
				case "BroadcastSender": log = Logger.BroadcastSender; break;
				case "Client": log = Logger.Client; break;
				case "Server": log = Logger.Server; break;
				case "Main": log = Logger.Main; break;
				case "Table": log = Logger.Table; break;
				case "Seat": log = Logger.Seat; break;
				case "Philosopher": log = Logger.Philosopher; break;
				case "PhilosopherStatus": log = Logger.PhilosopherStatus; break;
				case "Master": log = Logger.Master; break;
				case "BackupRightThread": log = Logger.BackupRightThread; break;
				case "SpeadPhilosopherThread": log = Logger.SpeadPhilosopherThread; break;
				case "CheckPhilosophersThread": log = Logger.CheckPhilosophersThread; break;
				
				}
				
				if(log != null) {
					master.disableLogging(log);
				} else {
					help();
				}
			} else {
				help();
			}
		} catch (RemoteException e) {
			System.out.println("Client maybe no longer available");
		}
	}

}
