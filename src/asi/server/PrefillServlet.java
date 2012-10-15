package asi.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

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

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import asi.beans.ChatToDatastore;
import asi.beans.Configuration;
import asi.beans.EstimatorException;
import asi.beans.Prefill;
import asi.beans.SplitOutputStream;


/**
 * --Very very messy.
 * 
 * Gets xml request
 * Uses Prefill class to generate a response
 * Sends xml response
 * 
 * @author Steven Turner.
 *
 */
public class PrefillServlet extends HttpServlet { 
	private static final long serialVersionUID = 5514918310492869453L;
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		try {
			
			ByteArrayOutputStream savedData = new ByteArrayOutputStream();
			OutputStream output = new SplitOutputStream(resp.getOutputStream(), savedData);	
			
			processStream(req.getInputStream(), output);
			
			output.close();
			
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
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(input, writer);
		String text = writer.toString();
		
		Prefill pf = new Prefill(text);
		
		Map<String,String> prefills = pf.getResults();
		
		String returnString = "";
		
		if (prefills != null ) {
			returnString = prefills.get("Average Consumption") + "," +
				prefills.get("Feed In") + "," +
				prefills.get("Electricity Cost");

		} else {
			returnString="?";
		}

		output.write(returnString.getBytes(Charset.forName("UTF-8")));

	}

	
}
