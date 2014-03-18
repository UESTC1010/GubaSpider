package crawl;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.emul.org.bson.types.ObjectId;

public class TopicDAO extends BasicDAO<Topic, ObjectId> {
	 public TopicDAO(Datastore ds) {  
	        super(ds);  
	        ds.ensureIndexes();  
	        ds.ensureCaps();  
	    }  
}