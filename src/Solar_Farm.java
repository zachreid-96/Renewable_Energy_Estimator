
// Used for SolarPanel ONLY Farms (NO Wind Turbines ALLOWED)

public class Solar_Farm {
	
	private double useablePercent;
	
	private long totalArea_SqFeet; // ft^2
	private long useableArea; // ft^2
	private int acres;
	private String name;
	private long price;
	private long panelCount;
	
	public Solar_Farm(String name) {
		this.totalArea_SqFeet = 0;
		this.name = name;
	}
	
	public Solar_Farm(int acres, String name, Solar_Panel solarPanel) {
		
		this.acres = acres;
		this.totalArea_SqFeet = Conversions.convertAcres_ToSqFeet(acres);
		
		if (name.equals("MicroGrid")) {
			this.useablePercent = getPercent(0.35, 0.45);
		} else if (name.equals("IndustrialGrid")) {
			this.useablePercent = getPercent(0.2, 0.3);
		}
		
		this.useableArea = (long) Math.ceil(this.totalArea_SqFeet * this.useablePercent);
		this.name = name;
		
		setPanelCount(solarPanel);
		
	}
	
	/**
	 ** min = a minimum of 10 acres is required for an Industrial Solar Farm
	 ** max = a maximum of 200 acres is alloted for an Industrial Solar Farm
	 **
	 ** Based on Solar Farms like Solar Star, California (21%)
	 ** 	and Datong Panda Plant, China (28%) 
	 ** 	a reasonable percentage range is 15-30%
	 **
	 ** Based on Micro Grids like Pilbara, Australias Horizon Power (40%) plant
	 **		a 35%-45% seems reasonable
	 **
	 ** @return percent = Randomly chosen percent between min and max
	 **/
	
	private double getPercent(double min, double max) {
		
		return (Math.random() * (max - min) + min);
	} // End getPercent
	
	/** 
	 ** @param solarPanel = Solar Panel of Choice
	 ** @return panels = Number of Solar Panels that would fit within the calculated area
	 **/
	
	private void setPanelCount(Solar_Panel solarPanel) {
		
		double solarPanelArea = solarPanel.getArea();
		int panelCount = (int) Math.floor(this.useableArea / solarPanelArea);
		
		this.price = solarPanel.getPrice() * panelCount;
		
		this.panelCount = panelCount;
	} // End getPanelCount
	
	public long getPanelCount() {
		return this.panelCount;
	}
	
	public String getName() {
		return this.name;
	}
	
	public long getTotalArea() {
		return this.totalArea_SqFeet;
	}
	
	public long getPrice() {
		return this.price;
	}
	
}
