package asi.beans;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LocationTest {
	
	//Constants
	final private String VALID_COUNTRY = "Australia";
	final private String VALID_CITY = "Brisbane";
	final private String INVALID_COUNTRY = "Atlantis";
	final private String INVALID_CITY = "Hobo Town";
	
	final private double TOO_LOW_SUNLIGHT = -0.1;
	final private double TOO_HIGH_SUNLIGHT = 24.1;
	final private double GOOD_SUNLIGHT = 4.0;
	
	Location location;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		location = new Location( VALID_COUNTRY );
	}

	@After
	public void tearDown() throws Exception {
	}

	
	// TESTING THE CONSTRUCTORS...
	@Test
	public void ConstCountry() {
		try {
			location = new Location ( VALID_COUNTRY );
		} catch ( EstimatorException e) {
			// squelch - this will be picked up elsewhere.
			fail("Constructing with valid country:\""+VALID_COUNTRY+"\" should not throw an exception.");
		}
	}
	
	@Test
	public void ConstCountryCity() {
		try {
			location = new Location ( VALID_COUNTRY , VALID_CITY );
		} catch ( EstimatorException e ) {
			fail( "Constructing with valid country:\""+VALID_COUNTRY+"\" & city:\""+VALID_CITY+"\" should not throw an exception.");
		}
	}
	
	@Test
	public void ConstBadCountry() {
		try {
			location = new Location ( INVALID_COUNTRY );
			fail( "Constructing with invalid Country:\""+INVALID_COUNTRY+"\" should throw an exception.");
		} catch ( EstimatorException e){}; //squelch - test passed
	}
	
	@Test
	public void ConstBadCountryCity1() {
		try {
			location = new Location ( INVALID_COUNTRY, VALID_CITY );
			fail( "Constructing with invalid country:\""+INVALID_COUNTRY+"\" & valid city:\""+VALID_CITY+"\" should throw an exception.");
		} catch ( EstimatorException e){}; //squelch - test passed
	}
	
	@Test
	public void ConstBadCountryCity2() {
		try {
			location = new Location ( INVALID_COUNTRY, INVALID_CITY );
			fail( "Constructing with invalid country:\""+INVALID_COUNTRY+"\" & valid city:\""+INVALID_CITY+"\" should throw an exception.");
		} catch ( EstimatorException e) {} //squelch - test passed
	}
	
	// OTHER TESTS
	@Test
	public void SetSunlight() {
		try {
			location.SetSunlight( GOOD_SUNLIGHT );
		} catch ( EstimatorException e ) {
			fail( GOOD_SUNLIGHT + "hours of sunlight should not throw an exception.");
		}
	}
	
	@Test
	public void SetLowSunlight() {
		try {
			location.SetSunlight( TOO_LOW_SUNLIGHT );
			fail( TOO_LOW_SUNLIGHT + " hours of sunlight should throw an exception. ");
		} catch ( EstimatorException e ) {}
		
	}
	
	@Test
	public void SetHighSunlight() {
		try {
			location.SetSunlight( TOO_HIGH_SUNLIGHT );
			fail( TOO_HIGH_SUNLIGHT + " hours of sunlight should throw an exception. ");
		} catch ( EstimatorException e ) {}
	}
	
	@Test
	public void GetSunlight() {
		try {
			location.SetSunlight( GOOD_SUNLIGHT );
		} catch ( EstimatorException e ) {}
		
		assertEquals("getSunlight did not return the correct value.", GOOD_SUNLIGHT, location.getSunlight(), 0.0 );
	}
}
