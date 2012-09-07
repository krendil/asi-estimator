/**
 * 
 */
package asi.beans;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

/**
 * @author David Osborne
 *
 */
public class HoursOfSunlightTest {

	private final double GOOD_HOURS = 4.5;
	private final double NEG_HOURS = -0.4;
	private final double HIGH_HOURS = 25.0;
	private final double MAX_HOURS = 24.0;
	private final double DAYS_IN_YEAR = 365.25;
	
	private final double EPSILON = 0.1;
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test
	public void testConstructorGood() throws EstimatorException {
		HoursOfSunlight hours = new HoursOfSunlight(GOOD_HOURS);
	}
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test(expected=EstimatorException.class)
	public void testConstructorNegative() throws EstimatorException {
		HoursOfSunlight hours = new HoursOfSunlight(NEG_HOURS);
	}
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test(expected=EstimatorException.class)
	public void testConstructorHigh() throws EstimatorException {
		HoursOfSunlight hours = new HoursOfSunlight(HIGH_HOURS);
	}
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test
	public void testConstructorZero() throws EstimatorException {
		HoursOfSunlight hours = new HoursOfSunlight(0);
	}
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test
	public void testConstructorMax() throws EstimatorException {
		HoursOfSunlight hours = new HoursOfSunlight(MAX_HOURS);
	}

	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#getMultiplier()}.
	 * @throws EstimatorException 
	 */
	@Test
	public void testGetMultiplier() throws EstimatorException {
		double expected = GOOD_HOURS * DAYS_IN_YEAR;
		HoursOfSunlight hours = new HoursOfSunlight(GOOD_HOURS);
		double mult = hours.getMultiplier(0);
		Assert.assertEquals(expected, mult, EPSILON);
	}

}
