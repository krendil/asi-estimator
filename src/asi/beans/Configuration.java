package asi.beans;

import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.jasper.compiler.Node.DoBodyAction;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents the entire input configuration, including solar panels,
 * location and other modifiers.
 * 
 * @author David Osborne
 *
 */
public class Configuration {
	
	private enum TagType {
		array, location, feedin, consumption
	}
	
	SolarArray array;
	Location location;
	
	//TODO refactor these away
	double currentCost;
	double feedinRate;
	
	List<Modifier> modifiers;
	
	
//	Note from the internet....
//	"In South East Queensland the average household used 11,503 kWh from 
//	December 2005 to November 2006, which is significantly higher than both the 
//	state and the national average consumption"
//  http://www.vinnies.org.au/files/NAT/SocialJustice/CustomerProtectionsandSmartMetersIssuesforQld.pdf
	double avgPowerConsumption;	//kWh average per day.
	/**
	 * That's great, but we don't need to know this here - this should be in the client side
	 * Also, that figure is per year, not per day.
	 * ~David
	 */
	
	
	
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
	
	private void makeFeedin(Node n) {
		String rate = ((Element) n).getAttribute("rate");
		this.feedinRate = Double.parseDouble(rate);
	}
	
	private void makeConsumption(Node n) {
		Element el = (Element) n;
		String power = el.getAttribute("power");
		String rate = el.getAttribute("rate");
		
		this.currentCost = Double.parseDouble(power) * Double.parseDouble(rate);
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
				makeLocation(n);
				break;
			case feedin:
				makeFeedin(n);
				break;
			case consumption:
				makeConsumption(n);
				break;
			default:
				throw new EstimatorException( "Unimplemented tag: " + n.getNodeName() );
					//break;
			}
		}
	}
	
	/**
	 * Creates an estimate of future power returns, and generates an XML Document representing that estimate
	 * @return
	 * @throws EstimatorException 
	 */
	public Document generateEstimate() throws EstimatorException {
		double totalPower = 0.0;
		for(double[] bank : array.getDetails()) {
			double power = bank[1];
			for(Modifier mod : modifiers) {
				power *= mod.getMultiplier();
			}
			totalPower += power;
		}
		
		double totalRevenue = totalPower * this.feedinRate;
		Document doc;
		
		try {
			doc = makeDocument(totalPower, totalRevenue);
		} catch (ParserConfigurationException e) {
			throw new EstimatorException(e.getMessage());
		}
		
		return doc;
	}
	
	private Document makeDocument(double totalPower, double totalRevenue) throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		DOMImplementation di = db.getDOMImplementation();
		
		DocumentType doctype = di.createDocumentType("solarresponse", "", "http://asi-estimator.appspot.com/solarquery.dtd");
		Document doc = di.createDocument(null, "solarresponse", doctype);
		
		doc.createElement("power").setTextContent(Double.toString(totalPower));
		doc.createElement("revenue").setTextContent(Double.toString(totalRevenue));
		
		return doc;
	}

}
