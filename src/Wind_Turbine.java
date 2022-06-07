
public class Wind_Turbine {
	
	public int height; // in feet
	public double armLength; // in feet
	public int ratedWatt; // rated kiloWatts (converted from MegaWatts)
	public double HAWT;
	
	public Wind_Turbine() {
		this.height = 0;
		this.armLength = 0;
		this.ratedWatt = 0;
		this.HAWT = 0.0;
	} // End default (empty) constructor
	
	public Wind_Turbine(int height, double armLength, double rating) {
		this.height = height;
		this.armLength = armLength;
		this.ratedWatt = (int) (rating * 1000); // Converting to kiloWatts
		this.HAWT = Math.PI * Math.pow(armLength / 3.281, 2); // Converting area to m^2
	} // End full constructor
	
	public int getHeight() {
		return this.height;
	}
	
	public double getArmLength() {
		return this.armLength;
	}
	
	public int getRating() {
		return this.ratedWatt;
	}
	
	// this.armLength * 2 = diameter
	// * 7 = total unobstructed operating area
	// needs to be 7 rotor diameters away from other objects to work effectively
	public double getArea() {
		return (this.armLength * 2 * 7);
	} // End getArea
	
	// Gets the 'real' efficiency of a turbine using random numGen withing specifications
	public double getEfficiency() {
		
		/**
		 * turbineEfficiency = It must be lower than the Betz limit (59.3%), and is typically between 30-40%
		 * lossTerrain = the wake losses due to neighboring turbines and the terrain topography, typically 3-10%
		 * lossMechanical = the mechanical losses of the blades and gearbox, typically 0-0.3%
		 * lossElectrical = the electrical losses of the turbine, typically 1-1.5%
		 * lossGrid = the electrical losses of transmission to grid, typically 3-10%
		 * lossMaintenance = the percentage of time out of order due to failure or maintenance, typically 2-3%
		 * 
		 * ALL STATS chosen at random due to the unpredictable nature of wind and mechanical failures
		 * 
		 */
		
		double turbineEfficiency = (Math.random() * (0.45 - 0.25) + 0.25);
		double lossTerrain = (Math.random() * (0.10 - 0.03) + 0.03);
		double lossMechanical = (Math.random() * (0.003 - 0.0) + 0.0);
		double lossElectrical = (Math.random() * (0.015 - 0.01) + 0.01);
		double lossGrid = (Math.random() * (0.1 - 0.03) + 0.03);
		double lossMaintenance = (Math.random() * (0.03 - 0.02) + 0.02);
				
		double realEfficiency = (1 - lossMechanical) * (1 - lossElectrical) * (1 - lossGrid) * (1 - lossMaintenance) 
				* (1 - lossTerrain) * turbineEfficiency;
		
		if (realEfficiency < 0.2) {
			return -1;
		}
		
		//System.out.println("\nGetEfficiency:");
		//System.out.print("turbineEfficiency: " + turbineEfficiency);
		//System.out.print(" lossTerrain: " + lossTerrain);
		//System.out.println(" lossMechanical: " + lossMechanical);
		//System.out.print("lossElectrical: " + lossElectrical);
		//System.out.print(" lossGrid: " + lossGrid);
		//System.out.print(" lossMaintenance: " + lossMaintenance);
		//System.out.println(" realEfficiency: " + realEfficiency);
		
		return realEfficiency;
	} // End getEfficiency
	
	private double getPotentialPower(double[] windSpeed, double airDensity) {
		
		double min = windSpeed[0];
		double max = windSpeed[1];
		
		double SpeedOfWind = (Math.random() * (max - min) + min);
		
		double potentialWindPower = 0.5 * airDensity * Math.pow(SpeedOfWind, 3) * this.HAWT;
		
		//System.out.println("\ngetPotentialPower:");
		//System.out.print("SpeedOfWind: " + SpeedOfWind);
		//System.out.print(" SpeedOfWind ^ 3: " + Math.pow(SpeedOfWind, 3));
		//System.out.print(" airDensity: " + airDensity);
		//System.out.println(" potentialWindPower: " + (potentialWindPower / 1000));
		
		return (potentialWindPower / 1000);
	} // End getPotentialPower
	
	public long getOutputPower(double[] windSpeed, double airDensity) {
		
		if (windSpeed[0] < 3.0) {
			return -1;
		}
		
		double realEfficiency = getEfficiency();
		double potentialWindPower = getPotentialPower(windSpeed, airDensity);
		
		if (realEfficiency != -1) {
			
			// System.out.printf("Eff: %.2f wind: %.2f", realEfficiency, potentialWindPower);
			
			// multiplied by 24 hours a day
			// and by 100-365 days of operation
			// this will net the 'potential' energy created by this Wind Turbine
			// Watts to kiloWatt = watts * 1000
			int hours = (int) (Math.random() * (15 - 3) + 3);
			int days = (int) (Math.random() * (125 - 25) + 25);
			
			long estimatedPower = (long) Math.ceil(realEfficiency * potentialWindPower);
			
			//System.out.println("estimatedPower: " + estimatedPower);
			
			if (estimatedPower < 25) {
				return -1;
			}
			
			long estimatedPowerYear = (long) Math.ceil(realEfficiency * potentialWindPower * hours * days);
			
			//System.out.println("\ngetPotentialPower:");
			//System.out.print(" estimatedPower: " + estimatedPower);
			//System.out.print(" hours: " + hours);
			//System.out.print(" days: " + days);
			//System.out.println(" estimatedPowerYear: " + estimatedPowerYear);
			
			return estimatedPowerYear;
		}
		
		return -1;
	} // End getOutputPower
	
} // End class Wind_Turbine
