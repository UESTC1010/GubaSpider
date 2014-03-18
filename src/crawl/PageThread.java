package crawl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;

public class PageThread implements Runnable {
	
	TagNode tag = null;
	String frameUrl = null;
	//Set<Topic> topicSet = new HashSet<Topic>(); //һ��page���topic���ϣ�Ӧ��ûɶ�ã����Կ���ɾ��
	static long topicCount = 0;
	static int OtherDateT = 0;
	
	
	public PageThread(TagNode tag,String frameUrl) {
		this.tag = tag;				//��ǰtopic��ǩ
		this.frameUrl = frameUrl;	//topic����
	
	}

	@Override
	public void run() {
		//�����µ�topic
//		Topic tempTopic = UglyDB
//				.getAndNewTopic(tag.toPlainTextString());
		Topic tempTopic = new Topic();
		tempTopic.uri = frameUrl;
		tempTopic.title = tag.toPlainTextString();
		
		//System.out.println(tempTopic.title);
		
		// ��ȡ�����Ļ�����Ϣ
		// �����
		Node fathertag = tag.getParent().getParent();
		Node topicClick = fathertag.getFirstChild().getNextSibling();
		tempTopic.topicClick = Long.parseLong(topicClick
				.toPlainTextString());
		//System.out.println(tempTopic.topicClick);
		
		// �ظ���
		Node topicComment = topicClick.getNextSibling();
		tempTopic.topicComment = Long.parseLong(topicComment
				.toPlainTextString());
		// System.out.println(tempTopic.topicComment);
		
		// ������
		Node topicPublisher = topicClick.getNextSibling()
				.getNextSibling().getNextSibling();
		String topicPublisherName = topicPublisher.toPlainTextString();
		topicPublisherName = topicPublisherName
				.replaceAll("&nbsp;", "");
		topicPublisherName = topicPublisherName.replaceAll("&amp;", "");
		// System.out.println(topicPublisherName);
		//�������publisher��¼����ȡԭ�ȶ��󣬷����½�publisher����
		tempTopic.publisher = UglyDB.GetOrNewPublisher(topicPublisherName);
		
		
			// ����ʱ��
			Date publishTime = new Date();
			PageHandle commentpage = new PageHandle();
			String htmlcode = commentpage.downloadpage(frameUrl);
			Date tempTime = commentpage.HandlerBody(htmlcode);
			if(tempTime == null){
				System.out.println("publishtime parser error!!!!!!!!!!!!!!!!!!!!!!");
				//System.exit(0);
			}
			else
				publishTime = tempTime;
			tempTopic.publishDate = publishTime;
			//Node publishDate = topicPublisher.getNextSibling();
			//DateFormat format = new SimpleDateFormat("MM-dd");
			//tempTopic.publishDate = format.parse(publishDate.toPlainTextString());
			//tempTopic.publishDate.setYear(113);
			//System.out.println(tempTopic.publishDate);
			
			// ���ظ�ʱ��
			Date updateDate = new Date();
			
			long lastpage = (tempTopic.topicComment-1)/40+1;
 			String htmlcode1 = commentpage.downloadpage(frameUrl.substring(0, frameUrl.length() - 5) + "_"
					+ lastpage + ".html");
			Date tempTime1 = commentpage.Handlerlastpage(htmlcode1);
			if(tempTime1 == null){
				//System.out.println("no comment!!!!!!!!!!!!!!!!!!!!!!");
				updateDate = tempTopic.publishDate;
			}
			else
				updateDate = tempTime1;
			tempTopic.updateDate = updateDate;
			//Node updateDate = topicPublisher.getNextSibling().getNextSibling();
			//DateFormat format = new SimpleDateFormat("MM-dd HH:mm");
			//tempTopic.updateDate = format.parse(updateDate.toPlainTextString());
			//Date thisyear = new Date();
			//tempTopic.updateDate.setYear(thisyear.getYear());
				
			//System.out.println(tempTopic.updateDate);
		
		if(tempTopic.updateDate.after(CrawlTime.START_TIME) && tempTopic.updateDate.before(CrawlTime.END_TIME) ){
			//System.out.println("����ʱ��--------------->"+tempTopic.updateDate);
			if(tempTopic.publishDate.after(CrawlTime.INITIAL_TIME)){
				//System.out.println("fabiaoʱ��--------------->"+tempTopic.updateDate);
				//����topic��ȫ����ȡ
				tempTopic.getDetailComments();
				UglyDB.putTopic(tempTopic.getTitle(),tempTopic);
			}
		}
		else{
			OtherDateT++;
			System.out.println(tempTopic.title);
			System.out.println(tempTopic.updateDate);
		}
		
		//synchronized(this){
			//topicCount++;
			//System.out.println("�߳���"+Thread.currentThread().getThreadGroup().getName()+"��topicCount------------>"+topicCount);
		//}
	}

}
