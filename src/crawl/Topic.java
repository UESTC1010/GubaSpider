package crawl;

import guba.GraphicNode;

import java.io.Serializable;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;


import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

@Entity(value="Topics",noClassnameStored=true)
public class Topic implements Serializable , GraphicNode {
	@Id
	private String id;
	
	@Indexed(value = IndexDirection.ASC, name = "topictitle")
	String title;
	
	/**
	 * 
	 */
	public Topic() {
		
	}

	@Embedded
	Publisher publisher;
	
	String uri;
	long topicClick;
	long topicComment;
	Date publishDate;
	Date updateDate;

	//Set<Publisher> commentators = new HashSet<Publisher>();
	//Set<NewsSource> newsSources = new HashSet<NewsSource>();
	//Set<Comment> comment = new HashSet<Comment>();


	void getDetailComments() {
		//如果回复数大于2，将评论页依次加入线程池中运行
		if(topicComment > 2){
			for (int i = 0; i <= topicComment / 41; i++) {
				//getCommentFromPage(uri.substring(0, uri.length() - 5) + "_"+ (i + 1) + ".html");
				
				ThreadPool.tt.execute( new CommentThread(uri.substring(0, uri.length() - 5) + "_"
						+ (i + 1) + ".html", uri, title));
			}
		}
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getTopicClick() {
		return topicClick;
	}

	public void setTopicClick(long topicClick) {
		this.topicClick = topicClick;
	}

	public long getTopicComment() {
		return topicComment;
	}

	public void setTopicComment(long topicComment) {
		this.topicComment = topicComment;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	
	public void getCommentFromPage(String pageUri) {
		try {
			System.out.println(pageUri);
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
						if(compub.toHtml().indexOf("href")>-1){
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
							    String commentDateStr = comdate.toPlainTextString().substring(comdate.getText()
											.indexOf("楼&nbsp;&nbsp; ") + "楼&nbsp;&nbsp;bal".length());
							    DateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
								try {
									commentDate = format.parse(commentDateStr);
									System.out.println("commentDate--------------->"+commentDateStr);
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
						}
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

	
	public static void main(String args[]){
		Topic topic = new Topic();
		topic.getCommentFromPage("http://guba.eastmoney.com/news,000157,94721557.html");
	}
	
}
