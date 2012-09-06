package asi.beans;

import java.util.LinkedList;
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

/**
 * Represents the entire input configuration, including solar panels,
 * location and other modifiers.
 * 
 * @author David Osborne
 *
 */
public class Configuration {
	
	// bank of panels xml attributes
	final private String SOLAR_BANK = "bank";
	final private String SB_ORIENTATION = "facing";
	final private String SB_QUANTITY = "number";
	final private String SB_POWER = "power";
	final private String SB_TILT = "tilt";
	
	// location xml attributes
	final private String LOCATION = "location";
	final private String L_COUNTRY = "country";
	final private String L_CITY = "city";
	
	// grid xml attributes
	final private String GRID = "grid";
	final private String G_CONSUMPTION = "power";
	final private String G_TARIFF = "rate";
	final private String G_FEEDIN = "feedin";
	
	// sunlight xml attributes
	final private String S_HOURS = "hours";
	
	
	private enum TagType {
		array, location, feedin, consumption, sunlight, ERROR
	}
	
	private SolarArray array;
	private Location location;
	private ElectricalGrid grid;
	private List<Modifier> modifiers;
//	SolarArray array;
//	Location location;
//	
//	//TODO refactor these away
	double currentCost;
	double feedinRate;
//	
//	List<Modifier> modifiers;
	
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
		
		double totalRevenue= totalPower * this.feedinRate;
		Document doc;
		
		try {
			doc = makeDocument(totalPower, totalRevenue);
		} catch (ParserConfigurationException e) {
			throw new EstimatorException(e.getMessage());
		}
		
		return doc;
	}

	/**
	 * Configures the configuration based on the given XML Document, (adding modifiers and so on)
	 * 
	 * @param doc
	 * @throws EstimatorException 
	 */
	public void parseDocument(Document doc) throws EstimatorException{
		modifiers.clear();
		Node solarQuery = doc.getElementsByTagName("solarquery").item(0);
		for(Node n = solarQuery.getFirstChild(); n != null; n = n.getNextSibling()) {
			if(n.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			
			TagType tag;
			try {
				tag = TagType.valueOf(n.getNodeName());
			} catch (Exception e) {
				throw new EstimatorException("Unknown tag type: " + n.getNodeName());
			}
					
			switch( tag ) {
			case array:
				makeArray(n);
				break;
			case location:
				makeLocation(n);
				break;
//			case grid:
//				makeGrid(n);
//				break;
			case feedin:
				makeFeedin(n);
				break;
			case consumption:
				makeConsumption(n);
				break;
			case sunlight:
				makeSunlight(n);
				break;
			default:
				throw new EstimatorException( "Unimplemented tag: " + n.getNodeName() );
					//break;
			}
		}
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
			double dTilt;
			
			String facing = b.getAttribute( SB_ORIENTATION );
			String number = b.getAttribute( SB_QUANTITY );
			String power = b.getAttribute( SB_POWER );
			String tilt = b.getAttribute( SB_TILT );
			
			try {
				if(facing.isEmpty()) {
					orientation = 0.0;
				} else {
					orientation = Double.parseDouble(facing);
				}
				
				kW = Integer.parseInt(number) * Double.parseDouble(power) / 1000.0;
				dTilt = Double.parseDouble(tilt);
			} catch (NumberFormatException exc) {
				throw new EstimatorException("Error parsing xml: "+exc.getMessage());
			}
			
			BankOfPanels bank = new BankOfPanels(orientation, kW, dTilt);
			sArray.addPanels(bank);
		}
		this.array = sArray;
	}
	
	private void makeConsumption(Node n) {
		Element el = (Element) n;
		String power = el.getAttribute("power");
		String rate = el.getAttribute("rate");
		
		this.currentCost = Double.parseDouble(power) * Double.parseDouble(rate);
	}

	private Document makeDocument(double totalPower, double totalRevenue) throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		DOMImplementation di = db.getDOMImplementation();
		
		DocumentType doctype = di.createDocumentType("solarresponse", "", "http://asi-estimator.appspot.com/solarquery.dtd");
		Document doc = di.createDocument(null, "solarresponse", doctype);
		Element root = doc.getDocumentElement();
		
		Element power = doc.createElement("power");
		power.setTextContent(Double.toString(totalPower));
		Element revenue = doc.createElement("revenue");
		revenue.setTextContent(Double.toString(totalRevenue));
		root.appendChild(power);
		root.appendChild(revenue);
		
		return doc;
	}

	private void makeFeedin(Node n) {
		String rate = ((Element) n).getAttribute("rate");
		this.feedinRate = Double.parseDouble(rate);
	}
	
	/**
		 * converts xml data into ElectricalGrid object
		 * @param n
		 * @throws EstimatorException
		 */
		private void makeGrid( Node n ) throws EstimatorException {		
			
			//TODO: not sure if correct
			
			Element el = (Element)n;
	//		NodeList grid = el.getElementsByTagName( GRID );
	//		
	//		Element g = (Element)grid.item(0);
	//		String consumption = g.getAttribute( G_CONSUMPTION );
	//		String tariff = g.getAttribute( G_TARIFF );
	//		String feedin = g.getAttribute( G_FEEDIN );
			
			
			String consumption = el.getAttribute( G_CONSUMPTION );
			String tariff = el.getAttribute( G_TARIFF );
			String feedin = el.getAttribute( G_FEEDIN );
			
	
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
	 * Converts xml data into Location object "location"
	 * @param n
	 * @throws EstimatorException
	 */
	private void makeLocation(Node n) throws EstimatorException {
		/**
		 * TODO: the direction, tilt and latitude all combine to 
		 *
		 * affect the amount of sunlight captured according to
		 * this equation, averaged over a year:
		 * R = sin(l)cos(t) - cos(l)cos(d) + cos(l)sin(t)cos(d)
		 *		+ (cos(l)^2/sin(l))
		 * where
		 * l = latitude
		 * t = tilt from horizontal
		 * d = direction from ideal (0 in southern hem., 180 in north)
		 * 
		 * Equations modified from
		 * http://www.sciencedirect.com/science/article/pii/S0960148104000060
		 * In particular, the formulas for R_b, THETA_I and THETA_Z, with
		 * delta = latitude (averaged over year)
		 * and
		 * omega = 0 (averaged over each day)
		 */ 
		
		
		//TODO: not sure if correct
		
		Element el = (Element)n;
//		NodeList location = el.getElementsByTagName( LOCATION );
//		
//		Element l = (Element)location.item(0);
		
		String country = el.getAttribute( L_COUNTRY );
		String city = el.getAttribute( L_CITY );

		
		if ( city != "" ) {
			this.location = new Location( country, city );
		} else {
			this.location = new Location( country );
		}
	}
	
	private void makeSunlight(Node n) throws EstimatorException {
		Element el = (Element) n;
		String hours = el.getAttribute( S_HOURS );
		HoursOfSunlight mod;
		try {
			mod = new HoursOfSunlight(Double.parseDouble(hours));
		} catch (NumberFormatException e) {
			throw new EstimatorException("Sunlight hours cannot be converted to a number");
		}
		this.modifiers.add(mod);
	}

}
