package com.chitter.web.state;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chitter.utility.ExceptionPrinter;

public class GtalkAuthState extends AbstractState {

	private static final long serialVersionUID = -4684344231264002559L;

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("gtalkLoginUrl", userService.createLoginURL(request.getRequestURI()));
	}
	
	public void forward(HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher rd = request.getRequestDispatcher("gtalk.jsp");
		try {
			rd.forward(request, response);
		} catch (Exception e) {
			ExceptionPrinter.print(System.err, e, "I couldn't forwarded GtalkAuthState's response to gtalk.jsp");
		}	
	}	
}
