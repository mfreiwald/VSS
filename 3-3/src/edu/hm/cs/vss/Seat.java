package edu.hm.cs.vss;

public class Seat {

	private volatile boolean isBusy = false;
	private final Fork leftFork;
	private final Fork rightFork;
	private final int nr;
	
	public Seat(int nr, Fork left, Fork right) {
		this.nr = nr;
		this.leftFork = left;
		this.rightFork = right;
	}
	
	public synchronized void takeForks() {
		while(leftFork.isInUse() || rightFork.isInUse()) {
			try {
				System.out.println("Waiting to Take Fork (" +leftFork + " " + leftFork.isInUse() + ", " + rightFork + " " + rightFork.isInUse() + ")");
				wait();
			} catch (InterruptedException e) {
				System.err.println("Take Fork");
				e.printStackTrace();
			}
		}
		
		leftFork.take();
		rightFork.take();
	}
	
	public synchronized void releaseForks() {
		System.out.println("Relase Forks ("+leftFork+","+rightFork+")");
		leftFork.release();
		rightFork.release();
	}
	
	public synchronized boolean getIsBusy()
	{
		return isBusy;
	}
	
	public synchronized void take()
	{
		isBusy = true;
	}
	
	public synchronized void release()
	{
		isBusy = false;
	}
	
	public Fork getLeftFork()
	{
		return leftFork;
	}
	
	public Fork getRightFork()
	{
		return rightFork;
	}
	
	public String toString() {
		return "Seat "+nr;
	}
	
}
