<%@ page import="com.google.appengine.api.users.User" %>
<%
	User user = (User) session.getAttribute("user");
	String twitterAuthenticateUrl = (String)request.getAttribute("twitterAuthenticateUrl");
	String gtalkLogoutUrl = (String)request.getAttribute("gtalkLogoutUrl");
	String twitterScreenName = (String) request.getAttribute("twitterScreenName");
	String reauthorize = (String) request.getAttribute("reauthorize");
%>

<%@include file="WEB-INF/jspf/header.jspf" %>
<div id="content">
	<div class="wrapper">
		<div class="step">
			<%
				if(reauthorize!=null && reauthorize.equals("reauthorize")){ 
					out.println("<img src=\"img/red.png\" alt=\"\" >");
				} else {
					out.println("<img src=\"img/blue.png\" alt=\"\" >");
				}
				
				out.println("<p class=\"nomargin\">Welcome back, <b>"+user.getEmail()+"</b> <a class=\"tiny\" href=\" "+ gtalkLogoutUrl +"\">logout</a>.</p>");
				if(reauthorize!=null && reauthorize.equals("reauthorize")){
					out.println("<p class=\"nomargin\">You should reauthorize Aspect Chitter at <a href=\" "+ twitterAuthenticateUrl +"\">Twitter</a>.</p>");
				} else {
					out.println("<p class=\"nomargin\">Your current twitter account is <b>"+twitterScreenName+"</b> <a class=\"tiny\" href=\" "+ twitterAuthenticateUrl +"\">change</a>.</p>");
				}
			%>
		</div>
	</div>
</div>
<%@include file="WEB-INF/jspf/footer.jspf" %>