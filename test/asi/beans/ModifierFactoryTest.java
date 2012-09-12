package asi.beans;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import asi.TestUtils;

public class ModifierFactoryTest {

	static Element testNodes;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		//Test that the example queries do what they claim
		
		testNodes = TestUtils.getDocFromFile("test/validquery.xml").getDocumentElement(); //No exceptions thrown
		
	}

	@Test
	public void testCreateModifier() throws Exception {
		
		for(Node child = testNodes.getFirstChild(); child != null; child = child.getNextSibling()) {
			
			if(child.getNodeType() != Node.ELEMENT_NODE) continue;
			
			ModifierFactory.createModifier(child);
		}
	}


}
