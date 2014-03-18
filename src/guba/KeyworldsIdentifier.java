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
 * @author Xiaoqing �������ڣ�2014-3-17����10:48:26 �޸����ڣ�
 */
public class KeyworldsIdentifier {

	/*
	 * �Դ���Ĳ������зִʣ���ͳ�ƺ�ÿ���ε�Ƶ��
	 * IKSegmenter�Ƿִʵ���Ҫ�࣬������ֱ��Ƿִʵľ��ӻ������£�����Ĳ������Ƿ�������ģʽ���������Ͱ���С����֡�
	 * �ִʵĽ����Lexeme����࣬�����е�getLexemeText()��������ȡ����صķִʽ����
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
	 * �����Ƶ���ִʽ���ͳ��ִ����ŵ�һ��map�ṹ�У�map��value��Ӧ�˴ʵĳ��ִ���������ע��һ�£�ֻ��¼�����ּ����������ϵķִʽ����
	 * ��Ҫ�Էִʽ������Ƶ���ճ��ִ�������û���Լ�ȥдʵ�֣���Ҫ������collections��sort������
	 */
	public static void sortSegmentResult(Map<String, Integer> wordsFrenMaps,
			int topWordsCount) {
		System.out.println("����ǰ:================");
		Iterator<Map.Entry<String, Integer>> wordsFrenMapsIterator = wordsFrenMaps
				.entrySet().iterator();
		while (wordsFrenMapsIterator.hasNext()) {
			Map.Entry<String, Integer> wordsFrenEntry = wordsFrenMapsIterator
					.next();
			System.out.println(wordsFrenEntry.getKey() + "             �Ĵ���Ϊ"
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
		System.out.println("�����:================");
		for (int i = 0; i < topWordsCount && i < wordFrenList.size(); i++) {
			Map.Entry<String, Integer> wordFrenEntry = wordFrenList.get(i);
			if (wordFrenEntry.getValue() > 1) {
				System.out.println(wordFrenEntry.getKey() + "             �Ĵ���Ϊ"
						+ wordFrenEntry.getValue());
			}
		}
	}

	public static void main(String args[]) throws IOException {

//		 String text = "IKAnalyzer��һ����Դ�ģ�����java���Կ����������������ķִʹ��߰�������Դ" ;
//		 int topWordsCount=5;
//		 Map<String,Integer> wordsFrenMaps=getTextDef(text);
//		 sortSegmentResult(wordsFrenMaps,topWordsCount);
	}

	public void keyword(Date startDate, Date endDate) {

		/* �������ӵ�����GubaGraphic���establishGraphic�ķ����� */
		// ����mongoDB���ݿ�
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
			
			//������
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
