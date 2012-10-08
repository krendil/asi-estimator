package asi.client;

//import com.example.myproject.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import asi.client.Asi_Gui;

import com.google.gwt.geolocation.client.*;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Asi_estimator implements EntryPoint {

	public Asi_Gui webGui;
	
	public void onModuleLoad() 
	{		
		webGui = new Asi_Gui();
		
		// Add to location panel
		webGui.addToPanel(webGui.locationPanel, webGui.longitude, webGui.longitudeLabel);
		webGui.addToPanel(webGui.locationPanel, webGui.latitude, webGui.latitudeLabel);
		webGui.locationPanel.add(webGui.locationNextButton);
		
		//Add to cost panel
	    webGui.addToPanel(webGui.costPanel, webGui.panelCost, webGui.panelCostLabel);
	    webGui.addToPanel(webGui.costPanel, webGui.installCost, webGui.installCostLabel); 
	    webGui.addToPanel(webGui.costPanel, webGui.inverterCost, webGui.inverterCostLabel);
	    webGui.costPanel.add(webGui.costNextButton);
	    
	    //Add to panelPanel
	    webGui.addToPanel(webGui.panelPanel, webGui.nPanels, webGui.nPanelsLabel);
	    webGui.addToPanel(webGui.panelPanel, webGui.panelAngle, webGui.panelAngleLabel);
	    webGui.addToPanel(webGui.panelPanel, webGui.panelDirection, webGui.panelDirectionLabel);
	    webGui.addToPanel(webGui.panelPanel, webGui.panelWattage, webGui.panelWattageLabel);
	    webGui.addToPanel(webGui.panelPanel, webGui.panelDegradation,  webGui.panelDegradationLabel);
	    webGui.addToPanel(webGui.panelPanel, webGui.hoursOfSun, webGui.hoursOfSunLabel);
	    webGui.addToPanel(webGui.panelPanel, webGui.inverterEfficiency, webGui.inverterEfficiencyLabel);
	    webGui.panelPanel.add(webGui.panelsNextButton);


	    //Add to powerPanel
	    webGui.addToPanel(webGui.powerPanel, webGui.powerConsumption, webGui.powerConsumptionLabel);
	    webGui.addToPanel(webGui.powerPanel, webGui.panelPower, webGui.panelPowerLabel);
	    webGui.addToPanel(webGui.powerPanel, webGui.tariffRates, webGui.tariffRatesLabel);
	    webGui.addToPanel(webGui.powerPanel, webGui.feedInTariff, webGui.feedInTariffLabel);
	    webGui.addToPanel(webGui.powerPanel, webGui.elecCost,webGui. elecCostLabel);
	    webGui.powerPanel.add(webGui.calculateButton);
	    
	    //Add to resultsPanel
	    webGui.resultsPanel.add(webGui.resultsLabel);
	   	    
		//Add to tabPanel
	    webGui.tabPanel.add(webGui.homeText, "Home");
	    webGui.tabPanel.add(webGui.locationPanel, "Location");
	    webGui.tabPanel.add(webGui.costPanel, "Cost");
	    webGui.tabPanel.add(webGui.panelPanel, "Panels");
	    webGui.tabPanel.add(webGui.powerPanel, "Power");
	    webGui.tabPanel.add(webGui.resultsPanel, "Results");
	    
	    
	    // Debugging...
	    webGui.tabPanel.ensureDebugId("tabPanel");
		
//		use google maps api
	    
	    detectLocation();
	    
		
	    
	    RootPanel.get("interface").add(webGui.tabPanel);
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
			 */			private void sendNameToServer() {
					// First, we validate the input.
					
					String textToServer = generateXML(); //Generate XML using this input
					
					//would be nice to have verification here

					// Then, we send the input to the server.
					webGui.calculateButton.setEnabled(false);
					
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

							webGui.calculateButton.setEnabled(true);
							
							
							String responseText = response.getText();
							
							Document doc = XMLParser.parse(responseText);
							
							NodeList powerTag = doc.getElementsByTagName("power");
							NodeList revenueTag = doc.getElementsByTagName("revenue");
							NodeList costTag = doc.getElementsByTagName("cost");
							
							String[] powers = powerTag.item(0).getFirstChild().getNodeValue().split("\\s");
							String[] revenues = revenueTag.item(0).getFirstChild().getNodeValue().split("\\s");
							String[] costs = costTag.item(0).getFirstChild().getNodeValue().split("\\s");
							//The follow code creates the HTML table
							FlexTable table = new FlexTable();
							int nYears = powers.length;
							//Years row
							table.setText(0, 0, "Years");
							for(int i = 0; i<nYears; i++){
								table.setText(0, i+1, Integer.toString(i));
								table.setText(1, i+1, String.format("%.2f kWh", Double.parseDouble(powers[i])));
								table.setText(2, i+1, String.format("$%.2f", Double.parseDouble(revenues[i])));
								table.setText(3, i+1, String.format("$%.2f", Double.parseDouble(costs[i])));	
							}													
							
							webGui.resultsPanel.add(table);
							
						}

						@Override
						public void onError(Request request, Throwable exception) {
							webGui.calculateButton.setEnabled(true);
							
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
			webGui.calculateButton.addClickHandler(new MyHandler());
			

	}
	
	private void detectLocation() {
		Geolocation location = Geolocation.getIfSupported();
	    if(location != null) {
	    	location.getCurrentPosition(new Callback<Position, PositionError>() {
				@Override
				public void onFailure(PositionError reason) {
					// Do nothing, the user will have to fill it in themselves	
				}
				@Override
				public void onSuccess(Position result) {
					webGui.longitude.setText(Double.toString(result.getCoordinates().getLongitude()));
					webGui.latitude.setText(Double.toString(result.getCoordinates().getLatitude()));
				}		
	    	});
	    }
	}
	
	private String generateXML() {
		
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
			"<!DOCTYPE solarquery SYSTEM \"http://asi-estimator.appspot.com/solarquery.dtd\">"+
			"<solarquery>"+
			"	<array>"+
			"		<bank facing=\""+getFacing(webGui.panelDirection.getItemText(webGui.panelDirection.getSelectedIndex())) +
				"\" number=\""+webGui.nPanels.getText()+"\" power=\""+webGui.panelPower.getText()+
				"\" tilt=\""+(String)webGui.panelAngle.getValue(webGui.panelAngle.getSelectedIndex())+"\"" +
						" price=\""+webGui.panelCost.getText()+"\"/>"+
			"	</array>"+
			"	<feedin rate=\""+webGui.feedInTariff.getText()+"\" />"+
			"	<consumption power=\""+webGui.powerConsumption.getText()+"\" rate=\""+webGui.tariffRates.getText()+"\"/>" +
			"	<sunlight hours=\""+webGui.hoursOfSun.getText()+"\" />" +
			"	<inverter efficiency=\""+webGui.inverterEfficiency.getText()+"\" price=\""+webGui.inverterCost.getText()+"\" />"+
			"</solarquery>";
	}
	
	private String getFacing(String cardin) {
		if(cardin== "N"){
			return "0";
		}
		else if(cardin== "NE"){
			return "45";
		}
		else if(cardin== "E"){
			return "90";
		}
		else if(cardin== "SE"){
			return "135";
		}
		else if(cardin== "S"){
			return "180";
		}
		else if(cardin== "SW"){
			return "225";
		}
		else if(cardin== "W"){
			return "270";
		}
		else if(cardin== "NW"){
			return "315";
		}
		else{return "0";}
	}


	
}
	
	
