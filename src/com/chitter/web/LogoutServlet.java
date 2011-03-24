package com.chitter.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {
		
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
		request.getSession().invalidate();
		System.out.println("I'm logging out");
		response.sendRedirect(userService.createLogoutURL("/"));
	}
}
