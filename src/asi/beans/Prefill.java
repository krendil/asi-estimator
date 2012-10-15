package asi.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.appengine.api.datastore.EntityNotFoundException;


/**
 * -- This class is a total mess at the moment. Needs a lot of work
 * 
 * Reads in the location and what pre-fill data should be requested.
 * Query datastore, supplying location as key.
 * Build response to send back to servlet.
 * 
 * @author Steven Turner.
 *
 */
public class Prefill {

	private Map<String,String> prefills = new HashMap<String,String>();
	
	private String location;
	
	public Prefill(String location){
		PopulateDatabase.getInstance();
		this.location = location;
	}
	
	public Map<String,String> getResults() {
		queryDatabase();
		return prefills;
	}

	/**
	 * queries database and fills hashmap
	 */
	private void queryDatabase() {
		try {
			prefills = ChatToDatastore.getPrefill(location);
		} catch (EntityNotFoundException e) {
			//Couldn't find location in database.
			prefills = null;
		}
		
	}
}
