import java.io.*;
import java.util.*;

public class Renewable_Energy {
	
	/**
	 ** Used for reading, writing file
	 **/
	
	static FileWriter writer;
	static FileReader FileReader;
	static BufferedReader reader;
	static File file;

	/**
	 ** masterPSH = 'adjustable' filter of average Peak Sun Hours (psh)
	 ** states = ArrayList of all states read from "states.txt"
	 ** useableStates = ArrayList of all viable states with average Peak Sun Hours (psh) of masterPSH or greater
	 ** state = holder place for state chosen from useableStates list
	 ** randomGen = a Random Generator for picking a Random State from useableStates ArrayList
	 **/
	
	static double masterPSH = 3.5;
	static ArrayList<State> states = new ArrayList<State>();
	static ArrayList<State> useableStates = new ArrayList<State>();
	static State state;
	final static Random randomGen = new Random();
	
	/** Parameters:
	 **
	 ** solarFarmSize = A randomly chosen integer (10-200) acres in size
	 **/
	
	static SolarFarm solarFarmArray;

	/** Parameters:
	 **
	 ** height = height of Solar Panel (shorter measurement) in inches
	 ** width = width of Solar Panel (longer measurement) in inches
	 ** cells = Number of Photovalic (PV) cells
	 ** wattLow = Low end of the Rated Watt in kiloWatts (kW)
	 ** wattHigh = High end of the Rated Watt in kW
	 **/

	static Solar_Panel Commercial_Panel;
	static Solar_Panel Residential_Panel;

	/** Parameters:
	 **
	 ** height = height of Wind Turbine in feet
	 ** armLength = length of arm in feet
	 ** ratedWatt = Watt rating in MegaWatts (MW) coverted and stored as kiloWatts (kW)
	 **/

	static Wind_Turbine turbine_262;
	static Wind_Turbine turbine_295;

	/**
	 ** Total kWh (kiloWatt Hours) produced in 2021 by USA
	 ** Can be adjusted for more accurate counts
	 **/
	
	final static long usedHours_2021 = 4120000000000L;

	/** If using the Auto_Test class
	 ** 
	 ** avgPanelCount = average panel count 'generated' across all tests
	 ** avgEnergyGen = average kWh's generated across all tests
	 ** avgMiles = average used Miles across all tests
	 ** avgFarmsWithTurbine = average Farms utilizing a Wind Turbine across all tests
	 ** avgFarmsWithoutTurbine = average Farms not utilizing a Wind Turbine across all tests
	 ** avgFarmTotal = average amount of farms across all tests
	 **/
	
	final static int tests = 100;
	
	static long avgPanelCount = 0;
	static long avgEnergyGen = 0;
	static long avgMiles = 0;
	static long avgFarmsWithTurbine = 0;
	static long avgFarmsWithoutTurbine = 0;
	static long avgFarmTotal = 0;
	
	public static void main(String[] args) {
		
		// Initializes Solar Panels and Wind Turbines
		initializeEnergySources();
		
		// Reads and Populates states ArrayList
		readStates();
		
		// Populates useableStates ArrayList
		populateUseableStates();
		
		// Uses data and runs a full test
		getStats();
		
		// Clears Generated Stats from the states in useableStates ArrayList
		clearStats();
		
	} // End main
	
	/**
	 ** Clears the generated Stats from the list to allow for multiple tests
	 ** Mainly needed in the Renewable_Energy_Auto Class
	 ** 	where x amount of tests are run instead of just 1 test
	 **/
	
	public static void clearStats() {
		
		for (State state : useableStates) {
			
			state.powerGenerated = 0;
			state.solarFields_withoutTurbine = 0;
			state.solarFields_withTurbine = 0;
			state.usedLand = 0;
		}
	} // End clearStats
	
	/**
	 ** Wrapper class that passes a null value to the main getStats function
	 ** This allows a single test and multiple tests to be run with the same code
	 ** 	and not having duplicate code
	 ** 	This does not mess up the Writing Test Function
	 **/
	
	public static void getStats() {
		getStats(null);
	} // End getStats
	
	/** 
	 ** Main functionality of the program is here
	 ** Calculates the amount of area a Solar Farm can Have
	 ** Calculates the amount of Solar Panels will fit inside said farm
	 ** Picks a State at Random
	 ** Retrieves Peak Sun Hours (psh) from State
	 ** Calculates the amount of kiloWatt Hours (kWh) of a solar panel farm via psh
	 ** Adds totals to tracker variables
	 ** Calculates Wind Power for state based on elevation, air Density, and randomly chosen windSpeed of state
	 ** If viable power source it is included in stats, else not included (but still tracked in farmWithoutTurbine variable)
	 ** Calls writeTest to write the test result in a file
	 ** 
	 ** @param i = test number
	 ** @param i = null Value if not run through Renewable_Energy_Auto Class
	 **/
	
	public static void getStats(Integer i) {
		
		// Variables to keep track of the generated stats
		long totalPanel = 0;
		long totalPower = 0;
		long totalLandUsed = 0;
		long totalFarms = 0;
		long farmWithTurbine = 0;
		long farmWithoutTurbine = 0;
		
		// Variable 'initialization' (holders) for various Calculated fields below
		State currState;
		double sunHours;
		long farmArea;
		int panelCount;
		long windPower;
		long powerGen;
		
		Solar_Panel usedPanel = Commercial_Panel;

		// Loops through and choose a state from said list at random
		while (totalPower < usedHours_2021) {

			if (totalPower > usedHours_2021) {
				break;
			}

			currState = useableStates.get(randomGen.nextInt(useableStates.size()));
			sunHours = state.psh;

			// Creates a Solar Farm for our Solar Panels
			createSolarFarm();

			farmArea = solarFarmArray.totalArea;
			// System.out.println("FarmArea: " + farmArea);

			if (currState.usedLand + farmArea < currState.useableLand) {

				panelCount = solarFarmArray.getPanelCount(usedPanel);

				totalPanel += panelCount;
				avgPanelCount += panelCount;

				windPower = turbine_295.getOutputPower(currState.windSpeed, currState.airDensity);
				// long windPower = -1L;

				if (panelCount != -1) {
					powerGen = (long) Math.ceil(usedPanel.getYearWatt(sunHours) * panelCount);
					currState.powerGenerated += powerGen;
					currState.usedLand += farmArea;
					totalLandUsed += farmArea;
					avgMiles += farmArea;

					totalPower += powerGen;
					avgEnergyGen += powerGen;
					
					// If WindPower if viable it uses it
					// If not, then it does not use it
					
					if (windPower != -1) {
						currState.powerGenerated += windPower;
						totalPower += windPower;
						currState.solarFields_withTurbine++;
						
						farmWithTurbine++;
						totalFarms++;
						avgEnergyGen += windPower;
						avgFarmsWithTurbine++;
						avgFarmTotal++;
					} else {
						currState.solarFields_withoutTurbine++;
						farmWithoutTurbine++;
						totalFarms++;
						avgFarmsWithoutTurbine++;
						avgFarmTotal++;
					}
				}
			}
		}
		// Calls the writeTest function to write the test in a ".txt" file
		writeTest(i, totalPower, totalPanel, totalLandUsed, farmWithTurbine, farmWithoutTurbine, totalFarms);
	} // End getStats
	
	/**
	 ** Initializes various Renewable Energy Sources
	 **/
	
	public static void initializeEnergySources() {
		
		// Initializes Solar Panels (see above for Parameters)
		Commercial_Panel = new Solar_Panel(39, 77, 72, 350, 400);
		Residential_Panel = new Solar_Panel(39, 66, 60, 250, 300);
		
		// Initializes Wind Turbines (see above for Parameters)
		turbine_262 = new Wind_Turbine(262, 137.5, 1.8);
		turbine_295 = new Wind_Turbine(295, 205, 3);

	} // End createPanels
	
	/**
	 ** Reads the "states.txt" file
	 **	Passes the whole line to the State class to Create an Instance of the State
	 ** Populates the states ArrayList
	 **/
	
	public static void readStates() {
		try {
			
			String line;
			reader = new BufferedReader(new FileReader("states.txt"));
			
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				state = new State(line);
				states.add(state);
			}
			reader.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	} // End readStates
	
	/**
	 ** Populates useableStates ArrayList from the designated double masterPSH
	 ** Only states that have a STARTING PSH greater than master PSH will be included in ArrayList
	 **/
	
	public static void populateUseableStates() {
		
		// Adds states to a new ArrayList if lowest WindSpeed AVG is above masterPSH m/s
		for (State state : states) {
			if (state.psh > masterPSH && !state.name.equals("Hawaii")) {
				useableStates.add(state);
			}
		}
	} // End populateUseableStates
	
	/**
	 ** Picks a random number betwen (10-200)
	 ** Creates a New Instance of solarFarmArray (overwriting previous instance)
	 **/
	
	public static void createSolarFarm() {
		
		// Picks from a random number between 10 and 200 acres
		int solarFarmSize = (int) (Math.random() * (200 - 10) + 10);
		
		solarFarmArray = new SolarFarm(solarFarmSize);
	} // End createSolarFarm
	
	/**
	 ** Takes Parameters and Writes them to an output file specific for that test number
	 ** 
	 ** @param i = test number
	 ** @param totalPower = total Power Generated (kWh) in test
	 ** @param totalPanel = total Solar Panel count for test
	 ** @param totalLandUsed = total Square Feet used in test
	 ** @param farmWithTurbine = total Farms utilizing a Wind Turbine
	 ** @param farmWithoutTurbine = total Farms not utilizing a Wind Turbine
	 ** @param totalFarms = total amount of Farms
	 **
	 ** These output stats are complete adjustable to whatever data is needed (or wanted)
	 **/
	
	public static void writeTest(Integer i, long totalPower, long totalPanel, long totalLandUsed, long farmWithTurbine, long farmWithoutTurbine, long totalFarms) {
		
		// fileName is unique to the test number (test #1 is "test_1.txt" and test #47 is "test_47.txt")
		String fileName;
		if (i == null) {
			fileName = "Testing_Data/test";
		} else {
			fileName = "Testing_Data/test_" + i;
		}

		try {
			file = new File(fileName);
			file.createNewFile();
			writer = new FileWriter(file);

			for (State state : useableStates) {

				writer.write(state.name + "\n");
				writer.write("\tUsed Land: " + state.usedLand + "(ft^2)\n");
				writer.write("\tUsed Land: " + convertSqFeet_ToSqAcre(state.usedLand) + "(acre^2)\n");
				writer.write("\tUsed Land: " + covertSqFeet_ToSqMiles(state.usedLand) + "(mi^2)\n");
				writer.write("\tPower Gen: " + state.powerGenerated + " (kWh)\n");
				writer.write("\tFarm w/Turbine: " + state.solarFields_withTurbine + "\n");
				writer.write("\tFarm w/o Turbine: " + state.solarFields_withoutTurbine + "\n");
			}
			
			writer.write("\nTotal Power Gen: " + totalPower + "\n");
			writer.write("Needed Power Gen: " + usedHours_2021 + "\n");
			writer.write("PanelCount: " + totalPanel + "\n");
			writer.write("Land Used: " + covertSqFeet_ToSqMiles(totalLandUsed) + "(mi^2)\n");
			writer.write("Farms w/Turbines: " + farmWithTurbine + "\n");
			writer.write("Farms w/o Turbines: " + farmWithoutTurbine + "\n");
			writer.write("Farms Total: " + totalFarms + "\n");
			
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	} // End writeTest
	
	/**
	 ** Converts ft^2 to acres^2
	 ** 
	 ** @param feet = passed ft^2
	 ** @return acres = conversion of feet to acres
	 **/
	
	public static double convertSqFeet_ToSqAcre(long feet) {
		
		int SqFoot_ToSqAcre = 43560;
		
		return (feet / SqFoot_ToSqAcre);
	} // End convertSqFeet_ToSqAcre

	/**
	 ** Converts ft^2 to miles^2
	 ** 
	 ** @param feet = passed ft^2
	 ** @return acres = conversion of feet to miles
	 **/
	
	public static long covertSqFeet_ToSqMiles(long feet) {
		
		// This represents how many square Feet are in 1 square Mile		
		long squareMilesToSquareFeet = 27878400L;

		return (long) Math.ceil(feet / squareMilesToSquareFeet);
	} // End covertSqFeet_ToSqMiles

	/**
	 ** Writes the average stats to a designated file
	 **/
	
	public static void writeAverages() {

		String fileName = "test_results";

		try {
			file = new File(fileName);
			file.createNewFile();
			writer = new FileWriter(file);

			writer.write("avgPanelCount: " + (avgPanelCount / tests) + "\n");
			writer.write("avgEnergyGen: " + (avgEnergyGen / tests) + " (kWh)\n");
			writer.write("avgMiles: " + convertSqFeet_ToSqAcre(avgMiles / tests) + " (acre^2)\n");
			writer.write("avgMiles: " + covertSqFeet_ToSqMiles(avgMiles / tests) + " (mi^2)\n");
			writer.write("avgFarmsWithTurbine: " + (avgFarmsWithTurbine / tests) + "\n");
			writer.write("avgFarmsWithoutTurbine: " + (avgFarmsWithoutTurbine / tests) + "\n");
			writer.write("avgFarmTotal: " + (avgFarmTotal / tests));

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	} // End writeAverages
	
	/**
	 ** Used for updating the "states.txt" file
	 **/
	
	public static void updateStates() {
		try {
			file = new File("states.txt");
			file.createNewFile();
			writer = new FileWriter(file);

			for (State state : states) {
				writer.write(state.toString());
			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	} // End updateStates
	
} // End Class Renewable_Energy
