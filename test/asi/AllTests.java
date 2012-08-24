package asi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import asi.server.DataProcessingServletTest;
import asi.beans.LocationTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	DataProcessingServletTest.class,
	DummyTest.class,
	LocationTest.class
})
public class AllTests {

}
