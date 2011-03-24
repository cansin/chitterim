<%@ page import="com.google.appengine.api.users.User" %>
<%
	String twitterLoginUrl = (String)request.getAttribute("twitterLoginUrl");
	User user = (User) session.getAttribute("user");
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
			<img src="img/yellow.png" alt="" >
			<p>Please authorize Chitter.im at <a href="<%=twitterLoginUrl%>">Twitter</a>.
		</div>
		
		<div class="step greyedOut">
			<img src="img/grey.png" alt="" >
			<p>Finally you will add Chitter.im Bot at Gtalk.</p>
		</div>
	</div>
</div>
<%@include file="WEB-INF/jspf/footer.jspf" %>