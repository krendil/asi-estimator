package asi.beans;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;

public class SolarArrayTest {
	
	private static final double GOOD_ORIENTATION1 = 120.0;
	private static final double GOOD_KW1 = 5.0;
	private static final double GOOD_ORIENTATION2 = 100.0;
	private static final double GOOD_KW2 = 3.0;	
	private static final double GOOD_TILT1 = 32.0;
	private static final double GOOD_TILT2 = 27.0;
	
	private BankOfPanels bankOfPanels1;
	private BankOfPanels bankOfPanels2;
	private SolarArray solarArray;


	@Before
	public void setUp() throws Exception {
		bankOfPanels1 = new BankOfPanels(GOOD_ORIENTATION1, GOOD_KW1, GOOD_TILT1);
		bankOfPanels2 = new BankOfPanels(GOOD_ORIENTATION2, GOOD_KW2, GOOD_TILT2);
		solarArray = new SolarArray();
	}

	@Test
	public void addPanels() {
		solarArray.addPanels(bankOfPanels1);
	}
	
	
	@Test
	public void removeUnknownPanels() {
		try {
			solarArray.removePanels(bankOfPanels1);
			fail("Cannot remove panels that do not exist in solar array.");
		} catch (EstimatorException e ){ }
	}
	
	@Test
	public void removeKnownPanels() {
		solarArray.addPanels(bankOfPanels1);
		
		try {
			solarArray.removePanels(bankOfPanels1);
		} catch (EstimatorException e) {
			fail("Unable to remove panels that were added. ");
		}
	}
	
	@Test
	public void removeDifferentPanels() {
		solarArray.addPanels(bankOfPanels1);
		
		try { 
			solarArray.removePanels(bankOfPanels2);
			fail("Able to remove panels that do not exist in the solar array");
		} catch ( EstimatorException e ) { }
	}
	
	
	@Test
	public void detailsFromEmptyArray() {
		
		try {
			solarArray.getDetails();
			fail("Should not be able to get details of an empty solar array.");
		} catch (EstimatorException e) { }
	}
	
	@Test
	public void canGetDetails() {
		solarArray.addPanels(bankOfPanels1);
		solarArray.addPanels(bankOfPanels2);
		
		try {
			solarArray.getDetails();
		} catch (EstimatorException e) {
			fail("Should be able to get details of a non-empty solar array");
		}
	}
	
	@Test
	public void returnValidDetails() throws EstimatorException {
		solarArray.addPanels(bankOfPanels1);
		solarArray.addPanels(bankOfPanels2);
		
		double[][] expectedResults = {
				{120.0, 5.0},
				{100.0, 3.0}
		};
		
		assertArrayEquals("Array does not match panels.", expectedResults, solarArray.getDetails() );
		
	}

}
