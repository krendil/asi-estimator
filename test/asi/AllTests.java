package asi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import asi.server.*;
import asi.beans.*;

@RunWith(Suite.class)
@SuiteClasses({ 
	//System and sanity
	DummyTest.class,
	
	//Modifiers and beans
	BankOfPanelsTest.class,
	ConsumptionTest.class,
	ElectricalGridTest.class,
	FeedInRateTest.class,
	HoursOfSunlightTest.class,
	InverterTest.class,
	ModifierFactoryTest.class,
	SolarArrayTest.class,
	
	//Servlets
	DataProcessingServletTest.class,
	DbHistoryTest.class
})
public class AllTests {

}
