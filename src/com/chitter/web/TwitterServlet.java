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
import com.chitter.web.state.BotInvitationState;
import com.chitter.web.state.LoggedInState;
import com.chitter.web.state.TwitterAuthState;
import com.chitter.web.state.TwitterReauthState;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class TwitterServlet extends HttpServlet {

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

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalStateException {
		//if(request.getServerName().equals("chitterim.appspot.com")) response.sendRedirect("http://www.chitter.im");

		AbstractState state = (AbstractState)request.getSession().getAttribute("state");
		System.out.println("Boss, previous state was "+state.toString());
		if(state.getClass().equals(TwitterAuthState.class)) {
			state = new BotInvitationState();
			request.getSession().setAttribute("state", state);
		} else if(state.getClass().equals(TwitterReauthState.class)) {
			state = new BotInvitationState();
			request.getSession().setAttribute("state", state);
			state.processRequest(request, response);
			state = new LoggedInState();
			request.getSession().setAttribute("state", state);
		} else if(state.getClass().equals(LoggedInState.class)) {
			state = new BotInvitationState();
			request.getSession().setAttribute("state", state);
			state.processRequest(request, response);
			try {
				Twitter twitter = TwitterAPI.getInstanceFor(new UserAccount(userService.getCurrentUser().getEmail()));
				twitter.getScreenName();
				state = new LoggedInState();
			} catch (TwitterException e) {
				state = new TwitterReauthState();
			}
			request.getSession().setAttribute("state", state);
		} else {
			System.out.println("Boss, I'm redirecting");
			response.sendRedirect("/");
		}
			
		state = (AbstractState) request.getSession().getAttribute("state");
		state.processRequest(request, response);
		state.forward(request, response);
	}
}
