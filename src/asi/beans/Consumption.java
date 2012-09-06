package asi.beans;


/**
 * This class checks and stores information about the customer's electricity consumption and the rate
 * at which it is purchased from the electricity supplier.
 * @author Steven Turner
 *
 */
public class Consumption {
	double power;	// kWh
	double rate;	// $ per kWh
	
	public Consumption ( double power, double rate ) throws EstimatorException {
		// sanity checks
		if ( power < 0 ) {
			throw new EstimatorException ( "Power consumption output cannot be less than zero." );
		}
		
		if ( rate <= 0 ) {
			throw new EstimatorException ( "Consumption $ rate cannot be equal to, or under, $0.00" );
		}
		
		this.power = power;
		this.rate = rate;
	}
	
	/**
	 * @return the current cost given an amount of power consumed, and the going electricty rate.
	 */
	public double getCurrentCost() {
		return this.power * this.rate;
	}
	
	public double getPower() {
		return this.power;
	}
	
	public double getRate() {
		return this.rate;
	}
	
	
}
