package asi.beans;

import java.util.HashMap;
import java.util.Map;

public class PopulateDatabase {
	
	private static volatile PopulateDatabase instance = null;
	 
    private PopulateDatabase() {
		String[] fields = {"Average Consumption", "Feed In", "Electricity Cost"};
		String[][] loc = {
			{"au.qld", "9000", ".14",".195"}, //http://www.deedi.qld.gov.au/documents/energy/43-_Extra-Gazette-Electricity-Prices-2012-13.pdf
			{"au.nsw", "9000", ".1", ".193"},
			{"au.sa", "9000", ".258", ".202"},
			{"au.vic", "9000", ".8", ".203"},
			{"au.tas", "9000", ".209", ".209"},
			{"au.wa", "9000", ".27", ".217"},
			{"au.nt", "9000", ".1923", ".208"},
			{"au.act", "9000", ".163", ".163"}
		};
		
		
		Map<String,Map<String,String>> prefill = new HashMap<String,Map<String,String>>();
		
		for (int i = 0; i < loc.length; i++) {
			
			Map<String,String> data = new HashMap<String,String>();
			
			for (int j = 0; j < fields.length; j++) {
				data.put(fields[j], loc[i][j+1]);
			}
			
			prefill.put(loc[i][0], data);
			
			ChatToDatastore.setPrefill(loc[i][0], prefill.get(loc[i][0]));
		}
	}
 
	public static PopulateDatabase getInstance() {
        if (instance == null) {
                synchronized (PopulateDatabase .class){
                        if (instance == null) {
                                instance = new PopulateDatabase ();
                        }
              }
        }
        return instance;
	}

}
