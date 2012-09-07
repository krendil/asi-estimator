package asi.beans;

import java.math.BigDecimal;

/**
 * An interface representing any efficiency modifier that 
 * affects the power produced by the solar panels
 * 
 * @author David Osborne
 *
 */
public abstract class Modifier {
	
	/**
	 * Gets a multiplier value that may change over time
	 * @param years The number of complete years in the future (this year would be 0)
	 * @return
	 */
	public double getMultiplier(int years){
		return 1.0;
	}
	
	public BigDecimal getCurrentCost(int years) {
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getNewCost(int years) {
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getRevenueMultiplier(int years) {
		return BigDecimal.ONE;
	}

}
