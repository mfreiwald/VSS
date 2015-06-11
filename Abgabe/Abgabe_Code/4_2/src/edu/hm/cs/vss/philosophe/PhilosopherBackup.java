package edu.hm.cs.vss.philosophe;

import java.io.Serializable;

public class PhilosopherBackup implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private final long timesEating;
	private final boolean isHungry;
	private final boolean hasToStop;
	private final long startTime;
	
	public PhilosopherBackup(long timesEating, boolean isHungry,
			boolean hasToStop, long startTime) {
		super();
		this.timesEating = timesEating;
		this.isHungry = isHungry;
		this.hasToStop = hasToStop;
		this.startTime = startTime;
	}

	public long getTimesEating() {
		return timesEating;
	}

	public boolean isHungry() {
		return isHungry;
	}

	public boolean isHasToStop() {
		return hasToStop;
	}

	public long getStartTime() {
		return startTime;
	}
	
}
