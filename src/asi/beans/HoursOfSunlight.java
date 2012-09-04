package asi.beans;

public class HoursOfSunlight implements Modifier {

	private static final double DAYS_IN_YEAR = 365.25;
	private static final double MAX_HOURS = 24.0;
	private static final double MIN_HOURS = 0.0;
	
	private double hours;
	
	public HoursOfSunlight(double hoursPerDay) throws EstimatorException {
		setHours(hoursPerDay);
	}
	
	private void setHours(double hours) throws EstimatorException {
		if(Double.isNaN(hours)) {
			throw new EstimatorException("Hours must be a valid number");
		} else if(hours < MIN_HOURS) {
			throw new EstimatorException("Hours must be at least 0");
		} else if (hours > MAX_HOURS) {
			throw new EstimatorException("There are not more that 24 hours in a day");
		}
		this.hours = hours;
	}
	
	@Override
	public double getMultiplier() {
		return this.hours * DAYS_IN_YEAR;
	}

}
