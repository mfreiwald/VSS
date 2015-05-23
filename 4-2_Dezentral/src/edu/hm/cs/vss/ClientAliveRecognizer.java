package edu.hm.cs.vss;

import java.rmi.RemoteException;

public class ClientAliveRecognizer extends Thread {

	private final IClient client;

	public ClientAliveRecognizer(IClient client) {
		this.client = client;
	}

	public void run() {
		try {
			while (this.client.isAlive()) {
				try {
					sleep(Config.SEND_ALIVE_INTERVAL);
				} catch (InterruptedException e) {
					Logging.log(Logger.ClientAliveRecognizer,
							"Sleep Exception. " + e.getMessage());
				}
			}
		} catch (RemoteException e) {
			Logging.log(Logger.ClientAliveRecognizer,
					"Client is no longer alive. " + e.getMessage());
		}
	}
}
