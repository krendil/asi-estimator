package asi.beans;

/**
 * 
 * @author Steven Turner
 * 
 * This is the Location object.
 *
 */
public class Location {
	
	final private double DEFAULT_DAILY_SUNLIGHT = 4.0; //hours. Used if unable to find daylight hours
	
	private String country;
	private String city;
	private double sunlight;	//average hours of daily sunlight
	
	
	
	/**
	 * Constructor 1
	 * This constructor only uses a country as the input.
	 * 
	 * @param country
	 * @throws EstimatorException
	 */
	public Location( String country ) throws EstimatorException {
		try {
			ValidateCountry( country );
		} catch ( EstimatorException e ) {
			throw e;
		}

		this.country = country;
		this.city = null;
		
		this.sunlight = DEFAULT_DAILY_SUNLIGHT;
	}

	/**
	 * Constructor 1
	 * 
	 * This constructor takes the country and city as input.
	 * 
	 * @param country
	 * @param city
	 * @throws EstimatorException
	 */
	public Location( String country, String city ) throws EstimatorException {
		this.country = country;
		this.city = city;
		
		this.sunlight = DEFAULT_DAILY_SUNLIGHT;
	}
	
	/**
	 * Method to validate the name of the country.
	 * @param country
	 * @throws EstimatorException
	 */
	private void ValidateCountry( String country ) throws EstimatorException {
		// TODO Must check country against database
		
	}
	
	/**
	 * Method to validate the name of the city in a given country.
	 * @param country
	 * @param city
	 * @throws EstimatorException
	 */
	private void ValidateCity( String country, String city ) throws EstimatorException {
		// TODO Must check country against database
		
	}

	
	/**
	 * Used to manually input sunlight levels.
	 * 
	 * @param sunlight	must be between 0.0 and 24.0
	 * @throws EstimatorException
	 */
	public void SetSunlight( double sunlight ) throws EstimatorException {
		
		// Sanity checks
		if ( sunlight > 24.0 ) {
			throw new EstimatorException("Cannot have more than 24 hours of sunlight in one day");
		}
		
		if ( sunlight < 0.0 ) {
			throw new EstimatorException("Cannot have less than 0 hours of sunlight in one day");
		}
		
		this.sunlight = sunlight;  
	}

	public double getSunlight() {
		return sunlight;
	}
	
	
	
	
}
