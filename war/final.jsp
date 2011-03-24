<%@page import="com.google.appengine.api.users.User" %>
<%
	User user = (User) session.getAttribute("user");
	String twitterScreenName = (String)request.getAttribute("twitterScreenName");
%>

<%@include file="WEB-INF/jspf/header.jspf" %>
<div id="content">
	<div class="wrapper">
		<p>Get started with Chitter.im bot.</p>
		<div class="step">	
			<img src="img/green.png" alt="" >
			<p>Thank you. You're now signed in as <b><%=user.getEmail()%></b>.</p>
		</div>
		<div class="step">
			<img src="img/green.png" alt="" >
			<p>Your twitter username is <b><%=twitterScreenName%></b>.</p>
		</div>
		<div class="step">
			<img src="img/yellow.png" alt="" >
			<p>Please add <b>chitterim@appspot.com</b> to your contacts at Gtalk.</p>
		</div>
	</div>
</div>
<%@include file="WEB-INF/jspf/footer.jspf" %>