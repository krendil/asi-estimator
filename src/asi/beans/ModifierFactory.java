package asi.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.w3c.dom.Node;

public class ModifierFactory {
	private enum ModifierTag {
		array (SolarArray.class),
		sunlight (HoursOfSunlight.class),
		feedin (FeedInRate.class),
		consumption (Consumption.class),
		inverter (Inverter.class);
		
		Class<? extends Modifier> modifierClass;
		
		private ModifierTag(Class<? extends Modifier> cls){
			this.modifierClass = cls;
		}
		
		public Modifier getModifier(Node n) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
			Constructor<? extends Modifier> c = modifierClass.getConstructor(Node.class);
			return c.newInstance(n);
		}
	}
	
	public static Modifier createModifier(Node n) throws EstimatorException{
		Modifier m;
		
		try {
			m = ModifierTag.valueOf(n.getNodeName()).getModifier(n);
		} catch (SecurityException e){	
			e.printStackTrace();
			throw new EstimatorException(e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new EstimatorException(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new EstimatorException(e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new EstimatorException(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new EstimatorException(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new EstimatorException(e.getMessage());
		}
		
		return m;
	}
	
}
