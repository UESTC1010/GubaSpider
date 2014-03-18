package crawl;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.emul.org.bson.types.ObjectId;

public class PublisherDAO extends BasicDAO<Publisher, ObjectId> {
	 public PublisherDAO(Datastore ds) {  
	        super(ds);  
	        ds.ensureIndexes();  
	        ds.ensureCaps();  
	    }  
}
