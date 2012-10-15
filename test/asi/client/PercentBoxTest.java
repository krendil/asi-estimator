package asi.client;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import asi.TestUtils;

public class PercentBoxTest extends GWTTestCase {

	PercentBox box;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	
	public void testGetValue_Normal() {
		box = new PercentBox();
		box.setText("93");
		
		assertEquals(box.getValue(), 0.93, TestUtils.EPSILON);
	}
	
	
	public void testGetValue_Invalid() {
		box = new PercentBox();
		box.setText("blah");
		assertNull(box.getValue());
	}
	
	public void testGetValue_Zero() {
		box = new PercentBox();
		box.setText("0");
		assertEquals(box.getValue(), 0.0, TestUtils.EPSILON);
	}

	@Override
	public String getModuleName() {
		return "asi.Asi_estimator";
	}

}
