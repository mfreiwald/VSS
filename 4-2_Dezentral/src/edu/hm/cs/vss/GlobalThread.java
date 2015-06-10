package edu.hm.cs.vss;

import java.rmi.RemoteException;

public class GlobalThread extends Thread {

	public void run() {

		while (true) {
			searchAVG();
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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

				}
			} else {
				
			}

		} catch (RemoteException e) {
			Logging.log("GlobalThread", e.getMessage());
		}
	}
}
