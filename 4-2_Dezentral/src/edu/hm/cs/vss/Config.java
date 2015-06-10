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

	
	public static final int TIME_SLEEP = 80;
	public static final int TIME_EAT = 20;
	public static final int TIME_MEDITATE = 50;
	public static final int TIME_MEDIATE_HUNGRY = 30;
	public static final int TIME_STOP_EATING = 30;
	
	public static final int STATUS_UPDATE_INTERVAL = 10;
	
	public static final int BACKUP_INTERVAL = 1000;
	public static final int SPREAD_PHILOSOPHERS_INTERVAL = 3000;
	public static final int CHECK_PHILOSOPHERS_INTERVAL = 0;
	public static final int DIFFERENZ_EATING_PHILOSOPHERS = 10;
	public static final int DIFFERENZ_GLOBAL_EATING_PHILOSOPHERS = 10;
	
}
