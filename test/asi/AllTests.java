package asi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import asi.server.*;
import asi.beans.*;
import asi.client.*;

@RunWith(Suite.class)
@SuiteClasses({ 
	//System and sanity
	DummyTest.class,
	
	//Modifiers and beans
	BankOfPanelsTest.class,
	ChatToDatastoreTest.class,
	ConsumptionTest.class,
	ElectricalGridTest.class,
	FeedInRateTest.class,
	HoursOfSunlightTest.class,
	InverterTest.class,
	ModifierFactoryTest.class,
	SolarArrayTest.class,
	SplitOutputStreamTest.class,
	
	//Servlets
	DataProcessingServletTest.class,
	//DbHistoryTest.class
	
	//Client
	PercentBoxTest.class
})
public class AllTests {

}
