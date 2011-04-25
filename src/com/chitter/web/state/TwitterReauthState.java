package com.chitter.web.state;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

import com.chitter.external.TwitterAPI;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.users.User;

public class TwitterReauthState extends AbstractState {

	private static final long serialVersionUID = 4272556448532779933L;

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();

		User user = userService.getCurrentUser();
		session.setAttribute("user", user);
		
		RequestToken requestToken;
		try {
			requestToken = TwitterAPI.getRequestToken();
			session.setAttribute("requestToken", requestToken);
			
			request.setAttribute("reauthorize", "reauthorize");

			request.setAttribute("gtalkLogoutUrl", userService.createLogoutURL(request.getRequestURI()));
			request.setAttribute("twitterAuthenticateUrl", requestToken.getAuthenticationURL()+"&force_login=true");
		} catch (TwitterException e) {
			ExceptionPrinter.print(System.err, e, "I couldn't get twitter request token at TwitterReauthState");
		}	
	}
	
	public void forward(HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher rd = request.getRequestDispatcher("none.jsp");
		try {
			rd.forward(request, response);	
		} catch (Exception e) {
			ExceptionPrinter.print(System.err, e, "I couldn't forwarded TwitterReauthState's response to none.jsp");
		}
	}	
}
