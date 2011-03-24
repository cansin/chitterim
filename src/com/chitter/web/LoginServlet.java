package com.chitter.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.web.state.AbstractState;
import com.chitter.web.state.GtalkAuthState;
import com.chitter.web.state.LoggedInState;
import com.chitter.web.state.TwitterAuthState;
import com.chitter.web.state.TwitterReauthState;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	
	private static final UserService userService = 
		UserServiceFactory.getUserService();

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

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		//if(request.getServerName().equals("chitterim.appspot.com")) response.sendRedirect("http://www.chitter.im");
		
		// First Step
		if(!userService.isUserLoggedIn()) {
			System.out.println("Boss, current state is GtalkAuth");
			request.getSession().setAttribute("state", new GtalkAuthState());
		} else {
			//GTalk Login
			User user = userService.getCurrentUser();
			UserAccount userAccount = new UserAccount(user.getEmail());
			
			// Second Step
			if(userAccount == null || userAccount.getGtalkId() == null) {
				System.out.println("Boss, current state is TwitterAuth");
				request.getSession().setAttribute("state", new TwitterAuthState());
			// Fourth Step (Third Step is at TwitterServlet)
			} else {
				try {
					Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
					twitter.getScreenName();
					System.out.println("Boss, current state is LoggedIn");
					request.getSession().setAttribute("state", new LoggedInState());
				} catch (TwitterException e) {
					System.out.println("Boss, current state is TwitterReauth");
					request.getSession().setAttribute("state", new TwitterReauthState());
				}
			}
		}

		System.out.println("Boss, I will processRequest now!");
		AbstractState state = (AbstractState) request.getSession().getAttribute("state");
		state.processRequest(request, response);
		state.forward(request, response);
	}
}
