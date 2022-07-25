
public class Conversions {

	final static long SqFoot_ToSqAcre = 43560;
	final static long squareMilesToSquareFeet = 27878400L;
	final static long watts_TokiloWatts = 1000;
	final static long kiloWatts_ToMegaWatts = 1000;
	
	// Land Conversions
	
	public static double convertSqFeet_ToSqAcre(double feet) {
		return (double) (feet / SqFoot_ToSqAcre);
	}

	public static double convertSqFeet_ToSqMiles(double feet) {
		return (double) (feet / squareMilesToSquareFeet);
	}
	
	public static long convertAcres_ToSqFeet(double acres) {
		return (long) Math.ceil((long) acres * SqFoot_ToSqAcre);
	}
	
	public static long convertMiles_ToSqFeet(int miles) {
		return (long) Math.ceil((long) miles * squareMilesToSquareFeet);
	}
	
	public static long convertMiles_ToSqFeet(double miles) {
		return (long) Math.ceil((long) miles * squareMilesToSquareFeet);
	}
	
	// Power Conversions
	
	public static double convertWatts_TokiloWatts(double watts) {
		return (double) (watts / watts_TokiloWatts);
	}
	
	public static double convertkiloWatts_ToMegaWatts(double kiloWatts) {
		return (double) (kiloWatts / kiloWatts_ToMegaWatts);
	}
	
	public static double convertkiloWatts_ToWatts(double watts) {
		return (double) (watts * watts_TokiloWatts);
	}
	
	public static double convertMegaWatts_TokiloWatts(double kiloWatts) {
		return (double) (kiloWatts * kiloWatts_ToMegaWatts);
	}
	
}
