package asi.beans;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents the entires input configuration, including solar panels,
 * location and other modifiers.
 * 
 * @author David Osborne
 *
 */
public class Configuration {
	
	private enum TagType {
		array, location
	}
	
	SolarArray array;
	Location location;
	List<Modifier> modifiers;
	
	
//	Note from the internet....
//	"In South East Queensland the average household used 11,503 kWh from 
//	December 2005 to November 2006, which is significantly higher than both the 
//	state and the national average consumption"
//  http://www.vinnies.org.au/files/NAT/SocialJustice/CustomerProtectionsandSmartMetersIssuesforQld.pdf
	double avgPowerConsumption;	//kWh average per day.
	
	
	
	public Configuration() {
		modifiers = new LinkedList<Modifier>();
	}
	
	/**
	 * Converts xml data to SolarArray object "array"
	 * 
	 * @param n
	 * @throws EstimatorException
	 */
	private void makeArray(Node n) throws EstimatorException {
		Element el = (Element)n;
		NodeList banks = el.getElementsByTagName("bank");
		SolarArray sArray = new SolarArray();
		
		for(int i = 0; i < banks.getLength(); i++) {
			Element b = (Element)banks.item(i);
			double orientation;
			double kW;
			
			String facing = b.getAttribute("facing");
			String number = b.getAttribute("number");
			String power = b.getAttribute("power");
			
			try {
				if(facing.isEmpty()) {
					orientation = 0.0;
				} else {
					orientation = Double.parseDouble(facing);
				}
				
				kW = Integer.parseInt(number) * Double.parseDouble(power) / 1000.0;
			} catch (NumberFormatException exc) {
				throw new EstimatorException("Error parsing xml: "+exc.getMessage());
			}
			
			BankOfPanels bank = new BankOfPanels(orientation, kW);
			sArray.addPanels(bank);
		}
		this.array = sArray;
	}
	
	
	/**
	 * Converts xml data into Location object "location"
	 * @param n
	 */
	private void makeLocation(Node n) {
		
	}
	
	/**
	 * Configures the configuration based on the given XML Document, (adding modifiers and so on)
	 * 
	 * @param doc
	 * @throws EstimatorException 
	 */
	public void parseDocument(Document doc) throws EstimatorException{
		modifiers.clear();
		
		for(Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
			if(n.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			switch( TagType.valueOf(n.getNodeName()) ) {
			case array:
				makeArray(n);
				break;
			case location:
				break;
			default:
				throw new EstimatorException( "Unknown tag type: " + n.getNodeName() );
					//break;
			}
		}
	}
	
	/**
	 * Creates an estimate of future power returns, and generates an XML Document representing that estimate
	 * @return
	 */
	public Document generateEstimate() {
		return null;
	}

}
