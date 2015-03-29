package edu.hm.css.vss;
import java.util.ArrayList;


public class Data {

	private ArrayList<String> list = new ArrayList<String>();
	private boolean isAvailable = false;
	
	public synchronized void write(int threadNr, long i) {
		while(!isAvailable) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		isAvailable = false;
		String value = "Write Data: Procuer "+threadNr+": "+i;
		System.out.println(value);
		list.add(value);
		notifyAll();
	}
	
	public synchronized String read(int threadNr) {
		while(isAvailable) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		String value = "";
		if(list.size() > 0) {
			value = list.get(list.size()-1);
			System.out.println("Read Data: Consumer "+threadNr);
			}
		

		isAvailable = true;
		notifyAll();
		return value;
	}
}
