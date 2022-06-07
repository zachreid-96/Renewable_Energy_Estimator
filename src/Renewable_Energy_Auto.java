public class Renewable_Energy_Auto {

	// How many tests are going to be run (changeable here ONLY)
	final static int tests = 100;

	public static void main(String[] args) {

		// Initializes Solar Panels and Wind Turbines
		Renewable_Energy.initializeEnergySources();

		// Reads and Populates states ArrayList
		Renewable_Energy.readStates();
		
		// Populates useableStates ArrayList
		Renewable_Energy.populateUseableStates();

		for (Integer i = 0; i < tests; i++) {

			// Uses data and runs a full test
			Renewable_Energy.getStats(i);

			// Clears Generated Stats from the states in useableStates ArrayList
			Renewable_Energy.clearStats();
		}
		// Writes the averages over all tests
		Renewable_Energy.writeAverages();
	} // End main
} // End Class Renewable_Energy_Auto
