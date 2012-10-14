package asi.beans;

import java.util.HashMap;
import java.util.Map;

public class PopulateDatabase {
	

	public static void Go() {
		
		Map<String,String> data = new HashMap<String,String>();
		
		data.put("Average Consumption", "5620");
		data.put("Feed In", ".15");
		data.put("Electricity Cost", ".50");
		
		ChatToDatastore.setPrefill("qld", data);
		
		
	}
	
	
}
