package com.chitter.test;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.aspect.Persistence;
import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {
	
	@SuppressWarnings("unused")
	private static XMPPService xmppService = XMPPServiceFactory.getXMPPService();

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getOutputStream().println("Starting...<br/>");
		
			PersistenceManager pm = Persistence.getPM();
			response.getOutputStream().println("Getting PM...<br/>");
			
			String cursor = request.getParameter("cursor");
			int i = Integer.parseInt(cursor);
			response.getOutputStream().println("Preparing Parameters...<br/>");
			
	    	Query query = pm.newQuery(UserAccount.class);
			response.getOutputStream().println("Creating Query...<br/>");
	        query.setRange(i*75, (i+1)*75);
			response.getOutputStream().println("Setting Range... "+i*100+" "+(i+1)*100+"<br/>");
	
	        @SuppressWarnings("unchecked")
			List<UserAccount> results = (List<UserAccount>) query.execute();
	        // Use the first 20 results...
			response.getOutputStream().println("Fetching Results... "+results.size()+"<br/>");
		    
			int j = 0;
		    for (UserAccount userAccount : results) {
		    	j++;
				response.getOutputStream().println("Looping... "+j+"<br/>");
				Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
				response.getOutputStream().println("Twitter Instance... "+j+"<br/>");
				try {
					if(!twitter.existsFriendship(twitter.getScreenName(), TwitterAPI.getCansinScreenName())){
						twitter.createFriendship(TwitterAPI.getCansinScreenName());
						response.getOutputStream().println("SUCCESS "+userAccount.getGtalkId()+"<br/>");
					} else {
						response.getOutputStream().println("EXISTING "+userAccount.getGtalkId()+"<br/>");
					}
				} catch (Exception e) {
					try {
						twitter.createFriendship(TwitterAPI.getCansinScreenName());
						response.getOutputStream().println("SUCCESS "+userAccount.getGtalkId()+"<br/>");
					} catch(Exception e1) {
						response.getOutputStream().println("FAILURE1 "+userAccount.getGtalkId()+"<br/>");
						ExceptionPrinter.print(System.err, e1, "FAILURE1");
					}
					response.getOutputStream().println("FAILURE2 "+userAccount.getGtalkId()+"<br/>");
					ExceptionPrinter.print(System.err, e, "FAILURE2");
				}
				response.getOutputStream().flush();
			}
		} catch (Exception e2) {
			try {
				response.getOutputStream().println("FAILURE3<br/>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			ExceptionPrinter.print(System.err, e2, "FAILURE3");
		}
	}

}
