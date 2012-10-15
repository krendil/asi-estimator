package asi.beans;

import java.math.BigDecimal;
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

/**
 * Represents the entire input configuration, including solar panels,
 * location and other modifiers.
 * 
 * @author David Osborne
 *
 */
public class Configuration {
	
	private static final int YEARS = 20;
	private List<Modifier> modifiers;

	public Configuration() {
		modifiers = new LinkedList<Modifier>();
		
	}
	
	/**
	 * Creates an estimate of future power returns, and generates an XML Document representing that estimate
	 * @return
	 * @throws EstimatorException 
	 */
	public Document generateEstimate() throws EstimatorException {
		
		double[] totalPower = new double[YEARS];
		BigDecimal[] currentCosts = new BigDecimal[YEARS];
		BigDecimal[] newCosts = new BigDecimal[YEARS];
		BigDecimal[] totalRevenue = new BigDecimal[YEARS];

		for(int year = 0; year < YEARS; year++) {
			totalPower[year] = 1.0;
			currentCosts[year] = BigDecimal.ZERO;
			newCosts[year] = BigDecimal.ZERO;
			totalRevenue[year] = BigDecimal.ONE;
			
			for(Modifier mod : modifiers) {
				totalPower[year] *= mod.getMultiplier(year);
				currentCosts[year] = currentCosts[year].add(mod.getCurrentCost(year));
				newCosts[year] = newCosts[year].add(mod.getNewCost(year));
				totalRevenue[year] = totalRevenue[year].multiply(mod.getRevenueMultiplier(year));
			}
			totalRevenue[year] = totalRevenue[year].multiply(new BigDecimal(totalPower[year]));
		}
		
		Document doc;
		
		try {
			doc = makeDocument(totalPower, totalRevenue, newCosts);
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
		
		for(Node child = doc.getDocumentElement().getFirstChild(); child != null; child = child.getNextSibling()) {
			if(child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			modifiers.add(ModifierFactory.createModifier(child));
		}
	}
	
	private Document makeDocument(double[] totalPower, BigDecimal[] totalRevenue, BigDecimal[] newCosts) 
		throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		DOMImplementation di = db.getDOMImplementation();
		
		DocumentType doctype = di.createDocumentType("solarresponse", null, "http://asi-estimator.appspot.com/solarresponse.dtd");
		Document doc = di.createDocument(null, "solarresponse", null);
		doc.appendChild(doctype);
		Element root = doc.getDocumentElement();
		
		Element power = doc.createElement("power");
		power.setTextContent(printArray(totalPower));
		Element revenue = doc.createElement("revenue");
		revenue.setTextContent(printArray(totalRevenue));
		Element cost = doc.createElement("cost");
		cost.setTextContent(printArray(newCosts));
		root.appendChild(power);
		root.appendChild(revenue);
		root.appendChild(cost);
		
		return doc;
	}
	
	private String printArray(double[] arr) {
		StringBuilder sb = new StringBuilder();
		for(double d : arr) {
			sb.append(d);
			sb.append(' ');
		}
		return sb.toString();
	}
	
	private String printArray(BigDecimal[] arr) {
		StringBuilder sb = new StringBuilder();
		for(BigDecimal bd : arr) {
			sb.append(bd.toPlainString());
			sb.append(' ');
		}
		return sb.toString();
	}
}
