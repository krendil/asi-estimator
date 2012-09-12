package asi.beans;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import asi.TestUtils;

public class ConsumptionTest {
	
	private final double GOOD_POWER = 22.9;
	private final double BAD_POWER = -12.8;
	
	private final BigDecimal GOOD_RATE = new BigDecimal(0.25);
	private final BigDecimal BAD_RATE = new BigDecimal(-0.58);
	
	private String XML_TAG = "<consumption power=\"11500\" rate=\"25.378\"/>";
	
	private Consumption consumption; 
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		consumption = new Consumption ( GOOD_POWER, GOOD_RATE );
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void constructor() {
		try {
			consumption = new Consumption ( GOOD_POWER, GOOD_RATE );
		} catch ( EstimatorException e ) {
			fail ("Good power and rate shouldn't cause exception to be thrown. ");
		}
	}

	@Test
	public void badPower() {
		try {
			consumption = new Consumption ( BAD_POWER, GOOD_RATE );
			fail ("Bad power and good rate should cause exception to be thrown. ");
		} catch ( EstimatorException e ) { }
	}

	@Test
	public void badPowerBadRate() {
		try {
			consumption = new Consumption ( BAD_POWER, BAD_RATE );
			fail ("Bad power and bad rate should cause exception to be thrown. ");
		} catch ( EstimatorException e ) { }
	}

	@Test
	public void badRate() {
		try {
			consumption = new Consumption ( GOOD_POWER, BAD_RATE );
			fail ("Good power and bad rate should cause exception to be thrown. ");
		} catch ( EstimatorException e ) { }
	}
	

	@Test
	public void zeroRate() {
		try {
			consumption = new Consumption ( GOOD_POWER, BigDecimal.ZERO );
			fail ("Good power and 0 rate should cause exception to be thrown. ");
		} catch ( EstimatorException e ) { }
	}
	
	@Test
	public void zeroPower() {
		try {
			consumption = new Consumption ( 0, GOOD_RATE );
		} catch ( EstimatorException e ) { 
			fail ("0 power and good rate shouldn't cause exception to be thrown. ");
		}
	}
	
	@Test
	public void zeroPowerZeroRate() {
		try {
			consumption = new Consumption ( 0, BigDecimal.ZERO );
			fail ("0 power and 0 rate should cause exception to be thrown. ");
		} catch ( EstimatorException e ) { }
	}

	@Test
	public void cost() {
		assertEquals("Current cost is not calculated properly.",
				consumption.getCurrentCost(0),
				GOOD_RATE.multiply(new BigDecimal(GOOD_POWER)));
	}
	
	@Test
	public void constructFromNode() throws EstimatorException, ParserConfigurationException, SAXException, IOException {
		consumption = new Consumption(TestUtils.getNodeFromString(XML_TAG));
		assertEquals(11500, consumption.getPower(), TestUtils.EPSILON);
		
		assertTrue(TestUtils.compareBigDecs(consumption.getRate(), new BigDecimal(25.378)) );
	}

}
