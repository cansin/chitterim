package com.chitter.web.state;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.users.User;

public class LoggedInState extends AbstractState {

	private static final long serialVersionUID = 8740066188624690012L;

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();

		User user = userService.getCurrentUser();
		session.setAttribute("user", user);
		UserAccount userAccount = new UserAccount(user.getEmail().toLowerCase());
		
		RequestToken requestToken;
		try {
			requestToken = TwitterAPI.getRequestToken();
			session.setAttribute("requestToken", requestToken);
			
			Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
			String twitterScreenName = twitter.getScreenName();
			request.setAttribute("twitterScreenName", twitterScreenName);
			

			request.setAttribute("gtalkLogoutUrl", "/logout");
			request.setAttribute("twitterAuthenticateUrl", requestToken.getAuthenticationURL()+"&force_login=true");
		
		} catch (TwitterException e) {
			ExceptionPrinter.print(System.err, e, "I couldn't connect to twitter at LoggedInState for"+userAccount.getGtalkId());
		}	
		
	}
	
	public void forward(HttpServletRequest request, HttpServletResponse response)  {
		RequestDispatcher rd = request.getRequestDispatcher("none.jsp");
		try {
			rd.forward(request, response);	
		} catch (Exception e) {
			ExceptionPrinter.print(System.err, e, "I couldn't forwarded LoggedInState's response to none.jsp");
		}
	}	
}
