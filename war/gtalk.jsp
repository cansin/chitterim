<%
	String gtalkLoginUrl = (String)request.getAttribute("gtalkLoginUrl");
%>

<%@include file="WEB-INF/jspf/header.jspf" %>
<div id="content">
	<div class="wrapper">
		<p>Get started with Aspect Chitter bot.</p>
		<div class="step">	
			<img src="img/yellow.png" alt="" >
			<p>Please first login to <a href="<%=gtalkLoginUrl%>">Google</a>.</p>
		</div>
		<div class="step greyedOut">
			<img src="img/grey.png" alt="" >
			<p>Then we will ask your permission at Twitter.</p>
		</div>
		<div class="step greyedOut">
			<img src="img/grey.png" alt="" >
			<p>Finally you will add Aspect Chitter Bot at Gtalk.</p>
		</div>
	</div>
</div>
<%@include file="WEB-INF/jspf/footer.jspf" %>