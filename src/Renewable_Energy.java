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
	
	static ArrayList<State> states = new ArrayList<State>();
	static ArrayList<State> useableSolarStates = new ArrayList<State>();
	static ArrayList<State> useableWindStates = new ArrayList<State>();
	static State initializeState;
	final static Random randomGen = new Random();
	
	/** Parameters:
	 **
	 ** solarFarmSize = A randomly chosen integer (10-200) acres in size
	 **/
	
	static Solar_Farm solarIndustrialGrid;
	static Solar_Farm solarMicroGrid;
	static ArrayList<Solar_Farm> solarFarmList = new ArrayList<Solar_Farm>();

	/** Parameters:
	 **
	 ** height = height of Solar Panel (shorter measurement) in inches
	 ** width = width of Solar Panel (longer measurement) in inches
	 ** cells = Number of Photovalic (PV) cells
	 ** wattLow = Low end of the Rated Watt in kiloWatts (kW)
	 ** wattHigh = High end of the Rated Watt in kW
	 **/

	static Solar_Panel monoPERC_72x144_415;
	static Solar_Panel monoPERC_66x132_390;
	static Solar_Panel monoPERC_60x120_345;
	static ArrayList<Solar_Panel> solarPanelList = new ArrayList<Solar_Panel>();

	/** Parameters:
	 **
	 ** height = height of Wind Turbine in feet
	 ** armLength = length of arm in feet
	 ** ratedWatt = Watt rating in MegaWatts (MW) coverted and stored as kiloWatts (kW)
	 **/

	static Wind_Turbine turbine_262;
	static Wind_Turbine turbine_295;
	static Wind_Turbine turbine_328;
	static ArrayList<Wind_Turbine> windTurbineList = new ArrayList<Wind_Turbine>();
	
	static Wind_Farm smallWindFarm;
	static Wind_Farm largeWindFarm;
	static int[] smallWindFarmSize;
	static int[] largeWindFarmSize;
	static ArrayList<Wind_Farm> windFarmList = new ArrayList<Wind_Farm>();
	
	static ArrayList<String> energySources = new ArrayList<String>();
	
	/**
	 ** Total kWh (kiloWatt Hours) produced in 2021 by USA
	 ** Can be adjusted for more accurate counts
	 **/
	
	final static long usedHours_2021 = 4120000000000L;
	static long targetHours;

	/** If using the Auto_Test class
	 ** 
	 ** avgPanelCount = average panel count 'generated' across all tests
	 ** avgEnergyGen = average kWh's generated across all tests
	 ** avgMiles = average used Miles across all tests
	 ** avgFarmsWithTurbine = average Farms utilizing a Wind Turbine across all tests
	 ** avgFarmsWithoutTurbine = average Farms not utilizing a Wind Turbine across all tests
	 ** avgFarmTotal = average amount of farms across all tests
	 **/
		
	static long avgPanelCount = 0;
	static long avgTurbineCount = 0;
	static long avgEnergyGen = 0;
	static long avgFeet = 0;
	static long avgSolarFarms = 0;
	static long avgWindFarms = 0;
	static long avgFarmTotal = 0;
	
	/**
	 ** Clears the generated Stats from the list to allow for multiple tests
	 ** Mainly needed in the Renewable_Energy_Auto Class
	 ** 	where x amount of tests are run instead of just 1 test
	 **/
	
	public static void clearStats() {
		
		for (State state : states) {
			
			state.usedLand = 0;
			state.powerGenerated = 0;
			state.microGrids_Used = new long[] {0,0};
			state.industrialGrids_Used = new long[] {0,0};
			state.windFarms_262 = new long[] {0,0};
			state.windFarms_295 = new long[] {0,0};
			state.used = false;
		}
	} // End clearStats
	
	/**
	 ** Wrapper class that passes a null value to the main getStats function
	 ** This allows a single test and multiple tests to be run with the same code
	 ** 	and not having duplicate code
	 ** 	This does not mess up the Writing Test Function
	 **/
	
	public static void getStats(Integer i) {
		
		// Variables to keep track of the generated stats
		
		ArrayList<Long> statsList = new ArrayList<Long>();
		
		long totalPower = 0;
		long totalPanel = 0;
		long totalLandUsed = 0;
		long totalFarms = 0;
		long solarFarms_Used = 0;
		long microGrids_Used = 0;
		long industrialGrids_Used = 0;
		long turbine262_Used = 0;
		long turbine295_Used = 0;
		long turbine328_Used = 0;
		long windFarms_Used = 0;
		long powerGen = 0;
		
		while (totalPower < targetHours) {
			
			State currState;
			double sunHours;
			long farmArea;
			long panelCount;
			
			if (totalPower >= targetHours) {
				break;
			}
			
			// Picks 'randomly' WindTurbine or SolarFarm
			
			String usedEnergySource = energySources.get(randomGen.nextInt(energySources.size()));
			
			if (usedEnergySource.equals("Solar Farm")) {
				
				if (useableSolarStates.size() == 0) {
					System.out.println("OUT OF STATES");
					break;
				}
				
				currState = useableSolarStates.get(randomGen.nextInt(useableSolarStates.size()));
				
				Solar_Farm usedFarm = solarFarmList.get(randomGen.nextInt(solarFarmList.size()));
				Solar_Panel usedPanel = solarPanelList.get(randomGen.nextInt(solarPanelList.size()));
				
				if (usedFarm.getName().equals("IndustrialGrid")) {
					int solarFarmSize = (int) (Math.random() * (200 - 10) + 10);
					usedFarm = new Solar_Farm(solarFarmSize, "IndustrialGrid", usedPanel);
				} else if (usedFarm.getName().equals("MicroGrid")) {
					usedFarm = new Solar_Farm(3, "MicroGrid", usedPanel);
				}
				
				farmArea = usedFarm.getTotalArea();
				sunHours = currState.getPSH();
				
				if ((currState.usedLand + farmArea) < currState.getUseableLand()) {
					
					panelCount = usedFarm.getPanelCount();
					currState.used = true;
					
					if (panelCount > 0) {
						powerGen = (long) Math.ceil(usedPanel.getYearWatt(sunHours) * panelCount);
						currState.powerGenerated += powerGen;
						currState.usedLand += farmArea;
						totalLandUsed += farmArea;
						
						totalPower += powerGen;
						solarFarms_Used++;
						totalFarms++;
						totalPanel += panelCount;
						
						// Variables for Averages in Larger Test Pools
						
						avgEnergyGen += powerGen;
						avgFarmTotal++;
						avgFeet += farmArea;
						avgPanelCount += panelCount;
						avgSolarFarms++;
						
						// Keeps track of specific farms (can be utilized later for data collection)
						
						switch(usedFarm.getName()) {
						case "MicroGrid":
							microGrids_Used++;
							currState.microGrids_Used[0]++;
							currState.microGrids_Used[1] += panelCount;
							break;
						case "IndustrialGrid":
							industrialGrids_Used++;
							currState.industrialGrids_Used[0]++;
							currState.industrialGrids_Used[1] += panelCount;
							break;
						} // End switch(usedFarm.getName())
					}
				} else if ((currState.usedLand + farmArea) > currState.getUseableLand()) {
					useableSolarStates.remove(currState);
				}
				System.out.println("totalFarms: " + totalFarms);
				System.out.println("useableSolarStates.size(): " + useableSolarStates.size());
				// End Solar Farm Block and Begin Wind Power Block
			} else if (usedEnergySource.equals("Wind Power")) {
				
				// Chooses a state 'randomly' from user selected presets (by having a min. windSpeed >= user picked windSped)
				
				if (useableWindStates.size() == 0) {
					System.out.println("OUT OF STATES");
					break;
				}
				
				currState = useableWindStates.get(randomGen.nextInt(useableWindStates.size()));
				
				Wind_Turbine usedTurbine = windTurbineList.get(randomGen.nextInt(windTurbineList.size()));
				Wind_Farm usedFarm = windFarmList.get(randomGen.nextInt(windFarmList.size()));
				
				if (usedFarm.getName().equals("SmallWind")) {
					usedFarm = new Wind_Farm("SmallWind", currState.getWindSpeed(),
							currState.getDensity(usedTurbine.getHeight()), usedTurbine, smallWindFarmSize);
				} else if (usedFarm.getName().equals("LargeWind")) {
					usedFarm = new Wind_Farm("LargeWind", currState.getWindSpeed(),
							currState.getDensity(usedTurbine.getHeight()), usedTurbine, largeWindFarmSize);
				}

				if (currState.usedLand + usedFarm.getArea_Feet() > currState.getUseableLand()) {
					useableWindStates.remove(currState);
				}
				
				if ((currState.usedLand + usedFarm.getArea_Feet() < currState.getUseableLand())
						&& (usedFarm.getWindPower_Farm() > 0)) {
					
					currState.used = true;
					
					switch(usedTurbine.getName()) {
					case "Turbine 262":
						turbine262_Used++;
						currState.windFarms_262[0]++;
						currState.windFarms_262[1] += usedFarm.getTurbineCount();
						break;
					case "Turbine 295":
						turbine295_Used++;
						currState.windFarms_295[0]++;
						currState.windFarms_295[1] += usedFarm.getTurbineCount();
						break;
					}
					
					currState.usedLand += usedFarm.getArea_Feet();
					currState.powerGenerated += usedFarm.getWindPower_Farm();
					totalLandUsed += usedFarm.getArea_Feet();
					totalPower += usedFarm.getWindPower_Farm();
					totalFarms++;
					windFarms_Used++;
					
					// Variables for Averages in Larger Test Pools
					
					avgEnergyGen += usedFarm.getWindPower_Farm();
					avgFarmTotal++;
					avgFeet += usedFarm.getArea_Feet();
					avgWindFarms++;
					avgTurbineCount += usedFarm.getTurbineCount();
					
				}
				
			} // End WindPower
			
		} // End While Loop
		
		// Adding all stats to statsList to be passed to writeTest Function

		statsList.add(totalPower);
		statsList.add(totalPanel);
		statsList.add(totalLandUsed);
		statsList.add(totalFarms);
		statsList.add(solarFarms_Used);
		statsList.add(microGrids_Used);
		statsList.add(industrialGrids_Used);
		statsList.add(turbine262_Used);
		statsList.add(turbine295_Used);
		statsList.add(turbine328_Used);
		statsList.add(windFarms_Used);

		// Calls the writeTest function to write the test in a ".txt" file
		writeTest(i, statsList);
		
	} // End getStats
	
	/**
	 ** Initializes various Renewable Energy Sources
	 ** Called by Renewable_Energy_GUI when 'Run' Button is hit 
	 ** 	Passes a string name and calls appropriate function (Wrapper Function of sorts)
	 **		To Initialize correct Objects
	 **/
	
	public static void initializeEnergySources(String name) {
		
		switch(name) {
		case "Wind Turbine (265 ft.)":
			initializeTurbine_262();
			break;
		case "Wind Turbine (295 ft.)":
			initializeTurbine_295();
			break;
		case "Wind Turbine (328 ft.)":
			initializeTurbine_328();
			break;
		case "Mono PERC 66 Cell - 390W":
			initializeMonoPERC_66x132_390();
			break;
		case "Mono PERC 72 Cell - 415W":
			initializeMonoPERC_72x144_415();
			break;
		case "Mono PERC 60 Cell - 345W":
			initializeMonoPERC_60x120_345();
			break;
		case "Micro Farm":
			initializeMicroGrid();
			break;
		case "Industrial Farm":
			initializeIndustrialGrid();
			break;
		case "Small (10-150 Turbines)":
			initializeSmallWind();
			break;
		case "Large (100-250 Turbines)":
			initializeLargeWind();
			break;
		}
	} // End initializeEnergySources
	
	public static void initializeTurbine_262() {
		
		turbine_262 = new Wind_Turbine(262, 137.5, 1.8, "Turbine 262");
		windTurbineList.add(turbine_262);
		
		if (energySources.indexOf("Wind Power") == -1) {
			//System.out.println("Added - 1");
			energySources.add("Wind Power");
		}
	} // End initializeTurbine_262
	
	public static void initializeTurbine_295() {
		
		turbine_295 = new Wind_Turbine(295, 205, 3, "Turbine 295");
		windTurbineList.add(turbine_295);
		
		if (energySources.indexOf("Wind Power") == -1) {
			//System.out.println("Added - 2");
			energySources.add("Wind Power");
		}
	} // End initializeTurbine_295
	
	public static void initializeTurbine_328() {

		turbine_328 = new Wind_Turbine(328, 246, 6, "Turbine 328");
		windTurbineList.add(turbine_328);
		
		if (energySources.indexOf("Wind Power") == -1) {
			//System.out.println("Added - 3");
			energySources.add("Wind Power");
		}
	} // End initializeTurbine_295
	
	public static void initializeMonoPERC_72x144_415() {
		
		monoPERC_72x144_415 = new Solar_Panel(41, 82, 72, 415, 439);
		solarPanelList.add(monoPERC_72x144_415);
	} // End initializeCommercial_Panel
	
	public static void initializeMonoPERC_66x132_390() {
		
		monoPERC_66x132_390 = new Solar_Panel(41, 75, 66, 390, 439);
		solarPanelList.add(monoPERC_66x132_390);
	} // End initializeResidential_Panel
	
	public static void initializeMonoPERC_60x120_345() {

		monoPERC_60x120_345 = new Solar_Panel(41, 69, 60, 345, 319);
		solarPanelList.add(monoPERC_66x132_390);
	} // End initializeResidential_Panel
	
	public static void initializeMicroGrid() {

		solarMicroGrid = new Solar_Farm("MicroGrid");
		solarFarmList.add(solarMicroGrid);
		
		if (energySources.indexOf("Solar Farm") == -1) {
			//System.out.println("Added - 4");
			energySources.add("Solar Farm");
		}
	} // End initializeMicroGrid

	public static void initializeIndustrialGrid() {

		solarIndustrialGrid = new Solar_Farm("IndustrialGrid");
		solarFarmList.add(solarIndustrialGrid);
		
		if (energySources.indexOf("Solar Farm") == -1) {
			//System.out.println("Added - 5");
			energySources.add("Solar Farm");
		}
	} // End initializeResidential_Panel
	
	public static void initializeSmallWind() {

		smallWindFarm = new Wind_Farm("SmallWind");
		windFarmList.add(smallWindFarm);
		smallWindFarmSize = new int[] { 10, 150 };
		
		if (energySources.indexOf("Wind Farm") == -1) {
			// System.out.println("Added - 5");
			energySources.add("Wind Farm");
		}
	}

	public static void initializeLargeWind() {

		largeWindFarm = new Wind_Farm("LargeWind");
		windFarmList.add(largeWindFarm);
		largeWindFarmSize = new int[] { 100, 250 };

		if (energySources.indexOf("Wind Farm") == -1) {
			// System.out.println("Added - 5");
			energySources.add("Wind Farm");
		}
	}
	
	// Called by Renewable_Energy_GUI and sets targetHours from user Specified Field (measured in kWH)
	
	public static void setHours(long hours) {
		targetHours = hours;
	}
	
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
				initializeState = new State(line);
				states.add(initializeState);
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
	
	public static void populateUseableStates(double valuePSH, double valueWindSpeed) {
		
		if (solarFarmList.size() != 0) {
			
			if (useableSolarStates.size() > 0) { useableSolarStates.clear(); }
			
			for (State solarState : states) {
				if (solarState.getPSH() >= valuePSH) {
					useableSolarStates.add(solarState);
				}
			}
		} // End solarFarm block
		
		if (windTurbineList.size() != 0) {
			
			if (useableWindStates.size() > 0) { useableWindStates.clear(); }
			
			for (State windState : states) {
				if (windState.getWindSpeed()[0] >= valueWindSpeed) {
					useableWindStates.add(windState);
				}
			}
		} // End windFarm block
		
	} // End populateUseableStates
	
	/**
	 ** Called by Renewable_Energy_GUI
	 ** Used to clear .../Testing_Data/.. files in order to not mixup any results
	 **/
	
	public static void cleanDirectory() {
		File temp = new File("Testing_Data/");
		
		for (File f: Objects.requireNonNull(temp.listFiles())) {
			if (!f.isDirectory()) {
				f.delete();
			}
		}
	} // End cleanDirectory
	
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
	
	public static void writeTest(Integer i, ArrayList<Long> statsList) {
		
		// fileName is unique to the test number (test #1 is "test_1.txt" and test #47 is "test_47.txt")
		String fileName;
		if (i == null) {
			fileName = "Testing_Data/test.txt";
		} else {
			fileName = "Testing_Data/test_" + i + ".txt";
		}
		
		try {
			file = new File(fileName);
			file.createNewFile();
			writer = new FileWriter(file);
			
			for (State state : states) {
				if (state.used) {
					
					writer.write(state.getName() + "\n");
					writer.write("\tUsed Land: " + state.usedLand + "(ft^2)\n");
					writer.write("\tUsed Land: " + Conversions.convertSqFeet_ToSqAcre(state.usedLand) + "(acre^2)\n");
					writer.write("\tUsed Land: " + Conversions.convertSqFeet_ToSqMiles(state.usedLand) + "(mi^2)\n");
					writer.write("\tPower Gen: " + state.powerGenerated + " (kWh)\n");
					writer.write("\tMicroGrids: " + state.microGrids_Used[0] + 
							" || Total Panels: " + state.microGrids_Used[1] + "\n");
					writer.write("\tIndustrialGrids: " + state.industrialGrids_Used[0] + 
							" || Total Panels: " + state.industrialGrids_Used[1] + "\n");
					writer.write("\tWindFarms_262: " + state.windFarms_262[0] + 
							" || Total Turbines: " + state.windFarms_262[1] + "\n");
					writer.write("\tWindFarms_295: " + state.windFarms_295[0] + 
							" || Total Turbines: " + state.windFarms_295[1] + "\n");
				}
			} // End for Loop
			
			// Writing full Test (not individual state stats)
			
			writer.write("\nTotal Power Gen: " + statsList.get(0) + " (kWh)\n");
			writer.write("\tNeeded Power Gen: " + targetHours + " (kWh)\n");
			writer.write("\tUsed Land: " + statsList.get(2) + "(ft^2)\n");
			writer.write("\tUsed Land: " + Conversions.convertSqFeet_ToSqAcre(statsList.get(2)) + "(acre^2)\n");
			writer.write("\tUsed Land: " + Conversions.convertSqFeet_ToSqMiles(statsList.get(2)) + "(mi^2)\n");
			writer.write("\tMicroGrids: " + statsList.get(5) + "\n");
			writer.write("\tIndustrialGrids: " + statsList.get(6) + "\n");
			writer.write("\tPanelCount: " + statsList.get(1) + "\n");
			writer.write("\tTotal SolarFarms: " + statsList.get(4) + "\n");
			writer.write("\tTurbine 262's: " + statsList.get(7) + "\n");
			writer.write("\tTurbine 295's: " + statsList.get(8) + "\n");
			writer.write("\tTotal WindFarms: " + statsList.get(10) + "\n");
			writer.write("\tTotal Farms: " + statsList.get(3) + "\n");
			
			writer.close();
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
	} // End writeTest

	/**
	 ** Writes the average stats to a designated file
	 **/
	
	public static void writeAverages(Integer tests) {

		String fileName = "avg_results.txt";

		try {
			file = new File(fileName);
			file.createNewFile();
			writer = new FileWriter(file);
			
			writer.write("avg EnergyGen: " + (avgEnergyGen / tests) + " (kWh)\n");
			writer.write("avg Miles: " + Conversions.convertSqFeet_ToSqAcre(avgFeet / tests) + " (acre^2)\n");
			writer.write("avg Miles: " + Conversions.convertSqFeet_ToSqMiles(avgFeet / tests) + " (mi^2)\n");
			writer.write("avg SolarFarms: " + (avgSolarFarms / tests) + "\n");
			writer.write("avg PanelCount: " + (avgPanelCount / tests) + "\n");
			writer.write("avg WindFarms: " + (avgWindFarms / tests) + "\n");
			writer.write("avg WindFarms: " + (avgTurbineCount / tests) + "\n");
			writer.write("avg FarmTotal: " + (avgFarmTotal / tests));

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	} // End writeAverages
	
	public static void clear() {
		
		// Resetting avg Variables
		
		avgPanelCount = 0;
		avgTurbineCount = 0;
		avgEnergyGen = 0;
		avgFeet = 0;
		avgSolarFarms = 0;
		avgWindFarms = 0;
		avgFarmTotal = 0;
		
		// Clearing all Lists containing a state for multiple tests
		
		useableSolarStates.clear();
		useableWindStates.clear();
		states.clear();
		
	}
	
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
