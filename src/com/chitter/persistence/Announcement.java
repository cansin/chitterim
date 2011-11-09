package com.chitter.persistence;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable
public class Announcement implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Category {HowTo, DidYouKnowThat, NewFeature};
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String title;
	
	@Persistent
	private String body;
	
	@Persistent
	private String url;
	
	@Persistent
	private Category category;

	public Announcement(String title, String body, String url, Category category) {
		super();
		this.title = title;
		this.body = body;
		this.url = url;
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Key getKey() {
		return key;
	}
	
	public String toTweetString() {
		String ret="";
		ret += body;
		if(url.isEmpty()){
			return ret;
		} else {
			ret += " Read more at ";
			ret += url;
			return ret;
		}
	}

	@Override
	public String toString() {
		return "Announcement [key=" + key + ", title=" + title + ", body="
				+ body + ", url=" + url + ", category=" + category + "]";
	}
}
