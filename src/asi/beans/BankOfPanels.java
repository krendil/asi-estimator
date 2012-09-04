package asi.beans;

/**
 * Bank of panels class
 * @author Steven Turner
 *
 */
public class BankOfPanels {
	private static final double MAX_ORIENTATION = 360.0;
	private static final double MIN_ORIENTATION = 0.0;
	private static final double MAX_TILT = 90.0;
	private static final double MIN_TILT = 0.0;
	
	private double orientation;	// degrees from North
	private double kW; // kilowatt
	private double tilt; //degrees from horizontal
	
	/**
	 * Constructor
	 * @param orientation in degrees
	 * @param kW rated kilowatt output
	 */
	public BankOfPanels( double orientation, double kW, double tilt ) throws EstimatorException {
		this.setOrientation(orientation);
		this.setKW(kW);
		this.setTilt(tilt);
	}

	/**
	 * Setter for orientation
	 * @param orientation
	 */
	private void setOrientation(double orientation) throws EstimatorException {
		// sanity checks
		if ( orientation >= MAX_ORIENTATION ) {
			throw new EstimatorException ( "Panel orientation cannot be set above 360 degrees" );
		}
		if ( orientation < MIN_ORIENTATION ) {
			throw new EstimatorException ( "Panel orientation cannot be set below 0 degreess" );
		}
		
		//TODO We should decide if north is 0.0 or 360.0 degrees and make modifications for it here.
		
		this.orientation = orientation;
	}
	
	/**
	 * Setter for kW
	 * @param kW
	 */
	private void setKW(double kW) throws EstimatorException {
		// sanity checks
		
		if ( kW <= 0 ) {
			throw new EstimatorException( "Solar panels output should be higher than 0." );
		}
		
		this.kW = kW;
	}
	
	private void setTilt(double tilt) throws EstimatorException {
		// sanity checks
		
		if (tilt < MIN_TILT) {
			throw new EstimatorException( "Panel tilt cannot be set below 0 degrees" );
		}
		if (tilt >= MAX_TILT ) {
			throw new EstimatorException( "Panel tilt cannot be set above 90 degrees" );
		}
		
		this.tilt = tilt;
	}
	
	

	public double getOrientation() {
		return orientation;
	}

	public double getKW() {
		return kW;
	}
	public double getTilt() {
		return tilt;
	}

}
