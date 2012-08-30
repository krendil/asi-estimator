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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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
	public void testProcessStream_valid() throws Exception{
		FileInputStream query = new FileInputStream("test/validquery.xml");
		PipedOutputStream output = new PipedOutputStream();
		PipedInputStream response = new PipedInputStream(output);
		servlet.processStream(query, output);
		
		DocumentBuilder db = getDocBuilder();

		db.parse(response); //No exceptions thrown
	}
	
	@Test(expected=SAXException.class)
	public void testProcessStream_garbage() throws FileNotFoundException {
		FileInputStream query = new FileInputStream("test/validquery.xml");
		PipedOutputStream output = new PipedOutputStream();
		servlet.processStream(query, output);
	}
	
	@Test(expected=SAXException.class)
	public void testProcessStream_invalid() throws FileNotFoundException {
		FileInputStream query = new FileInputStream("test/validquery.xml");
		PipedOutputStream output = new PipedOutputStream();
		servlet.processStream(query, output);
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
