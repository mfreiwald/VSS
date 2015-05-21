package edu.hm.cs.vss.init;

import java.io.Serializable;

public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String name;
	private final int port;
	
	public Client(String name, int port) {
		this.name = name;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}
	
	public String toString() {
		return this.name+":"+this.port;
	}
	
	
}
