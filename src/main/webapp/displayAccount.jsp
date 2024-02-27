<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.sql.*, java.util.*, com.banksystem.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Account Statement</title>
<link rel="stylesheet" href="baseStyle.css">
</head>
<body>
<h2>Account Statement</h2>
<h3><font color = white>Existing Accounts</font></h3>
<%
	DBHelper dbhelper = new DBHelper();
	String userName=request.getSession(false).getAttribute("userName").toString();
	ArrayList<StringBuilder> accounts;
	accounts = dbhelper.getAccounts(userName.toLowerCase());
	if(!accounts.isEmpty()){
		for(StringBuilder account : accounts){
			out.println("<p><font color = white>" + account + "</font></p>");
		}
	}
	else{
		out.println("<p><font color = white>No existing accounts yet</font></p>");
	}
%>
	<form>
	<label for="date"><font face="Noto Serif" size="2px">Start Date:</font></label>
	<input type="date" name="startDate" id="startDate">
		
	
	<label for="endDate"><font face="Noto Serif" size="2px">End Date:</font></label>	
	<input type="date" name="endDate" id = "endDate" >
		
	
	<input type="submit" value="Generate Statement">
	</form>
	
<%
	ArrayList<StringBuilder> results;
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	
	if(startDate == null && endDate == null){
		results = dbhelper.noTransact(userName.toLowerCase());	
	}
	else if(!(startDate == null) && endDate == null){
		results = dbhelper.fromTransact(userName.toLowerCase(), startDate);
	}
	else if(startDate == null && !(endDate == null)){
		results = dbhelper.toTransact(userName.toLowerCase(), endDate);
	}
	else{
		results = dbhelper.bothTransact(userName.toLowerCase(), startDate, endDate);
	}
	
	for (StringBuilder result : results){
		out.println("<p><font color = white>" + result + "</font></p>");
	}
%>
</body>
</html>