package edu.hm.css.vss;
import java.util.ArrayList;


public class Data {

	private ArrayList<String> list = new ArrayList<String>();
	private final String mutex = "MUTEX";
	
	public void writeData(int threadNr, long i) {
		synchronized(mutex) {
			System.out.println("Write Data: Procuer "+threadNr+": "+i);
			list.add("Producer "+threadNr+": "+i);	
		}
	}
	
	public String readData(int threadNr) {
		synchronized(mutex) {
			if(list.size() == 0) {
				return "";
			}
			String s = list.get(list.size()-1);
			System.out.println("Read Data: Consumer "+threadNr+": "+s);
			return s;
		}
	}
	
}
