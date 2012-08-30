package asi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class DataProcessingServlet extends HttpServlet {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = -7560967511324311368L;

	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		processStream(req.getInputStream(), resp.getOutputStream());
	}
	
	/**
	 * Process the solar panel configuration XML contained in input,
	 * and generate appropriate response XML.
	 * 
	 * This method is provided to make testing easier.
	 * 
	 * @param input The input stream containing request XML
	 * @return An output stream containing response xml
	 */
	public void processStream(InputStream input, OutputStream output) {
		//Read XML into Doc
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(input);
		
		//Create Configuration from Doc
		Configuration conf = new Configuration();
		conf.parseDocument(doc);
		//Get Info from Configuration
		Document estimate = conf.generateEstimate();
		
		//Create XML from estimate Doc
		Source source = new DOMSource(estimate);
		Result result = new StreamResult(output);
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.transform(source, result);		
	}
}
