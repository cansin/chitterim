package com.chitter.persistence;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class UserTwitterTimeline {
	@PrimaryKey
	@Persistent
	private String gtalkId;

	@Persistent
	private Long twitterTimelineSinceId;

	@Persistent
	private Integer twitterDirectMessageSinceId;
	

	public UserTwitterTimeline(String gtalkId, Long twitterTimelineSinceId, Integer twitterDirectMessageSinceId){
		this.gtalkId=gtalkId;
		this.twitterTimelineSinceId=twitterTimelineSinceId;
		this.twitterDirectMessageSinceId=twitterDirectMessageSinceId;
	}
	
	public UserTwitterTimeline(String gtalkId){
		this.gtalkId="";
		this.twitterTimelineSinceId=new Long(0);
		this.twitterDirectMessageSinceId=new Integer(0);
	}
	
	public String getGtalkId() {
		return gtalkId;
	}

	public void setGtalkId(String gtalkId) {
		this.gtalkId = gtalkId;
	}

	public void setTwitterTimelineSinceId(Long twitterTimelineSinceId) {
		this.twitterTimelineSinceId = twitterTimelineSinceId;
	}

	public Long getTwitterTimelineSinceId() {
		return twitterTimelineSinceId;
	}

	public void setTwitterDirectMessageSinceId(
			Integer twitterDirectMessageSinceId) {
		this.twitterDirectMessageSinceId = twitterDirectMessageSinceId;
	}

	public Integer getTwitterDirectMessageSinceId() {
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
