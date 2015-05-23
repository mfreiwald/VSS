package edu.hm.cs.vss;

import java.util.UUID;

public class Config {

	public static final int BROADCAST_PORT = 4711;
	public static final int BROADCAST_DELAY = 200;
	
	
	public static final int WAIT_FOR_BROADCAST_ANSWER_TIMEOUT = 1500; 
	public static final int TIMES_REPEAT_BROADCAST = 3;
	
	public static final String SERIAL_UUID = UUID.randomUUID().toString();
	
	public static final int SEND_ALIVE_INTERVAL = 500;
	
	
	public static final int RMI_PORT = 4712;

}
