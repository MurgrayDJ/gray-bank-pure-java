package com.banksystem;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//@WebServlet("/Login")
public class processLogin extends HttpServlet{
	private static final long serialVersionUID = 1349885L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		
		//Create writer
		PrintWriter pw=response.getWriter();
		response.setContentType("text/html");
		
		//Get parameters
		String userName=request.getParameter("userName");
		String userPassword=request.getParameter("userPassword");
		RequestDispatcher dispatcher = null;
		
		//Establish database connection
		DBHelper dbhelper = new DBHelper();
		dbhelper.createConnection();
		
		if(dbhelper.canLogin(userName, userPassword)) {
			
			//Create cookie and session object
			Cookie userCookie = new Cookie("userName", userName);
			userCookie.setMaxAge(60*60*48);
			response.addCookie(userCookie);
			HttpSession session = request.getSession();
			session.setAttribute("userName", userName);
			session.setAttribute("userPassword", userPassword);
			
			//Forward request and response information
			dispatcher = request.getRequestDispatcher("/MainMenu.html");
			dispatcher.forward(request, response);
		}
		else {
			pw.println("<p style='text-align:center'><font color=white>Invalid username or password</font></p>");
			dispatcher = request.getRequestDispatcher("/login.html");
			dispatcher.include(request, response);
		}
	}
}