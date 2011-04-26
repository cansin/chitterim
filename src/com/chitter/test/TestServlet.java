package com.chitter.test;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chitter.persistence.UserAccount;
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

	public void processRequest(HttpServletRequest request, HttpServletResponse response)  {
		@SuppressWarnings("unchecked")
		List<UserAccount> timelineActiveAndOnlineUsers = UserAccount.getTimelineActiveAndOnlineUsers();
		try {
			response.getOutputStream().print("<p> TimelineActive And Online User Count: "+timelineActiveAndOnlineUsers.size()+"</p>");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
