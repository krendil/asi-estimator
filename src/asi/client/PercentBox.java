package asi.client;

import com.google.gwt.user.client.ui.DoubleBox;

public class PercentBox extends DoubleBox {

	@Override
	public Double getValue() {
		Double value = super.getValue();
		if( value == null || value == 0.0 ) {
			return value;
		} else {
			return value / 100.0;
		}
	}

}
