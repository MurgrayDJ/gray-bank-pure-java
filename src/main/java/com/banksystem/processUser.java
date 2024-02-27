package com.banksystem;
import java.io.IOException;
import java.io.PrintWriter;

import com.banksystem.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class processUser extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		//Create writer
		PrintWriter pw=response.getWriter();
		response.setContentType("text/html");
		
		//Get parameters and create user object
		String userName = request.getParameter("userName");
		String userPassword = request.getParameter("userPassword");
		String repeatPassword = request.getParameter("repeatPassword");
		User newUser = new User(userName.toLowerCase(), userPassword);
		
		RequestDispatcher dispatcher = null;
		
		if(userPassword.equals(repeatPassword) && userPassword.length() >= 8){
			
				//Establish database connection
				DBHelper dbhelper = new DBHelper();
				dbhelper.createConnection();
				
				//Input new user
				int insertionResult = dbhelper.insertUser(newUser);
				switch(insertionResult) {
					case 1://User created successfully
						//Have them login
						pw.println("<p style='text-align:center'><font color = white>User created successfully. Please login</font></p>");
						dispatcher = request.getRequestDispatcher("/login.html");
						dispatcher.include(request, response);
						break;
					case 2: //User already exists
						//Reload page
						pw.println("<p style='text-align:center'><font color = white>That username is taken.</font></p>");
						dispatcher = request.getRequestDispatcher("/newUser.html");
						dispatcher.include(request, response);
						break;
					case 3: //Something else went wrong
						//Start over at index page
						pw.println("<p style='text-align:center'><font color = white>An error occurred. Please try again later.</font></p>");
						dispatcher = request.getRequestDispatcher("/index.html");
						dispatcher.include(request, response);
						break;
				}
		}
		else if(!userPassword.equals(repeatPassword)){
			pw.println("<p style='text-align:center'>Passwords do not match</font></p>");
			dispatcher = request.getRequestDispatcher("/newUser.html");
			dispatcher.include(request, response);
		}
		else if(userPassword.length() < 8){
			pw.println("<p style='text-align:center'>Password is not long enough</font></p>");
			dispatcher = request.getRequestDispatcher("/newUser.html");
			dispatcher.include(request, response);
		}
	}
}
