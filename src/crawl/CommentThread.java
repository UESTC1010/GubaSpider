package crawl;

import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

public class CommentThread implements Runnable {
	String pageUri;
	//Set<Comment> comment = null;
	String uri = null; 
	String title = null;
	public CommentThread(String pageUri, String uri, String title){
		this.pageUri = pageUri;
		//this.comment = comment;
		this.title = title;
		this.uri = uri;
	}
	
	@Override
	public void run() {
		try {
			//System.out.println(pageUri);
			//下载评论页
			PageHandle commentpage = new PageHandle();
			String htmlcode = commentpage.downloadpage(pageUri);
			//System.out.println(htmlcode);
			
			//解析页面，按特定filter提取Node
			if(htmlcode!=null){
			Parser parser = new Parser(htmlcode);
			
			parser.setEncoding(parser.getEncoding());
			AndFilter filter = new AndFilter( 
	                              new TagNameFilter("div"), 
	                             new HasAttributeFilter("class","zwli clearfix") 
	              ); 
			NodeList list = parser.extractAllNodesThatMatch(filter);
			//System.out.println("hehe"+list.size());
			
			if(list.size()>0)
			{
				//开始提取comments
				for(int i = 0; i<list.size(); i++){
					String commentStr = null;
					String commentPublisher = null;
					Date commentDate = null;
					//提取主内容node
					Node node = list.elementAt(i).getLastChild().getPreviousSibling()
															.getFirstChild().getNextSibling();
					if(node.getText().indexOf("zwlitxt")>-1){
						//评论人姓名节点
						Node compub = node.getFirstChild().getNextSibling()
													.getFirstChild().getNextSibling();
						//如果是实名用户
						//if(compub.toHtml().indexOf("href")>-1){
							commentPublisher = compub.toPlainTextString();
							commentPublisher = commentPublisher.replaceAll("&nbsp;", "");
							commentPublisher = commentPublisher.replaceAll("&amp;", "");
							//System.out.println(commentPublisher);
							
							//评论内容节点
							Node ComStrTag = node.getLastChild().getPreviousSibling()
														.getPreviousSibling().getPreviousSibling();
							//another condition, multiply reply
							
							if(ComStrTag != null)
							    commentStr = ComStrTag.toPlainTextString();
							    //System.out.println(commentStr);
							    //handle the commentStr
							    
							 //评论日期节点
							 Node comdate = ComStrTag.getNextSibling().getNextSibling().getFirstChild().getNextSibling();
							 if(comdate.getText().indexOf("zwlitxb") > -1){
							    String commentDateStr = comdate.toPlainTextString();
							    DateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
								try {
									commentDate = format.parse(commentDateStr);
									//System.out.println(commentDate);
								} catch (Exception e) {
									//handle date error
								}
							}
							
							Comment tempComment = new Comment(
									UglyDB.GetOrNewPublisher(commentPublisher), commentDate,
									this.title,commentStr);

							tempComment.topicUrl = this.uri;
							
							//判断是否是新增评论
							UglyDB.putComment(tempComment.publisher.name
									+ tempComment.publishDate, tempComment);
							
							
							//添加进topic对象的评论集合
							//comment.add(tempComment);	
						//}
					}
				}
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (e instanceof ConnectException){
				System.out.println("hehe");
			}
			e.printStackTrace();
		}
	}	
}
