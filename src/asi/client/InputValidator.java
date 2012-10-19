package asi.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.UIObject;

public class InputValidator implements ValueChangeHandler<Object> {

	static final String INVALID_STYLE = "asiInvalidField";
	private Asi_estimator controller;
	
	public InputValidator(Asi_estimator controller) {
		this.controller = controller;
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<Object> event) {
		if(event.getValue() == null) {
			((UIObject) event.getSource()).addStyleName("asiInvalidField");
			controller.validateFields();
		} else {
			((UIObject) event.getSource()).removeStyleName("asiInvalidField");
			controller.validateFields();
		}
	}

}
