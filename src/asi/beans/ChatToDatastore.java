package asi.beans;

import java.util.HashMap;
import java.util.Map;
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

// text object for large strings
import com.google.appengine.api.datastore.Text;

public class ChatToDatastore {
	
	
	private static final Logger logger = Logger.getLogger(ChatToDatastore.class.getCanonicalName());
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static MemcacheService keycache = MemcacheServiceFactory.getMemcacheService();
	
	// DATASTORE INDEXES
	private static final String KIND_HISTORY = "History"; // table
		private static final String PROP_CONTENT = "Content"; // column or attribute
	
	private static final String KIND_PREFILL = "Prefill";

		
	/**
	 * gets content from the database, returning to calling method.
	 * @param content
	 * @return
	 * @throws EntityNotFoundException
	 */
	public static String dsLoadHistory( Object index ) throws EntityNotFoundException {
		//key must be long in this instance.
		if (index.getClass() == Long.class) {
			return getAttribute( (Long)index, KIND_HISTORY, PROP_CONTENT );
		} else if (index.getClass() == String.class ) {
			return getAttribute( (String)index, KIND_HISTORY, PROP_CONTENT );
		}
		return null;
	}
	
	
	/**
	 * save content to database, returning unique id
	 * @param content
	 * @return
	 */
	public static String dsSaveHistory( String content ) {
		Text bigContent = new Text(content);
		return setAttribute( bigContent, KIND_HISTORY, PROP_CONTENT );
	}
	
	
	/**
	 * Query datastore and return property value as string.
	 * @param index
	 * @param kind
	 * @param property
	 * @return
	 * @throws EntityNotFoundException 
	 */
	private static String getAttribute(Object id, String kind, String property) throws EntityNotFoundException{
		logger.log(Level.INFO, "Search the entity");
		Entity entity = datastore.get( makeKey(id, kind) );
		return ( ( Text )entity.getProperty( property ) ).getValue();
	}
	
	/**
	 * return all properties of entity
	 * @param id
	 * @param kind
	 * @return
	 * @throws EntityNotFoundException
	 */
	private static Map<String, Object> getAttributes(Object id, String kind ) throws EntityNotFoundException {
		logger.log(Level.INFO, "Search the entity");
		Entity entity = datastore.get( makeKey(id, kind) );
		return entity.getProperties();
	}
	
	
	/**
	 * Makes a key from an Object.class type
	 * 
	 * @param id
	 * @param kind
	 * @return
	 * @throws EntityNotFoundException
	 */
	private static Key makeKey(Object id, String kind) throws EntityNotFoundException {
		Key key;
		
		if ( Long.class == id.getClass() ) {
			key = KeyFactory.createKey( kind , Long.class.cast(id) );
		} else if (String.class == id.getClass() ) {
			key = KeyFactory.createKey( kind , String.class.cast(id) );
		} else {
			key = KeyFactory.createKey( kind , "Bad key id type");
			throw new EntityNotFoundException(key);
		}
		
		return key;
	}


	/**
	 * Write hashmap (columnName,Data) to entity (index of a given table)
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	private static Entity writeAttributes(Entity entity, Map<String, Object> data ) {
		Key key = entity.getKey();
		
		// add data to the entity
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			String property = entry.getKey();
		    Object content = entry.getValue();
		    entity.setProperty(property, ( content.getClass().cast(content) ) );
		}
		
		logger.log(Level.INFO, "Saving entity");
	    
		// the write
		Transaction txn = datastore.beginTransaction();
	    try {
	    	datastore.put(entity);
			txn.commit();
			key = entity.getKey();
			System.out.println("Writing to datastore: "+key.toString());
			
	    } finally {
		  if (txn.isActive()) {
		    txn.rollback();
		  } else {
			addToCache(key, entity);
		  }
	    }
		
		return entity;
	}
	
	/**
	 * Saves a new object into the database, returning its key.
	 * 
	 * @param content
	 * @param table
	 * @param attribute
	 * @return
	 */
	private static String setAttribute( Object content, String kind, String property ){
		Entity entity = new Entity( kind );
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(property, content);
		
		writeAttributes( entity, data );
		
	    // rip the id out of the key and return
	    return String.valueOf(entity.getKey().getId());
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

	// pulls the prefill data from the datastore
	public static Map<String, String> getPrefill(String location) throws EntityNotFoundException {
		Map<String, Object> incData = getAttributes(location, KIND_PREFILL);
		Map<String, String> outData = new HashMap<String, String>();
		
		// I'm sure there is an easier way of converting the types here.
		for (Map.Entry<String, Object> entry : incData.entrySet()) {
			String property = entry.getKey();
		    Object content = entry.getValue();
		    outData.put(property, (String)content);
		}
		
		return outData;
	}
	
	/**
	 * put prefills in datastore
	 * @param location
	 * @param incData
	 */
	public static void setPrefill(String location, Map<String,String> data) {
		Key key = KeyFactory.createKey( KIND_PREFILL , location );
		Entity entity = new Entity(key);
		
		Map<String,Object> outData = new HashMap<String,Object>();
		
		for (Map.Entry<String, String> entry : data.entrySet()) {
			String property = entry.getKey();
		    Object content = entry.getValue();
		    outData.put(property, (String)content);
		}
		
		writeAttributes(entity, outData);
	}
	
}
