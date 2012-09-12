package asi.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


public class DbHistory extends HttpServlet  {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	private final String DB_TABLE = "Results";
	private final String DB_CONTENT = "Content";
	
	private final String SAVE = "save";
	private final String LOAD = "load";
	
	private final String ERROR = "There was an error with the supplied arguements.";
	private final String E_ERROR = "Not found in datastore.";
	
	private String variable;
	
 

	/**
	 * auto-generated
	 */
	private static final long serialVersionUID = 5514918310492869453L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		variable = null;
		
		Enumeration<String> parameterName = req.getParameterNames();
		
		if ( !validate(parameterName) ) {
			
			PrintWriter out = resp.getWriter();
			out.println( ERROR );
			
		} else {
			try {
				makeResponse( resp, queryDatabase( variable,
						(String)req.getAttribute( variable ) ) );
				
			} catch ( EntityNotFoundException e ) {
				PrintWriter out = resp.getWriter();
				out.println( E_ERROR );
			}
			
		}
	}

	

	/**
	 * 
	 * @param resp
	 * @param content
	 * @throws IOException
	 */
	private void makeResponse(HttpServletResponse resp, String content) throws IOException {
		PrintWriter out;

		out = resp.getWriter();
		out.print(content);

	}



	/**
	 * Decides to either save to the database or retrieve from the database.
	 * @param variable
	 * @param content
	 * @return
	 * @throws EntityNotFoundException 
	 */
	private String queryDatabase(String variable, String content ) throws EntityNotFoundException {
		
		String response = null;
		
		if ( variable == SAVE ) {
			response = dbSave(content);

		} else {
			response = dbLoad(content); 
		}
		
		return response;
	}




	/**
	 * gets content from the database, returning to calling method.
	 * @param content
	 * @return
	 * @throws EntityNotFoundException 
	 */
	private String dbLoad( String index ) throws EntityNotFoundException {
		
		Key key = KeyFactory.createKey(DB_TABLE, index);
		
		Entity entity = datastore.get(key);
		
		return entity.getProperty(DB_CONTENT).toString();		
	}




	/**
	 * save content to database, returning unique id
	 * @param content
	 * @return
	 */
	private String dbSave(String content) {
		
		Entity results = new Entity( DB_TABLE );
		
		results.setProperty( DB_CONTENT , content);
		
		datastore.put( results );
		
		return results.getKey().toString();

	}




	/**
	 * performs the sanity checks and verifies that the supplied parameter exists and is usable.
	 * @param parameterName
	 * @return
	 */
	private boolean validate( Enumeration<String> parameterName ) {

		if ( !parameterName.hasMoreElements() ) return false;
		
		variable = parameterName.nextElement();
		if ( variable == SAVE || variable == LOAD ) return true;
		
		return false;
	}
}
