package edu.hm.cs.vss;

import java.io.Serializable;

public class ClientInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public final IClient client;
	public final String uuid;
	public final double eatAVG;
	public final int nrPhilosophers;
	public final int nrCPUs;
	
	public ClientInfo(Client c) {
		this.client = c;
		this.uuid = c.getUUID();
		this.eatAVG = c.localEatAVG();
		this.nrPhilosophers = c.nrRunningPhilosophers();
		this.nrCPUs = Runtime.getRuntime().availableProcessors();
	}
	
	public void print() {
		Logging.log(Logger.Client, "ClientInfo: " + this.uuid);
		Logging.log(Logger.Client, "CPUs: " + this.nrCPUs);
		Logging.log(Logger.Client, "eatAVG: " + this.eatAVG);
		Logging.log(Logger.Client, "Philosophers: " + this.nrPhilosophers);
	}
}
