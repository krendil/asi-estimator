package asi.client;

import com.google.gwt.user.client.ui.DoubleBox;

public class PercentBox extends DoubleBox {

	@Override
	public Double getValue() {
		return super.getValue() / 100.0;
	}

}
