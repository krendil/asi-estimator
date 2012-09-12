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

public class FeedInRateTest {
	FeedInRate feedInRate;
	
	final BigDecimal GOOD_VALUE = new BigDecimal(0.25);
	final BigDecimal BAD_VALUE = new BigDecimal(-0.67);
	

	private final String XML_TAG = "<feedin rate=\"0.08\" />";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setup() throws Exception {
		feedInRate = new FeedInRate(GOOD_VALUE);
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void goodValue() {
		try {
			feedInRate = new FeedInRate( GOOD_VALUE );
		} catch ( EstimatorException e ) {
			fail ( "A positive value should not throw an exception." );
		}
	}

	@Test
	public void badValue() {
		try {
			feedInRate = new FeedInRate( BAD_VALUE );
			fail ( "A negative value should throw an exception." );
		} catch ( EstimatorException e ) { }
	}

	@Test
	public void zeroValue() {
		try {
			feedInRate = new FeedInRate( BigDecimal.ZERO );
			fail ( "A zero value should throw an exception." );
		} catch ( EstimatorException e ) { }
	}

	@Test
	public void getter() {
		assertEquals ( "getter does not return the correct value.", GOOD_VALUE, feedInRate.getFeedInRate());
	}
	
	@Test
	public void constructFromNode() throws EstimatorException, ParserConfigurationException, SAXException, IOException {
		feedInRate = new FeedInRate(TestUtils.getNodeFromString(XML_TAG));

		//Check if the difference is less than the epsilon, i.e. they are roughly equal
		assertTrue( TestUtils.compareBigDecs(feedInRate.getFeedInRate(), new BigDecimal(0.08)) );
	}

	
}
