/**
 * 
 */
package crawl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @author Xiaoqing
 * 创建日期：2014-3-2下午7:53:32
 * 修改日期：
 */
public class Stocklist {
	static String stocklistpage = "http://guba.eastmoney.com/geguba_list.html";
	public static void main(String[] args) {
		
		
			PageHandle topicpage = new PageHandle();
			String htmlcode = topicpage.downloadpage(stocklistpage);
			
			if(htmlcode != null){
				Parser parser = null;
				NodeList list = null;
				Set<String> s = new HashSet<String>();
				//System.out.println(htmlcode);
				try {
					parser = Parser.createParser(htmlcode, "utf-8");
					parser.setEncoding("utf-8");
					NodeFilter frameFilter = new LinkRegexFilter("topic");
					list = parser.extractAllNodesThatMatch(frameFilter);
					System.out.println(list.size());
				
				for (int i = 0; i < list.size(); i++) {
					TagNode tag = (TagNode) list.elementAt(i);
					String stock = tag.toPlainTextString();
					
					String regex1 = "\\(\\d{6}\\).*?";
					Pattern pattern = Pattern.compile(regex1);
					Matcher matcher = pattern.matcher(stock);
					while(matcher.find()){	
						s.add(stock);
					}
				}

				System.out.println(s.size());
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
	}

}
