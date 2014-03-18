package crawl;

import guba.GraphicNode;

import java.io.Serializable;

import com.google.code.morphia.annotations.*;
import com.google.code.morphia.utils.IndexDirection;

@Entity(value="Publishers",noClassnameStored=true)
public class Publisher implements  Serializable , GraphicNode  {
	@Id
	private String id;
	
	/**
	 * 
	 */
	public Publisher() {
	
	}
	@Indexed(value = IndexDirection.ASC, name = "publishername")
	String name;
	long topicAmount;
	long commentAmount;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTopicAmount() {
		return topicAmount;
	}
	public void setTopicAmount(long topicAmount) {
		this.topicAmount = topicAmount;
	}
	public long getCommentAmount() {
		return commentAmount;
	}
	public void setCommentAmount(long commentAmount) {
		this.commentAmount = commentAmount;
	}

	// Set<Topic> topicSet;
	// Set<Comment> commentSet;
//	public Publisher(String name,long topicAmount,long commentAmount) {
//		this.name = name;
//		this.topicAmount = topicAmount;
//		this.commentAmount = commentAmount;
//		// TODO Auto-generated constructor stub
//	}

}
