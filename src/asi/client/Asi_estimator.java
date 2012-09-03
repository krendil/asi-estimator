package asi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.http.client.*;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Asi_estimator implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//add widgets
		final Button submitButton = new Button("Submit");
		final TextBox powerGenerationField = new TextBox();
		powerGenerationField.setText("500");
		final Label errorLabel = new Label();
		final HTML resultsHTML = new HTML();
		
		//add widgets to html
		RootPanel.get("powerGenerationFieldContainer").add(powerGenerationField);
		RootPanel.get("submitButtonContainer").add(submitButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		RootPanel.get("resultsHTMLContainer").add(resultsHTML);
		
		powerGenerationField.setFocus(true);
		powerGenerationField.selectAll();
		
		
		
		
		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

		
			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = powerGenerationField.getText(); //Generate XML using this input
				//would be nice to have verification here

				// Then, we send the input to the server.
				submitButton.setEnabled(false);
				
				RequestBuilder request = new RequestBuilder(RequestBuilder.POST, "www.google.com");//To be made a constant, yes google.com
				
				request.setRequestData(textToServer);//Change to xml string
				
				
				request.setCallback(new RequestCallback(){

					@Override
					public void onResponseReceived(Request request,
							Response response) {
						String responseText = response.getText();
						
						Document doc = XMLParser.parse(responseText);
						
						Node powerTag = doc.getElementsByTagName("power").item(0);
						Node revenueTag = doc.getElementsByTagName("revenue").item(0);
						
						String powerString = powerTag.getNodeValue();
						String revenueString = revenueTag.getNodeValue();
						
						String resultString = "Power = " + powerString + "</br> Revenue: " + revenueString;
						
						resultsHTML.setHTML(resultString);
						
					}

					@Override
					public void onError(Request request, Throwable exception) {
						// TODO Auto-generated method stub
						
					}
					
				} );
				
			}
		}
		//create handlers for widgets
			//Submit
		//add handlers to widgets
		
		
		
	}
	
	
}
