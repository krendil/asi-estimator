package asi.beans;

import static org.junit.Assert.*;

import java.math.BigDecimal;
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

	final BigDecimal GOOD_PRICE1 = new BigDecimal(1000);
	final BigDecimal GOOD_PRICE2 = new BigDecimal(8000);
	
	private BankOfPanels bankOfPanels1;
	private BankOfPanels bankOfPanels2;
	private SolarArray solarArray;


	@Before
	public void setUp() throws Exception {
		bankOfPanels1 = new BankOfPanels(GOOD_ORIENTATION1, GOOD_KW1, GOOD_TILT1, GOOD_PRICE1);
		bankOfPanels2 = new BankOfPanels(GOOD_ORIENTATION2, GOOD_KW2, GOOD_TILT2, GOOD_PRICE2);
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

}
