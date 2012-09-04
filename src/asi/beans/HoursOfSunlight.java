package asi.beans;

public class HoursOfSunlight implements Modifier {

	private static final double DAYS_IN_YEAR = 365.25;
	private double hours;
	
	public HoursOfSunlight(double hoursPerDay) {
		this.hours = hoursPerDay;
	}
	
	@Override
	public double getMultiplier() {
		return this.hours * DAYS_IN_YEAR;
	}

}
