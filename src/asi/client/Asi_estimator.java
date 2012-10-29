package asi.client;

//import com.example.myproject.shared.FieldVerifier;
//import com.google.appengine.api.datastore.EntityNotFoundException;

import asi.client.Asi_Gui.Panel;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;

import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Asi_estimator implements EntryPoint {
	
	private boolean chartReady = false;
	
	private int nYears;
	private double[] dPowers;
	private double[] dRevenues;
	private double[] dCosts;
	

	private static final String URL = 
			//*   //<-- Comment toggler, add leading / to enable first section
			"http://asi-estimator.appspot.com/asi_estimator"
			/*/
			"http://127.0.0.1:8888/asi_estimator"
			//*/
			;

	public Asi_Gui webGui;

	public void onModuleLoad() 
	{

		webGui = new Asi_Gui();

		InputValidator iv = new InputValidator(this);
		for (ValueBoxBase<Object> box : webGui.getBoxes().values()) {
			box.addValueChangeHandler(iv);
		}

		// Debugging...
		webGui.getTabPanel().ensureDebugId("tabPanel");

		detectLocation();
		
		//loads maps
		Maps.loadMapsApi("AIzaSyBBqVqXtIiOLPkbBYmaqRxrOSFD9YrZ2ig", "2", false, new Runnable() 
		{
			public void run() 
			{
				buildMapUi();
			}
		});
		
		VisualizationUtils.loadVisualizationApi(new Runnable() {
			@Override
			public void run() {
				if(chartReady) {
					makeChart();
				} else {
					chartReady = true;
				}
			}
		}, LineChart.PACKAGE);

		RootPanel.get("interface").add(webGui.tabPanel);  

		webGui.tabPanel.selectTab(Panel.HOME.ordinal());

		// Create a handler for the sendButton and nameField
		class CalculateHandler implements ClickHandler {
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
						webGui.calculateButton.setEnabled(true);
						webGui.getPanel("resultsPanel").clear();
						showResults(response.getText(), response.getHeader("ResultId"));
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
		webGui.calculateButton.addClickHandler(new CalculateHandler());
		
		String queryCode = Window.Location.getParameter("q");
		if(queryCode != null) {
			loadOldQuery(queryCode);
		}

	}


	protected void makeChart() {
		Options options = Options.create();
		options.setHeight(400);
		options.setTitle("Power Generated and Total Profit over Time");
		options.setWidth(800);
		// options.setInterpolateNulls(true);
		AxisOptions vAxisOptions = AxisOptions.create();
		vAxisOptions.setMinValue(-2000);
		vAxisOptions.setMaxValue(2000);


		AxisOptions hAxisOptions = AxisOptions.create();
		hAxisOptions.setMinValue(0);
		hAxisOptions.setMaxValue(nYears);
		hAxisOptions.setTitle("Years");
		options.setVAxisOptions(vAxisOptions);
		options.setHAxisOptions(hAxisOptions);
		//Creates data
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.NUMBER, "Year");
		data.addColumn(ColumnType.NUMBER, "Power Generated (kWh)");
		data.addColumn(ColumnType.NUMBER, "Cumulative Profit ($)");
		data.addRows(nYears);
		
		double totalProfit = 0.0;
		for(int i=0; i < nYears; i++){
			data.setValue(i, 0, i);
			data.setValue(i, 1, dPowers[i]);

			totalProfit += dRevenues[i] - dCosts[i];
			data.setValue(i, 2, totalProfit);
		}

		LineChart line = new LineChart(data, options);
		webGui.getPanel("resultsPanel").insert(line, 0);
	}


	/**
	 * 
	 * @param responseText
	 */
	private void showResults(String responseText, String queryCode) {
		//

		Document doc = XMLParser.parse(responseText);

		NodeList powerTag = doc.getElementsByTagName("power");
		NodeList revenueTag = doc.getElementsByTagName("revenue");
		NodeList costTag = doc.getElementsByTagName("cost");

		String[] powers = powerTag.item(0).getFirstChild().getNodeValue().split("\\s");
		String[] revenues = revenueTag.item(0).getFirstChild().getNodeValue().split("\\s");
		String[] costs = costTag.item(0).getFirstChild().getNodeValue().split("\\s");

		nYears = powers.length;

		dPowers = new double[nYears];
		dRevenues = new double[nYears];
		dCosts = new double[nYears];
		for(int i = 0; i<nYears; i++){
			dPowers[i] = Double.parseDouble(powers[i]);
			dRevenues[i] = Double.parseDouble(revenues[i]);
			dCosts[i] = Double.parseDouble(costs[i]);
		}

		//The follow code creates the HTML table
		FlexTable table = new FlexTable();

		//Years row
		table.setText(0, 0, "Years");
		table.setText(1, 0, "Power produced");
		table.setText(2, 0, "Revenue");
		table.setText(3, 0, "Cost");

		NumberFormat pfmt = NumberFormat.getFormat("#,##0.0#");
		NumberFormat cfmt = NumberFormat.getFormat("$#,##0.00");

		for(int i = 0; i<nYears; i++){
			table.setText(0, i+1, Integer.toString(i));
			table.setText(1, i+1, pfmt.format(dPowers[i]));
			table.setText(2, i+1, cfmt.format(dRevenues[i]));
			table.setText(3, i+1, cfmt.format(dCosts[i]));	
		}

		//The following code creates the line chart
		//Sets options
		if( chartReady ) {
			makeChart();
		} else {
			chartReady = true;
		}
		

		double totalProfit = 0.0;
		boolean brokeEven = false;
		int yearToBreakEven = -1;

		for(int i=0; i < nYears; i++){
			totalProfit += dRevenues[i] - dCosts[i];

			if(!brokeEven) {
				brokeEven = totalProfit >= 0;
				if(brokeEven) {
					yearToBreakEven = i;
				}
			}
		}

		String breakEvenMessage;
		if(yearToBreakEven < 0) {
			breakEvenMessage = "You will not make a profit within " + Integer.toString(nYears+1) +
					" years.";
		} else  {
			breakEvenMessage = "You will break even in year " + Integer.toString(yearToBreakEven) + ".";
		}
		Label breakEvenLabel = new Label(breakEvenMessage);

		
		
		String uniqUrl;
		//gwt.codesvr=127.0.0.1:9997

		uniqUrl = Window.Location.createUrlBuilder().setParameter("q",  queryCode).buildString();

		  
		HTML queryCodeLabel = new HTML("The unique URL for these results is <a href="+uniqUrl+">"+uniqUrl+"</a>");

		// attach the results panel on first run.
		if (!webGui.getPanel("resultsPanel").isAttached()) {
			webGui.tabPanel.add(webGui.getPanel("resultsPanel"), "Results");
		}

		
		webGui.getPanel("resultsPanel").add(breakEvenLabel);
		webGui.getPanel("resultsPanel").add(table);
		webGui.getPanel("resultsPanel").add(queryCodeLabel);
		webGui.tabPanel.selectTab(Asi_Gui.Panel.RESULTS.ordinal());
	}

	/**
	 * Query beans for pre-filling of fields, given a location
	 */
	private void prefillFields() {
		//TODO: send requests to beans for pre-filling
		//RequestBuilder request = new RequestBuilder(RequestBuilder.POST, URL+"/prefill");
		
//		String latlng = webGui.getLat()+","+webGui.getLng();
		LatLng point = LatLng.newInstance(webGui.getLat(), webGui.getLng());

		Geocoder geo = new Geocoder();
		geo.getLocations(point, new LocationCallback() {

			@Override
			public void onFailure(int statusCode) {
				// We won't do any prefilling.
			}
			@Override
			public void onSuccess(JsArray<Placemark> locations) {
				String location = locations.get(0).getCountry()+"."+locations.get(0).getState();				
				doPrefill(location);
			}
		});
		
	}


	private void doPrefill(String location) {
		location = location.toLowerCase();
		
		RequestBuilder request = new RequestBuilder(RequestBuilder.POST, URL+"/prefill");

		request.setRequestData(location);//Change to xml string

		request.setCallback( new RequestCallback() {
			@Override
			public void onResponseReceived ( Request request, Response response ) {
				showPrefills(response.getText());
			}

			@Override
			public void onError(Request request, Throwable exception) {

			}
		});

		try {
			request.send();
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// split up the string and display the information
	private void showPrefills(String text) {

		char[] chars = text.toCharArray(); 
		int count = 0;
		
		for ( int i = 0; i < chars.length; i++ ) {
			if ( chars[i] == ',' ) {
				count++;
			}
		}

		// if it's faulty, we'll change it to a complete unknown
		if (count != 2) {
			text = "?,?,?";
		}

		String splitText[] = text.split(",");
		
		// change ? to empty text (we didnt find anything in the database)
		for (int i = 0; i < splitText.length; i++ ) {
			if (splitText[i].equals("?")) {
				splitText[i] = "";
			}
		}
		
		String avgCons = splitText[0];
		String feedIn = splitText[1];
		String elecCost = splitText[2];

		webGui.getBox("elecCost").setText(elecCost);
		webGui.getBox("powerConsumption").setText(avgCons);
		webGui.getBox("feedInTariff").setText(feedIn);

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
					//webGui.setLng(result.getCoordinates().getLongitude());
					//webGui.setLat(result.getCoordinates().getLatitude());
					setMapLocation( result.getCoordinates().getLatitude() , result.getCoordinates().getLongitude() );
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
				"\" number=\""+webGui.getBox("nPanels").getText()+"\" power=\""+webGui.getBox("panelWattage").getText()+
				"\" tilt=\""+(String)webGui.panelAngle.getValue(webGui.panelAngle.getSelectedIndex())+
				"\" price=\""+webGui.getBox("panelCost").getText()+
				"\" latitude=\""+Double.toString(webGui.getLat())+"\" />"+
				"	</array>"+
				"	<feedin rate=\""+webGui.getBox("feedInTariff").getText()+"\" />"+
				"	<consumption power=\""+webGui.getBox("powerConsumption").getText()+"\" rate=\""+webGui.getBox("elecCost").getText()+"\"/>" +
				"	<sunlight hours=\""+webGui.getBox("hoursOfSun").getText()+"\" />" +
				"	<inverter efficiency=\""+webGui.getBox("inverterEfficiency").getValue().toString()+"\"" +
						" price=\""+((Double)webGui.getBox("inverterCost").getValue()
								   + (Double)webGui.getBox("installCost").getValue())+"\" />"+
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

	//Builds a map, lat and lng are pulled from instance variables lat and lng
	//USES GOOGLE MAPS FOR GWT 1.1.1
	//TODO actionListener for the lat and long textboxes, relaod map each time they alter - will do soon - Liam
	public void buildMapUi() 
	{

		MapWidget map;
		Marker mapMarker;

		LatLng latLng = LatLng.newInstance(webGui.getLat(), webGui.getLng());

		map = new MapWidget(latLng, 4);
		map.setSize("400px", "400px");
//		map.setGoogleBarEnabled(true);

		// Add some controls for the zoom level
		map.addControl(new SmallMapControl());    		        

		mapMarker = new Marker(latLng);
		map.addOverlay(mapMarker);

		map.addMapClickHandler(new MapClickHandler(){
			@Override
			public void onClick(MapClickEvent event) {
				LatLng latLng = event.getLatLng();
				setMapLocation(latLng.getLatitude(), latLng.getLongitude());
			}
		});

		webGui.addMap(map, mapMarker);
		setMapLocation(latLng.getLatitude(), latLng.getLongitude());
	}

	/**
	 * Moves the map marker and recenters the map on the given place.
	 * @param latitude
	 * @param longitude
	 */
	public void setMapLocation(double latitude, double longitude) {
		webGui.setLat(latitude);
		webGui.setLng(longitude);
		
		if(webGui.mapExists()) {
			LatLng latLng = LatLng.newInstance(latitude, longitude);
			webGui.mapUpdate(latLng);
		}
		
		prefillFields();
	}
	
	public void validateFields() {
		boolean allValid = true;
		for(ValueBoxBase<Object> box : webGui.getBoxes().values()) {
			if(box.getValue() == null) {
				allValid = false;
				break;
			}
		}
		webGui.setCalculateEnabled(allValid);
	}
	
	public void loadOldQuery(final String queryCode) {
		RequestBuilder request = new RequestBuilder(RequestBuilder.POST, URL+"/history");

		request.setRequestData(queryCode);//Change to xml string

		request.setCallback( new RequestCallback() {
			@Override
			public void onResponseReceived ( Request request, Response response) {
				webGui.getPanel("resultsPanel").clear();
				showResults(response.getText(), queryCode);
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


