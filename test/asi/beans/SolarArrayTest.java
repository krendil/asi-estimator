package asi.beans;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import asi.TestUtils;

public class SolarArrayTest {
	
	private static final double GOOD_ORIENTATION1 = 120.0;
	private static final double GOOD_KW1 = 5.0;
	private static final double GOOD_ORIENTATION2 = 100.0;
	private static final double GOOD_KW2 = 3.0;	
	private static final double GOOD_TILT1 = 32.0;
	private static final double GOOD_TILT2 = 27.0;

	final BigDecimal GOOD_PRICE1 = new BigDecimal(1000);
	final BigDecimal GOOD_PRICE2 = new BigDecimal(8000);
	
	private final String XML_TAG = "<array>" +
								   "	<bank facing=\"0.0\" number=\"5\" power=\"200\" tilt=\"32.0\" price=\"1000.0\" latitude=\"-27.0\"/>" +
								   "</array>";
	
	private BankOfPanels bankOfPanels1;
	private BankOfPanels bankOfPanels2;
	private SolarArray solarArray;


	@Before
	public void setUp() throws Exception {
		bankOfPanels1 = new BankOfPanels(GOOD_ORIENTATION1, GOOD_KW1, GOOD_TILT1, GOOD_PRICE1);
		bankOfPanels2 = new BankOfPanels(GOOD_ORIENTATION2, GOOD_KW2, GOOD_TILT2, GOOD_PRICE2);
		solarArray = new SolarArray();
	}

	@Test
	public void addPanels() {
		solarArray.addPanels(bankOfPanels1);
	}
	
	
	@Test
	public void removeUnknownPanels() {
		try {
			solarArray.removePanels(bankOfPanels1);
			fail("Cannot remove panels that do not exist in solar array.");
		} catch (EstimatorException e ){ }
	}
	
	@Test
	public void removeKnownPanels() {
		solarArray.addPanels(bankOfPanels1);
		
		try {
			solarArray.removePanels(bankOfPanels1);
		} catch (EstimatorException e) {
			fail("Unable to remove panels that were added. ");
		}
	}
	
	@Test
	public void removeDifferentPanels() {
		solarArray.addPanels(bankOfPanels1);
		
		try { 
			solarArray.removePanels(bankOfPanels2);
			fail("Able to remove panels that do not exist in the solar array");
		} catch ( EstimatorException e ) { }
	}
	
	@Test
	public void constructFromNode() throws EstimatorException, ParserConfigurationException, SAXException, IOException {
		solarArray = new SolarArray(TestUtils.getNodeFromString(XML_TAG));

		assertTrue( TestUtils.compareBigDecs(solarArray.getNewCost(0), new BigDecimal(5 * 1000.0)) );
	}

}
