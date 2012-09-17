package asi.beans;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ChatToDatastore {
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private final String HISTORY = "History";
	private final String HISTORY_CONTENT = "Content";
	
	

	/**
	 * gets content from the database, returning to calling method.
	 * @param content
	 * @return
	 * @throws EntityNotFoundException 
	 */
	public String dsLoadHistory( String index ) throws EntityNotFoundException {
		
		Key key = KeyFactory.createKey( HISTORY , index );
		
		Entity entity = datastore.get( key );
		
		return entity.getProperty( HISTORY_CONTENT ).toString();
		
//		return "dbLoad called with \""+index+"\".";
	}



	/**
	 * save content to database, returning unique id
	 * @param content
	 * @return
	 */
	public String dsSaveHistory(String content) {
		
		Entity results = new Entity( HISTORY );
		
		results.setProperty( HISTORY_CONTENT , content);
		
		datastore.put( results );
		
		return results.getKey().toString();
		
//		return "dbLoad called with \""+content+"\".";

	}
	
	
}
