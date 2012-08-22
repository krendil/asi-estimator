package asi.beans;

/**
 * 
 * @author Steven Turner
 * 
 * Exceptions for use with the Estimator classes
 *
 */
@SuppressWarnings("serial")
public class EstimatorException extends Exception {

	/**
	 *  generic exception
	 */
	public EstimatorException() {
		super();
	}

	/**
	 * Exception with a message
	 * @param message
	 */
	public EstimatorException(String message) {
		super("EstimatorException: " + message);
	}
}
