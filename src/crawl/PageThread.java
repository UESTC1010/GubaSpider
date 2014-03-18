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
	//Set<Topic> topicSet = new HashSet<Topic>(); //一个page里的topic集合，应该没啥用，可以考虑删除
	static long topicCount = 0;
	static int OtherDateT = 0;
	
	
	public PageThread(TagNode tag,String frameUrl) {
		this.tag = tag;				//当前topic标签
		this.frameUrl = frameUrl;	//topic链接
	
	}

	@Override
	public void run() {
		//生成新的topic
//		Topic tempTopic = UglyDB
//				.getAndNewTopic(tag.toPlainTextString());
		Topic tempTopic = new Topic();
		tempTopic.uri = frameUrl;
		tempTopic.title = tag.toPlainTextString();
		
		//System.out.println(tempTopic.title);
		
		// 获取其他的话题信息
		// 点击数
		Node fathertag = tag.getParent().getParent();
		Node topicClick = fathertag.getFirstChild().getNextSibling();
		tempTopic.topicClick = Long.parseLong(topicClick
				.toPlainTextString());
		//System.out.println(tempTopic.topicClick);
		
		// 回复数
		Node topicComment = topicClick.getNextSibling();
		tempTopic.topicComment = Long.parseLong(topicComment
				.toPlainTextString());
		// System.out.println(tempTopic.topicComment);
		
		// 发帖人
		Node topicPublisher = topicClick.getNextSibling()
				.getNextSibling().getNextSibling();
		String topicPublisherName = topicPublisher.toPlainTextString();
		topicPublisherName = topicPublisherName
				.replaceAll("&nbsp;", "");
		topicPublisherName = topicPublisherName.replaceAll("&amp;", "");
		// System.out.println(topicPublisherName);
		//如果已有publisher记录则提取原先对象，否则新建publisher对象
		tempTopic.publisher = UglyDB.GetOrNewPublisher(topicPublisherName);
		
		
			// 发表时间
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
			
			// 最后回复时间
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
			//System.out.println("更新时间--------------->"+tempTopic.updateDate);
			if(tempTopic.publishDate.after(CrawlTime.INITIAL_TIME)){
				//System.out.println("fabiao时间--------------->"+tempTopic.updateDate);
				//新增topic，全部爬取
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
			//System.out.println("线程组"+Thread.currentThread().getThreadGroup().getName()+"的topicCount------------>"+topicCount);
		//}
	}

}
