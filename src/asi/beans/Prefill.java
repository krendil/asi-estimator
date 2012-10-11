package asi.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	private enum Suggest {
		AVG_CONSUMPTION,
		FEED_IN_RATE,
		ELEC_COST
	}
	
	List<String> reqPreFills = new ArrayList<String>();
	HashMap<String,String> prefills = new HashMap<String,String>();
	
	String location;
	
	/**
	 * Configures the configuration based on the given XML Document, (adding modifiers and so on)
	 * 
	 * @param doc
	 * @throws EstimatorException 
	 */
	public void parseDocument(Document doc) throws EstimatorException {
		reqPreFills.clear();
		
		for(Node child = doc.getDocumentElement().getFirstChild(); child != null; child = child.getNextSibling()) {
			if(child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			addPreFill(child);
		}
		
		//TODO: get location from long and lat
		location = "QLD";
	}
	
	private void addPreFill(Node n) {
		Element el = (Element)n;
		NodeList prefills = el.getElementsByTagName( "prefills" );
		
		for(int i = 0; i < prefills.getLength(); i++) {
			Element b = (Element)prefills.item(i);
			
			String name = b.getAttribute( "name" );
			
			reqPreFills.add(name);
		}
	}
	
	
	/**
	 * transforms a hash-map into a doc to send back to the servlet
	 * @return
	 * @throws ParserConfigurationException
	 */
	public Document getResults() throws ParserConfigurationException{
		
		queryDatabase();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		DOMImplementation di = db.getDOMImplementation();
		
		//TODO: This needs to be changed to a prefill type
		DocumentType doctype = di.createDocumentType("solarresponse", null, "http://asi-estimator.appspot.com/solarresponse.dtd");
		Document doc = di.createDocument(null, "solarresponse", null);
		
		return doc;
	}

	/**
	 * queries database and fills hashmap
	 */
	private void queryDatabase() {
		// TODO Auto-generated method stub
		for (String name : reqPreFills) {

			try {
				
				if ( name == "Average Consumption" ) {
					prefills.put(name, ChatToDatastore.getAvgCons(location));
				} else if ( name == "Feed In Rates" ) {
					prefills.put(name, ChatToDatastore.getFeedIn(location));
				} else if ( name == "Electricity Cost" ) {
					prefills.put(name, ChatToDatastore.getElecCost(location));
				} else {
					// unknown type, leave blank
					prefills.put(name, "");
				}
			
			} catch (EntityNotFoundException e) {
				// Couldn't find location - leave blank
				prefills.put(name, "");
			}
		}
	}
}
