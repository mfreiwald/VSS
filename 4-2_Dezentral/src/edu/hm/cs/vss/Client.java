package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.vss.philosophe.Philosopher;
import edu.hm.cs.vss.philosophe.PhilosopherBackup;
import edu.hm.cs.vss.seat.Fork;
import edu.hm.cs.vss.seat.Seat;

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
		Logging.log(Logger.Client, "Try to Connect to..");

		try {

			// Fehlerfall Beispiel:
			// Hier: newRight läuft noch.. newLeft kennt newRight nicht mehr
			// nach dem Aufruf, kennt allerdings noch seinen 2ten
			IClient newRight = newLeft.newRight(this);

			this.setLeft(newLeft);

			boolean setRightSuccess = false;

			while (!setRightSuccess) {

				if (newRight == null) {
					this.setRight(this.left1);
				} else {
					this.setRight(newRight);
				}

				// Inzwischen ist newRight beendet worden oder abgestürzt
				// Methodenaufruf wirft Exception, da newRight nicht mehr
				// vorhanden.. Was tun?
				// Lösung: newLeft.getRight2() holen und diesen als rechten
				// Partner nehmen..

				try {
					this.right1.newLeft(this);
					setRightSuccess = true;
				} catch (RemoteException e) {
					// ConnectionTimeOut -> Sicher das right1 nicht mehr
					// existiert?

					// Wenn wir nur zu zweit sind und die Exception fliegt, dann
					// ist newLeft auch weg..
					if (this.right1 == this.left1) {
						this.right1 = null;
						this.left1 = null;
						this.right2 = null;
						this.left2 = null;
						return false;
					}

					Logging.log(Logger.Client, "Probier den übernächsten");

					// Dann bei newLeft nach dem zweiten rechten Anfragen
					newRight = newLeft.getRight2();
					setRightSuccess = false;
				}
			}

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
		try {
			if (newLeft == null) {
				this.left1 = null;
				this.left2 = null;
				this.right2 = null;
				this.right1 = null;
			} else if (newLeft.getUUID().equals(this.getUUID())) {
				this.left1 = null;
				this.left2 = null;
				this.right2 = null;
				this.right1 = null;
			}
			this.left1 = newLeft;
		} catch (RemoteException e) {
			Logging.log(Logger.Client, "Set Left Remote Exception "+e.getMessage());
			this.left1 = null;
			this.left2 = null;
			this.right2 = null;
			this.right1 = null;
		}
	}

	private synchronized void setRight(IClient newRight) {
		try {
			if (newRight == null) {
				this.left1 = null;
				this.left2 = null;
				this.right2 = null;
				this.right1 = null;
			} else if (newRight.getUUID().equals(this.getUUID())) {
				this.left1 = null;
				//this.left2 = null;
				this.right2 = null;
				this.right1 = null;
			}
			this.right1 = newRight;
		} catch (RemoteException e) {
			Logging.log(Logger.Client, "Set Right Remote Exception "+e.getMessage());
			this.left1 = null;
			this.left2 = null;
			this.right2 = null;
			this.right1 = null;
		}
	}

	@Override
	public String getUUID() {
		return Config.SERIAL_UUID;
	}

	@Override
	public IClient newRight(IClient newRight) throws RemoteException {
		// Main.getBroadcastServer().enableDelay();
		// Erst warten bis der ganz rechte Platz nicht mehr belegt ist.. und
		// dann blockieren
		Seat rightestSeat = Main.getTable().getSeat(
				Main.getTable().nrSeats() - 1);
		
		Main.getBroadcastServer().enableDelay();
		
		rightestSeat.block();

		IClient oldRight = this.right1;

		this.setRight(newRight);

		// ganz rechten Platz wieder freigeben
		rightestSeat.unblock();
		
		Main.getBroadcastServer().disableDelay();

		return oldRight;
	}

	@Override
	public void newLeft(IClient newLeft) throws RemoteException {
		// prüfen ob erste linke Gabel remote blockiert ist
		// kann nur blockiert sein, wenn der linke Client ausgefallen ist

		Fork leftFork = this.getFirstSeat().getLeftFork();
		if (leftFork.isRemoteAcquire()) {
			leftFork.newFork();
		}

		this.setLeft(newLeft);
	}

	
	@Override
	public IClient getLeft() {
		if (this.left1 == null) {
			return null;
		}
		boolean isAlive = false;
		try {
			isAlive = this.left1.isAlive();
		} catch (RemoteException e) {
			isAlive = false;
		}

		if (!isAlive) {
			
			this.setLeft(this.left2);
			/*
			try {

				findNeighbours();

				if (this.left1 != null)
					this.left1.findNeighbours();

				if (this.right1 != null)
					this.right1.findNeighbours();

				if (this.left2 != null)
					this.left2.findNeighbours();

				if (this.right2 != null)
					this.right2.findNeighbours();
			} catch (RemoteException e) {
				// ToDo:
				Logging.log(Logger.Client, "getLeft Exception "+e.getMessage());
			}
			*/
			
		}

		return this.left1;
	}
	
	
	@Override // synchronized??
	public synchronized IClient getRight() {
		if (this.right1 == null) {
			return null;
		}
		boolean isAlive = false;
		try {
			isAlive = this.right1.isAlive();
		} catch (RemoteException e) {
			isAlive = false;
		}

		if (!isAlive) {

			// Schattenkopien wiederherstellen
			Logging.log(Logger.Client, "Rechter Client ist ausgefallen.");
			
			this.setRight(this.right2);
			try {

				findNeighbours();
				
				if (this.left1 != null) {
					Logging.log(Logger.Client, "left1 soll Nachbarn suchen");
					this.left1.findNeighbours();
				}
				
				if (this.right1 != null) {
					Logging.log(Logger.Client, "right1 soll Nachbarn suchen");
					this.right1.findNeighbours();
				}
				
				if (this.left2 != null) {
					Logging.log(Logger.Client, "left2 soll Nachbarn suchen");
					this.left2.findNeighbours();
				}

				if (this.right2 != null) {
					Logging.log(Logger.Client, "right2 soll Nachbarn suchen");
					this.right2.findNeighbours();
				}
				
			} catch (RemoteException e) {
				Logging.log(Logger.Client, "getRight Exception "+e.getMessage());
			}

			Main.getMaster().restoreBackup();
		}

		return this.right1;
	}
	
	@Override
	public IClient getRightRemote() {
		if (this.right1 == null) {
			return null;
		}
		boolean isAlive = false;
		try {
			isAlive = this.right1.isAlive();
		} catch (RemoteException e) {
			isAlive = false;
		}

		if (!isAlive) {
			return this.right2;
		}
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
		
		if (left1 != null) {
			Logging.log(Logger.Client, "Suche left2 Nachbarn..");
			left2 = left1.getLeft();
		}
		
		if (left2 != null && left2.getUUID().equals(this.getUUID())) {
			Logging.log(Logger.Client, "left2 bin ich selbst");
			left2 = null;
		}

		if (right1 != null) {
			Logging.log(Logger.Client, "Suche right2 Nachbarn..");
			right2 = right1.getRightRemote();
		}
		
		if (right2 != null && right2.getUUID().equals(this.getUUID())) {
			Logging.log(Logger.Client, "right2 bin ich selbst");
			right2 = null;
		}
		Logging.log(Logger.Client, "Alle Nachbarn gefunden.");
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
	public Seat getFirstSeat() {
		return Main.getTable().getSeat(0);
	}
	
	@Override
	public List<PhilosopherBackup> backup() {
		Logging.log(Logger.BackupRightThread, "Send Philosophers Backup");
		List<PhilosopherBackup> backup = new ArrayList<>();
		for(Philosopher p: Main.getMaster().getPhilosophers()) {
			backup.add(p.backup());
		}
		return backup;
	}
	
	@Override
	public int nrRunningPhilosophers() {
		return Main.getMaster().getPhilosophers().size();
	}

	@Override
	public void importPhilosophers(List<PhilosopherBackup> philosophers)
			throws RemoteException {

		Logging.log(Logger.Client, "Import " + philosophers.size() + " Philosophers");
		Main.getMaster().importPhilosophers(philosophers);
		
	}
	
	@Override
	public String toString() {
		return this.getUUID();
	}
	/*
	 * @Override public boolean equals(Object obj) { return
	 * this.toString().equals(obj.toString()); }
	 */

	
}
