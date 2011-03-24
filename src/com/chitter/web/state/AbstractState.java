package com.chitter.web.state;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public abstract class AbstractState implements Serializable {

	private static final long serialVersionUID = 5390407378003624843L;

	protected static final UserService userService = 
		UserServiceFactory.getUserService();
	
	abstract public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	abstract public void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
