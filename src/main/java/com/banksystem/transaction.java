package com.banksystem;
import java.io.IOException;
import java.io.PrintWriter;

import com.banksystem.model.Transaction;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class transaction extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		
		//Create writer
		PrintWriter pw=response.getWriter();
		response.setContentType("text/html");
		HttpSession mySession = request.getSession(false);
		RequestDispatcher dispatcher = null;
		
		if(mySession != null) {
			//Get parameters
			int accountNumber=Integer.valueOf(request.getParameter("accountNumber"));
			String amount=request.getParameter("amount");
			String transactionType=request.getParameter("transaction_type");
			String userName=mySession.getAttribute("userName").toString();
			//
			Transaction newTransaction = new Transaction(transactionType, Double.valueOf(amount), accountNumber, userName); 
			
			//Establish database connection
			DBHelper dbhelper = new DBHelper();
			dbhelper.createConnection();		
			int canTransact = dbhelper.doTransaction(newTransaction);
			
			switch(canTransact) {
				case 1: //Transaction completed successfully
					pw.println("<p style='text-align:center'><font color = white>Transaction completed successfully.</font></p>");
					dispatcher = request.getRequestDispatcher("/MainMenu.html");
					dispatcher.include(request, response);
					break;
				case 2://Transaction completed successfully, but balance < $100
					pw.println("<p style='text-align:center'><font color = white>Transaction completed. WARNING: Balance less than $100</font></p>");
					dispatcher = request.getRequestDispatcher("/MainMenu.html");
					dispatcher.include(request, response);
					break;
				default: //Something went wrong
					pw.println("<p style='text-align:center'><font color = white>Transaction failed. Please try again.</font></p>");
					dispatcher = request.getRequestDispatcher("/transact.html");
					dispatcher.include(request, response);
					break;
			}
		}
		else {
			pw.println("<p style='text-align:center'><font color = white>Transaction failed. Please try again.</font></p>");
			dispatcher = request.getRequestDispatcher("/transact.html");
			dispatcher.include(request, response);
		}

	}	
}