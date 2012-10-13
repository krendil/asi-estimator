package asi.client;

//import com.example.myproject.shared.FieldVerifier;
//import com.google.appengine.api.datastore.EntityNotFoundException;

import asi.client.Asi_Gui.Panel;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Asi_estimator implements EntryPoint {

	private static final String URL = 
			/*   //<-- Comment toggler, add leading / to enable first section
			"http://asi-estimator.appspot.com/asi_estimator"
			/*/
			"http://127.0.0.1:8888/asi_estimator"
			//*/
			;
		
	public Asi_Gui webGui;
	
	public void onModuleLoad() 
	{
		
		webGui = new Asi_Gui();
		
	    // Debugging...
	    webGui.tabPanel.ensureDebugId("tabPanel");
	    
	    detectLocation();
	   
	    //loads maps
	   Maps.loadMapsApi("", "2", false, new Runnable() 
	   {
		      public void run() 
		      {
		        webGui.buildMapUi();
		      }
   	   });
		    
	    
	    RootPanel.get("interface").add(webGui.tabPanel);  
	    
	    
	    
	    webGui.tabPanel.selectTab(Panel.HOME.ordinal());
	    
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
				
				String textToServer = generateXML(); //Generate XML using this input
				
				//TODO: would be nice to have verification here
				
				// Then, we send the input to the server.
				webGui.calculateButton.setEnabled(false);
				
				RequestBuilder request = new RequestBuilder(RequestBuilder.POST, URL+"/estimate");
					
				request.setRequestData(textToServer);//Change to xml string
				
				request.setCallback( new RequestCallback() {
					@Override
					public void onResponseReceived ( Request request, Response response) {
						showResults(response.getText());
					}
					@Override
					public void onError(Request request, Throwable exception) {
						webGui.calculateButton.setEnabled(true);
					}
				});
				
				
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
	
	
	/**
	 * 
	 * @param responseText
	 */
	private void showResults(String responseText) {
		webGui.calculateButton.setEnabled(true);
		
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
		table.setText(1, 0, "Power produced");
		table.setText(2, 0, "Revenue");
		table.setText(3, 0, "Cost");
		
		NumberFormat pfmt = NumberFormat.getFormat("#,##0.0#");
		NumberFormat cfmt = NumberFormat.getFormat("$#,##0.00");
		
		for(int i = 0; i<nYears; i++){
			table.setText(0, i+1, Integer.toString(i));
			table.setText(1, i+1, pfmt.format(Double.parseDouble(powers[i])));
			table.setText(2, i+1, cfmt.format(Double.parseDouble(revenues[i])));
			table.setText(3, i+1, cfmt.format(Double.parseDouble(costs[i])));	
		}
		
		// attach the results panel on first run.
		if (!webGui.resultsPanel.isAttached()) {
			webGui.tabPanel.add(webGui.resultsPanel, "Results");
		}
		
		webGui.resultsPanel.clear();
		webGui.resultsPanel.add(table);
		webGui.tabPanel.selectTab(Asi_Gui.Panel.RESULTS.ordinal());
	}
	
	/**
	 * Query beans for pre-filling of fields, given a location
	 */
	private void prefillFields() {
		//TODO: send requests to beans for pre-filling
		//RequestBuilder request = new RequestBuilder(RequestBuilder.POST, URL+"/prefill");
		String latlng = webGui.latitude.getText()+","+webGui.longitude.getText();
		
		// Geocode
		String returnType = "xml"; // "xml" or "json"
		String geocodePath = "http://maps.googleapis.com/maps/api/geocode/"+returnType+"?latlng="+latlng+"&sensor=false";
		
		System.out.println("Geocode path: \""+geocodePath+"\"");
		
		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, geocodePath);
		request.setRequestData("");
		request.setCallback( new RequestCallback(){
			@Override
			public void onResponseReceived ( Request request, Response response ) {
				doPrefill(response.getText());
			}
			@Override
			public void onError(Request request, Throwable exception) {
				//TODO: it broke
				System.out.println("Asi_estimator: Excepion while trying to do prefill.");
			}
		} );
		
		try {
			System.out.println("'Bout to send request to get reverse geocode");
			request.send();
			
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void doPrefill(String response) {
		//TODO: fix this
		//i'm unable to grab the response text at this point in time. 
		
		System.out.println("Geocode response: \""+response+"\"");
	}
	
	/**
	 * Detect location using in-built client-side browser
	 */
	private void detectLocation() {
		Geolocation location = Geolocation.getIfSupported();
	    if(location != null) {
	    	location.getCurrentPosition(new Callback<Position, PositionError>() {
				@Override
				public void onFailure(PositionError reason) {
					// cannot perform location-specific functionality without valid location.
				}
				@Override
				public void onSuccess(Position result) {
					//webGui.longitude.setText(Double.toString(result.getCoordinates().getLongitude()));
					//webGui.latitude.setText(Double.toString(result.getCoordinates().getLatitude()));		
					webGui.setMapLocation(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude());
					prefillFields();
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
				"\" number=\""+webGui.nPanels.getText()+"\" power=\""+webGui.panelWattage.getText()+
				"\" tilt=\""+(String)webGui.panelAngle.getValue(webGui.panelAngle.getSelectedIndex())+"\"" +
						" price=\""+webGui.panelCost.getText()+"\"/>"+
			"	</array>"+
			"	<feedin rate=\""+webGui.feedInTariff.getText()+"\" />"+
			"	<consumption power=\""+webGui.powerConsumption.getText()+"\" rate=\""+webGui.elecCost.getText()+"\"/>" +
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
	
	
