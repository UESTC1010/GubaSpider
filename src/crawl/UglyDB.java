package crawl;

import java.io.File;
import java.io.FileOutputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;

public class UglyDB {
	static Mongo mongo = null;
	static Morphia morphia = null;
	static Datastore ds = null;
	static TopicDAO topicDAO = null;
	static CommentDAO CommentDAO = null;
	static PublisherDAO PublisherDAO = null;
	
	public static void initDB() {	
		try {
			mongo = new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		morphia = new Morphia();
		ds = morphia.createDatastore(mongo, "600010");
		
		morphia.map(Topic.class);
		morphia.map(Comment.class);
		morphia.map(Publisher.class);
		
		topicDAO = new TopicDAO(ds);
		CommentDAO = new CommentDAO(ds);
		PublisherDAO = new PublisherDAO(ds);
	}
	
	
	static Map<String, Topic> topicSet = new Hashtable<String, Topic>();
	static void putTopic(String title, Topic tempTopic) {
		topicSet.put(title, tempTopic);
	}
	
	static HashMap<String, Publisher> publisherSet = new HashMap<String, Publisher>();
	static Publisher GetOrNewPublisher(String name) {
		if (name != null) {
			name = name.replaceAll("&nbsp;", "");
			name = name.replaceAll("&amp;", "");
		}

		if (publisherSet.containsKey(name)) {
			return publisherSet.get(name);
		}
		Publisher tempPublisher = new Publisher();
		tempPublisher.name = name;
		publisherSet.put(name, tempPublisher);

		return publisherSet.get(name);
	}

	
	static HashMap<String, Comment> commentSet = new HashMap<String, Comment>();
	static void putComment(String contentkey, Comment comment) {
		if (commentSet.containsKey(contentkey))
			return;
		commentSet.put(contentkey, comment);
	}
	
	static void saveUglyDB(){
		Query query = null;
		Iterator it = null;
		
		it = topicSet.values().iterator(); // 获得一个迭代子 
		while(it.hasNext()) { 
			Topic obj = (Topic) it.next(); // 得到下一个元素 
			query = ds.createQuery(Topic.class).field("title").equal(obj.getTitle());	
			if(topicDAO.exists(query)){
				topicDAO.deleteByQuery(query);
			}
			topicDAO.save(obj);
		} 
		
		it = publisherSet.values().iterator(); // 获得一个迭代子 
		while(it.hasNext()) { 
			Publisher obj = (Publisher) it.next(); // 得到下一个元素 
			query = ds.createQuery(Topic.class).field("title").equal(obj.getName());	
			if(PublisherDAO.exists(query)){
				PublisherDAO.deleteByQuery(query);
			}
			PublisherDAO.save(obj);
		}
		
		it = commentSet.values().iterator(); // 获得一个迭代子 
		while(it.hasNext()) { 
			Comment obj = (Comment) it.next(); // 得到下一个元素 
			query = ds.createQuery(Topic.class).filter("title", obj.getTopicTitle())
												.filter("publishDate",obj.getPublishDate());	
			if(CommentDAO.exists(query)){
				CommentDAO.deleteByQuery(query);
			}
			CommentDAO.save(obj);
		}
	}

	public static void clear() {
		topicSet.clear();
		publisherSet.clear();
		commentSet.clear();
	}
	
/*
	static void saveUglyDB() {
		try {

			// save topicSet 2
			XStream xs = new XStream(new DomDriver());
			FileOutputStream fs = new FileOutputStream("topicSet.txt");
			xs.toXML(topicSet, fs);
			fs.flush();
			fs.close();

			// save publisherSet 2
			fs = new FileOutputStream("publisherSet.txt");
			xs.toXML(publisherSet, fs);
			fs.flush();
			fs.close();

			// save commentSet 2
			fs = new FileOutputStream("commentSet.txt");
			xs.toXML(commentSet, fs);
			fs.flush();
			fs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}
