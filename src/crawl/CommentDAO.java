package crawl;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.emul.org.bson.types.ObjectId;

public class CommentDAO extends BasicDAO<Comment, ObjectId> {
	 public CommentDAO(Datastore ds) {  
	        super(ds);  
	        ds.ensureIndexes();  
	        ds.ensureCaps();  
	    }  
}
