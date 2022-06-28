
public class Solar_Panel {

	// ALL measurements are inches

	private double width;
	private double height;

	private int cells;

	private int wattLow; // rated in watts
	private int wattHigh; // rated in watts

	public Solar_Panel() {

		this.width = 0;
		this.height = 0;

		this.cells = 0;

		this.wattLow = 0;
		this.wattHigh = 0;

	} // End default (empty) constructor

	public Solar_Panel(double height, double width, int cells, int low_Watt, int high_Watt) {

		this.width = width; // length wise (longer)
		this.height = height; // height (shorter)

		this.cells = cells;

		this.wattLow = low_Watt;
		this.wattHigh = high_Watt;

	} // End full constructor

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public int getArea() {
		// Returns area in ft^2
		return (int) Math.ceil((this.width / 12) * (this.height / 12));
	}

	public int getCells() {
		return this.cells;
	}

	public int getWattLow() {
		return this.wattLow;
	}

	public int getWattHigh() {
		return this.wattHigh;
	}

	public double getAvgWatt() {
		return (this.wattLow + this.wattHigh) / 2;
	}
	
	// Estimates Power Output in kW based on passed sunHours
	
	public double getYearWatt(double sunHours) {
		return (getAvgWatt() * sunHours * 365);
	}

} // End class Solar_Panel
