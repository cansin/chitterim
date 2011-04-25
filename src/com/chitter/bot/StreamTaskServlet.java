package com.chitter.bot;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chitter.utility.ExceptionPrinter;

import twitter4j.TwitterException;

@SuppressWarnings("serial")
public class StreamTaskServlet  extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}
	
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		String gtalkId = request.getParameter("gtalkId");
		String accessToken = request.getParameter("accessToken");
		String accessTokenSecret = request.getParameter("accessTokenSecret");

		try {
			StreamCronjobServlet.sendTimelineUpdates(gtalkId, accessToken, accessTokenSecret);
		} catch (TwitterException e) {
			ExceptionPrinter.print(System.err, e, "I couldn't send timeline updates to "+gtalkId);
		}		
	}
}