
// Used for SolarPanel ONLY Farms (NO Wind Turbines ALLOWED)

public class SolarFarm {
	
	public int maxLand = 200; // acres
	public int minLand = 10; // acres
	
	public double useablePercent;
	
	public long totalArea; // ft^2
	public long useableArea; // ft^2
	public int panelCount;
	public long panelPower;
	public int acres;
	
	public SolarFarm() {
		this.totalArea = 0;
	}
	
	public SolarFarm(int acres) {
		
		this.acres = acres;
		this.totalArea = convertAcreFoot(acres);
		this.useablePercent = getPercent();
		this.useableArea = (long) Math.ceil(this.totalArea * this.useablePercent);
		
	}
	
	/**
	 ** Coverts acres to ft^2
	 ** 
	 ** @param acres = passed amount of acres
	 ** @return squareFeet = acres coverted to ft^2
	 */
	
	private long convertAcreFoot(double acres) {
		
		// 1 acre = 43560 ft^2
		
		return (long) Math.floor(acres * 43560);
		
	}
	
	/**
	 ** min = a minimum of 10 acres is required for an Industrial Solar Farm
	 ** max = a maximum of 200 acres is alloted for an Industrial Solar Farm
	 ** useablePercent = a maximum of 60% of the allocated land can actually be used
	 **
	 ** But for whatever random reason lets assume a range of 10%-60% is useable
	 ** 	This could be for roads, farms, highways, forests, parks, homes, etc.
	 ** 	are nearby and made less land useable
	 **
	 ** @return percent = Randomly chosen percent between min and max
	 **/
	
	private double getPercent() {
		
		double min = 0.10;
		double max = 0.60;
		
		return (Math.random() * (max - min) + min);
	} // End getPercent
	
	/**
	 ** Gets the area of one (1) Solar Panel
	 ** Partitions this.usesableArea to allow for space between rows
	 ** Then divides useableArea by the area of one (1) Solar Panel to get
	 ** 	how many Solar Panels could fit nicely
	 ** 
	 ** @param solarPanel = Solar Panel of Choice
	 ** @return panels = Number of Solar Panels that would fit within the calculated area
	 **/
	
	public int getPanelCount(Solar_Panel solarPanel) {
		
		double solarPanelArea = solarPanel.getArea();

		int solarPanelSpace;
		int panels;

		if (this.useableArea % 2 == 0) {
			solarPanelSpace = (int) Math.floor(this.useableArea * (1.0 / 2.0));
		} else {
			solarPanelSpace = (int) Math.floor(this.useableArea * (2.0 / 3.0));
		}

		panels = (int) Math.floor(solarPanelSpace / solarPanelArea);

		return panels;
	} // End getPanelCount
	
	
}
