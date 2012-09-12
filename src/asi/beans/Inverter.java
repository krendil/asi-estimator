package asi.beans;

import java.math.BigDecimal;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Inverter extends Modifier {

	private double efficiency;
	private BigDecimal price;
	
	public Inverter(Node n) throws NumberFormatException, EstimatorException {

		setEfficiency( Double.parseDouble(((Element) n).getAttribute("efficiency")) );
		setPrice( new BigDecimal(((Element) n).getAttribute("price")) );
	}
	
	public Inverter(double efficiency, BigDecimal price) throws EstimatorException {
		setEfficiency(efficiency);
		setPrice(price);
	}
	
	private void setEfficiency(double efficiency) throws EstimatorException {
		if( Double.isNaN(efficiency)) {
			throw new EstimatorException("Efficiency must not be NaN.");
		} else if( efficiency > 1.0 ) {
			throw new EstimatorException("Efficiency must not be greater than 1.");
		} else if( efficiency < 0.0 ) {
			throw new EstimatorException("Efficiency must not be negative.");
		}
		this.efficiency = efficiency;
	}
	
	private void setPrice(BigDecimal price) throws EstimatorException {
		if( price == null ) {
			throw new EstimatorException("Price must not be null");
		} else if( price.signum() < 0 ) {
			throw new EstimatorException("Price must not be negative.");
		}
		
		this.price = price;
	}
	
	@Override
	public double getMultiplier(int year) {
		return efficiency;
	}
	
	@Override
	public BigDecimal getNewCost( int year ) {
		if(year == 0) {
			return price;
		}
		return BigDecimal.ZERO;
	}
	
	public double getEfficiency() {
		return efficiency;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
}
