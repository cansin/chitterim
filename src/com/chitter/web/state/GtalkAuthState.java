package com.chitter.web.state;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GtalkAuthState extends AbstractState {

	private static final long serialVersionUID = -4684344231264002559L;

	@Override
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{

		System.out.println("Boss, I started processRequest routine of GtalkAuthState");
		request.setAttribute("gtalkLoginUrl", userService.createLoginURL(request.getRequestURI()));
	}
	
	public void forward(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Boss, current state is "+toString());
		RequestDispatcher rd = request.getRequestDispatcher("gtalk.jsp");
		rd.forward(request, response);	
	}	
}
