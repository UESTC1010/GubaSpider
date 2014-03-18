package crawl;


import java.util.concurrent.ThreadPoolExecutor;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

public class Stock {

	String name;
	String code;// 股票代码
	String uri;
	
	
	Stock(String name, String code) {
		this.name = name;
		this.code = code;
	}

	void getUri(Guba myGuba) {
		this.uri = myGuba.getStockUri(this.name,this.code);
	}

	void getRecentTopics() {
		ThreadPool.init(400);
		int OtherDateT = 0;
		int pageNumber = 1;
		
		
		do{
			PageThread.OtherDateT = 0;
			OtherDateT = getTopicFromPage(uri.substring(0, uri.length() - 5)
					+ "_" + pageNumber + ".html");
			pageNumber++;
			
		}while( OtherDateT < 8);
		ThreadPool.shutdown();
		
/*		
 		Long topicAmout = 0L;
		int pageNumber = 1;
		while (topicAmout < amount) {
			//按页数顺序爬取topic，返回当前已爬取的topic数量
			topicAmout = getTopicFromPage(uri.substring(0, uri.length() - 5)
					+ "_" + pageNumber + ".html");
			System.out.println("---------------------->topicAmout"+topicAmout);
		}
*/
		
	}

	//获取topic
	private int getTopicFromPage(String pageUri) {
		try {
			//下载当前页面
			PageHandle topicpage = new PageHandle();
			String htmlcode = topicpage.downloadpage(pageUri);
			
			//解析页面，提取有“news”字段的链接tag
			Parser parser = new Parser( htmlcode);
			parser.setEncoding(parser.getEncoding());
			NodeFilter frameFilter = new LinkRegexFilter("news");
			NodeList list = parser.extractAllNodesThatMatch(frameFilter);
			System.out.println(list.size());
			
			//将topic依次放入线程池中运行。
			for (int i = 0; i < list.size(); i++) {
					TagNode tag = (TagNode) list.elementAt(i);
					String frameUrl = tag.getAttribute("href");// 提取链接
					if (frameUrl.indexOf("guba") == -1)
							frameUrl = Guba.gubaBaseUri + frameUrl;
					if (frameUrl.indexOf(this.code) == -1)
							continue;
						
					ThreadPool.tt.execute(new PageThread(tag,frameUrl));
			}
			
			//判断当前topic线程是否已经全部爬取完
			while(((ThreadPoolExecutor)ThreadPool.tt).getActiveCount() > 0){}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PageThread.OtherDateT;
		//return PageThread.topicCount;
	}

}
