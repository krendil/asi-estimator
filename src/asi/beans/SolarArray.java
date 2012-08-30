package asi.beans;

import java.util.ArrayList;
import java.util.List;

public class SolarArray {

	List<BankOfPanels> solarArray;
	
	public SolarArray() {
		this.solarArray = new ArrayList<BankOfPanels>();
	}

	/**
	 * Add a bank of panels to the current solar array
	 * @param bankOfPanels
	 */
	public void addPanels(BankOfPanels bankOfPanels) {
		solarArray.add(bankOfPanels);
	}
	
	/**
	 * Remove a bank of panes from a solar array
	 * @param bankOfPanels
	 */
	public void removePanels(BankOfPanels bankOfPanels) throws EstimatorException {
		
		if (!solarArray.contains(bankOfPanels)) {
			throw new EstimatorException( "Unable to find bank of panels in Solar Array." );
		}
		
		solarArray.remove(bankOfPanels);
		
	}

	
	
	/**
	 * method to get the details about the solar array
	 * @return a list of lists. [bank number][0 = orientation, 1 = kW]
	 * @throws EstimatorException
	 */
	public double[][] getDetails() throws EstimatorException {
		// sanity check
		if (solarArray.isEmpty()) {
			throw new EstimatorException("Cannot give solar array details on empty solar array");
		}
		
		double[][] details = new double[solarArray.size()][2];
		
		for (int i = 0; i < solarArray.size(); i++ ) {
			details[i][0] = solarArray.get(i).getOrientation();
			details[i][1] = solarArray.get(i).getKW();
		}
		
//		used in testing
//		for ( BankOfPanels bank: solarArray ){
//			System.out.println("Orientation: " + bank.getOrientation() + "\nPower: " + bank.getKW() + "kW\n\n");
//		}

		return details;
	}
} 
