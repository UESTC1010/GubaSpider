/**
 * 
 */
package guba;

import guba.GraphicEdge;
import guba.Main;
import guba.GraphicEdge.EdgeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.IOException;
import java.io.StringReader;
import java.net.UnknownHostException;

import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.core.IKSegmenter;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import crawl.Comment;
import crawl.Publisher;
import crawl.Topic;

/**
 * @author Xiaoqing 创建日期：2014-3-17上午10:48:26 修改日期：
 */
public class KeyworldsIdentifier {

	/*
	 * 对传入的参数进行分词，并统计好每个次的频率
	 * IKSegmenter是分词的主要类，其参数分别是分词的句子或者文章，后面的参数是是否开启智能模式，不开启就按最小词义分。
	 * 分词的结果是Lexeme这个类，用其中的getLexemeText()方法就能取出相关的分词结果。
	 */
	public Map getTextDef(String text) throws IOException {
		Map<String, Integer> wordsFren = new HashMap<String, Integer>();
		IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(text), true);
		Lexeme lexeme;
		while ((lexeme = ikSegmenter.next()) != null) {
			if (lexeme.getLexemeText().length() > 1) {
				if (wordsFren.containsKey(lexeme.getLexemeText())) {
					wordsFren.put(lexeme.getLexemeText(),
							wordsFren.get(lexeme.getLexemeText()) + 1);
				} else {
					wordsFren.put(lexeme.getLexemeText(), 1);
				}
			}
		}
		return wordsFren;
	}

	/*
	 * 计算词频将分词结果和出现次数放到一个map结构中，map的value对应了词的出现次数。这里注意一下，只记录两个字及两个字以上的分词结果。
	 * 主要对分词结果及词频按照出现次数排序，没有自己去写实现，主要借用了collections的sort方法。
	 */
	public static void sortSegmentResult(Map<String, Integer> wordsFrenMaps,
			int topWordsCount) {
		System.out.println("排序前:================");
		Iterator<Map.Entry<String, Integer>> wordsFrenMapsIterator = wordsFrenMaps
				.entrySet().iterator();
		while (wordsFrenMapsIterator.hasNext()) {
			Map.Entry<String, Integer> wordsFrenEntry = wordsFrenMapsIterator
					.next();
			System.out.println(wordsFrenEntry.getKey() + "             的次数为"
					+ wordsFrenEntry.getValue());
		}

		List<Map.Entry<String, Integer>> wordFrenList = new ArrayList<Map.Entry<String, Integer>>(
				wordsFrenMaps.entrySet());
		Collections.sort(wordFrenList,
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> obj1,
							Map.Entry<String, Integer> obj2) {
						return obj2.getValue() - obj1.getValue();
					}
				});
		System.out.println("排序后:================");
		for (int i = 0; i < topWordsCount && i < wordFrenList.size(); i++) {
			Map.Entry<String, Integer> wordFrenEntry = wordFrenList.get(i);
			if (wordFrenEntry.getValue() > 1) {
				System.out.println(wordFrenEntry.getKey() + "             的次数为"
						+ wordFrenEntry.getValue());
			}
		}
	}

	public static void main(String args[]) throws IOException {

//		 String text = "IKAnalyzer是一个开源的，基于java语言开发的轻量级的中文分词工具包开发开源" ;
//		 int topWordsCount=5;
//		 Map<String,Integer> wordsFrenMaps=getTextDef(text);
//		 sortSegmentResult(wordsFrenMaps,topWordsCount);
	}

	public void keyword(Date startDate, Date endDate) {

		/* 这类加添加到构建GubaGraphic类的establishGraphic的方法中 */
		// 加载mongoDB数据库
		Set<GraphicNode> nodeSet = new HashSet<GraphicNode>();
		Set<GraphicEdge> edgeSet = new HashSet<GraphicEdge>();

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
		DBCollection topics2 = db.getCollection("Topics");
		DBCollection comments2 = db.getCollection("Comments");
		DBCollection publishers2 = db.getCollection("Publishers");
		com.google.code.morphia.query.Query<Comment> query = datastore
				.createQuery(Comment.class).filter("publishDate >", startDate)
				.filter("publishDate <", endDate);
		List<Comment> xComment = query.asList();

		System.out.println("query.list:" + query.asList().size());
		Iterator<Comment> iterator = xComment.iterator();
		while (iterator.hasNext()) {
			Comment comment = iterator.next();
			nodeSet.add(comment);
			
			//。。。
			String commentText = comment.getCommentContent();
			
			
			Publisher commentPublisher = comment.getPublisher();
			if (commentPublisher != null) {
				if (nodeSet.contains(commentPublisher)) {
				} else {
					nodeSet.add(commentPublisher);
				}
				edgeSet.add(new GraphicEdge(comment, commentPublisher,
						EdgeType.p2c));
			}

			String topicTitle = comment.getTopicTitle();
			if ((datastore.createQuery(Topic.class).filter("title", topicTitle)
					.asList().size()) != 0) {
				Topic topic = datastore.createQuery(Topic.class)
						.filter("title", topicTitle).asList().get(0);

				if (topic != null) {
					if (nodeSet.contains(topic)) {
					} else {

						nodeSet.add(topic);
					}
					edgeSet.add(new GraphicEdge(topic, comment, EdgeType.t2c));

				}

				Publisher topicPublisher = topic.getPublisher();
				if (topicPublisher != null) {
					if (nodeSet.contains(topicPublisher)) {
					} else {
						nodeSet.add(topicPublisher);

					}
					edgeSet.add(new GraphicEdge(topic, topicPublisher,
							EdgeType.p2t));
				}
			}
		}
	}
}
