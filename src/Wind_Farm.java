
public class Wind_Farm {
	
	private int turbineCount;
	
	private double[] state_windSpeed;
	private double state_airDensity;
	
	private long windPower_Farm = 0;
	
	private long windFarmArea_feetSQ = -1;
	private long windFarmArea_acreSQ = -1;
	
	private int min;
	private int max;
	
	private String name;
	
	public Wind_Farm(String name) {
		
		this.name = name;
		this.turbineCount = 0;
		this.state_windSpeed = new double[] { };
		this.state_airDensity = 0;

	}

	public Wind_Farm(String name, double[] windSpeed, double airDensity, Wind_Turbine turbine, int[] size) {
		
		this.name = name;
		this.min = size[0];
		this.max = size[1];
		this.turbineCount = (int) Math.floor(Math.random() * (this.max - this.min) + this.min);
		this.state_windSpeed = windSpeed;
		this.state_airDensity = airDensity;
		
		setWindPower(turbine);

	}
	
	public String getName() {
		return this.name;
	}
	
	public int getTurbineCount() {
		return this.turbineCount;
	}
	
	public long getWindPower_Farm() {
		return this.windPower_Farm;
	}
	
	private void setWindPower(Wind_Turbine turbine) {
		
		int succeeds = 0;
		
		for (int i = 0; i < this.turbineCount; i++) {
			
			long windPower = turbine.getOutputPower(this.state_windSpeed, this.state_airDensity);
			if (windPower != -1) {
				this.windPower_Farm += windPower;
				succeeds++;
			}
		}
		
		if (succeeds > this.min) {
			this.turbineCount = succeeds;
			this.windFarmArea_feetSQ = (long) Math.ceil(turbine.getArea() * this.turbineCount);
			this.windFarmArea_acreSQ = (long) Math.ceil(Conversions.convertSqFeet_ToSqAcre(this.windFarmArea_feetSQ));
		} else if (succeeds < this.min) {
			this.turbineCount = 0;
			this.windPower_Farm = 0;
		}
	}
	
	public double getArea_Feet() {
		return this.windFarmArea_feetSQ;
	}
	
	public double getArea_Acre() {
		return this.windFarmArea_acreSQ;
	}
	
}
