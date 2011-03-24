package com.chitter.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chitter.persistence.UserStatistic;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class AnalyticsServlet extends HttpServlet {

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
		String type=request.getParameter("type");
		if(type==null || type.isEmpty()) {
			;
		} else if(type.equals("min")){
			request.setAttribute("analytic", UserStatistic.getMinAnalytic());
		} else if (type.equals("max")){
			request.setAttribute("analytic", UserStatistic.getMaxAnalytic());
		} else if (type.equals("avg")){
			request.setAttribute("analytic", UserStatistic.getAvgAnalytic());
		} else if (type.equals("sum")){
			request.setAttribute("analytic", UserStatistic.getSumAnalytic());
		} else if (type.equals("user")){
			request.setAttribute("analytic", new UserStatistic(request.getParameter("gtalkId")));
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("analytics.jsp");
		rd.forward(request, response);
	}

}
