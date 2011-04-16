package com.chitter.web.state;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.google.appengine.api.users.User;

public class LoggedInState extends AbstractState {

	private static final long serialVersionUID = 8740066188624690012L;

	@Override
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Boss, current state is "+toString());
		HttpSession session = request.getSession();

		User user = userService.getCurrentUser();
		session.setAttribute("user", user);
		UserAccount userAccount = new UserAccount(user.getEmail());
		
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
			System.err.println("-----------------Logged-in-State-------------------");
			System.err.println(e);
			for(int i=0;i<e.getStackTrace().length;i++)
				System.err.println(e.getStackTrace()[i].toString());
			System.err.println("---------------------------------------------------");
		}	
		
	}
	
	public void forward(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("none.jsp");
		rd.forward(request, response);
	}	
}
