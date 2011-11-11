<%@page import="com.chitter.persistence.UserStatistic" %>
<%@page import="com.chitter.persistence.UserAccount" %>
<%@page import="java.util.Arrays" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Set" %>
<%
	UserStatistic analytic = (UserStatistic)request.getAttribute("analytic");
%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="SHORTCUT ICON" href="/favicon.ico">
<link rel="icon" 
      type="image/ico" 
      href="/favicon.ico" />
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.2.0/build/cssbase/base-min.css">
<link rel="stylesheet" type="text/css" href="/style.css">
<title>Chitter.im: a Gtalk bot for Twitter</title>
</head>
<body>

<div id="headerLine">
	<div class="wrapper">
		<a href="/">Home</a> <a href="http://blog.chitter.im">Blog</a> <span>Analytics</span>
	</div>
</div>

<div id="container">
<div id="header">
	<div class="wrapper">
		<div id="logo">
			<a href="/"><img src="img/logo.png" alt="Chitter.im" ></a>
		</div>
		<div id="explanation">
			<p>
				<span class="chitter">Chitter.im</span> is a Gtalk bot that allows you to communicate with Twitter very simply. 
				<img src="img/doodle.png" alt="Chitter.im" >
			</p>
		</div>
	</div>
</div>
<div id="content">
	<% if(analytic!=null) { %>
		<%
		String vals=new String();
		String labels=new String();
		float[] valArr=analytic.getStatistics();
		float[] valArrSorted=analytic.getStatistics();
		String[] labelsArr=UserStatistic.statisticsLabels;
		float valMax=0;
		for(int i=0;i<valArr.length;i++){
			if(valArr[i]>valMax) valMax=valArr[i];
			vals+=valArr[i]+",";
			labels+="|"+labelsArr[i];
		}
		vals=vals.substring(0,vals.length()-1);
		int chartWidth = 1000;
		int barWidth = (int)(chartWidth/labelsArr.length)/2;
		String imgSrc="http://chart.apis.google.com/chart?cht=bvg&chco=4D89F9&chd=t:"+vals+"&chs="+chartWidth+"x250&chxt=x,y&chxl=0:"+labels+"&chds=0,"+valMax+"&chxr=1,0,"+valMax+"&chbh="+barWidth+",0,"+barWidth;
		imgSrc=imgSrc.replace(" ","%20");
		%>
		<h1>Analytics result for <%= analytic.getGtalkId() %> </h1>
		<img src=<%= imgSrc %> alt="chart"/>
	<% } else { %>
			<%
				@SuppressWarnings("unchecked")
				Set<String> onlineUsers = (Set<String>)request.getAttribute("onlineUsers");
				@SuppressWarnings("unchecked")
				List<UserAccount> timelineActiveUsers = (List<UserAccount>)request.getAttribute("timelineActiveUsers");
				@SuppressWarnings("unchecked")
				List<UserAccount> users = (List<UserAccount>)request.getAttribute("users");
			%>
			<h1> Welcome to analytics. </h1>
			<p> <b>User Count:</b> <%=users.size()%></p>
			<p> <b>TimelineActive User Count:</b> <%=timelineActiveUsers.size()%></p>
			<p> <b>Online User Count:</b> <%=onlineUsers.size()%></p>
		
	<% } %>
	
</div>

<div id="footer">
	<div class="wrapper">
		<p>
			<a href="analytics?type=avg">Avg</a>
			<a href="analytics?type=sum">Sum</a>
		</p>
		
		<form name="user" action="analytics" method="get">
			<p>Gtalk Id: <input type="text" name="gtalkId" />
			<input type="submit" value="Submit" />
			<input type="hidden" name="type" value="user" /></p>
		</form>
	</div>
</div>
<div id="footerLine">
	<div class="wrapper">
		<p>This site built with Eclipse and Google App Engine. 
		Created and designed by <a href="http://cs.bilkent.edu.tr/~cansin">Cansin Yildiz</a>.</p>
	</div>
</div>
</div>

</body>
</html>