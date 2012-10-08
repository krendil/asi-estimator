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
	
	// Change this to your local chromedriver file
	//		download from: http://code.google.com/p/chromium/downloads/list
	private static String chromedriverLocation = "M:\\chromedriver\\chromedriver.exe";
	
	// Change this to your local gwt chrome plugin location
	//		get plugin here in chrome url type > "chrome://plugins/" > hit top right details + > find gwt dev mode pluging location:
	private static String gwtPluginLocation = "C:\\Users\\Shkibr.ASUS-PC\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Extensions\\jpjpnpmbddbjkfaccnmhnkdgjideieim\\1.0.9738_0\\WINNT_x86-msvc/npGwtDevPlugin.dll";
	
	// Change this to the web page you want to test 
	private static String URL = "http://127.0.0.1:8888/Asi_estimator.html?gwt.codesvr=127.0.0.1:9997";
	

	private static WebDriver driver;
	
	// names of the panels
	private static String homePanel = "tabPanel-bar-tab0";
	private static String locationPanel = "tabPanel-bar-tab1";
	private static String costPanel = "tabPanel-bar-tab2";
	private static String panelPanel = "tabPanel-bar-tab3";
	private static String powerPanel = "tabPanel-bar-tab4";
	private static String resultsPanel = "tabPanel-bar-tab5";
  

	@BeforeClass
	public static void initWebDriver() throws IOException {
		// set the path to the chrome driver
		System.setProperty("webdriver.chrome.driver", chromedriverLocation );
		
		// chrome driver will load with no extras, so lets tell it to load with gwtdev plugin
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		String gwtDevPluginPath = "--load-plugin=" + gwtPluginLocation;
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
	public void checkPanels() {
		clickOn(homePanel);
		clickOn(locationPanel);
		clickOn(costPanel);
		clickOn(panelPanel);
		clickOn(powerPanel);
		clickOn(resultsPanel);
	}
 
	@Test
	public void checkButtons() {
		clickOn(homePanel);
		clickOn(locationPanel);
		clickOn("locationNextButton");
		assertEquals( findE("costPanel").isDisplayed(), true );
		clickOn("costNextButton");
		assertEquals( findE("panelPanel").isDisplayed(), true );
		clickOn("panelsNextButton");
		assertEquals( findE("powerPanel").isDisplayed(), true );
	
	}
  
	@Test
	public void prematureCalculate() {
		clickOn(powerPanel);
		assertEquals( "Shouldn't be able to click on \"Calculate\" before valid data has been entered",
				findE("calculateButton").isEnabled(), false);
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
		clickOn("panelDegradation").sendKeys("0.8");
		clickOn("hoursOfSun").sendKeys("5");
		clickOn("inverterEfficiency").sendKeys("98");
		clickOn("panelsNextButton");
		
		clickOn("powerConsumption").sendKeys("30");
		clickOn("panelPower").sendKeys("3");
		clickOn("tariffRates").sendKeys(".65");
		clickOn("feedInTariff").sendKeys(".11");
		clickOn("elecCost").sendKeys(".60");
		
//			clickOn("calculate");
//		clickOn(resultsPanel);
		
		assertEquals( "Should be able to click on \"Calculate\" after valid data has been entered",
				findE("calculateButton").isEnabled(), true);
	}
}