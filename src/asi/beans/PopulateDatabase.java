package asi.beans;

import java.util.HashMap;
import java.util.Map;

public class PopulateDatabase {
	
	private static volatile PopulateDatabase instance = null;
	 
    private PopulateDatabase() {
		String[] fields = {"Average Consumption", "Feed In", "Electricity Cost"};
		String[][] loc = {
			{"qld", "5620", ".14",".50"},
			{"nsw", "5620", ".145", ".50"},
			{"sa", "5620", ".258", ".50"},
			{"vic", "5620", ".15", ".50"},
			{"tas", "5620", ".50", ".50"},
			{"wa", "5620", ".27", ".50"},
			{"nt", "5620", ".1923", ".1923"},
			{"act", "5620", ".50", ".50"}
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
