package asi.client;

import com.google.gwt.user.client.ui.DoubleBox;

public class PercentBox extends DoubleBox {
	
	@Override
	public void setValue(Double value, boolean fireEvents) {
		super.setValue(value/100.0, fireEvents);
	}
	
	@Override
	public void setValue(Double value) {
		setValue(value, false);
	}

}
