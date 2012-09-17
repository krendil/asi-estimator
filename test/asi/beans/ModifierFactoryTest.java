package asi.beans;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ModifierFactoryTest {

	static Element testNodes;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		//Test that the example queries do what they claim
		
		DocumentBuilder db = getDocBuilder();
		
		testNodes = db.parse(new File("test/validquery.xml")).getDocumentElement(); //No exceptions thrown
		
	}

	@Test
	public void testCreateModifier() throws Exception {
		
		for(Node child = testNodes.getFirstChild(); child != null; child = child.getNextSibling()) {
			
			if(child.getNodeType() != Node.ELEMENT_NODE) continue;
			
			ModifierFactory.createModifier(child);
		}
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

}
