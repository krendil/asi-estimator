package asi.server;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DbHistoryTest {

	private final static String SAVE = "save";
	private final static String LOAD = "load";
	private  static String BAD_VARIABLE = "laskjf";
	
	private  static String CONTENT = "This is the content.";
	
	private  static String ERROR = "Error with arguements.";
	
	static HttpServletRequest req;
	static HttpServletRequest badReq;
	
	HttpServletResponse resp;
	
	DbHistory dbHistory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		//req.setAttribute( SAVE , CONTENT );
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
//		resp.reset();
//		dbHistory = new DbHistory();
//		badReq.setAttribute( BAD_VARIABLE, CONTENT );
	}

	@After
	public void tearDown() throws Exception {
	}
	
	

	@Test
	public void test() {
		
//		try {
//			dbHistory.doPost(badReq, resp);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		assertEquals("Bad parameter pickup.",ERROR,resp.toString());
		
		fail ("I HAVE NO IDEA WHAT IM DOING!");
	}

}
