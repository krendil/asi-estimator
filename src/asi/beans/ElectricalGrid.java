package asi.beans;


/**
 * ElectricalGrid represents the grid that the current domain is connected to.
 * This class keeps track of things like the different rates - feed in and tariff. etc.
 * 
 * @author Steven Turner
 *
 */
public class ElectricalGrid {
	
	private double[] avgPowerConsumption; // kWh average per day.
	private double[] tariffRate; // $ per kWh
	
	private double[] avgPowerGeneration; // kWh average per day.
	private double[] feedInRate; // $ per kWh
	
	/**
	 * Constructor 1 - single tariff & consumption
	 */
	public ElectricalGrid ( double avgPowerConsumption, double tariffRate,
			double avgPowerGeneration, double feedInRate ) throws EstimatorException {
		
		constSanityChecks( avgPowerConsumption, tariffRate,
				avgPowerGeneration, feedInRate );

		double inPwr[] = { avgPowerConsumption };
		double inRate[] = { tariffRate };
		double outPwr[] = { avgPowerGeneration };
		double outRate[] = { feedInRate };
		
		this.avgPowerConsumption = inPwr;
		this.tariffRate = inRate;
		this.avgPowerGeneration = outPwr;
		this.feedInRate = outRate;
	}
	
	
	/**
	 * Constructor 2 - multiple tariffs & consumptions
	 */
	public ElectricalGrid ( double[] avgPowerConsumption, double[] tariffRate,
			double[] avgPowerGeneration, double[] feedInRate ) throws EstimatorException {
		
		for ( int i = 0; i < tariffRate.length; i++ ) {
			constSanityChecks( avgPowerConsumption[i], tariffRate[i],
					avgPowerGeneration[i], feedInRate[i] );
		}

		this.avgPowerConsumption = avgPowerConsumption;
		this.tariffRate = tariffRate;
		this.avgPowerGeneration = avgPowerGeneration;
		this.feedInRate = feedInRate;
	}
	
	
	/**
	 * private class to error-check for invalid constructor input.
	 * @return
	 * @throws EstimatorException
	 */
	private void constSanityChecks( double avgPowerConsumption, double tariffRate,
			double avgPowerGeneration, double feedInRate ) throws EstimatorException {
		
		if ( avgPowerConsumption <= 0 ) {
			throw new EstimatorException( "Cannot supply a negative average power consumption." );
		}
		
		if ( tariffRate <= 0 ) {
			throw new EstimatorException( "Cannot supply a zero or negative tariff rate." );
		}
		
		if ( avgPowerGeneration <= 0 ) {
			throw new EstimatorException( "Cannot supply a negative average power generation." );
		}
		
		if ( feedInRate <= 0 ) {
			throw new EstimatorException( "Cannot supply a zero or negative feed in rate." );
		}
	}
	
	
	/**
	 * iterate over arrays to calculate daily cost in dollars
	 * @return $
	 */
	public double getDailyCost() {
		double tally = 0.0;
		
		for ( int i = 0; i < tariffRate.length; i++ ) {
			tally += tariffRate[i] * avgPowerConsumption[i];
		}
		return tally;
	}
	
	/**
	 * calculate daily credit generation in dollars
	 * @return $
	 */
	public double getDailyCredit() {
		double tally = 0.0;
		
		for ( int i = 0; i < feedInRate.length; i++ ) {
			tally += feedInRate[i] * avgPowerGeneration[i];
		}
		return tally;
	}
	
}
