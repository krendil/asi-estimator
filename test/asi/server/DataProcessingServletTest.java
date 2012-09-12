package asi.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import asi.TestUtils;
import asi.beans.EstimatorException;

public class DataProcessingServletTest {
	
	DataProcessingServlet servlet;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		//Test that the example queries do what they claim
		
		DocumentBuilder db = TestUtils.getDocBuilder();
		
		db.parse(new File("test/validquery.xml")); //No exceptions thrown

		try {
			db.parse(new File("test/garbagequery.xml"));
			assert false; //Error - invalid XML looks valid!
		} catch (SAXException e) {
			//Ok, invalid XML is invalid
		}
		
		try {
			db.parse(new File("test/invalidquery.xml"));
			assert false; //Error - invalid XML looks valid!
		} catch (SAXException e) {
			//Ok, invalid XML is invalid
		}
	}
	
	@Before
	public void setUp() {
		servlet = new DataProcessingServlet();
	}
	
	@Test
	public void testProcessStream_valid() throws Exception{
		
		FileInputStream query = new FileInputStream("test/validquery.xml");
		OutputStream output = new ByteArrayOutputStream();
		servlet.processStream(query, output);
		InputStream response = new ByteArrayInputStream(((ByteArrayOutputStream) output).toByteArray());
		
		DocumentBuilder db = TestUtils.getDocBuilder();     
		db.parse(response); //No exceptions thrown
	}
	
	@Test(expected=SAXException.class)
	public void testProcessStream_garbage() throws SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, EstimatorException {
		
		FileInputStream query = new FileInputStream("test/garbagequery.xml");
		OutputStream output = new ByteArrayOutputStream();
		servlet.processStream(query, output);	
		InputStream response = new ByteArrayInputStream(((ByteArrayOutputStream) output).toByteArray());
		
		DocumentBuilder db = TestUtils.getDocBuilder();
		db.parse(response);
	}
	
	@Test(expected=EstimatorException.class)
	public void testProcessStream_invalid() throws SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, EstimatorException {
		
		FileInputStream query = new FileInputStream("test/invalidquery.xml");	
		OutputStream output = new ByteArrayOutputStream();
		servlet.processStream(query, output);
		InputStream response = new ByteArrayInputStream(((ByteArrayOutputStream) output).toByteArray());
		
		DocumentBuilder db = TestUtils.getDocBuilder();
		db.parse(response);
	}
	


}
