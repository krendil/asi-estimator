package asi.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ValueBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
//GoogleMaps 1.1.1

public class Asi_Gui {

	public enum Panel {
		HOME, LOCATION, COST, PANELS,
		POWER, RESULTS 
	}

	//Buttons
	protected Button calculateButton;
	private Button locationNextButton;
	private Button costNextButton;
	private Button panelsNextButton;

	/*Elements*/
	// Cost Tab
	private Map<String, ValueBoxBase> boxes;

	
//
//	// Panels Tab

	protected ListBox panelAngle;
	protected ListBox panelDirection;


	//TabPanel
	protected TabPanel tabPanel;

	//VerticalPanels
	private Map<String,VerticalPanel> panels;
	

	//Labels
	private InlineLabel powerConsumptionLabel;
	private InlineLabel feedInTariffLabel;
	private InlineLabel hoursOfSunLabel;
	private InlineLabel nPanelsLabel;
	private InlineLabel panelCostLabel;
	private InlineLabel installCostLabel;
	private InlineLabel panelAngleLabel;
	private InlineLabel panelDirectionLabel;
	private InlineLabel panelWattageLabel;
	private InlineLabel elecCostLabel;
	private InlineLabel resultsLabel;
	private InlineLabel inverterCostLabel;
	private InlineLabel inverterEfficiencyLabel;

	//map
//	private VerticalPanel mapPanel; //Placeholding container to allow map to be loaded later
	private MapWidget map;
	private Marker mapMarker;
	private double lat;
	private double lng;

	//HTML
	public InlineHTML space; 
	public HTML homeText;

	//Array

	public String[] directions;

	public Asi_Gui()
	{
		
		// Create a tab panel
		tabPanel = new TabPanel();
		tabPanel.setWidth("100%");

		String[] panelNames = { "costPanel", "panelPanel", "powerPanel", "resultsPanel", "locationPanel", "mapPanel" };
		panels = new HashMap<String,VerticalPanel>();
		
		for (int i = 0; i < panelNames.length; i++) {
			panels.put(panelNames[i], new VerticalPanel());
			panels.get(panelNames[i]).ensureDebugId(panelNames[i]);
		}
		
		//Labels

		powerConsumptionLabel = new InlineLabel("Enter Annual Power Consumption (kWh):");
		feedInTariffLabel = new InlineLabel("Enter Feed-In Tariff Rates ($/kWh):");
		hoursOfSunLabel = new InlineLabel("Enter the average hours of sunlight per day:");
		nPanelsLabel = new InlineLabel("Enter the number of panels:");
		panelCostLabel = new InlineLabel("Enter the cost of each panel:");
		installCostLabel = new InlineLabel("Enter the cost of installation:");
		panelAngleLabel = new InlineLabel("Select angle of solar panels");
		panelDirectionLabel = new InlineLabel("Select direction of solar panels");
		panelWattageLabel = new InlineLabel("Enter your panel wattage (W):");
		elecCostLabel = new InlineLabel("Enter the cost of your electricity ($/kWh)");
		resultsLabel = new InlineLabel("Results here");
		inverterCostLabel = new InlineLabel("Enter the cost of your inverter");
		inverterEfficiencyLabel = new InlineLabel("Enter the efficiency of your inverter (%)");

		
		//TextBoxes
		boxes = new HashMap<String, ValueBoxBase>();
		boxes.put("powerConsumption", new DoubleBox());
		boxes.put("feedInTariff", new DoubleBox());
		boxes.put("hoursOfSun", new DoubleBox());
		boxes.put("nPanels", new IntegerBox());
		boxes.put("powerConsumption", new DoubleBox());
		boxes.put("panelCost", new DoubleBox());
		boxes.put("installCost", new DoubleBox());
		boxes.put("panelWattage", new DoubleBox());
		boxes.put("elecCost", new DoubleBox());
		boxes.put("inverterCost", new DoubleBox());
		boxes.put("inverterEfficiency", new PercentBox());
		
		for (String key : boxes.keySet()) {
		    boxes.get(key).ensureDebugId(key);
		    boxes.get(key).addValueChangeHandler(new InputValidator());
		}
		
		//Listboxes
		panelAngle = new ListBox();
		panelDirection = new ListBox();

		//Add values to panel Angle
		for (int degrees = 5; degrees <= 180; degrees += 5)
		{
			panelAngle.addItem(Integer.toString(degrees) + " degrees", Integer.toString(degrees));
		}

		//Add values to panel direction
		directions = new String[]{"N", "NE", "E", "SE", "S", "SW", "W", "SW"};

		for (int i = 0; i < directions.length; i++)
		{
			panelDirection.addItem(directions[i]);
		}


		//Buttons

		calculateButton = new Button("Calculate");
		calculateButton.ensureDebugId("calculateButton");

		locationNextButton = new Button("Next", new ClickHandler() 
		{
			public void onClick(ClickEvent event)
			{
				tabPanel.selectTab(Panel.COST.ordinal());
			}
		});
		locationNextButton.ensureDebugId("locationNextButton");

		costNextButton = new Button("Next", new ClickHandler() 
		{
			public void onClick(ClickEvent event)
			{
				tabPanel.selectTab(Panel.PANELS.ordinal());
			}
		});
		
		costNextButton.ensureDebugId("costNextButton");

		panelsNextButton = new Button("Next",new ClickHandler() 
		{
			public void onClick(ClickEvent event)
			{
				tabPanel.selectTab(Panel.POWER.ordinal());
			}
		});
		panelsNextButton.ensureDebugId("panelsNextButton");

		//HTML ELEMENTS

		homeText = new HTML("<p>Hi and welcome to the Solar Calculator</p>" +
				"<p>Click on the Location tab to start!</p>"+
				"<p>Developed by Agilis Sol Industria to help you choose the right solar panel for you!</p>");
		homeText.ensureDebugId("homePanel");

		space = new InlineHTML("<br/>");

		// Add to location panel
		panels.get("locationPanel").add(panels.get("mapPanel"));
		panels.get("locationPanel").add(locationNextButton);

		//Add to cost panel
		addToPanel(panels.get("costPanel"), boxes.get("panelCost"), panelCostLabel);
		addToPanel(panels.get("costPanel"), boxes.get("installCost"), installCostLabel); 
		addToPanel(panels.get("costPanel"), boxes.get("inverterCost"), inverterCostLabel);
		panels.get("costPanel").add(costNextButton);

		//Add to panelPanel
		addToPanel(panels.get("panelPanel"), boxes.get("nPanels"), nPanelsLabel);
		addToPanel(panels.get("panelPanel"), panelAngle, panelAngleLabel);
		addToPanel(panels.get("panelPanel"), panelDirection, panelDirectionLabel);
		addToPanel(panels.get("panelPanel"), boxes.get("panelWattage"), panelWattageLabel);
		addToPanel(panels.get("panelPanel"), boxes.get("hoursOfSun"), hoursOfSunLabel);
		addToPanel(panels.get("panelPanel"), boxes.get("inverterEfficiency"), inverterEfficiencyLabel);
		panels.get("panelPanel").add(panelsNextButton);


		//Add to powerPanel
		addToPanel(panels.get("powerPanel"), boxes.get("powerConsumption"), powerConsumptionLabel);
		addToPanel(panels.get("powerPanel"), boxes.get("feedInTariff"), feedInTariffLabel);
		addToPanel(panels.get("powerPanel"), boxes.get("elecCost"), elecCostLabel);
		panels.get("powerPanel").add(calculateButton);

		//Add to resultsPanel
		panels.get("resultsPanel").add(resultsLabel);

		//Add to tabPanel
		tabPanel.add(homeText, "Home");
		tabPanel.add(panels.get("locationPanel"), "Location");
		tabPanel.add(panels.get("costPanel"), "Cost");
		tabPanel.add(panels.get("panelPanel"), "Panels");
		tabPanel.add(panels.get("powerPanel"), "Power");

	}

	//Method for adding Textboxes to panel
	private void addToPanel(VerticalPanel vPanel, ValueBoxBase textbox, InlineLabel label)
	{
		vPanel.add(label);
		vPanel.add(textbox);
		vPanel.add(space);
	}

	//Overloaded method for adding listboxes to panel
	private void addToPanel(VerticalPanel vPanel, ListBox listbox, InlineLabel label)
	{	
		vPanel.add(label);
		vPanel.add(listbox);
		vPanel.add(space);
	}

	
	/* PUBLIC METHODS */
	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLat(double latitude) {
		this.lat = latitude;
	}
	
	public void setLng(double longitude) {
		this.lng = longitude;
	}

	public void addMap(MapWidget map, Marker mapMarker) {
		this.map = map;
		this.mapMarker = mapMarker;
		panels.get("mapPanel").add(this.map);
	}
	
	public boolean mapExists() {
		return map != null;
	}

	public void mapUpdate(LatLng latLng) {
		this.mapMarker.setLatLng(latLng);
		this.map.setCenter(latLng);
		this.map.checkResizeAndCenter();
	}

	public VerticalPanel getPanel(String panelName) {
		return panels.get(panelName);
	}
	
	public TabPanel getTabPanel(){
		return tabPanel;
	}

	public Button getButton(String string) {
		return null;
	}

	public ValueBoxBase getBox(String boxName) {
		return boxes.get(boxName);
	}
	
}
