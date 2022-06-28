
// Used for SolarPanel ONLY Farms (NO Wind Turbines ALLOWED)

public class Solar_Farm {
	
	private int maxLand = 200; // acres
	private int minLand = 10; // acres
	
	private double useablePercent;
	
	private long totalArea; // ft^2
	private long useableArea; // ft^2
	private int panelCount;
	private long panelPower;
	private int acres;
	private String name;
	
	public Solar_Farm(String name) {
		this.totalArea = 0;
		this.name = name;
	}
	
	public Solar_Farm(int acres, String name) {
		
		this.acres = acres;
		this.totalArea = convertAcreFoot(acres);
		this.useablePercent = getPercent();
		this.useableArea = (long) Math.ceil(this.totalArea * this.useablePercent);
		this.name = name;
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
	 **
	 ** Based on Solar Farms like Solar Star, California (21%)
	 ** 	and Datong Panda Plant, China (28%) 
	 ** 	a reasonable percentage range is 15-30%
	 **
	 ** @return percent = Randomly chosen percent between min and max
	 **/
	
	private double getPercent() {
		
		double min = 0.15;
		double max = 0.30;
		
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
		
		return (int) Math.floor(this.useableArea / solarPanelArea);
	} // End getPanelCount
	
	public String getName() {
		return this.name;
	}
	
	public long getTotalArea() {
		return this.totalArea;
	}
	
}
