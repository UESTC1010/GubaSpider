package crawl;

import guba.GraphicNode;

import java.io.Serializable;
import java.util.Date;

import com.google.code.morphia.annotations.*;

@Entity(value="Comments",noClassnameStored=true)
@Indexes( @Index("topicTitle, publishDate") ) 
public class Comment implements Serializable , GraphicNode  {
	@Id
	private String id;
	
	@Embedded
	Publisher publisher;
	
	String topicTitle;	
	Date publishDate;
	//Comment superComment;	//¸¸»Ø¸´
	//NewsSource newsSources;
	String topicUrl;
	String commentContent;

	//boolean wasVisited;
	
	Comment(Publisher publisher, Date publishDate, String topicTitle,String commentContent) {
		this.publisher = publisher;
		this.publishDate = publishDate;
		this.topicTitle = topicTitle;
		this.commentContent = commentContent;
	}

	/**
	 * 
	 */
	public Comment() {
	
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getTopicUrl() {
		return topicUrl;
	}

	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

}
