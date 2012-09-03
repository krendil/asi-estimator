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
	final private double DEFAULT_ORIENTATION = 100.0;
	
	private String country;
	private String city;
	private double sunlight;	//average hours of daily sunlight
	private double idealOrientation;	// in degrees
	
	
	
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
		
		
		// TODO query database to get the average sunlight hours and assign them here.
		this.sunlight = DEFAULT_DAILY_SUNLIGHT;
		this.idealOrientation = DEFAULT_ORIENTATION;
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
		
		// TODO query database to get the average sunlight hours and assign them here.
		this.sunlight = DEFAULT_DAILY_SUNLIGHT;
		this.idealOrientation = DEFAULT_ORIENTATION;
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
	public void setSunlight( double sunlight ) throws EstimatorException {
		
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
	
	
	public double getOrientation() {
		return idealOrientation;
	}
	
	
	
	
}
