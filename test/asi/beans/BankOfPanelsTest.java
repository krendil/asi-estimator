package asi.beans;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BankOfPanelsTest {
	final double GOOD_ORIENTATION = 135.0;	// degrees
	final double TOO_LOW_ORIENTATION = -1.0;
	final double MIN_ORIENTATION = 0.0;
	final double MAX_ORIENTATION = 360.0;
	final double TOO_HIGH_ORIENTATION = 361.0;
	
	final double GOOD_KW = 5.0; //kilowatts
	final double NEG_KW = -1.0;
	
	BankOfPanels bankOfPanels;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		bankOfPanels = new BankOfPanels( GOOD_ORIENTATION, GOOD_KW );
	}

	@After
	public void tearDown() throws Exception {
	}

	
	
	@Test
	public void constructorTest() {
		try {
			bankOfPanels = new BankOfPanels( GOOD_ORIENTATION, GOOD_KW );
		} catch ( EstimatorException e ) {
			fail( "Should not throw exception when constructing with good parameters" );
		}
	}
	
	
	@Test
	public void lowOrientation() {
		try {
			bankOfPanels = new BankOfPanels( TOO_LOW_ORIENTATION, GOOD_KW );
			fail("Cannot assign an orientation lower than 0.0 degrees.");
		} catch ( EstimatorException e ) { }
	}
	
	@Test
	public void highOrientation() {
		try {
			bankOfPanels = new BankOfPanels( TOO_HIGH_ORIENTATION, GOOD_KW );
			fail( "Cannot assign an orientation higher than 360 degrees. " );
		} catch ( EstimatorException e ) { }
	}
	
	@Test
	public void minOrientation() {
		try {
			bankOfPanels = new BankOfPanels( MIN_ORIENTATION, GOOD_KW );
		} catch ( EstimatorException e ) {
			fail( "Must be able to assign " + MIN_ORIENTATION + " as the minimum orientation." );
		}
	}
	
	@Test
	public void maxOrientation() {
		try {
			bankOfPanels = new BankOfPanels( MAX_ORIENTATION, GOOD_KW );
		} catch ( EstimatorException e ) {
			fail( "Must be able to assign " + MAX_ORIENTATION + " as the maximum orientation." );
		}
	}
	
	@Test
	public void testGetOrientation() {
		try {
			bankOfPanels = new BankOfPanels( GOOD_ORIENTATION, GOOD_KW );
		} catch ( EstimatorException e ) { }
		
		assertEquals("getOrientation did not return the correct value.",
				GOOD_ORIENTATION, bankOfPanels.getOrientation(), 0.0);
	}
	
	
	@Test
	public void setPowerGenerationZero() {
		try {
			bankOfPanels = new BankOfPanels( GOOD_ORIENTATION, 0 );
			fail("must have power output above zero");
		} catch ( EstimatorException e ) { }
	}
	
	@Test
	public void setPowerGenerationNegative() {
		try {
			bankOfPanels = new BankOfPanels( GOOD_ORIENTATION, NEG_KW );
			fail( "Must have power output above zero" );
		} catch ( EstimatorException e ) { }
	}
	
	@Test
	public void getKWPower() {
		try {
			bankOfPanels = new BankOfPanels( GOOD_ORIENTATION, GOOD_KW );
		} catch ( EstimatorException e ) { }
		
		assertEquals("The KW amount that has been set does not match the stored value.",
				GOOD_KW, bankOfPanels.getKW(), 0.0 );
	}
	

}


