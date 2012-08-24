package asi.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DataProcessingServletTest {
	
	DataProcessingServlet servlet;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		//Test that the example queries do what they claim
		
		DocumentBuilder db = getDocBuilder();
		
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
	public void testProcessCall_valid() throws Exception{
		String result = servlet.processCall(readFromFile("test/validquery.xml"));
		
		DocumentBuilder db = getDocBuilder();
		
				//Welcome to Java
		db.parse(new InputSource(new ByteArrayInputStream(result.getBytes("utf-8")))); //No exceptions thrown
	}
	
	@Test(expected=SAXException.class)
	public void testProcessCall_garbage() throws FileNotFoundException {
		String result = servlet.processCall(readFromFile("test/garbagequery.xml"));
	}
	
	@Test(expected=SAXException.class)
	public void testProcessCall_invalid() throws FileNotFoundException {
		String result = servlet.processCall(readFromFile("test/invalidquery.xml"));
	}
	
	private static DocumentBuilder getDocBuilder() throws ParserConfigurationException{
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		df.setValidating(true);
		DocumentBuilder db = df.newDocumentBuilder();
		db.setErrorHandler(new ErrorHandler(){
			@Override
			public void error(SAXParseException arg0) throws SAXException {
				throw new SAXException(arg0);
			}
			@Override
			public void fatalError(SAXParseException arg0) throws SAXException {
				throw new SAXException(arg0);
			}
			@Override
			public void warning(SAXParseException arg0) throws SAXException {
				throw new SAXException(arg0);
			}		
		});
		return db;
	}
	
	/**
	 * Read files into strings (by scanning until end of input)
	 * @param filename
	 * @return The contents of the file in a string
	 * @throws FileNotFoundException
	 */
	private static String readFromFile(String filename) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(filename));
		String ret = sc.useDelimiter("\\Z").next();
		sc.close();
		return ret;
	}

}
