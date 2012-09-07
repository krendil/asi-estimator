package asi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Asi_estimator implements EntryPoint {

	Button calculateButton;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		 // Create a tab panel
	    TabPanel tabPanel = new TabPanel();

	    // Set the width to 600 pixels
	    
	    tabPanel.setWidth("100%");

	    // Add a home tab
	    HTML homeText = new HTML("Hi and welcome to the Solar Calulcator" +
	    		"developer by Agilis Sol Industria to help benefit" +
	    		"this filler text we are filler text" +
	    		"filler text </br></br>" +
	    		"filler text </br></br>" +
	    		"filler text </br></br>" +
	    		"filler text </br></br>" +
	    		"filler text </br></br>" +
	    		"filler text </br></br>" +
	    		"filler text </br></br>" +
	    		"filler text </br></br>" +
	    		"filler text </br></br>" +
	    		"filler text </br></br>");
	    
	    tabPanel.add(homeText, "Home");

	    // Add a tab for calulating
	    
	    //Housing Panel
	    VerticalPanel housingPanel = new VerticalPanel();
	    // For vPANEL
	    final VerticalPanel vPanel = new VerticalPanel();

	       
	    //Labels
	    InlineLabel powerConsumptionLabel = new InlineLabel("Enter Power Consumption:");
	    InlineLabel tariffRatesLabel = new InlineLabel("Enter Tariff Rates:");
	    InlineLabel feedInTariffLabel = new InlineLabel("Enter Feed-In Tariff Rates:");
	    InlineLabel hoursOfSunLabel = new InlineLabel("Enter the average hours of sunlight per day");
	    
	    	    
	    //TextBoxes
	    TextBox powerConsumption = new TextBox();
	    TextBox tariffRates = new TextBox();
	    TextBox feedInTariff = new TextBox();
	    TextBox hoursOfSun = new TextBox();
	    

	    //Buttons
	    calculateButton = new Button("Calculate");
	    
	    //Space
	    InlineHTML space = new InlineHTML("<br/>");
	    
	    
	    //Add to panel
	    vPanel.add(powerConsumptionLabel);
	    vPanel.add(powerConsumption);
	    vPanel.add(space);
	    
	    vPanel.add(tariffRatesLabel);
	    vPanel.add(tariffRates);
	    vPanel.add(space);
	    
	    vPanel.add(feedInTariffLabel);
	    vPanel.add(feedInTariff);
	    vPanel.add(space);
	    
	    vPanel.add(hoursOfSunLabel);
	    vPanel.add(hoursOfSun);
	    vPanel.add(space);
	    
	    vPanel.add(calculateButton);
	    
	    //For Results Panel in Calculate tab
	    
	    final VerticalPanel resultsPanel = new VerticalPanel();
	    resultsPanel.setVisible(false);
	    
	    //Add back button
	    
	    Button backButton = new Button("Back");
	    
	    //Add to panel
	    resultsPanel.add(new InlineHTML("RESULTS DISPLAYED HERE <br/>"));
	    resultsPanel.add(backButton);
	    
	    
	    housingPanel.add(vPanel);
	    housingPanel.add(resultsPanel);
	    tabPanel.add(housingPanel, "Calculate ");
	    
	    
	    
	    
	    
	    

	    // Previous results tab
	    HTML prevResults = new HTML("Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>" +
	    		"Previous results Here <br/>");
	    
	    tabPanel.add(prevResults, "Previous Results");
	    
	    //Add tab panel to the interface
	    
	    RootPanel.get("interface").add(tabPanel);
	    
	    
	    // Listen for mouse events on the Add button.
	    calculateButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        vPanel.setVisible(false);
	        resultsPanel.setVisible(true);
	        
	      }
	    });
	    
	    backButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event2) {
	    		resultsPanel.setVisible(false);
	    		vPanel.setVisible(true);

	    	}
	    });
	    
	    
		
		
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
				//errorLabel.setText("");
				String textToServer = generateXML();//powerGenerationField.getText(); //Generate XML using this input
				
				//would be nice to have verification here

				// Then, we send the input to the server.
				calculateButton.setEnabled(false);
				
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
						
						//resultsHTML.setHTML(resultString);
						calculateButton.setEnabled(true);	
					}

					@Override
					public void onError(Request request, Throwable exception) {
						// TODO Auto-generated method stub
						calculateButton.setEnabled(true);		
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
		calculateButton.addClickHandler(new MyHandler());
	}
	
	private String generateXML() {
		//FIXME Obviously, this is bogus
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
			"<!DOCTYPE solarquery SYSTEM \"http://asi-estimator.appspot.com/solarquery.dtd\">"+
			"<solarquery>"+
			"	<array>"+
			"		<bank facing=\"0.0\" number=\"5\" power=\"200\" tilt=\"32.0\" price=\"1000.0\"/>"+
			"	</array>"+
			"	<feedin rate=\"0.08\" />"+
			"	<consumption power=\"11500\" rate=\"25.378\"/>" +
			"	<sunlight hours=\"4.5\" />" +
			"</solarquery>";
	}
	
	
}
