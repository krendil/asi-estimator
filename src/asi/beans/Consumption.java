package asi.beans;

import java.math.BigDecimal;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * This class checks and stores information about the customer's electricity consumption and the rate
 * at which it is purchased from the electricity supplier.
 * @author Steven Turner
 *
 */
public class Consumption extends Modifier{
	private double power;	// kWh
	private BigDecimal rate;	// $ per kWh
	
	public Consumption(Node n) throws EstimatorException {
		Element el = (Element) n;
		double power = Double.parseDouble(el.getAttribute("power"));
		BigDecimal rate = new BigDecimal(el.getAttribute("rate"));

		setPower(power);
		setRate(rate);
	}
	
	public Consumption ( double power, BigDecimal rate ) throws EstimatorException {
		setPower(power);
		setRate(rate);
	}
	
	private void setPower(double power) throws EstimatorException {
		if ( power < 0 ) {
			throw new EstimatorException ( "Power consumption output cannot be less than zero." );
		}
		this.power = power;
	}
	
	private void setRate(BigDecimal rate) throws EstimatorException {

		if ( rate.signum() <= 0 ) {
			throw new EstimatorException ( "Consumption $ rate cannot be equal to, or under, $0.00" );
		}
		
		this.rate = rate;
	}
	
	/**
	 * @return the current cost given an amount of power consumed, and the going electricity rate.
	 */
	@Override
	public BigDecimal getCurrentCost(int year) {
		return this.rate.multiply(new BigDecimal(this.power));
	}
	
	public double getPower() {
		return this.power;
	}
	
	public BigDecimal getRate() {
		return this.rate;
	}
	
	
}
