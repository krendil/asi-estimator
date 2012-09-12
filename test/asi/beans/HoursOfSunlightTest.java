/**
 * 
 */
package asi.beans;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import asi.TestUtils;

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
	
	private final double EPSILON = 1E-6;
	
	private String XML_TAG = "<sunlight hours=\"4.8\" />";
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test
	public void testConstructorGood() throws EstimatorException {
		new HoursOfSunlight(GOOD_HOURS);
	}
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test(expected=EstimatorException.class)
	public void testConstructorNegative() throws EstimatorException {
		new HoursOfSunlight(NEG_HOURS);
	}
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test(expected=EstimatorException.class)
	public void testConstructorHigh() throws EstimatorException {
		new HoursOfSunlight(HIGH_HOURS);
	}
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test
	public void testConstructorZero() throws EstimatorException {
		new HoursOfSunlight(0);
	}
	
	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#HoursOfSunlight(double)}.
	 * @throws EstimatorException 
	 */
	@Test
	public void testConstructorMax() throws EstimatorException {
		new HoursOfSunlight(MAX_HOURS);
	}

	/**
	 * Test method for {@link asi.beans.HoursOfSunlight#getMultiplier()}.
	 * @throws EstimatorException 
	 */
	@Test
	public void testGetMultiplier() throws EstimatorException {
		double expected = GOOD_HOURS * DAYS_IN_YEAR;
		HoursOfSunlight hours = new HoursOfSunlight(GOOD_HOURS);

		for(int i = 0; i < 20; i++) {
			assertEquals(expected, hours.getMultiplier(0), EPSILON);
		}
	}
	
	@Test
	public void constructFromNode() throws EstimatorException, ParserConfigurationException, SAXException, IOException {
		HoursOfSunlight hours = new HoursOfSunlight(TestUtils.getNodeFromString(XML_TAG));
		double expected = 4.8 * DAYS_IN_YEAR;
		
		for(int i = 0; i < 20; i++) {
			assertEquals(expected, hours.getMultiplier(0), EPSILON);
		}
	}

}
