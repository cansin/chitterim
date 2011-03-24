package com.chitter.test;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chitter.persistence.UserAccount;
import com.chitter.persistence.UserStatistic;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {

	@SuppressWarnings("unused")
	private static final XMPPService xmppService =
		XMPPServiceFactory.getXMPPService();

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

	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Hello from Servlet!");
		/*
	
		
		//new UserAccount("cansinyildiz@gmail.com","37035986-ajgA0HG3lqIT0A9fKV0Y6qTBnQyJ3y4h0Xu73K3RI","bgz4jcjdEJiOHYa6NPGDFYOdlEiK35tmtBuBxxGbD4");

		UserAccount u1 = new UserAccount("cansinyildiz@gmail.com");
		

		List<UserAccount> userAccs = UserAccount.getUserAccountList();
		
		Iterator<UserAccount> it = userAccs.iterator();
		
		while(it.hasNext()){
			UserAccount ua=it.next();
			new UserStatistic(ua.getGtalkId(),new float[]{3,2,6,1,7,2,7,2,9,11,5});
		}
		
		System.out.println(UserStatistic.getSumAnalytic());
		response.getOutputStream().println(UserStatistic.getSumAnalytic().toString());*/
	}

}
