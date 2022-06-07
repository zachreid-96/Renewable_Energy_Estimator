
public class State {
	
	/** Variable Names Explained (FOR STATE)
	 **
	 ** name = State name
	 ** miles = Square Miles of State
	 ** psh = Average Peak Sun Hours of State
	 ** m100 = Average Wind Speed (in meters) of State
	 ** elevation = Mean Elevation of State
	 ** temp = Average Annual Temperature of State
	 ** humidity = Average 'Statewide' Afternoon Humidity Level of State
	 ** airDensity = preCalculated Air Density based off state Elevation and height of Wind Turbine (295ft is standard WT height)
	 **/
	
	public String name;
	public int miles;
	public double psh;
	public double[] windSpeed;
	public int elevation;
	public double temp;
	public int humidity;
	public double airDensity;
	public long useableLand;
	public long usedLand;
	public long powerGenerated;
	public int solarFields_withTurbine;
	public int solarFields_withoutTurbine;
	
	/** Variable Names Explained (FOR FORMULAS)
	 **
	 ** SPa = static pressure at sea level (Pa)
	 ** Rd = Gas Constant for dry air (287.05 J/(kg * K))
	 ** Rh = Gas Constant for water vapor (461.495 J/(kg * K))
	 **/
	
	private final int Pa = 101325;
	private final int Tk = 15; // Using Celcius
	private final static int SPa = 101325;
	private final static double Rd = 287.05;
	private final static double Rh = 461.495;
	
	public State() {
		this.name = "";
		this.miles = 0;
		this.psh = 0;
		this.windSpeed = new double[] {0,0};
		this.elevation = 0;
		this.temp = 0;
		this.humidity = 0;
		this.useableLand = 0;
		this.usedLand = 0;
		this.powerGenerated = 0;
		this.solarFields_withTurbine = 0;
		this.solarFields_withoutTurbine = 0;
	}
	
	public State(String name, int miles, double psh, double[] m100, int elevation, double temp, int humid) {
		this.name = name;
		this.miles = miles;
		this.psh = psh;
		this.windSpeed = m100;
		this.elevation = elevation;
		this.temp = temp;
		this.humidity = humidity;
		this.useableLand = setUseableLand();
		this.usedLand = 0;
		this.powerGenerated = 0;
		this.solarFields_withTurbine = 0;
		this.solarFields_withoutTurbine = 0;
		
	} // End full Constructor
	
	public State(String state) {
		
		String[] state_String = state.split(", ");
		
		this.name = state_String[0].split(": ")[1];
		this.miles = parseInt(state_String[1].split(": ")[1]);
		this.psh = parseDouble(state_String[2].split(":" )[1]);
		this.windSpeed = parseDoubleArr(state_String[3].split(": ")[1]);
		this.elevation = parseInt(state_String[4].split(": ")[1]);
		this.temp = parseDouble(state_String[5].split(": ")[1]);
		this.humidity = parseInt(state_String[6].split(": ")[1]);
		this.airDensity = parseDouble(state_String[7].split(": ")[1]);
		this.useableLand = setUseableLand();
		this.usedLand = 0;
		this.powerGenerated = 0;
		this.solarFields_withTurbine = 0;
		this.solarFields_withoutTurbine = 0;
	}
	
	private long setUseableLand() {
		
		long squareMilesToSquareFeet = 27878400L;
		
		return (long) Math.ceil(this.miles * 0.01 * squareMilesToSquareFeet);
		
	}
	
	public void setDensity() {
		if (this.humidity <= 40) {
			this.airDensity = dryAirDensity(this.elevation + 295, this.temp);
		} else if (this.humidity > 40) {
			this.airDensity = humidAirDensity(this.elevation + 295, this.temp);
		}
	}
	
	private int parseInt(String miles) {
		return Integer.parseInt(miles);
	}
	
	private double parseDouble(String psh) {
		return Double.parseDouble(psh);
	}
	
	private double[] parseDoubleArr(String m100) {
		
		double[] m = new double[2];
		String[] temp = m100.split(" - ");
		
		m[0] = Double.parseDouble(temp[0].substring(1));
		m[1] = Double.parseDouble(temp[1].substring(0, temp[1].length() - 1));
		
		return m;
	}
	
	private static double getPressure(double h) {		
		
		return (SPa * Math.pow((1 - 2.25577 * Math.pow(10, -5) * h), 5.25588));
	}
	
	private static double dryAirDensity(double h, double tk) {	
		
		double heightMeters = h / 3.2808;
		double tempK = (tk + 273.15);
		double pa = getPressure(heightMeters);
		
		return (getPressure(heightMeters) / (Rd * tempK));
	}
	
	private static double humidAirDensity(double h, double tk) {
		
		double tempK = (tk + 273.15);
		double part_1 = dryAirDensity(h, tk) / (Rd * tempK);
		double part_2 = waterVaporPressure(tk) / (Rh * tempK);
		
		return (part_1 + part_2);
	}
	
	private static double waterVaporPressure(double tk) {
		
		double T = ((tk -32) * 5 / 9);
		final double Eso = 6.1078;
		
		double c_0 = 0.99999683;
		double c_1 = -0.90826951 * Math.pow(10, -2);
		double c_2 = 0.78736169 * Math.pow(10, -4);
		double c_3 = -0.61117958 * Math.pow(10, -6);
		double c_4 = 0.43884187 * Math.pow(10, -8);
		double c_5 = -0.29883885 * Math.pow(10, -10);
		double c_6 = 0.21874425 * Math.pow(10, -12);
		double c_7 = -0.17892321 * Math.pow(10, -14);
		double c_8 = 0.11112018 * Math.pow(10, -16);
		double c_9 = -0.30994571 * Math.pow(10, -19);
		
		double p = (c_0 + T * (c_1 + T * (c_2 + T * (c_3 + T * (c_4 + T * (c_5 + T * (c_6 + T * (c_7 + T * (c_8 + T * (c_9))))))))));
		
		return (Eso / (Math.pow(p, 8))) * 100;
	}
	
	public String writeDouble(double[] m) {
		
		StringBuilder str = new StringBuilder();
		
		str.append("[" + m[0] + " - " + m[1] + "]");
		
		return str.toString();
		
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("State: " + this.name + ", ");
		str.append("Square Miles: " + this.miles + ", ");
		str.append("Average Peak Sun Hours: " + this.psh + ", ");
		str.append("Average Wind Speed 100m (m): " + writeDouble(this.windSpeed) + ", ");
		str.append("Mean Elevation (ft): " + this.elevation + ", ");
		str.append("Mean Annual Temperature (F): " + this.temp + ", ");
		str.append("Afternoon Humidity (%): " + this.humidity + ", ");
		str.append("Air Density (kg/m^3): " + this.airDensity + "\n");
		
		return str.toString();
	}
	
	
}
