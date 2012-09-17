package asi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import asi.beans.Configuration;
import asi.beans.EstimatorException;

/**
 * Accepts SolarRequest XML documents from clients, generates an estimate,
 * and responds with a SolarResponse XML document.
 * 
 * @author David Osborne
 *
 */
public class DataProcessingServlet extends HttpServlet {

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = -7560967511324311368L;

	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			processStream(req.getInputStream(), resp.getOutputStream());
		} catch (SAXException e) { //Malformed request XML
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (ParserConfigurationException e) { //Probably our fault
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (TransformerFactoryConfigurationError e) { //Who knows?
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (TransformerException e) { //Probably our fault as well
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (EstimatorException e) { //Definitely our fault
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	/**
	 * Process the solar panel configuration XML contained in input,
	 * and generate appropriate response XML.
	 * 
	 * This method is provided to make testing easier.
	 * 
	 * @param input The input stream containing request XML
	 * @return An output stream containing response xml
	 * @throws IOException If either of the streams have an error
	 * @throws SAXException If the input XML is malformed
	 * @throws ParserConfigurationException If there is an error creating the XML parser
	 * @throws TransformerFactoryConfigurationError If there is and error creating the XML transformer
	 * @throws TransformerException If an error occurs during XML transformation
	 * @throws EstimatorException 
	 */
	public void processStream(InputStream input, OutputStream output) throws SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, EstimatorException {
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
		tf.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://asi-estimator.appspot.com/solarresponse.dtd");
		tf.transform(source, result);
	}
}
