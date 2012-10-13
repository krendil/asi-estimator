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
	
	private Document results;
	
	public Prefill(Document doc){
		try {
			parseDocument(doc);
			queryDatabase();
			calculateResults();
		} catch (EstimatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Document getResults() {
		return results;
	}
	
	
	/**
	 * Configures the configuration based on the given XML Document, (adding modifiers and so on)
	 * 
	 * @param doc
	 * @throws EstimatorException 
	 */
	private void parseDocument(Document doc) throws EstimatorException {
		//TODO: get location from long and lat
		location = "QLD";
	}
	
	
	/**
	 * transforms a hash-map into a doc to send back to the servlet
	 * @return
	 * @throws ParserConfigurationException
	 */
	private Document calculateResults() throws ParserConfigurationException{
		
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
		try {
			prefills = ChatToDatastore.getPrefill(location);
		} catch (EntityNotFoundException e) {
			//Couldn't find location in database.
			
		}
		
	}
}
