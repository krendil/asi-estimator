package asi.server;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;




public class GetHistoryServletTest {
	
	GetHistoryServlet servlet;
	
	@Before
	public void setUp() {
		servlet = new GetHistoryServlet();
	}
	
	
	
	@Test
	public void testDataSent() throws Exception{

		String input = "test";

		InputStream query = new ByteArrayInputStream(input.getBytes());
		
		OutputStream response = new ByteArrayOutputStream();

		servlet.processStream(query, response);
		
		assertEquals("","dbLoad called with \""+input+"\".",response.toString());
		
	}
	
	

}
