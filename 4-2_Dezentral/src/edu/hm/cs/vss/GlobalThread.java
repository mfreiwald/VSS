package edu.hm.cs.vss;

import java.rmi.RemoteException;
import java.util.List;

public class GlobalThread extends Thread {

	public void run() {

		while (true) {
			// searchAVG();
			try {
				compareClients();
			} catch (RemoteException e1) {

			}

			try {
				sleep(1000);
			} catch (InterruptedException e) {
				//
			}
		}

	}

	private void compareClients() throws RemoteException {
		Client localClient = Main.getClient();
		ClientInfo localInfo = new ClientInfo(localClient);
		List<ClientInfo> clients = localClient.collectClientInfos("", null);
		
		double globalEatAVG = this.globalEatAVG(clients);
		
		for(ClientInfo clientInfo: clients) {
			
			if(clientInfo.uuid.equals(localClient.getUUID())) {
				continue;
			}
			
			
			
			
		}
		
	}
	
	private double globalEatAVG(List<ClientInfo> clients) {
		double avg = 0;
		for(ClientInfo client: clients) {
			avg += client.eatAVG;
		}
		avg /= clients.size();
		return avg;
	}

	private void searchAVG() {
		Client localClient = Main.getClient();

		try {

			double globalAVGEating = localClient.searchGlobalEatingAVG(null, 0);
			double localEatAVG = localClient.localEatAVG();

			// System.out.println("Local AVG: " + localEatAVG);
			// System.out.println("Global AVG: " + globalAVGEating);

			if (localEatAVG > 0) {
				if (localEatAVG < (globalAVGEating)) { // -
														// Config.DIFFERENZ_GLOBAL_EATING_PHILOSOPHERS))
														// {
					// Philosophen nach rechts weiter schieben, damit die
					// wenigen
					// übrigen mehr essen können
					// wie viele? und wen? der am meisten gegessen hat!

					System.out.println(localEatAVG + " < " + globalAVGEating); // +
																				// " - "
																				// +
																				// Config.DIFFERENZ_GLOBAL_EATING_PHILOSOPHERS
																				// +
																				// ")");

				} else {
					System.out.println(localEatAVG + " >= " + globalAVGEating); // +
																				// " - "
																				// +
																				// Config.DIFFERENZ_GLOBAL_EATING_PHILOSOPHERS
																				// +
																				// ")");

					// Move Philosophen zu rechten Partner, weil wir immer mehr
					// essen als der Durchschnitt
				}
			} else {

			}

		} catch (RemoteException e) {
			Logging.log("GlobalThread", e.getMessage());
		}
	}
}
