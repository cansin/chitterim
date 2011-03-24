package com.chitter.test;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		/*System.out.println("Hello from Servlet!");

		List<UserAccount> userAccs = UserAccount.getUserAccountList();
		
		Iterator<UserAccount> it = userAccs.iterator();
		
		while(it.hasNext()){
			UserAccount ua=it.next();
			new UserStatistic(ua.getGtalkId(),new float[]{0,0,0,0,0,0,0,0,0,0,0});
		}
		
		System.out.println(UserStatistic.getSumAnalytic());
		response.getOutputStream().println(UserStatistic.getSumAnalytic().toString());*/
	}

}
