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
	static ArrayList<State> useableStates = new ArrayList<State>();
	static ArrayList<State> useableSolarStates = new ArrayList<State>();
	static ArrayList<State> useableWindStates = new ArrayList<State>();
	static ArrayList<State> allStatesUsed = new ArrayList<State>();
	static State state;
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

	static Solar_Panel Commercial_Panel;
	static Solar_Panel Residential_Panel;
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
	static long avgEnergyGen = 0;
	static long avgMiles = 0;
	static long avgFarmsWithTurbine = 0;
	static long avgFarmsWithoutTurbine = 0;
	static long avgFarmTotal = 0;
	
	/**
	 ** Clears the generated Stats from the list to allow for multiple tests
	 ** Mainly needed in the Renewable_Energy_Auto Class
	 ** 	where x amount of tests are run instead of just 1 test
	 **/
	
	public static void clearStats() {
		
		for (State state : useableStates) {
			
			state.usedLand = 0;
			state.powerGenerated = 0;
			state.industrialFarm_withTurbine = 0;
			state.industrialFarm_withoutTurbine = 0;
			state.windTurbinesUsed = 0;
			state.solarFarmsUsed = 0;
			state.microGrid_withoutTurbine = 0;
			state.microGrid_withTurbine = 0;
			state.windTurbineTypes = new int[] { 0, 0, 0 };
			state.microGrids_Used = 0;
			state.industrialGrids_Used = 0;
		}
	} // End clearStats
	
	/**
	 ** Wrapper class that passes a null value to the main getStats function
	 ** This allows a single test and multiple tests to be run with the same code
	 ** 	and not having duplicate code
	 ** 	This does not mess up the Writing Test Function
	 **/
	
	public static void getStats(boolean turbinesNearSolarFarms, Integer i) {
		if (turbinesNearSolarFarms) {
			getStatsNotDistinguished(i);
		} else if (!turbinesNearSolarFarms) {
			getStatsDistinguished(i);
		}
		
	} // End getStats
	
	/** 
	 ** getStatsNotDistinguished(Integer i): Tries to add a Wind Turbine near every Solar Farm created
	 ** 
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
	 ** @param i = null Value if ran as a single test
	 **/
	
	public static void getStatsNotDistinguished(Integer i) {
		
		ArrayList<Long> statsList = new ArrayList<Long>();
		
		// Variables to keep track of the generated stats
		long totalPower = 0;
		long totalPanel = 0;
		long totalLandUsed = 0;
		long totalFarms = 0;
		long turbine262_Used = 0;
		long turbine295_Used = 0;
		long turbine328_Used = 0;
		long microGrids_WithoutTurbine = 0;
		long microGrids_WithTurbine = 0;
		long industrialGrid_WithoutTurbine = 0;
		long industrialGrid_WithTurbine = 0;
		
		// Variable 'initialization' (holders) for various Calculated fields below
		State currState;
		double sunHours;
		long farmArea;
		long panelCount;
		long windPower = -1;
		long powerGen;
		
		// Loops through and choose a state from said list at random
		while (totalPower < targetHours) {
			
			Solar_Panel usedPanel = solarPanelList.get(randomGen.nextInt(solarPanelList.size()));
			Solar_Farm usedFarm = solarFarmList.get(randomGen.nextInt(solarFarmList.size()));
			Wind_Turbine usedTurbine = new Wind_Turbine();
			
			if (usedFarm.getName().equals("IndustrialGrid")) {
				int solarFarmSize = (int) (Math.random() * (200 - 10) + 10);
				usedFarm = new Solar_Farm(solarFarmSize, "IndustrialGrid");
			}
			
			if (totalPower >= targetHours) {
				break;
			}

			currState = useableStates.get(randomGen.nextInt(useableStates.size()));
			sunHours = currState.getPSH();

			farmArea = usedFarm.getTotalArea();

			if (currState.usedLand + farmArea < currState.getUseableLand()) {

				panelCount = usedFarm.getPanelCount(usedPanel);
				
				totalPanel += panelCount;
				avgPanelCount += panelCount;

				if (panelCount != -1) {
					powerGen = (long) Math.ceil(usedPanel.getYearWatt(sunHours) * panelCount);
					currState.powerGenerated += powerGen;
					currState.usedLand += farmArea;
					totalLandUsed += farmArea;
					avgMiles += farmArea;

					totalPower += powerGen;
					avgEnergyGen += powerGen;
					
					totalFarms++;
					avgFarmTotal++;
					
					// If WindPower if viable it uses it
					// If not, then it does not use it
					
					if (windTurbineList.size() > 0) {
						usedTurbine = windTurbineList.get(randomGen.nextInt(windTurbineList.size()));
						windPower = usedTurbine.getOutputPower(currState.getWindSpeed(), currState.getDensity(usedTurbine.getHeight()));
					}
					
					if (windPower != -1) {
						
						currState.powerGenerated += windPower;
						totalPower += windPower;
						
						avgEnergyGen += windPower;
						avgFarmsWithTurbine++;
						
						// Keeps track of specific farms (can be utilized later for data collection)
						
						switch(usedFarm.getName()) {
						case "MicroGrid":
							microGrids_WithTurbine++;
							currState.microGrid_withTurbine++;
							break;
						case "IndustrialGrid":
							industrialGrid_WithTurbine++;
							currState.industrialFarm_withTurbine++;
							break;
						} // End switch(usedFarm.getName())
						
						// Keeps track of specific farms (can be utilized later for data collection)
						
						switch(usedTurbine.getName()) {
						case "Turbine 262":
							turbine262_Used++;
							currState.windTurbineTypes[0]++;
							break;
						case "Turbine 295":
							turbine295_Used++;
							currState.windTurbineTypes[1]++;
							break;
						case "Turbine 328":
							turbine328_Used++;
							currState.windTurbineTypes[2]++;
							break;
						} // End switch(usedTurbine.getName())
						
					} else {
						
						avgFarmsWithoutTurbine++;
						
						// Keeps track of specific farms (can be utilized later for data collection)
						
						switch(usedFarm.getName()) {
						case "MicroGrid":
							microGrids_WithoutTurbine++;
							currState.microGrid_withoutTurbine++;
							break;
						case "IndustrialGrid":
							industrialGrid_WithoutTurbine++;
							currState.industrialFarm_withoutTurbine++;
							break;
						} // End switch(usedFarm.getName())
					} // End windPower != -1 If/Else Block
				} // End panelCount != -1 If/Else Block
			} // End (currState.usedLand + farmArea < currState.getUseableLand()) If Block
		} // End while(totalPower < targetHours) Loop
		
		// Adding all stats to statsList to be passed to writeTest Function
		
		statsList.add(totalPower);
		statsList.add(totalPanel);
		statsList.add(totalLandUsed);
		statsList.add(totalFarms);
		statsList.add(turbine262_Used);
		statsList.add(turbine295_Used);
		statsList.add(turbine328_Used);
		statsList.add(microGrids_WithoutTurbine);
		statsList.add(microGrids_WithTurbine);
		statsList.add(industrialGrid_WithoutTurbine);
		statsList.add(industrialGrid_WithTurbine);
		
		// Calls the writeTest function to write the test in a ".txt" file
		writeTest(i, statsList, true);
	} // End getStats
	
	/** 
	 ** getStatsDistinguished(Integer i): Picks either a Wind Turbine or a Solar Farm Layout
	 ** 
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
	 ** @param i = null Value if ran as a single test
	 **/
	
	public static void getStatsDistinguished(Integer i) {
		
		ArrayList<Long> statsList = new ArrayList<Long>();
		
		// Variables to keep track of the generated stats
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

		// Variable 'initialization' (holders) for various Calculated fields below
		long powerGen;
		
		// Loops through and choose a state from said list at random
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
			
			// Solar Farm Block
			
			if (usedEnergySource.equals("Solar Farm")) {
				
				currState = useableSolarStates.get(randomGen.nextInt(useableSolarStates.size()));
				
				Solar_Farm usedFarm = solarFarmList.get(randomGen.nextInt(solarFarmList.size()));
				Solar_Panel usedPanel = solarPanelList.get(randomGen.nextInt(solarPanelList.size()));

				if (usedFarm.getName().equals("Industrial Farm")) {
					int solarFarmSize = (int) (Math.random() * (200 - 10) + 10);
					usedFarm = new Solar_Farm(solarFarmSize, "Industrial Farm");
				}

				farmArea = usedFarm.getTotalArea();
				sunHours = currState.getPSH();

				if (currState.usedLand + farmArea < currState.getUseableLand()) {

					panelCount = usedFarm.getPanelCount(usedPanel);

					totalPanel += panelCount;
					avgPanelCount += panelCount;

					if (panelCount != -1) {
						powerGen = (long) Math.ceil(usedPanel.getYearWatt(sunHours) * panelCount);
						currState.powerGenerated += powerGen;
						currState.usedLand += farmArea;
						totalLandUsed += farmArea;
						avgMiles += farmArea;

						totalPower += powerGen;
						avgEnergyGen += powerGen;

						solarFarms_Used++;
						currState.solarFarmsUsed++;
						totalFarms++;
						avgFarmTotal++;
						
						// Keeps track of specific farms (can be utilized later for data collection)
						
						switch(usedFarm.getName()) {
						case "MicroGrid":
							microGrids_Used++;
							currState.microGrids_Used++;
							break;
						case "IndustrialGrid":
							industrialGrids_Used++;
							currState.industrialGrids_Used++;
							break;
						} // End switch(usedFarm.getName())
					}
				}
				// End Solar Farm Block and Begin Wind Power Block
			} else if (usedEnergySource.equals("Wind Power")) {
				
				long windPower;
				
				// Chooses a state 'randomly' from user selected presets (by having a min. windSpeed >= user picked windSped)
				
				currState = useableWindStates.get(randomGen.nextInt(useableWindStates.size()));
				
				Wind_Turbine usedTurbine = windTurbineList.get(randomGen.nextInt(windTurbineList.size()));
				
				windPower = usedTurbine.getOutputPower(currState.getWindSpeed(), currState.getDensity(usedTurbine.getHeight()));
				
				currState.usedLand += usedTurbine.getArea();
				totalLandUsed += usedTurbine.getArea();
				avgMiles += usedTurbine.getArea();
				
				currState.powerGenerated += windPower;
				currState.windTurbinesUsed++;
				
				windFarms_Used++;
				totalPower += windPower;
				totalFarms++;
				avgFarmTotal++;
				
				// Keeps track of specific farms (can be utilized later for data collection)
				
				switch(usedTurbine.getName()) {
				case "Turbine 262":
					turbine262_Used++;
					currState.windTurbineTypes[0]++;
					break;
				case "Turbine 295":
					turbine295_Used++;
					currState.windTurbineTypes[1]++;
					break;
				case "Turbine 328":
					turbine328_Used++;
					currState.windTurbineTypes[2]++;
					break;
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
		writeTest(i, statsList, false);
		
	} // End getStatsDistinguished
	
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
		case "Residential Panel (60 cells)":
			initializeResidential_Panel();
			break;
		case "Industrial Panel (72 cells)":
			initializeCommercial_Panel();
			break;
		case "Micro Farm":
			initializeMicroGrid();
			break;
		case "Industrial Farm":
			initializeIndustrialGrid();
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
	
	public static void initializeCommercial_Panel() {
		
		Commercial_Panel = new Solar_Panel(39, 77, 72, 350, 400);
		solarPanelList.add(Commercial_Panel);
	} // End initializeCommercial_Panel
	
	public static void initializeResidential_Panel() {
		
		Residential_Panel = new Solar_Panel(39, 66, 60, 250, 300);
		solarPanelList.add(Residential_Panel);
	} // End initializeResidential_Panel
	
	public static void initializeMicroGrid() {

		solarMicroGrid = new Solar_Farm(2, "MicroGrid");
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
	
	public static void populateUseableStates(boolean turbinesNearSolarFarms, double valuePSH, double valueWindSpeed) {
		
		/**
		 ** Populates useableStates if placing Wind Turbines near created Solar Farms
		 ** Populates useableSolarStates/useableWindStates if not placing Wind Turbines near created Solar Farms
		 ** useableSolarStates use valuePSH (user Specified Field) to populate
		 ** useableWindStates use valueWindSpeed (user Specified Field) to populate
		 **/
		
		if (turbinesNearSolarFarms) {
			for (State state : states) {
				if (state.getPSH() >= valuePSH) {
					useableStates.add(state);
				}
			}
		} else if (!turbinesNearSolarFarms) {
			for (State state : states) {
				if (state.getPSH() >= valuePSH) {
					useableSolarStates.add(state);
					
					if (allStatesUsed.indexOf(state) == -1) {
						allStatesUsed.add(state);
					}
					
				}
				if (state.getWindSpeed()[0] >= valueWindSpeed) {
					useableWindStates.add(state);
					
					if (allStatesUsed.indexOf(state) == -1) {
						allStatesUsed.add(state);
					}
				}
			}
		}
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
	
	public static void writeTest(Integer i, ArrayList<Long> statsList, boolean placeTurbinesNearFarms) {
		
		//System.out.println("Writing Stats - START");
		//System.out.println();
		
		// fileName is unique to the test number (test #1 is "test_1.txt" and test #47 is "test_47.txt")
		String fileName;
		if (i == null) {
			fileName = "Testing_Data/test";
		} else {
			fileName = "Testing_Data/test_" + i;
		}
		
		if (placeTurbinesNearFarms) {

			try {
				file = new File(fileName);
				file.createNewFile();
				writer = new FileWriter(file);

				for (State state : useableStates) {

					writer.write(state.getName() + "\n");
					writer.write("\tUsed Land: " + state.usedLand + "(ft^2)\n");
					writer.write("\tUsed Land: " + convertSqFeet_ToSqAcre(state.usedLand) + "(acre^2)\n");
					writer.write("\tUsed Land: " + covertSqFeet_ToSqMiles(state.usedLand) + "(mi^2)\n");
					writer.write("\tPower Gen: " + state.powerGenerated + " (kWh)\n");
					writer.write("\tTurbine 262's: " + state.windTurbineTypes[0] + "\n");
					writer.write("\tTurbine 295's: " + state.windTurbineTypes[1] + "\n");
					writer.write("\tTurbine 328's: " + state.windTurbineTypes[2] + "\n");
					writer.write("\tMicro Grids w/o Turbines: " + state.microGrid_withoutTurbine + "\n");
					writer.write("\tMicro Grids w/ Turbines: " + state.microGrid_withTurbine + "\n");
					writer.write("\tIndustrial Grids w/o Turbines: " + state.industrialFarm_withoutTurbine + "\n");
					writer.write("\tIndustrial Grids w/ Turbines: " + state.industrialFarm_withTurbine + "\n");
				}

				writer.write("\nTotal Power Gen: " + statsList.get(0) + " (kWh)\n");
				writer.write("Needed Power Gen: " + targetHours + " (kWh)\n");
				writer.write("PanelCount: " + statsList.get(1) + "\n");
				writer.write("Used Land: " + statsList.get(2) + "(ft^2)\n");
				writer.write("Used Land: " + convertSqFeet_ToSqAcre(statsList.get(2)) + "(acre^2)\n");
				writer.write("Used Land: " + covertSqFeet_ToSqMiles(statsList.get(2)) + "(mi^2)\n");
				writer.write("Turbine 262's: " + statsList.get(4) + "\n");
				writer.write("Turbine 295's: " + statsList.get(5) + "\n");
				writer.write("Turbine 328's: " + statsList.get(6) + "\n");
				writer.write("Micro Grids w/o Turbines: " + statsList.get(7) + "\n");
				writer.write("Micro Grids w/ Turbines: " + statsList.get(8) + "\n");
				writer.write("Industrial Grids w/o Turbines: " + statsList.get(9) + "\n");
				writer.write("Industrial Grids w/ Turbines: " + statsList.get(10) + "\n");
				writer.write("Total Farms: " + statsList.get(3) + "\n");

				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (!placeTurbinesNearFarms) {
			
			try {
				file = new File(fileName);
				file.createNewFile();
				writer = new FileWriter(file);

				for (State state : allStatesUsed) {

					writer.write(state.getName() + "\n");
					writer.write("\tUsed Land: " + state.usedLand + "(ft^2)\n");
					writer.write("\tUsed Land: " + convertSqFeet_ToSqAcre(state.usedLand) + "(acre^2)\n");
					writer.write("\tUsed Land: " + covertSqFeet_ToSqMiles(state.usedLand) + "(mi^2)\n");
					writer.write("\tPower Gen: " + state.powerGenerated + " (kWh)\n");
					writer.write("\tTurbine 262's: " + state.windTurbineTypes[0] + "\n");
					writer.write("\tTurbine 295's: " + state.windTurbineTypes[1] + "\n");
					writer.write("\tTurbine 328's: " + state.windTurbineTypes[2] + "\n");
					writer.write("\tMicro Grid: " + state.microGrids_Used + "\n");
					writer.write("\tIndustrial Grids: " + state.industrialGrids_Used + "\n");
				}

				writer.write("\nTotal Power Gen: " + statsList.get(0) + " (kWh)\n");
				writer.write("Needed Power Gen: " + targetHours + " (kWh)\n");
				writer.write("PanelCount: " + statsList.get(1) + "\n");
				writer.write("Used Land: " + statsList.get(2) + "(ft^2)\n");
				writer.write("Used Land: " + convertSqFeet_ToSqAcre(statsList.get(2)) + "(acre^2)\n");
				writer.write("Used Land: " + covertSqFeet_ToSqMiles(statsList.get(2)) + "(mi^2)\n");
				writer.write("Turbine 262's: " + statsList.get(7) + "\n");
				writer.write("Turbine 295's: " + statsList.get(8) + "\n");
				writer.write("Turbine 328's: " + statsList.get(9) + "\n");
				writer.write("Micro Grid: " + statsList.get(5) + "\n");
				writer.write("Industrial Grids: " + statsList.get(6) + "\n");
				writer.write("Solar Farms: " + statsList.get(4) + "\n");
				writer.write("'Wind Farms': " + statsList.get(10) + "\n");
				writer.write("Total Farms: " + statsList.get(3) + "\n");

				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
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
	
	public static void writeAverages(Integer tests) {

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
