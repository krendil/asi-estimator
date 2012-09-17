package asi.beans;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class ChatToDatastoreTest {
	ChatToDatastore datastore;
	
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		datastore = new ChatToDatastore();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test (expected = EntityNotFoundException.class)  
	public void test() throws EntityNotFoundException {
		datastore.dsLoadHistory("asg9u");
	}

}
