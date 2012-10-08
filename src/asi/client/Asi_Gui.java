package asi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Asi_Gui {
	
	public enum Panel {
	    HOME, LOCATION, COST, PANELS,
	    POWER, RESULTS 
	}

	//Buttons
	public Button calculateButton;
	public Button locationNextButton;
	public Button costNextButton;
	public Button panelsNextButton;
	
	//Elements
	
	//location text boxes
	public TextBox longitude;
	public TextBox latitude;
	
	// Cost Tab
	public TextBox panelCost;
	public TextBox installCost;
	public TextBox inverterCost;
	
	// Panels Tab
	public TextBox nPanels;
	public ListBox panelAngle;
	public ListBox panelDirection;
	public TextBox panelWattage;
	public TextBox panelDegradation;
	public TextBox hoursOfSun;
	public TextBox inverterEfficiency;
	
	// Power Tab
	public TextBox powerConsumption;
	public TextBox feedInTariff;
	public TextBox elecCost;
	
	//TabPanel
	public TabPanel tabPanel;
	
	//VerticalPanels
	public VerticalPanel costPanel;
	public VerticalPanel panelPanel;
	public VerticalPanel powerPanel;
	public VerticalPanel resultsPanel;
	public VerticalPanel locationPanel;
	
    //Labels
	public InlineLabel powerConsumptionLabel;
	public InlineLabel feedInTariffLabel;
	public InlineLabel hoursOfSunLabel;
	public InlineLabel nPanelsLabel;
	public InlineLabel panelCostLabel;
	public InlineLabel installCostLabel;
	public InlineLabel panelAngleLabel;
	public InlineLabel panelDirectionLabel;
	public InlineLabel panelWattageLabel;
	public InlineLabel panelDegradationLabel;
	public InlineLabel elecCostLabel;
	public InlineLabel resultsLabel;
	public InlineLabel inverterCostLabel;
	public InlineLabel inverterEfficiencyLabel;
    
	//location text boxes
	public InlineLabel longitudeLabel;
	public InlineLabel latitudeLabel;
	
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

	    
	    
	    // Panels for tabPanel
	    
	    costPanel = new VerticalPanel();
	    costPanel.ensureDebugId("costPanel");
	    
	    panelPanel = new VerticalPanel();
	    panelPanel.ensureDebugId("panelPanel");
	    
	    powerPanel = new VerticalPanel();
	    powerPanel.ensureDebugId("powerPanel");
	    
	    resultsPanel = new VerticalPanel();
	    resultsPanel.ensureDebugId("resultsPanel");
	    
	    locationPanel = new VerticalPanel();
	    locationPanel.ensureDebugId("locationPanel");
	    

       
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
	    panelDegradationLabel = new InlineLabel("Enter your panel degradation(%/year)");
	    elecCostLabel = new InlineLabel("Enter the cost of your electricity ($/kWh)");
	    resultsLabel = new InlineLabel("Results here");
	    inverterCostLabel = new InlineLabel("Enter the cost of your inverter");
	    inverterEfficiencyLabel = new InlineLabel("Enter the efficiency of your inverter (%)");
	    
		longitudeLabel = new InlineLabel("Estimated longitude");
		latitudeLabel = new InlineLabel("Estimated latitude");
	    
	    
		
	    //TextBoxes
	     powerConsumption = new TextBox();
	     powerConsumption.ensureDebugId("powerConsumption");
	     
	     feedInTariff = new TextBox();
	     feedInTariff.ensureDebugId("feedInTariff");
	     
	     hoursOfSun = new TextBox();
	     hoursOfSun.ensureDebugId("hoursOfSun");
	 	
		 nPanels= new TextBox();
		 nPanels.ensureDebugId("nPanels");
		 
		 panelCost= new TextBox();
		 panelCost.ensureDebugId("panelCost");
		 
		 installCost = new TextBox();
		 installCost.ensureDebugId("installCost");
		 
		 panelWattage = new TextBox();
		 panelWattage.ensureDebugId("panelWattage");
		 
		 panelDegradation = new TextBox();
		 panelDegradation.ensureDebugId("panelDegradation");
		 
		 elecCost = new TextBox();
		 elecCost.ensureDebugId("elecCost");
		 
		 inverterCost = new TextBox();
		 inverterCost.ensureDebugId("inverterCost");
		 
		 inverterEfficiency = new TextBox();
		 inverterEfficiency.ensureDebugId("inverterEfficiency");
		 
		 longitude = new TextBox();
		 latitude = new TextBox();
		 
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
	    
	    homeText = new HTML("Hi and welcome to the Solar Calulcator" +
	    		"developer by Agilis Sol Industria to help you choose the right solar panel for you!");
	    homeText.ensureDebugId("homePanel");
 
	    space = new InlineHTML("<br/>");
	    
		// Add to location panel
		addToPanel(locationPanel, longitude, longitudeLabel);
		addToPanel(locationPanel, latitude, latitudeLabel);
		locationPanel.add(locationNextButton);
		
		//Add to cost panel
	    addToPanel(costPanel, panelCost, panelCostLabel);
	    addToPanel(costPanel, installCost, installCostLabel); 
	    addToPanel(costPanel, inverterCost, inverterCostLabel);
	    costPanel.add(costNextButton);
	    
	    //Add to panelPanel
	    addToPanel(panelPanel, nPanels, nPanelsLabel);
	    addToPanel(panelPanel, panelAngle, panelAngleLabel);
	    addToPanel(panelPanel, panelDirection, panelDirectionLabel);
	    addToPanel(panelPanel, panelWattage, panelWattageLabel);
	    addToPanel(panelPanel, panelDegradation,  panelDegradationLabel);
	    addToPanel(panelPanel, hoursOfSun, hoursOfSunLabel);
	    addToPanel(panelPanel, inverterEfficiency, inverterEfficiencyLabel);
	    panelPanel.add(panelsNextButton);


	    //Add to powerPanel
	    addToPanel(powerPanel, powerConsumption, powerConsumptionLabel);
	    addToPanel(powerPanel, feedInTariff, feedInTariffLabel);
	    addToPanel(powerPanel, elecCost, elecCostLabel);
	    powerPanel.add(calculateButton);
	    
	    //Add to resultsPanel
	    resultsPanel.add(resultsLabel);
	   	    
		//Add to tabPanel
	    tabPanel.add(homeText, "Home");
	    tabPanel.add(locationPanel, "Location");
	    tabPanel.add(costPanel, "Cost");
	    tabPanel.add(panelPanel, "Panels");
	    tabPanel.add(powerPanel, "Power");
	    tabPanel.add(resultsPanel, "Results");
	    
	}
	
	//Method for adding Textboxes to panel
	
	//THIS SEEMS TO BE AT FAULT IN CAUSING ERRORS>
	public void addToPanel(VerticalPanel vPanel, TextBox textbox, InlineLabel label)
	{
		vPanel.add(label);
		vPanel.add(textbox);
		vPanel.add(space);
	
	}
	
	//Overloaded method for adding listboxes to panel
	
	public void addToPanel(VerticalPanel vPanel, ListBox listbox, InlineLabel label)
	{	
		vPanel.add(label);
		vPanel.add(listbox);
		vPanel.add(space);
	
	}
	
	/**
	 * Rest of the XML/Server connection stuff is missing here given that a lot of the interface has changed
	 * and I wasn't sure how compatible it would be with what you have.
	 * 
	 **/
}