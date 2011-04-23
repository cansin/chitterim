package com.chitter.bot;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.Announcement;
import com.chitter.utility.ExceptionPrinter;

@SuppressWarnings("serial")
public class AnnounceCronjobServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Twitter twitter = TwitterAPI.getInstanceForChitter();
		
		@SuppressWarnings("unchecked")
		List<Announcement> announcements = Announcement.getAnnouncementList();
		
		int rand = new Random().nextInt(announcements.size());
		String announcement = announcements.get(rand).toTweetString();
		
		if(announcement.length()<140) {
			try {
				twitter.updateStatus(announcement);
			} catch (TwitterException e) {
				ExceptionPrinter.print(System.err, e, "I couldn't update chitterim's status while trying to make an announcement.");
			}
		} else {
			System.err.println("Announcement exceeds 140 chars: "+announcement);
		}
	}
}
