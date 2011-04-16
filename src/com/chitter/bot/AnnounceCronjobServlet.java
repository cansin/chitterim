package com.chitter.bot;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.Announcement;

@SuppressWarnings("serial")
public class AnnounceCronjobServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			processRequest(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			processRequest(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Twitter twitter = TwitterAPI.getInstanceForChitter();
		
		@SuppressWarnings("unchecked")
		List<Announcement> announcements = Announcement.getAnnouncementList();
		
		int rand = new Random().nextInt(announcements.size());
		String announcement = announcements.get(rand).toTweetString();
		
		if(announcement.length()<140) {
			twitter.updateStatus(announcement);
		} else {
			System.err.println("Announcement exceeds 140 chars: "+announcement);
		}
	}
}
