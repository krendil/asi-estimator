package asi;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class TestUtils {

	public static final double EPSILON = 1E-6;
	public static final BigDecimal B_EPSILON = new BigDecimal(EPSILON);
	
	public static DocumentBuilder getDocBuilder(boolean validating) throws ParserConfigurationException{
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		df.setValidating(validating);
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
	
	public static DocumentBuilder getDocBuilder() throws ParserConfigurationException {
		return getDocBuilder(true);
	}
	
	public static Document getDocFromFile(String filename) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilder db = getDocBuilder();
		
		return db.parse(new File(filename));
	}
	
	public static Element getNodeFromString(String str) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder bd = getDocBuilder(false);
		
		Document doc = bd.parse(new ByteArrayInputStream(str.getBytes()));
		
		return doc.getDocumentElement();
	}
	
	public static boolean compareBigDecs(BigDecimal a, BigDecimal b) {

		//Check if the difference is less than the epsilon, i.e. they are roughly equal
		return a.subtract(b).abs().compareTo(TestUtils.B_EPSILON) < 0;
	}
	
}
