package asi.beans;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class ChatToDatastore {
	
	
	private static final Logger logger = Logger.getLogger(ChatToDatastore.class.getCanonicalName());
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static MemcacheService keycache = MemcacheServiceFactory.getMemcacheService();
	
	private static final String HISTORY = "History";
	private static final String HISTORY_CONTENT = "Content";
	
	

	/**
	 * gets content from the database, returning to calling method.
	 * @param content
	 * @return
	 * @throws EntityNotFoundException 
	 */
	public static String dsLoadHistory( String index ) throws EntityNotFoundException {
		
		logger.log(Level.INFO, "Search the entity");
		
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
	public static String dsSaveHistory(String content) {
		
		Entity results = new Entity( HISTORY );
		
		results.setProperty( HISTORY_CONTENT , content);
		
		datastore.put( results );
		
		logger.log(Level.INFO, "Saving entity");
		    
		Key key = results.getKey();
	    Transaction txn = datastore.beginTransaction();
	    try {
	      datastore.put(results);
		  txn.commit();
	    } finally {
		  if (txn.isActive()) {
		    txn.rollback();
		  } else {
			addToCache(key, results);
		  }
	    }
		
		return results.getKey().toString();
		
//		return "dbLoad called with \""+content+"\".";

	}
	
	
	
	/**
	 *  *** written by google ***
	 * Adds the entity to cache
	 * 
	 * @param key
	 *          : key of the entity
	 * @param entity
	 *          : Entity being added
	 */
  public static void addToCache(Key key, Entity entity) {
    logger.log(Level.INFO, "Adding entity to cache");
    keycache.put(key, entity);
  }
	
}
