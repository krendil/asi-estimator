package asi.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SolarArray extends Modifier{

	//THe proportion the panel's power production degrades by per year
	private static final double DEGRADATION_RATE = 0.01;
	
	private List<BankOfPanels> solarArray;
	private double latitude;
	
	
	public SolarArray() {
		this.solarArray = new ArrayList<BankOfPanels>();
	}
	
	public SolarArray(Node n) throws EstimatorException {
		this();
		
		Element el = (Element)n;
		NodeList banks = el.getElementsByTagName( "bank" );
		
		for(int i = 0; i < banks.getLength(); i++) {
			Element b = (Element)banks.item(i);
			double orientation;
			double kW;
			double dTilt;
			BigDecimal cost;
			
			String facing = b.getAttribute( "facing" );
			String number = b.getAttribute( "number" );
			String power = b.getAttribute( "power" );
			String tilt = b.getAttribute( "tilt" );
			String price = b.getAttribute( "price" );
			String lat = b.getAttribute("latitude");
			
			try {
				
				orientation = Double.parseDouble(facing);	
				kW = Integer.parseInt(number) * Double.parseDouble(power) / 1000.0;
				dTilt = Double.parseDouble(tilt);
				cost = new BigDecimal(price).multiply(new BigDecimal(number));
				if(lat.isEmpty()) {
					this.latitude = -27.0;
				} else {
					latitude = Double.parseDouble(lat);
				}
			} catch (NumberFormatException exc) {
				throw new EstimatorException("Error parsing xml: "+exc.getMessage());
			}
			
			BankOfPanels bank = new BankOfPanels(orientation, kW, dTilt, cost);
			this.addPanels(bank);
		}
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

	@Override
	public BigDecimal getNewCost(int year) {
		if( year != 0) {
			return BigDecimal.ZERO;
		} else { 
			BigDecimal cost = BigDecimal.ZERO;
			for( BankOfPanels b : solarArray) {
				cost = cost.add(b.getPrice());
			}
			return cost;
		}
	}
	
	@Override
	public double getMultiplier(int year) {
		double total = getTotalPower();
		total -= year * DEGRADATION_RATE * total;
		return total;
	}
	
	private double getTotalPower() {
		/**
		 * The direction, tilt and latitude all combine to 
		 * affect the amount of sunlight captured according to
		 * this equation, averaged over a year:
		 * R = sin(l)^2*cos(t) - sin(l)*cos(l)*cos(d) + cos(l)^2*cos(t) + cos(l)*sin(l)*cos(t)
		 *   = cos(t) + sin(l)*cos(l)*(sin(t) - cos(d))
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
		double power = 0.0;
		
		double sL = Math.sin(Math.toRadians(Math.abs(latitude)));
		double cL = Math.cos(Math.toRadians(Math.abs(latitude)));
		
		for( BankOfPanels b : solarArray )  {
			double cD = Math.cos(Math.toRadians(b.getOrientation()));
			double cT = Math.cos(Math.toRadians(b.getTilt()));
			double sT = Math.sin(Math.toRadians(b.getTilt()));
			double ratio = cT + sL*cL*(sT - cD);
			power += b.getKW() * ratio;
		}
		
		return power;
	}
} 
