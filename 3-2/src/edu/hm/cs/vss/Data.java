package edu.hm.cs.vss;
import java.util.TreeSet;


public class Data {

	private TreeSet<String> list = new TreeSet<String>();
	private volatile int activeReaders = 0;
	private volatile int activeWriters = 0;
	
	private volatile int max = -1;
	
	public synchronized void write(int threadNr, long i) {
		while(activeReaders > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		activeWriters++;
		
		System.out.println("P"+threadNr+" write: "+i);
		list.add("P"+threadNr+"-"+i);
		max = list.size() > max ? list.size() : max;
		System.out.println("\t\t\t\tEntries: "+list.size()+"\t\t\t"+max);
		activeWriters--;
		notifyAll();
	}
	
	public synchronized void read(int threadNr) {
		while(activeWriters > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		activeReaders++;
		
		
		if(!list.isEmpty()) {
			String value = list.first();
			System.out.println("R"+threadNr+" read: "+value);

			list.remove(value);
		} else {
			System.out.println("R"+threadNr+" Empty");
		}
		//max = list.size() > max ? list.size() : max;
		System.out.println("\t\t\t\tEntries: "+list.size()+"\t\t\t"+max);
		
		activeReaders--;
		notifyAll();
	}
	
	
}
