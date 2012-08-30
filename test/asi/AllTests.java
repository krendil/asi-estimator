package asi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import asi.server.DataProcessingServletTest;
import asi.beans.BankOfPanelsTest;
import asi.beans.LocationTest;
import asi.beans.SolarArrayTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	DataProcessingServletTest.class,
	DummyTest.class,
	LocationTest.class,
	BankOfPanelsTest.class,
	SolarArrayTest.class
	
})
public class AllTests {

}
