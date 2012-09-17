package asi.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import asi.beans.ChatToDatastore;

import com.google.appengine.api.datastore.EntityNotFoundException;


public class GetHistoryServlet extends HttpServlet  {
 

	/**
	 * auto-generated
	 */
	private static final long serialVersionUID = 5514918310492869453L;

	/**
	 * Queries datastore and and reponds with xml or error.
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Check for error getting XML from datastore
		try {
			processStream( req.getInputStream(), resp.getOutputStream() );
			
		} catch ( EntityNotFoundException e ) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"The requested information does not exist.");
		}
	}
	
	
	/**
	 * actual processing
	 * @param inputStream
	 * @param outputStream
	 * @throws IOException
	 */
	public void processStream(InputStream inputStream,
			OutputStream outputStream) throws IOException, EntityNotFoundException {
		
		String key = getKey( inputStream ); // may throw EntityNotFoundException
		
		String xml = getXML(key);
		
		sendResponse( outputStream, xml);
	}


	/**
	 * Query datastore for this item
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private String getXML(String key) throws EntityNotFoundException {
		String xml = null;
		
		xml = ChatToDatastore.dsLoadHistory( key );
		
		return xml;
	}



	/**
	 * writes xml response to response object.
	 * @param resp
	 * @param xml
	 * @throws IOException
	 */
	private void sendResponse(OutputStream resp, String xml) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(resp);
		out.write(xml);
		out.close();
	}

	
	/**
	 * Get stream and query database, returning the database's response
	 * @param req
	 * @return
	 * @throws IOException
	 */
	private String getKey(InputStream req) throws IOException {
		
		String key = "";
		
        BufferedReader in = new BufferedReader(
        						new InputStreamReader(req));
        String inputLine;
        
        while ((inputLine = in.readLine()) != null) 
            key += inputLine;
        in.close();
        
        return key;
		
	}


}
