package asi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import asi.server.DataProcessingServletTest;
import asi.beans.*;

@RunWith(Suite.class)
@SuiteClasses({ 
	//System and sanity
	DummyTest.class,
	
	//Modifiers and beans
	BankOfPanelsTest.class,
	ElectricalGridTest.class,
	HoursOfSunlightTest.class,
	LocationTest.class,
	SolarArrayTest.class,
	
	//Servlets
	DataProcessingServletTest.class
})
public class AllTests {

}
