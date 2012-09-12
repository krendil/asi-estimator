package asi.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import asi.TestUtils;

public class InverterTest {

	private final double GOOD_EFFICIENCY = 0.95;
	private final double HIGH_EFFICIENCY = 1.08;
	private final double LOW_EFFICIENCY = -0.39;
	
	private final BigDecimal GOOD_PRICE = new BigDecimal(2500);
	private final BigDecimal LOW_PRICE = new BigDecimal(-2500);
	
	private final String XML_TAG = "<inverter efficiency=\"0.93\" price=\"2500\" />";
	

	@Test
	public void bothGood() throws EstimatorException {
		Inverter inverter = new Inverter(GOOD_EFFICIENCY, GOOD_PRICE);
		assertEquals(GOOD_EFFICIENCY, inverter.getEfficiency(), TestUtils.EPSILON);
		assertTrue(TestUtils.compareBigDecs(GOOD_PRICE, inverter.getNewCost(0)));
	}
	
	@Test(expected=EstimatorException.class)
	public void highEfficiency() throws EstimatorException {
		Inverter inverter = new Inverter(HIGH_EFFICIENCY, GOOD_PRICE);
	}

	@Test(expected=EstimatorException.class)
	public void lowEfficiency() throws EstimatorException {
		Inverter inverter = new Inverter(LOW_EFFICIENCY, GOOD_PRICE);
	}

	@Test
	public void zeroEfficiency() throws EstimatorException {
		Inverter inverter = new Inverter(0.0, GOOD_PRICE);
	}

	@Test(expected=EstimatorException.class)
	public void nanEfficiency() throws EstimatorException {
		Inverter inverter = new Inverter(Double.NaN, GOOD_PRICE);
	}
	
	@Test(expected=EstimatorException.class)
	public void infiniteEfficiency() throws EstimatorException {
		Inverter inverter = new Inverter(Double.POSITIVE_INFINITY, GOOD_PRICE);
	}
	

	@Test(expected=EstimatorException.class)
	public void nInfiniteEfficiency() throws EstimatorException {
		Inverter inverter = new Inverter(Double.NEGATIVE_INFINITY, GOOD_PRICE);
	}

	@Test(expected=EstimatorException.class)
	public void lowPrice() throws EstimatorException {
		Inverter inverter = new Inverter(GOOD_EFFICIENCY, LOW_PRICE);
	}

	@Test
	public void zeroPrice() throws EstimatorException {
		Inverter inverter = new Inverter(GOOD_EFFICIENCY, BigDecimal.ZERO);
	}

	@Test(expected=EstimatorException.class)
	public void nullPrice() throws EstimatorException {
		Inverter inverter = new Inverter(GOOD_EFFICIENCY, null);
	}
	
	
	@Test
	public void constructFromNode() throws EstimatorException, ParserConfigurationException, SAXException, IOException {
		Inverter inverter = new Inverter(TestUtils.getNodeFromString(XML_TAG));

		assertEquals(0.93, inverter.getEfficiency(), TestUtils.EPSILON);
		assertTrue(TestUtils.compareBigDecs(new BigDecimal(2500), inverter.getNewCost(0)));
	}

}
