package asi.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

// Download the files from -> http://seleniumhq.org/download/
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Got a lot of help and examples from this website.
 * {@link http://c.gwt-examples.com} 
 * 
 * 
 */

/**
 * 
 * @author Steven Turner
 * 
 * Unit testing web page with JUnit and Selenium.
 * 
 * Uses Google Chrome web browser.
 * 
 * Project must reference the Selenium libraries:
 * http://seleniumhq.org/download/
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class Asi_GuiTest extends TestCase {
	
	// Change this to the web page you want to test 
	private static String URL =
			/*   //<-- Comment toggler, add leading / to enable first section
			"http://asi-estimator.appspot.com/"
			/*/
			"http://127.0.0.1:8888/Asi_estimator.html?gwt.codesvr=127.0.0.1:9997"
			//*/
	;

	private static WebDriver driver;
	
	// names of the panels
	private static String homePanel = "tabPanel-bar-tab0";
	private static String locationPanel = "tabPanel-bar-tab1";
	private static String costPanel = "tabPanel-bar-tab2";
	private static String panelPanel = "tabPanel-bar-tab3";
	private static String powerPanel = "tabPanel-bar-tab4";
	private static String resultsPanel = "tabPanel-bar-tab5";
	
	private final String BAD_INPUT = "z";
	private final String NEGATIVE_INPUT = "-0.4";
  

	@BeforeClass
	public static void initWebDriver() throws IOException {
		
		// set the path to the chrome driver
		System.setProperty("webdriver.chrome.driver", Asi_GuiTestLocals.chromedriverLocation );
		
		// chrome driver will load with no extras, so lets tell it to load with gwtdev plugin
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		String gwtDevPluginPath = "--load-plugin=" + Asi_GuiTestLocals.chromeGwtPluginLocation;
		capabilities.setCapability("chrome.switches", Arrays.asList(gwtDevPluginPath));
		
		// init driver with GWT Dev Plugin
		driver = new ChromeDriver(capabilities);

	}

	@AfterClass
	public static void theEnd() {
		driver.quit();
	}

	@Before
	public void before() {
		System.out.print("\n---START NEW TEST---");
		
		// navigate to gwt app
		String pathToGwtApp = URL;
		driver.get(pathToGwtApp);
		checkElement(10, "tabPanel");
	}

	@After
	public void after() {
		System.out.println("\n---END CURRENT TEST---");
	}

	
	/**
	 * Simulates a click action.
	 * default 2 second wait timer.
	 * 
	 * @param id
	 * @return
	 */
	private WebElement clickOn( String id ) {
		WebElement element =  checkElement( 2, id );
		element.click();
		System.out.print(" --> Clicked.");
		
		return element;
	}
  
	
	/**
	 * waits for id to be enabled then clicks on it
	 * 
	 * @param seconds
	 * @param id
	 * @return
	 */
	private WebElement checkElement(  int seconds, final String id ) {
		System.out.print("\nSearching: gwt-debug-"+ id + ".");
		
		(new WebDriverWait(driver, seconds)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				WebElement element = findE(id);
				return element.isEnabled();
				}
			});
		
		System.out.print(" --> Enabled.");
		return findE(id);
	}
	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private WebElement findE(String id) {
		return driver.findElement(By.id("gwt-debug-"+ id));
	}
  
	@Test
	public void displaysHomePanel() {
		assertEquals("Not displaying home panel at startup.", true, findE("homePanel").isDisplayed() );
	}
	
	@Test
	public void checkPanels() {
		clickOn(homePanel);
		assertEquals( true, findE("homePanel").isDisplayed() );
		clickOn(locationPanel);
		assertEquals( true, findE("locationPanel").isDisplayed() );
		clickOn(costPanel);
		assertEquals( true, findE("costPanel").isDisplayed() );
		clickOn(panelPanel);
		assertEquals( true, findE("panelPanel").isDisplayed() );
		clickOn(powerPanel);
		assertEquals( true, findE("powerPanel").isDisplayed() );
	}
 
	@Test
	public void checkButtons() {
		clickOn(locationPanel);
		clickOn("locationNextButton");
		assertEquals( true, findE("costPanel").isDisplayed() );
		clickOn("costNextButton");
		assertEquals( true, findE("panelPanel").isDisplayed() );
		clickOn("panelsNextButton");
		assertEquals( true, findE("powerPanel").isDisplayed() );
	
	}
  
	@Test
	public void prematureCalculate() {
		clickOn(powerPanel);
		assertEquals( "Shouldn't be able to click on \"Calculate\" before valid data has been entered",
				false, findE("calculateButton").isEnabled() );
	}
	
	@Test
	public void timelyCalculate() {
		clickOn(costPanel);
		
		clickOn("panelCost").sendKeys("1000");
		clickOn("installCost").sendKeys("3000");
		clickOn("inverterCost").sendKeys("1000");
		clickOn("costNextButton");
	
		clickOn("nPanels").sendKeys("5");
		clickOn("panelWattage").sendKeys("11");
		clickOn("hoursOfSun").sendKeys("5");
		clickOn("inverterEfficiency").sendKeys("98");
		clickOn("panelsNextButton");
		
		
		clickOn("powerConsumption").sendKeys("30");
		clickOn("feedInTariff").sendKeys(".11");
		clickOn("elecCost").sendKeys(".60");
		
		assertEquals( "Should be able to click on \"Calculate\" after valid data has been entered",
				true, findE("calculateButton").isEnabled() );
	}
	
	@Test
	public void inputPanelCost() {
		String panel = costPanel;
		String box = "panelCost";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void inputInstallCost() {
		String panel = costPanel;
		String box = "installCost";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void inputInverterCost() {
		String panel = costPanel;
		String box = "inverterCost";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void inputnPanels() {
		String panel = panelPanel;
		String box = "nPanels";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void inputPanelWattage() {
		String panel = panelPanel;
		String box = "panelWattage";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void inputHoursOfSun() {
		String panel = panelPanel;
		String box = "hoursOfSun";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void inputInverterEfficiency() {
		String panel = panelPanel;
		String box = "inverterEfficiency";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void inputPowerConsumption() {
		String panel = panelPanel;
		String box = "powerConsumption";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void inputFeedInTariff() {
		String panel = panelPanel;
		String box = "feedInTariff";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void inputElecCost() {
		String panel = panelPanel;
		String box = "elecCost";
		String badInput = BAD_INPUT;
		validateTextbox(panel, box, badInput);
	}
	

	
	@Test
	public void negPanelCost() {
		String panel = costPanel;
		String box = "panelCost";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void negInstallCost() {
		String panel = costPanel;
		String box = "installCost";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void negInverterCost() {
		String panel = costPanel;
		String box = "inverterCost";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void negnPanels() {
		String panel = panelPanel;
		String box = "nPanels";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void negPanelWattage() {
		String panel = panelPanel;
		String box = "panelWattage";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void negHoursOfSun() {
		String panel = panelPanel;
		String box = "hoursOfSun";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void negInverterEfficiency() {
		String panel = panelPanel;
		String box = "inverterEfficiency";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void negPowerConsumption() {
		String panel = panelPanel;
		String box = "powerConsumption";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void negFeedInTariff() {
		String panel = panelPanel;
		String box = "feedInTariff";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	@Test
	public void negElecCost() {
		String panel = panelPanel;
		String box = "elecCost";
		String badInput = NEGATIVE_INPUT;
		validateTextbox(panel, box, badInput);
	}
	
	
	
	private void validateTextbox(String panel, String box, String badInput) {
		clickOn(costPanel);
		
		clickOn("panelCost").sendKeys("1000");
		clickOn("installCost").sendKeys("3000");
		clickOn("inverterCost").sendKeys("1000");
		clickOn("costNextButton");
	
		clickOn("nPanels").sendKeys("5");
		clickOn("panelWattage").sendKeys("11");
		clickOn("hoursOfSun").sendKeys("5");
		clickOn("inverterEfficiency").sendKeys("98");
		clickOn("panelsNextButton");
		
		clickOn("powerConsumption").sendKeys("30");
		clickOn("feedInTariff").sendKeys(".11");
		clickOn("elecCost").sendKeys(".60");
		
		clickOn(panel);
		clickOn(box).clear();
		clickOn(box).sendKeys(badInput);
		
		assertEquals( box + " should not allow \"" + badInput + "\"",
				false, findE("calculateButton").isEnabled() );
	}
}