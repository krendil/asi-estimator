package asi.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class ElectricalGridTest {
	
	ElectricalGrid electricalGrid;
	
	
	// electricity consumption
	private final double GOOD_POWER_IN = 24.5;	//kWh average per day
	private final double BAD_POWER_IN = -1.5;
	private final double MULTI_GOOD_POWER_IN[] = { 24.5, 22.6, 36.1 };
	
	private final double GOOD_RATE_IN = 0.256; // $ per kWh
	private final double BAD_RATE_IN = -0.3;
	private final double MULTI_GOOD_RATE_IN[] = { 24.5, 22.6, 36.1 };
	private final double MULTI_BAD_RATE_IN[] = { 24.5, 22.6, -36.1 };
	
	
	// electricity generation
	private final double GOOD_POWER_OUT = 11.2;	//kWh average per day
	private final double BAD_POWER_OUT = -0.89;
	private final double MULTI_GOOD_POWER_OUT[] = { 2.9, 12.7, 33.258 };
	
	private final double GOOD_RATE_OUT = 0.156; // $ per kWh
	private final double BAD_RATE_OUT = -1.9;
	private final double MULTI_GOOD_RATE_OUT[] = { 17.9, 64.8, 11.1 };
	private final double MULTI_BAD_RATE_OUT[] = { 26.4, -16.0, -2.1 };

	private void makeMulti() throws EstimatorException {
		
		electricalGrid = new ElectricalGrid( MULTI_GOOD_POWER_IN, MULTI_GOOD_RATE_IN,
				MULTI_GOOD_POWER_OUT, MULTI_GOOD_RATE_OUT );
		
	}

	@Before
	public void setUp() throws Exception {
		electricalGrid = new ElectricalGrid ( GOOD_POWER_IN, GOOD_RATE_IN, GOOD_POWER_OUT, GOOD_RATE_OUT );
	}

	@Test
	public void zeroPowerConsumption() {
		try {
			electricalGrid = new ElectricalGrid( 0.0, GOOD_RATE_IN, GOOD_POWER_OUT, GOOD_RATE_OUT );
			fail( "0.0kWh daily average power consumption is not allowed." );
		} catch (EstimatorException e) { }
	}

	@Test
	public void negPowerConsumption() {
		try {
			electricalGrid = new ElectricalGrid( BAD_POWER_IN, GOOD_RATE_IN, GOOD_POWER_OUT, GOOD_RATE_OUT );
			fail( "negative daily average power consumption is not allowed." );
		} catch (EstimatorException e) { }
	}

	@Test
	public void negPowerGeneration() {
		try {
			electricalGrid = new ElectricalGrid( GOOD_POWER_IN, GOOD_RATE_IN, BAD_POWER_OUT, GOOD_RATE_OUT );
			fail( "negative daily average power generation is not allowed." );
		} catch (EstimatorException e) { }
	}

	@Test
	public void zeroTariffRate() {
		try {
			electricalGrid = new ElectricalGrid( GOOD_POWER_IN, 0.0, GOOD_POWER_OUT, GOOD_RATE_OUT );
			fail( "$0 tariff rate is not allowed." );
		} catch (EstimatorException e) { }
	}

	@Test
	public void negTariffRate() {
		try {
			electricalGrid = new ElectricalGrid( GOOD_POWER_IN, BAD_RATE_IN, GOOD_POWER_OUT, GOOD_RATE_OUT );
			fail( "negative tariff rate is not allowed." );
		} catch (EstimatorException e) { }
	}

	@Test
	public void negFeedInRate() {
		try {
			electricalGrid = new ElectricalGrid( GOOD_POWER_IN, GOOD_RATE_IN, GOOD_POWER_OUT, BAD_RATE_OUT );
			fail( "negative feed in rate is not allowed." );
		} catch (EstimatorException e) { }
	}
	
	@Test
	public void calcSingleInput() {
		assertEquals ( "The daily cost does not match tariff cost and usage.",
				electricalGrid.getDailyCost(),
				GOOD_POWER_IN * GOOD_RATE_IN, 0.0 );
		
	}
	
	@Test
	public void negTariffRateMultiples() {
		try {
			electricalGrid = new ElectricalGrid( MULTI_GOOD_POWER_IN, MULTI_BAD_RATE_IN, MULTI_GOOD_POWER_OUT, MULTI_GOOD_RATE_OUT );
			fail( "negative tariff rate is not allowed." );
		} catch (EstimatorException e) { }
	}
	

	@Test
	public void negFeedInRateMultiples() {
		try {
			electricalGrid = new ElectricalGrid( MULTI_GOOD_POWER_IN, MULTI_GOOD_RATE_IN, MULTI_GOOD_POWER_OUT, MULTI_BAD_RATE_OUT );
			fail( "negative tariff rate is not allowed." );
		} catch (EstimatorException e) { }
	}
	
	
	@Test
	public void calcMultiCost() {
		try {
			makeMulti();
			assertEquals ( "The daily cost does not match tariff cost and usage.",
					electricalGrid.getDailyCost(),
					dailyCostHelper( MULTI_GOOD_POWER_IN, MULTI_GOOD_RATE_IN), 0.0 );
		} catch (EstimatorException e) { }
	}
	

	@Test
	public void calcMultiCredit() {
		try {
			makeMulti();
			assertEquals ( "The daily cost does not match tariff cost and usage.",
					electricalGrid.getDailyCredit(),
					dailyCostHelper( MULTI_GOOD_POWER_OUT, MULTI_GOOD_RATE_OUT), 0.0 );
		} catch (EstimatorException e) { }
	}
	
	/* helper method for multi-tariff grids */
	private double dailyCostHelper( double[] power, double[] rate) {
		double cost = 0.0;
		
		for ( int i = 0; i < power.length; i++ ) {
			cost = cost + power[i] * rate[i];
		}
		
		return cost;
	}

}
