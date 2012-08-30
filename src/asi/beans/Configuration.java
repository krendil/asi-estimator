package asi.beans;

import org.w3c.dom.Document;

/**
 * Represents the entires input configuration, including solar panels,
 * location and other modifiers.
 * 
 * @author David Osborne
 *
 */
public class Configuration {
	
	/**
	 * Add a Modifier to the configuration
	 * @param mod
	 */
	public void addModifier(Modifier mod){
		
	}
	
	/**
	 * Sets the configuration's main SolarArray variable
	 * 
	 * @param array
	 */
	public void setSolarArray(SolarArray array){
		
	}
	
	/**
	 * Configures the configuration based on the given XML Document, (adding modifiers and so on)
	 * 
	 * @param doc
	 */
	public void parseDocument(Document doc){
		
	}
	
	/**
	 * Creates an estimate of future power returns, and generates an XML Document representing that estimate
	 * @return
	 */
	public Document generateEstimate() {
		return null;
	}

}
