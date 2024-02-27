package com.banksystem;
import java.io.IOException;
import java.io.PrintWriter;

import com.banksystem.model.Account;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class createAccount extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		
		//Create writer
		PrintWriter pw=response.getWriter();
		response.setContentType("text/html");
		HttpSession mySession = request.getSession(false);
		RequestDispatcher dispatcher = null;
		
		if(mySession != null) {
			//Get parameters
			String userName=request.getSession().getAttribute("userName").toString().toLowerCase();
			String name=request.getParameter("Name");
			String DOB=request.getParameter("DOB");
			String address=request.getParameter("Address");
			String email=request.getParameter("Email");
			String accountType=request.getParameter("accountTypes");
			
			//
			Account newAccount = new Account(userName, name, DOB, address, email, accountType); 
			
			//Establish database connection
			DBHelper dbhelper = new DBHelper();
			dbhelper.createConnection();		
			int insertResult = dbhelper.canCreate(newAccount);
			
			switch(insertResult) {
				case 1: //Account created successfully
					pw.println("<p style='text-align:center'><font color = white>Account created successfully.</font></p>");
					dispatcher = request.getRequestDispatcher("/MainMenu.html");
					dispatcher.include(request, response);
					break;
				default: //Account exists already or something else went wrong
					pw.println("<p style='text-align:center'><font color = white>Account creation failed. Please try again.</font></p>");
					dispatcher = request.getRequestDispatcher("/createAccount.html");
					dispatcher.include(request, response);
					break;
			}
		}
		else {
			pw.println("<p style='text-align:center'><font color = white>Account creation failed. Please try again.</font></p>");
			dispatcher = request.getRequestDispatcher("/createAccount.html");
			dispatcher.include(request, response);
		}

	}	
}