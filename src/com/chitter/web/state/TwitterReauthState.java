package com.chitter.web.state;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.TwitterException;
import twitter4j.http.RequestToken;

import com.chitter.external.TwitterAPI;
import com.google.appengine.api.users.User;

public class TwitterReauthState extends AbstractState {

	private static final long serialVersionUID = 4272556448532779933L;

	@Override
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Boss, current state is "+toString());
		HttpSession session = request.getSession();

		User user = userService.getCurrentUser();
		session.setAttribute("user", user);
		
		RequestToken requestToken;
		try {
			requestToken = TwitterAPI.getRequestToken();
			String token = requestToken.getToken();
			String tokenSecret = requestToken.getTokenSecret();
			session.setAttribute("token", token);
			session.setAttribute("tokenSecret", tokenSecret);
			
			request.setAttribute("reauthorize", "reauthorize");

			request.setAttribute("gtalkLogoutUrl", userService.createLogoutURL(request.getRequestURI()));
			request.setAttribute("twitterAuthenticateUrl", requestToken.getAuthenticationURL()+"&force_login=true");
		} catch (TwitterException e) {
			System.err.println("---------------------------------------------------");
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
