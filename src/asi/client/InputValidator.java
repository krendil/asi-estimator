package asi.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.UIObject;

public class InputValidator<T> implements ValueChangeHandler<T> {

	static final String INVALID_STYLE = "asiInvalidField";
	
	@Override
	public void onValueChange(ValueChangeEvent<T> event) {
		if(event.getValue() == null) {
			((UIObject) event.getSource()).addStyleName("asiInvalidField");
		} else {
			((UIObject) event.getSource()).removeStyleName("asiInvalidField");
		}
	}

}
