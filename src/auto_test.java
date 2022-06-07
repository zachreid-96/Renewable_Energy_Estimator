import java.io.*;
import java.util.*;

public class auto_test {

	static FileWriter writer;
	static FileReader FileReader;
	static BufferedReader reader;
	static File file;

	static ArrayList<State> states = new ArrayList<State>();
	static State state;
	static ArrayList<State> useableStates = new ArrayList<State>();
	final static Random randomGen = new Random();
	static SolarFarm solarFarmArray;

	// height, width, cells, low_Watt, high_Watt

	static Solar_Panel Commercial_Panel;
	static Solar_Panel Residential_Panel;

	// height, armLength, rating, efficiency

	static Wind_Turbine turbine_262;
	static Wind_Turbine turbine_295;

	// Total Kwh (Kilo Watt Hours) produced in 2021 by USA
	final static long usedHours_2021 = 4120000000000L;

	final static int tests = 100;
	
	static long avgPanelCount = 0;
	static long avgEnergyGen = 0;
	static long avgMiles = 0;
	static long avgFarmsWithTurbine = 0;
	static long avgFarmsWithoutTurbine = 0;
	static long avgFarmTotal = 0;

	public static void main(String[] args) {

		// Creates Energy Condiuts (solar panels and the wind turbines)
		createEnergy();

		// Reads the states file and populates States list
		readStates();

		// Adds states to a new ArrayList if lowest WindSpeed AVG is above 3.5 m/s
		for (State state : states) {
			if (state.psh > 4.5 && !state.name.equals("Hawaii")) {
				useableStates.add(state);
			}
		}

		for (int i = 0; i < tests; i++) {

			getStats(i);
			clearStats();

		}

		writeTest("results");
		System.out.println("DONE");

	} // End main
	
	private static void clearStats() {
		
		for (State state : useableStates) {
			
			state.powerGenerated = 0;
			state.solarFields_withoutTurbine = 0;
			state.solarFields_withTurbine = 0;
			state.usedLand = 0;
			
		}
		
	}

	private static void getStats(int i) {

		long totalPanel = 0;
		long totalPower = 0;
		long totalLandUsed = 0;
		long totalFarms = 0;
		long farmWithTurbine = 0;
		long farmWithoutTurbine = 0;

		State currState;
		double sunHours;
		long farmArea;
		int panelCount;
		long windPower;
		long powerGen;

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

				panelCount = getPanelCount();

				totalPanel += panelCount;
				avgPanelCount += panelCount;

				windPower = turbine_295.getOutputPower(currState.windSpeed, currState.airDensity);
				// long windPower = -1L;

				if (panelCount != -1) {
					powerGen = (long) Math.ceil(Commercial_Panel.getYearWatt(sunHours) * panelCount);
					currState.powerGenerated += powerGen;
					currState.usedLand += farmArea;
					totalLandUsed += farmArea;
					avgMiles += farmArea;

					totalPower += powerGen;
					avgEnergyGen += powerGen;

					if (windPower != -1) {
						//
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

		writeTest(i, totalPower, totalPanel, totalLandUsed, farmWithTurbine, farmWithoutTurbine, totalFarms);

	} // End getStats

	private static int getPanelCount() {

		long useableArea = solarFarmArray.useableArea;
		double solarPanelArea = Commercial_Panel.getArea();

		int solarPanelSpace;
		int panels;

		if (useableArea % 2 == 0) {
			solarPanelSpace = (int) Math.floor(useableArea * (1.0 / 2.0));
		} else {
			solarPanelSpace = (int) Math.floor(useableArea * (2.0 / 3.0));
		}

		panels = (int) Math.floor(solarPanelSpace / solarPanelArea);

		return panels;

	}

	private static void createSolarFarm() {
		
		// Picks from a random number between 10 and 200 acres
		int solarFarmSize = (int) (Math.random() * (200 - 10) + 10);
		
		solarFarmArray = new SolarFarm(solarFarmSize);
	}

	private static void createEnergy() {

		Commercial_Panel = new Solar_Panel(39, 77, 72, 350, 400);
		Residential_Panel = new Solar_Panel(39, 66, 60, 250, 300);

		turbine_262 = new Wind_Turbine(262, 137.5, 1.8);
		turbine_295 = new Wind_Turbine(295, 205, 3);

	} // End createPanels

	private static void readStates() {
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
	}

	private static void writeTest(int i, long totalPower, long totalPanel, long totalLandUsed, long farmWithTurbine, long farmWithoutTurbine, long totalFarms) {

		String fileName = "Testing_Data/test_" + i;

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
	}
	
	private static double convertSqFeet_ToSqAcre(long feet) {
		
		int SqFoot_ToSqAcre = 43560;
		
		return (feet / SqFoot_ToSqAcre);
		
	}

	private static long covertSqFeet_ToSqMiles(long feet) {
		
		// This represents how many square Feet are in 1 square Mile		
		long squareMilesToSquareFeet = 27878400L;

		return (long) Math.ceil(feet / squareMilesToSquareFeet);
	}

	private static void writeTest(String i) {

		String fileName = "test_" + i;

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
	}

	private static void updateStates() {
		try {
			file = new File("states.txt");
			file.createNewFile();
			writer = new FileWriter(file);

			for (State state : states) {

				state.setDensity();
				writer.write(state.toString());

			}

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
