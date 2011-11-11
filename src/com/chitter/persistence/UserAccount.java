package com.chitter.persistence;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class UserAccount implements Serializable {
	private static final long serialVersionUID = 2L;
	@PrimaryKey
	@Persistent
	private String gtalkId;
	public void jdoPreStore() {
		gtalkId = gtalkId.toLowerCase();
	}
	
	@Persistent
	private String twitterAccessToken;
	
	@Persistent
	private String twitterAccessTokenSecret;

	@Persistent
	private Boolean isTimelineActive;

	public UserAccount(String gtalkId, String twitterAccessToken, String twitterAccessTokenSecret){
		this.gtalkId=gtalkId.toLowerCase();
		this.twitterAccessToken=twitterAccessToken;
		this.twitterAccessTokenSecret=twitterAccessTokenSecret;
		this.isTimelineActive=true;
	}

	public UserAccount(String gtalkId){
		this.gtalkId="";
		twitterAccessToken= new String();
		twitterAccessTokenSecret= new String();
		isTimelineActive= true;
	}
	
	public String getGtalkId() {
		return gtalkId;
	}

	public void setGtalkId(String gtalkId) {
		this.gtalkId = gtalkId.toLowerCase();
	}

	public String getTwitterAccessToken() {
		return twitterAccessToken;
	}

	public void setTwitterAccessToken(String twitterAccessToken) {
		this.twitterAccessToken = twitterAccessToken;
	}

	public String getTwitterAccessTokenSecret() {
		return twitterAccessTokenSecret;
	}

	public void setTwitterAccessTokenSecret(String twitterAccessTokenSecret) {
		this.twitterAccessTokenSecret = twitterAccessTokenSecret;
	}

	public void setIsTimelineActive(Boolean isTimelineActive) {
		this.isTimelineActive = isTimelineActive;
	}

	public Boolean getIsTimelineActive() {
		return isTimelineActive;
	}

	@Override
	public String toString() {
		return "UserAccount [gtalkId=" + gtalkId + ", twitterAccessToken="
				+ twitterAccessToken + ", twitterAccessTokenSecret="
				+ twitterAccessTokenSecret + ", isTimelineActive="
				+ isTimelineActive + "]";
	}
}
