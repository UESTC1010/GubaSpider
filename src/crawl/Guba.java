package crawl;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

public class Guba {
	static String gubaUri = "http://guba.eastmoney.com/geguba_list.html";
	static String gubaBaseUri = "http://guba.eastmoney.com/";

	String getStockUri(String stockName, String code) {
		
		/*try {
			Parser parser = new Parser(gubaUri);
			parser.setEncoding("UTF-8");
			
			NodeFilter frameFilter = new LinkRegexFilter("topic");
			NodeList list = null;
			try {
				list = parser.extractAllNodesThatMatch(frameFilter);				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				parser.setEncoding("gb2312");
			}
			for (int i = 0; i < list.size(); i++) {
				TagNode tag = (TagNode) list.elementAt(i);
				// String tempStr = tag.getAttribute("titled");
				if (tag.toString().indexOf(stockName) > -1) {
					String frameUrl = tag.getAttribute("href");
					if (frameUrl.indexOf("guba") == -1)
						frameUrl = gubaBaseUri + frameUrl;
					System.out.println("Guba 31th:"+frameUrl);
					return frameUrl;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		String url = "http://guba.eastmoney.com/list,"+code+".html";
		return url;

	}
}
