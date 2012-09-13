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
	TextBox powerConsumption;
	TextBox feedInTariff;
	TextBox tariffRates;
	TextBox hoursOfSun;
	TextBox panelPower;
	TextBox nPanels;
	TextBox panelCost;
	
	
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
	    InlineLabel panelPowerLabel = new InlineLabel("Enter the power rating of each panel");
	    InlineLabel nPanelsLabel = new InlineLabel("Enter the number of panels");
	    InlineLabel panelCostLabel = new InlineLabel("Enter the cost of each panel");
	    
	    	    
	    //TextBoxes
	     powerConsumption = new TextBox();
	     tariffRates = new TextBox();
	     feedInTariff = new TextBox();
	     hoursOfSun = new TextBox();
	 	panelPower = new TextBox();
		 nPanels= new TextBox();
		 panelCost= new TextBox();
	    

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
	    
	    vPanel.add(panelPowerLabel);
	    vPanel.add(panelPower);
	    vPanel.add(space);
	    
	    vPanel.add(nPanelsLabel);
	    vPanel.add(nPanels);
	    vPanel.add(space);
	    
	    vPanel.add(panelCostLabel);
	    vPanel.add(panelCost);
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
				String textToServer = generateXML(powerConsumption.getText(), tariffRates.getText(), 
						feedInTariff.getText(), hoursOfSun.getText(), nPanels.getText(), panelPower.getText(),
						panelCost.getText()); //Generate XML using this input
				
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
						
						//FOr debugging purposes
						String responseText = response.getText();
						String statusText = response.getStatusText();
						
						Document doc = XMLParser.parse(responseText);
						
						Node powerTag = doc.getElementsByTagName("power").item(0);
						Node revenueTag = doc.getElementsByTagName("revenue").item(0);
						
						String powerString = powerTag.getFirstChild().getNodeValue();
						String revenueString = revenueTag.getFirstChild().getNodeValue();
						
						String resultString = "Power = " + powerString.split(" ")[0] + " kWh</br> Revenue = $" + revenueString.split(" ")[0];
						
					    resultsPanel.add(new InlineHTML(resultString));
						
						//resultsHTML.setHTML(resultString);
						calculateButton.setEnabled(true);	
					}

					@Override
					public void onError(Request request, Throwable exception) {
						resultsPanel.add(new InlineHTML(exception.getMessage()));
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
	
	private String generateXML(String powerConsumption, String tariffRates, String feedInTariff,
			String hoursOfSun, String nPanels, String panelPower, String panelCost) {
		//FIXME Obviously, this is bogus
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
			"<!DOCTYPE solarquery SYSTEM \"http://asi-estimator.appspot.com/solarquery.dtd\">"+
			"<solarquery>"+
			"	<array>"+
			"		<bank facing=\"0.0\" number=\""+nPanels+"\" power=\""+panelPower+"\" tilt=\"32.0\" price=\""+panelCost+"\"/>"+
			"	</array>"+
			"	<feedin rate=\""+feedInTariff+"\" />"+
			"	<consumption power=\""+powerConsumption+"\" rate=\""+tariffRates+"\"/>" +
			"	<sunlight hours=\""+hoursOfSun+"\" />" +
			"</solarquery>";
	}
	
	
}
