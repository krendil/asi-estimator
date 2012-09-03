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
	
	// bank of panels xml
	final private String SOLAR_BANK = "bank";
	final private String SB_ORIENTATION = "facing";
	final private String SB_QUANTITY = "number";
	final private String SB_POWER = "power";
	
	// location xml
	final private String LOCATION = "location";
	final private String L_COUNTRY = "country";
	final private String L_CITY = "city";
	
	// grid xml
	final private String GRID = "grid";
	final private String G_CONSUMPTION = "consumption";
	final private String G_TARIFF = "tariff";
	final private String G_FEEDIN = "feedin";
	
	
	private enum TagType {
		array, location, grid
		//array, location, feedin, consumption
	}
	
	private SolarArray array;
	private Location location;
	private ElectricalGrid grid;
	private List<Modifier> modifiers;
//	SolarArray array;
//	Location location;
//	
//	//TODO refactor these away
//	double currentCost;
//	double feedinRate;
//	
//	List<Modifier> modifiers;
	
	
	/**
	 * That's great, but we don't need to know this here - this should be in the client side
	 * Also, that figure is per year, not per day.
	 * ~David
	 */
	
	public Configuration() {
		modifiers = new LinkedList<Modifier>();
		array = null;
		location = null;
		grid = null;
	}
	
	/**
	 * After grid has been made, results can be gathered.
	 */
	public double getCost() {
		return grid.getDailyCost();
	}
	
	/**
	 * After grid has been made, results can be gathered.
	 */
	public double getCredit() {
		return grid.getDailyCredit();
	}
	
	/**
	 * Converts xml data to SolarArray object "array"
	 * 
	 * @param n
	 * @throws EstimatorException
	 */
	private void makeArray(Node n) throws EstimatorException {
		Element el = (Element)n;
		NodeList banks = el.getElementsByTagName( SOLAR_BANK );
		SolarArray sArray = new SolarArray();
		
		for(int i = 0; i < banks.getLength(); i++) {
			Element b = (Element)banks.item(i);
			double orientation;
			double kW;
			
			String facing = b.getAttribute( SB_ORIENTATION );
			String number = b.getAttribute( SB_QUANTITY );
			String power = b.getAttribute( SB_POWER );
			
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
	
//	private void makeFeedin(Node n) {
//		String rate = ((Element) n).getAttribute("rate");
//		this.feedinRate = Double.parseDouble(rate);
//	}
//	
//	private void makeConsumption(Node n) {
//		Element el = (Element) n;
//		String power = el.getAttribute("power");
//		String rate = el.getAttribute("rate");
//		
//		this.currentCost = Double.parseDouble(power) * Double.parseDouble(rate);
//	}
	
	
	/**
	 * Converts xml data into Location object "location"
	 * @param n
	 * @throws EstimatorException
	 */
	private void makeLocation(Node n) throws EstimatorException {
		//TODO: not sure if correct
		
		Element el = (Element)n;
		NodeList location = el.getElementsByTagName( LOCATION );
		
		Element b = (Element)location.item(0);
		
		String country = b.getAttribute( L_COUNTRY );
		String city = b.getAttribute( L_CITY );

		
		if ( city != "" ) {
			this.location = new Location( country, city );
		} else {
			this.location = new Location( country );
		}
	}
	
	
	/**
	 * converts xml data into ElectricalGrid object
	 * @param n
	 * @throws EstimatorException
	 */
	private void makeGrid( Node n ) throws EstimatorException {		
		
		//TODO: not sure if correct
		
		Element el = (Element)n;
		NodeList grid = el.getElementsByTagName( GRID );
		
		Element g = (Element)grid.item(0);
		
		String consumption = g.getAttribute( G_CONSUMPTION );
		String tariff = g.getAttribute( G_TARIFF );
		String feedin = g.getAttribute( G_FEEDIN );

		double avgPowerConsumption = Double.parseDouble( consumption );
		double tariffRate = Double.parseDouble( tariff );
		double feedInRate = Double.parseDouble( feedin );
		
		// must use SolarArray and Location objects to determine
		// power generation
		double avgPowerGeneration = 0.0;	// kWh
		
		double[][] arrayDetails = array.getDetails();
		
		
		double orientation;
		double kW;
		
		double difference = 0.0; // difference between ideal orientation and actual orientation in degrees.
		
		for ( int i = 0; i < arrayDetails.length; i++ ) {
			orientation = arrayDetails[i][0];
			kW = arrayDetails[i][1];
			
			
			if ( orientation > location.getOrientation() ) {
				difference = orientation - location.getOrientation();
			} else {
				difference = location.getOrientation() - orientation;
			}
			
			//TODO do someting using "difference" variable to modify kW
			avgPowerGeneration += location.getSunlight() * kW;
		}
		
		
		ElectricalGrid eGrid = new ElectricalGrid(
				avgPowerConsumption, 
				tariffRate,
				avgPowerGeneration,
				feedInRate
				);
		
		this.grid = eGrid;
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
			case grid:
				makeGrid(n);
				break;
//			case feedin:
//				makeFeedin(n);
//				break;
//			case consumption:
//				makeConsumption(n);
//				break;
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
		
		double totalRevenue = grid.getDailyCredit();// = totalPower * this.feedinRate;
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
