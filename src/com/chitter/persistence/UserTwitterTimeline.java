package com.chitter.persistence;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class UserTwitterTimeline implements Serializable {
	private static final long serialVersionUID = 4L;
	@PrimaryKey
	@Persistent
	private String gtalkId;
	public void jdoPreStore() {
		gtalkId = gtalkId.toLowerCase();
	}

	@Persistent
	private Long twitterTimelineSinceId;

	@Persistent
	private Long twitterDirectMessageSinceId;
	

	public UserTwitterTimeline(String gtalkId, Long twitterTimelineSinceId, Long twitterDirectMessageSinceId){
		this.gtalkId=gtalkId.toLowerCase();
		this.twitterTimelineSinceId=twitterTimelineSinceId;
		this.twitterDirectMessageSinceId=twitterDirectMessageSinceId;
	}
	
	public UserTwitterTimeline(String gtalkId){
		this.gtalkId="";
		this.twitterTimelineSinceId=new Long(0);
		this.twitterDirectMessageSinceId=new Long(0);
	}
	
	public String getGtalkId() {
		return gtalkId;
	}

	public void setGtalkId(String gtalkId) {
		this.gtalkId = gtalkId.toLowerCase();
	}

	public void setTwitterTimelineSinceId(Long twitterTimelineSinceId) {
		this.twitterTimelineSinceId = twitterTimelineSinceId;
	}

	public Long getTwitterTimelineSinceId() {
		return twitterTimelineSinceId;
	}

	public void setTwitterDirectMessageSinceId(
			Long twitterDirectMessageSinceId) {
		this.twitterDirectMessageSinceId = twitterDirectMessageSinceId;
	}

	public Long getTwitterDirectMessageSinceId() {
		return twitterDirectMessageSinceId;
	}

	@Override
	public String toString() {
		return "UserTwitterTimeline [gtalkId=" + gtalkId
				+ ", twitterTimelineSinceId=" + twitterTimelineSinceId
				+ ", twitterDirectMessageSinceId="
				+ twitterDirectMessageSinceId + "]";
	}
}
