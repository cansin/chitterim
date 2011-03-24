package com.chitter.persistence;

import java.util.Arrays;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UserStatistic {

	public static final int statisticsCount = 11;
	public static final String[] statisticsLabels = {
		"Direct Message","Follow","Help","Incoming Friendship","Retweet","Subscribe",
		"Timeline Off","Timeline On","Tweet","Unfollow","What to Do"
	};
	public static final int DIRECTMESSAGE=0, FOLLOW=1, HELP=2, INCOMINGFRIENDSHIP=3, RETWEET=4,
		SUBSCRIBE=5, TIMELINEOFF=6, TIMELINEON=7, TWEET=8, UNFOLLOW=9, WHATTODO=10;
	
	@PrimaryKey
	@Persistent
	private String gtalkId;
	
	@Persistent
	private float[] statistics;

	public UserStatistic(String gtalkId, float[] analytics) {
		super();
		this.gtalkId = gtalkId;
		this.statistics = analytics;
	}
	
	public UserStatistic(String gtalkId) {
		this.gtalkId = "";
		this.statistics = new float[statisticsCount];
	}
	
	public void setGtalkId(String gtalkId) {
		this.gtalkId = gtalkId;
	}
	
	public String getGtalkId() {
		return gtalkId;
	}
	
	public void setStatistics(float[] statics){
		this.statistics = statics;
	}
	
	public float[] getStatistics(){
		return statistics;
	}
	
	public void setStatistic(int id, float val){
		statistics[id]=val;
	}
	
	public float getStatistic(int id){
		return statistics[id];
	}

	@Override
	public String toString() {
		return "UserStatistic [gtalkId=" + gtalkId + ", statistics="
				+ Arrays.toString(statistics) + "]";
	}
}
