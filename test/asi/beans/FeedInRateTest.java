package asi.beans;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FeedInRateTest {
	FeedInRate feedInRate;
	
	final double GOOD_VALUE = 0.25;
	final double BAD_VALUE = -0.67;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		feedInRate = new FeedInRate( GOOD_VALUE );
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
			feedInRate = new FeedInRate( 0 );
			fail ( "A zero value should throw an exception." );
		} catch ( EstimatorException e ) { }
	}

	@Test
	public void getter() {
		assertEquals ( "getter does not return the correct value.", GOOD_VALUE, feedInRate.getFeedInRate(), 0.0);
	}

	
}
