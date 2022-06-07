import java.io.*;
import java.util.*;

public class testing {

	static FileWriter writer;
	static FileReader FileReader;
	static BufferedReader reader;
	static File file;
	static ArrayList<Dictionary> states_Test = new ArrayList<Dictionary>();
	static HashMap<String, Object> state_old;

	static ArrayList<State> states = new ArrayList<State>();
	static State state;
	static ArrayList<State> useableStates = new ArrayList<State>();
	final static Random randomGen = new Random();
	static SolarFarm solarFarmArray;

	/**
	 * URL:
	 * https://www.currentresults.com/Weather/US/annual-average-humidity-by-state.php
	 * USE: Average Humidity Levels (%) per State
	 * 
	 * URL:https://www.currentresults.com/Weather/US/average-annual-state-temperatures.php
	 * USE: Average Annual Temperature (F) per State
	 * 
	 * URL:
	 * https://en.wikipedia.org/wiki/List_of_U.S._states_and_territories_by_elevation
	 * USE: Mean Elevation per State
	 * 
	 * URL: https://www.brisbanehotairballooning.com.au/calculate-air-density/ USE:
	 * Air Density Formula
	 * 
	 * URL: https://www.mide.com/air-pressure-at-altitude-calculator USE: Pressure
	 * Formula (not used)
	 * 
	 * URL: https://www.engineeringtoolbox.com/air-altitude-pressure-d_462.html
	 * USER: Pressure Formula (used)
	 * 
	 * URL:
	 * https://statesymbolsusa.org/symbol-official-item/national-us/uncategorized/states-size
	 * USE: Sizes of States (miles squared)
	 * 
	 * URL: https://sciencing.com/info-8337416-much-farmer-make-wind-turbine.html
	 * USE: Efficiency Formula of Wind Turbines AND Power Output Formula of Wind
	 * Turbines (less complete)
	 * 
	 * URL: https://www.nrel.gov/gis/wind-resource-maps.html USE: Average Wind
	 * Speeds (m/s) of States at 100m above surface
	 * 
	 * URL: https://www.omnicalculator.com/ecology/wind-turbine USE: Wind Turbine
	 * Wind Power, Efficiency, HAWT, and Power Formulas (more complete)
	 * 
	 * URL:
	 * https://www.ysgsolar.com/blog/top-5-solar-farm-land-requirements-ysg-solar
	 * USE: Solar Farm Layouts
	 * 
	 * URL: https://www.energy.gov/eere/articles/wind-turbines-bigger-better USE:
	 * Wind Turbine sizing and Watt Ratings
	 * 
	 */

	/**
	 * Variable Names Explained (FOR FORMULAS)
	 **
	 ** Pa = static pressure at sea level (Pa) Tk = standard temperature at sea level
	 * (Kelvin [K]) L = standard temperature lapse (K/m) Hsl = Height at Sea Level
	 * (m) Hbal = Height at the Bottom of the Atmospheric Layer (m) R = Universal
	 * Gas Constant G = Gravitional Acceleration Constant M = Molar Mass of Earth's
	 * Air
	 **/

	private final static int SPa = 101325;
	private final static double Rd = 287.05;
	private final static double Rh = 461.495;

	// height, width, cells, low_Watt, high_Watt

	static Solar_Panel Commercial_Panel;
	static Solar_Panel Residential_Panel;

	// height, armLength, rating, efficiency

	static Wind_Turbine turbine_262;
	static Wind_Turbine turbine_295;

	// Total Kwh (Kilo Watt Hours) produced in 2021 by USA
	final static long usedHours_2021 = 4120000000000L;

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

				windPower = turbine_295.getOutputPower(currState.windSpeed, currState.airDensity);
				// long windPower = -1L;

				if (panelCount != -1) {
					powerGen = (long) Math.ceil(Commercial_Panel.getYearWatt(sunHours) * panelCount);
					currState.powerGenerated += powerGen;
					currState.usedLand += farmArea;

					totalPower += powerGen;
					//System.out.println("WindPower: " + windPower);
					if (windPower != -1) {
						//
						currState.powerGenerated += windPower;
						totalPower += windPower;
						currState.solarFields_withTurbine++;
					} else {
						currState.solarFields_withoutTurbine++;
					}
				}
			}
		}

		for (State state : useableStates) {
			System.out.print(state.name + " - Used Land: " + covertSqFeet_ToSqMiles(state.usedLand) + "(mi^2)");
			System.out.print(" Power Gen: " + state.powerGenerated + " Farm w/Turbine: " + state.solarFields_withTurbine);
			System.out.println(" Farm w/o Turbine: " + state.solarFields_withoutTurbine);
			totalLandUsed += state.usedLand;
			farmWithoutTurbine += state.solarFields_withoutTurbine;
			farmWithTurbine += state.solarFields_withTurbine;
		}
		
		totalFarms = farmWithoutTurbine + farmWithTurbine;

		System.out.println("\nTotal Power Gen: " + totalPower);
		System.out.println("Needed Power Gen: " + usedHours_2021);
		System.out.println("PanelCount: " + totalPanel);
		System.out.println("Land Used: " + covertSqFeet_ToSqMiles(totalLandUsed) + "(mi^2)");
		System.out.println("Farms w/Turbines: " + farmWithTurbine);
		System.out.println("Farms w/o Turbines: " + farmWithoutTurbine);
		System.out.println("Farms Total: " + totalFarms);

	} // End Main

	private static void createSolarFarm() {
		// Something here needs FIXING
		// Picks from a random number between 10 and 200 acres
		int solarFarmSize = (int) (Math.random() * (200 - 10) + 10);
		solarFarmArray = new SolarFarm(solarFarmSize);
		//solarFarmArray = new SolarFarm(solarFarmSize);
	}
	
	private static long covertSqFeet_ToSqMiles(long feet) {
		
		long squareMilesToSquareFeet = 27878400L;
		
		return (long) Math.ceil(feet / squareMilesToSquareFeet);
	}

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

	private static void createStates() {

		try {
			file = new File("states.txt");
			file.createNewFile();
			writer = new FileWriter(file);

			writer.write(createState("Alabama", 50750, 4.23, new double[] { 4.0, 5.9 }, 500, 62.8, 52));
			writer.write(createState("Alaska", 570641, 3.99, new double[] { 0, 0 }, 1900, 26.6, 64));
			writer.write(createState("Arizona", 113624, 6.57, new double[] { 2.0, 4.9 }, 4100, 60.3, 25));
			writer.write(createState("Arkansas", 52075, 4.69, new double[] { 5.0, 5.9 }, 650, 60.4, 64));
			writer.write(createState("California", 115973, 5.38, new double[] { 2.0, 4.9 }, 2900, 59.4, 62));
			writer.write(createState("Colorado", 103730, 4.87, new double[] { 2.0, 5.9 }, 6800, 45.1, 35));
			writer.write(createState("Connecticut", 4845, 3.84, new double[] { 5.0, 5.9 }, 500, 49, 52));
			writer.write(createState("Delaware", 1955, 4.23, new double[] { 5.0, 5.9 }, 60, 55.3, 54));
			writer.write(createState("Florida", 53997, 5.67, new double[] { 4.0, 4.9 }, 100, 70.7, 57));
			writer.write(createState("Georgia", 57919, 4.74, new double[] { 4.0, 5.9 }, 600, 63.5, 50));
			writer.write(createState("Hawaii", 6423, 6.02, new double[] { 0, 0 }, 3030, 70, 56));
			writer.write(createState("Idaho", 82751, 4.92, new double[] { 2.0, 4.9 }, 5000, 44.4, 41));
			writer.write(createState("Illinois", 55593, 3.14, new double[] { 6.0, 7.9 }, 600, 51.8, 58));
			writer.write(createState("Indiana", 35870, 4.21, new double[] { 6.0, 7.9 }, 795, 51.7, 58));
			writer.write(createState("Iowa", 55875, 4.55, new double[] { 7.0, 7.9 }, 1100, 47.8, 56));
			writer.write(createState("Kansas", 81823, 5.79, new double[] { 7.0, 8.9 }, 2000, 54.3, 50));
			writer.write(createState("Kentucky", 39732, 4.94, new double[] { 6.0, 6.9 }, 750, 55.6, 55));
			writer.write(createState("Louisiana", 43566, 4.92, new double[] { 5.0, 5.9 }, 100, 66.4, 61));
			writer.write(createState("Maine", 30865, 4.51, new double[] { 6.0, 6.9 }, 600, 41, 61));
			writer.write(createState("Maryland", 9775, 4.47, new double[] { 5.0, 5.9 }, 350, 54.2, 52));
			writer.write(createState("Massachusetts", 7838, 3.84, new double[] { 5.0, 5.9 }, 500, 47.9, 59));
			writer.write(createState("Michigan", 56539, 4, new double[] { 5.0, 6.9 }, 900, 44.4, 61));
			writer.write(createState("Minnesota", 79617, 4.53, new double[] { 6.0, 7.9 }, 1200, 41.2, 55));
			writer.write(createState("Mississippi", 46914, 4.44, new double[] { 4.0, 5.9 }, 300, 63.4, 54));
			writer.write(createState("Missouri", 68898, 4.73, new double[] { 6.0, 7.9 }, 800, 54.4, 53));
			writer.write(createState("Montana", 145556, 4.93, new double[] { 5.0, 7.9 }, 3400, 42.7, 45));
			writer.write(createState("Nebraska", 76878, 4.79, new double[] { 6.0, 7.9 }, 2600, 48.8, 53));
			writer.write(createState("Nevada", 109806, 6.41, new double[] { 2.0, 4.9 }, 5500, 49.9, 32));
			writer.write(createState("New Hampshire", 8969, 4.61, new double[] { 5.0, 5.9 }, 1000, 43.8, 53));
			writer.write(createState("New Jersey", 7419, 4.21, new double[] { 5.0, 5.9 }, 250, 52.7, 59));
			writer.write(createState("New Mexico", 121365, 6.77, new double[] { 5.0, 6.9 }, 5700, 53.4, 29));
			writer.write(createState("New York", 47224, 3.79, new double[] { 4.0, 5.9 }, 1000, 45.4, 61));
			writer.write(createState("North Carolina", 48718, 4.71, new double[] { 4.0, 5.9 }, 700, 59, 52));
			writer.write(createState("North Dakota", 68994, 5.01, new double[] { 6.0, 7.9 }, 1900, 40.4, 51));
			writer.write(createState("Ohio", 40953, 4.15, new double[] { 6.0, 7.9 }, 850, 50.7, 57));
			writer.write(createState("Oklahoma", 68679, 5.59, new double[] { 6.0, 7.9 }, 1300, 59.6, 48));
			writer.write(createState("Oregon", 96003, 4.03, new double[] { 2.0, 4.9 }, 3300, 48.4, 59));
			writer.write(createState("Pennsylvania", 44820, 3.91, new double[] { 4.0, 5.9 }, 1100, 48.8, 54));
			writer.write(createState("Rhode Island", 1034, 4.23, new double[] { 5.0, 5.9 }, 200, 50.1, 57));
			writer.write(createState("South Carolina", 30111, 5.06, new double[] { 4.0, 5.9 }, 350, 62.4, 49));
			writer.write(createState("South Dakota", 75898, 5.23, new double[] { 6.0, 7.9 }, 2200, 45.2, 53));
			writer.write(createState("Tennessee", 41220, 4.45, new double[] { 5.0, 5.9 }, 900, 57.6, 53));
			writer.write(createState("Texas", 261914, 4.92, new double[] { 5.0, 8.9 }, 1700, 64.8, 49));
			writer.write(createState("Utah", 82168, 5.26, new double[] { 2.0, 4.9 }, 6100, 48.6, 43));
			writer.write(createState("Vermont", 9249, 4.13, new double[] { 5.0, 5.9 }, 1000, 42.9, 58));
			writer.write(createState("Virginia", 39598, 4.13, new double[] { 5.0, 5.9 }, 950, 55.1, 52));
			writer.write(createState("Washington", 66582, 3.57, new double[] { 2.0, 4.9 }, 1700, 48.3, 62));
			writer.write(createState("West Virginia", 24087, 3.65, new double[] { 5.0, 5.9 }, 1500, 51.8, 59));
			writer.write(createState("Wisconsin", 54314, 4.29, new double[] { 6.0, 7.9 }, 1050, 43.1, 58));
			writer.write(createState("Wyoming", 97105, 6.06, new double[] { 3.0, 6.9 }, 6700, 42, 43));

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	} // End createStates

	/**
	 * 
	 * @param name      = State name
	 * @param miles     = Square miles of state
	 * @param psh       = Average Peak Sun Hours of state
	 * @param m100      = Average wind speed (in meters) of state
	 * @param elevation = Mean elevation of state
	 * @param temp      = Average Annual Temperature (in Fahrenheit) of state
	 * @param humid     = Average Afternoon Humidity Percent of state
	 * @return
	 */
	private static String createState(String name, int miles, double psh, double[] m100, int elevation, double temp,
			int humid) {

		state_old = new LinkedHashMap();
		StringBuilder str = new StringBuilder();

		state_old.put("State: ", name);
		state_old.put("Square Miles: ", miles);
		state_old.put("Average Peak Sun Hours: ", psh);
		state_old.put("Average Wind Speed 100m (m): ", m100);
		state_old.put("Mean Elevation: ", elevation);
		state_old.put("Mean Annual Temperature (F): ", temp);
		state_old.put("Afternoon Humidity (%): ", humid);
		state_old.put("Air Density (kg/m^3): ", 0.0);

		for (Map.Entry<String, Object> entry : state_old.entrySet()) {
			str.append(entry.getKey());
			if (entry.getValue() instanceof double[]) {
				double[] val = (double[]) entry.getValue();
				str.append("[" + val[0] + " - " + val[1] + "]");
			} else if (!(entry.getValue() instanceof double[])) {
				str.append(entry.getValue());
			}
			str.append(", ");

		}
		str.append("\n");
		return str.toString();

	}

}
