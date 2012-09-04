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
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
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
				String textToServer = generateXML();//powerGenerationField.getText(); //Generate XML using this input
				
				//would be nice to have verification here

				// Then, we send the input to the server.
				submitButton.setEnabled(false);
				
				RequestBuilder request = new RequestBuilder(RequestBuilder.POST, 
						/*   //<-- Comment toggler, add leading / to enable first section
						"http://asi-estimator.appspot.com/asi_estimator/estimate"
						/*/
						"http://127.0.0.1:8888/asi_estimator/estimate"
						//*/
				);
				
				request.setRequestData(textToServer);//Change to xml string
				
				
				request.setCallback(new RequestCallback(){

					@Override
					public void onResponseReceived(Request request,
							Response response) {
						String responseText = response.getText();
						
						Document doc = XMLParser.parse(responseText);
						
						Node powerTag = doc.getElementsByTagName("power").item(0);
						Node revenueTag = doc.getElementsByTagName("revenue").item(0);
						
						String powerString = powerTag.getFirstChild().getNodeValue();
						String revenueString = revenueTag.getFirstChild().getNodeValue();
						
						String resultString = "Power = " + powerString + " kWh</br> Revenue = $" + revenueString;
						
						resultsHTML.setHTML(resultString);
						
					}

					@Override
					public void onError(Request request, Throwable exception) {
						// TODO Auto-generated method stub
						
					}
					
				} );
				
				try {
					request.send();
				} catch (RequestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//create handlers for widgets
			//Submit
		//add handlers to widgets
		submitButton.addClickHandler(new MyHandler());
		
		
		
	}
	
	private String generateXML() {
		//FIXME Obviously, this is bogus
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
			"<!DOCTYPE solarquery SYSTEM \"http://asi-estimator.appspot.com/solarquery.dtd\">"+
			"<solarquery>"+
			"	<array>"+
			"		<bank facing=\"0.0\" number=\"5\" power=\"200\" tilt=\"32.0\"/>"+
			"	</array>"+
			"	<location lat=\"-27.47815\" long=\"153.027687\" />"+
			"	<feedin rate=\"0.08\" />"+
			"	<consumption power=\"11500\" rate=\"25.378\"/>" +
			"	<sunlight hours=\"4.5\" />" +
			"</solarquery>";
	}
	
	
}
