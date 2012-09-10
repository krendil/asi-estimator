package asi.beans;

import java.math.BigDecimal;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * This is the feed in rate to the electrical company.
 * @author Steven Turner
 *
 */
public class FeedInRate extends Modifier {
	BigDecimal feedInRate; // $ amount feed in rate per kWh
	
	public FeedInRate(Node n) throws EstimatorException {
		setRate( new BigDecimal(((Element) n).getAttribute("rate")) );
	}
	
	public FeedInRate(BigDecimal rate) throws EstimatorException {
		setRate( rate );
	}
	
	/**
	 * 
	 * @param rate
	 * @throws EstimatorException
	 */
	private void setRate( BigDecimal rate ) throws EstimatorException {
		// sanity checks
		
		if ( rate.signum() <= 0 ) {
			throw new EstimatorException ( "Feed in rate cannot be equal to, or under, $0.00" );
		}
		
		this.feedInRate = rate;
	}
	
	
	public BigDecimal getFeedInRate() {
		return feedInRate;
	}
	
	@Override
	public BigDecimal getRevenueMultiplier(int year) {
		return feedInRate;
	}
	
}
