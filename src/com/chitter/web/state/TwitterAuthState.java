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

public class TwitterAuthState extends AbstractState {

	private static final long serialVersionUID = 8958236520397328935L;

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();

		User user = userService.getCurrentUser();
		session.setAttribute("user", user);
		
		RequestToken requestToken;
		try {
			requestToken = TwitterAPI.getRequestToken();
			session.setAttribute("requestToken",requestToken);
			
			request.setAttribute("twitterLoginUrl", requestToken.getAuthorizationURL());
			
		} catch (TwitterException e) {
			ExceptionPrinter.print(System.err, e, "I couldn't get a request token at TwitterAuthState");
		}
		
	}
	
	public void forward(HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher rd = request.getRequestDispatcher("twitter.jsp");
		try {
			rd.forward(request, response);	
		} catch (Exception e) {
			ExceptionPrinter.print(System.err, e, "I couldn't forwarded TwitterAuthState's response to twitter.jsp");
		}
	}	

}
