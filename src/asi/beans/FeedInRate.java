package asi.beans;


/**
 * This is the feed in rate to the electrical company.
 * @author Steven Turner
 *
 */
public class FeedInRate {
	double feedInRate; // $ amount feed in rate per kWh
	
	/**
	 * 
	 * @param rate
	 * @throws EstimatorException
	 */
	public FeedInRate( double rate ) throws EstimatorException {
		// sanity checks
		
		if ( rate <= 0.0 ) {
			throw new EstimatorException ( "Feed in rate cannot be equal to, or under, $0.00" );
		}
		
		this.feedInRate = rate;
	}
	
	
	public double getFeedInRate() {
		return feedInRate;
	}
	
}
