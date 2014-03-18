/**
 * 
 */
package guba;

import crawl.*;
import guba.GraphicEdge.EdgeType;

import java.awt.print.Printable;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.Query;

import jxl.write.DateTime;

import org.bson.NewBSONDecoder;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * @author Xiaoqing 创建日期：2013-11-29下午1:32:10 修改日期：
 */
public class GubaGraphic {

	Date startDate;
	Date endDate;

	Set<GraphicNode> nodeSet = new HashSet<GraphicNode>();
	Set<GraphicEdge> edgeSet = new HashSet<GraphicEdge>();

	int topics = 0;
	int comments = 0;
	int publishers = 0;
	int nodes = 0;
	int edges = 0;

	static int topWordsCount = 8;

	//
	// public GubaGraphic(Date startDate, Date endDate) {
	// startDate = this.startDate;
	// endDate = this.endDate;
	// }
	// 加入,String stockcode
	@SuppressWarnings("unchecked")
	public void establishGraphic(Date startDate, Date endDate) {
		// 分词相关的变量
		Map<String, Integer> wordsFrenMaps;
		String commentText = null;

		Mongo mg = null;
		Datastore datastore = null;
		try {
			mg = new Mongo();
			// mg = new Mongo("localhost",27017);
			datastore = new Morphia().createDatastore(mg, Main.stockCode);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		DB db = mg.getDB(Main.stockCode);
		DBCollection topics = db.getCollection("Topics");
		DBCollection comments = db.getCollection("Comments");
		DBCollection publishers = db.getCollection("Publishers");
		com.google.code.morphia.query.Query<Comment> query = datastore
				.createQuery(Comment.class).filter("publishDate >", startDate)
				.filter("publishDate <", endDate);
		List<Comment> xComment = query.asList();

		System.out.println("query.list:" + query.asList().size());
		Iterator<Comment> iterator = xComment.iterator();
		KeyworldsIdentifier keyworldsIdentifier = new KeyworldsIdentifier();
		while (iterator.hasNext()) {
			Comment comment = iterator.next();
			nodeSet.add(comment);
			this.comments++;
			this.nodes++;

			// 分词统计词频
			commentText = commentText + comment.getCommentContent();

			Publisher commentPublisher = comment.getPublisher();
			if (commentPublisher != null) {
				if (nodeSet.contains(commentPublisher)) {
				} else {
					nodeSet.add(commentPublisher);
					this.publishers++;
					this.nodes++;
				}
				edgeSet.add(new GraphicEdge(comment, commentPublisher,
						EdgeType.p2c));
				this.edges++;
			}

			String topicTitle = comment.getTopicTitle();
			if ((datastore.createQuery(Topic.class).filter("title", topicTitle)
					.asList().size()) != 0) {
				Topic topic = datastore.createQuery(Topic.class)
						.filter("title", topicTitle).asList().get(0);

				if (topic != null) {
					if (nodeSet.contains(topic)) {
					} else {
						this.topics++;
						this.nodes++;
						nodeSet.add(topic);
					}
					edgeSet.add(new GraphicEdge(topic, comment, EdgeType.t2c));
					this.edges++;
				}

				Publisher topicPublisher = topic.getPublisher();
				if (topicPublisher != null) {
					if (nodeSet.contains(topicPublisher)) {
					} else {
						nodeSet.add(topicPublisher);
						this.publishers++;
						this.nodes++;
					}
					edgeSet.add(new GraphicEdge(topic, topicPublisher,
							EdgeType.p2t));
					this.edges++;
				}
			}		 
		}
		
		try {
			if (commentText != null) {
				wordsFrenMaps = keyworldsIdentifier.getTextDef(commentText);
				KeyworldsIdentifier.sortSegmentResult(wordsFrenMaps,topWordsCount);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
