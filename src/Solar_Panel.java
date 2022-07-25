
public class Solar_Panel {

	// ALL measurements are inches

	private double width;
	private double height;

	private int cells;

	private int wattRating; // rated in watts
	private int price;

	public Solar_Panel() {

		this.width = 0;
		this.height = 0;

		this.cells = 0;

		this.wattRating = 0;

	} // End default (empty) constructor

	public Solar_Panel(double height, double width, int cells, int wattRating, int price) {

		this.width = width; // length wise (longer)
		this.height = height; // height (shorter)

		this.cells = cells;

		this.wattRating = wattRating;
		this.price = price;

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
	
	public int getPrice() {
		return this.price;
	}

	public int getWattRating() {
		return this.wattRating;
	}

	// Estimates Power Output in kW based on passed sunHours
	
	public double getYearWatt(double sunHours) {
		return (this.wattRating * sunHours * 365 / 1000);
	}

} // End class Solar_Panel
