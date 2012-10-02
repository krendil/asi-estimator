package asi.beans;

import java.util.HashMap;

public class SuggestPowerConsumption {
	// Everything will be treated as a square for the time being.
	private static HashMap<String, Double> suggestions;
	
	
	
	public static double getConsumption( double longitude, double latitude ) {
		double estimate = 0.0;
		String state = "QLD";
		
		//check if inside australia
			//check state
			estimate = getStateConsumption(state);
			
		
		return estimate;
	}

	private static double getStateConsumption(String state) {
		//refactor away
		populateHashMap();
		
		return suggestions.get(state);
	}

	
	
	private static void populateHashMap() {
		//"The typical Origin Queensland household consumption can vary between 1300 and 2,300 units per quarter"
		//"[One unit = 1 kWh = 1,000 watts operating for one hour.]"
		suggestions.put("QLD", 1800.0/3/4/7); //daily consumption
		
		// "an average NSW home uses 7,300 kWh of electricity a year"
		suggestions.put("NSW", 7300.0/365.25);
		
		// "^The typical Origin Victorian household consumption can vary between 1000 and 1,300 units per quarter "
		//"[One unit = 1 kWh = 1,000 watts operating for one hour.]"
		suggestions.put("Vic", 1150.0/3/4/7);
		
		suggestions.put("ACT", 13.0 );
		suggestions.put("SA", 13.0);
		suggestions.put("Tas", 13.0);
		suggestions.put("WA", 13.0);
		suggestions.put("NT", 13.0);
		
	}
}
