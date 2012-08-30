package asi.beans;

/**
 * An interface representing any efficiency modifier that 
 * affects the power produced by the solar panels
 * 
 * @author David Osborne
 *
 */
public interface Modifier {
	
	/**
	 * Gets a multiplier value that will be applied the the power generated
	 * @return The efficiency
	 */
	public double getMultiplier();
	
	/**
	 * Gets a multiplier value that may change over time
	 * @param years The number of complete years in the future (this year would be 0)
	 * @return
	 */
	/*public double getMultiplier(int years);*/ //We don't need this yet

}
