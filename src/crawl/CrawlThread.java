package crawl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

public class CrawlThread implements Runnable {
	public static long topicAmount = 0;
	public long topicCount = 0;
	public String code = null;
	Set<Topic> topicSet;
	Set<Publisher> publisherSet;
	
	public CrawlThread(String code) {
		this.code = code;
		topicSet = new HashSet<Topic>();
		publisherSet = new HashSet<Publisher>();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String url = null;
		try {
			url = dequeueURL();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(url);
		
		getTopicFromPage(url);
		//System.out.println("Stock 44th:"+url.substring(url.length()-6, url.length()-5));
		
	}
	public static synchronized String dequeueURL() throws InterruptedException{
		String visitUrl = (String)UnVisitedQueue.removeQueue();	
		//System.out.println("handle the url----->"+Thread.currentThread().getId());
		return visitUrl;

	}
	//获取
	private void getTopicFromPage(String pageUri) {

		try {
			Parser parser = new Parser(pageUri);
			
			parser.setEncoding(parser.getEncoding());

			NodeFilter frameFilter = new LinkRegexFilter("news");
			NodeList list = parser.extractAllNodesThatMatch(frameFilter);
			System.out.println(list.size());
			
			ExecutorService tt = Executors.newFixedThreadPool(100);
			
				for (int i = 0; i < list.size(); i++) {
					TagNode tag = (TagNode) list.elementAt(i);
					String frameUrl = tag.getAttribute("href");// 提取链接
					if (frameUrl.indexOf("guba") == -1)
						frameUrl = Guba.gubaBaseUri + frameUrl;
					if (frameUrl.indexOf(this.code) == -1)
						continue;
					
					tt.submit(new PageThread(tag,frameUrl));
				}
				
				while(((ThreadPoolExecutor)tt).getActiveCount() > 0){}
				synchronized(this){
					topicAmount+=topicCount;
					System.out.println("topicAmount------------>"+topicAmount);
				}
		} catch (Exception e) {
			UnVisitedQueue.enfirstqueue(pageUri);	
			e.printStackTrace();
		}
	}
}
