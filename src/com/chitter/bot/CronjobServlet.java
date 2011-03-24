package com.chitter.bot;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chitter.persistence.UserAccount;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class CronjobServlet extends HttpServlet {

	private static final XMPPService xmppService =
		XMPPServiceFactory.getXMPPService();
	
	private static final Queue queue = 
		QueueFactory.getDefaultQueue();
	
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
		
		@SuppressWarnings("unchecked")
		List<UserAccount> userAccounts = UserAccount.getUserAccountList();
		Iterator<UserAccount> it = userAccounts.iterator();
		
		while(it.hasNext()){
			UserAccount userAccount = it.next();
			Presence presence=xmppService.getPresence(new JID(userAccount.getGtalkId()));
			if(presence.isAvailable() && userAccount.getIsTimelineActive()) {
				queue.add(Builder.withUrl("/task?gtalkId="+userAccount.getGtalkId()+
										"&accessToken="+userAccount.getTwitterAccessToken()+
										"&accessTokenSecret="+userAccount.getTwitterAccessTokenSecret())
										.method(Method.GET));	
			}
		}

	}
}
