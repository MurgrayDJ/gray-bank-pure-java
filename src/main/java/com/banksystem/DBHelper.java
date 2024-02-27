package com.banksystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

import com.banksystem.model.Account;
import com.banksystem.model.Transaction;
import com.banksystem.model.User;


public class DBHelper {

Connection con;
	
	// Constructor 
	public DBHelper() {
		try {
			//Load the Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			System.out.println(">> Exception is : " + e);
		}
	}
	
	//Create a connection to the database
	public void createConnection() {
		try {
			//Connect to the database
			String url = "jdbc:mysql://localhost:3306/bankDB?";
			String user = " ";
			String password = " ";
			con = DriverManager.getConnection(url, user, password);
			System.out.println(">> Database connection established");
		} catch (Exception e) {
			System.out.println(">> Exception is : " + e);
		}
	}
	
	//Insert a new user
	public int insertUser(User newUser) {	
		try {		
			String sql = "INSERT INTO user_pass(userID, userPassword) VALUES(?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, newUser.userName);
			stmt.setString(2, newUser.password);
			stmt.executeUpdate();
			return 1;
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return 2;
		} catch (Exception e) {
			e.printStackTrace();
			return 3;
		}
	}
	
	//Check if account can be added to database
	public int canCreate(Account account) {
		try {		
			String sql = "INSERT INTO account_details(userID, fullName, DOB, Address, EmailID, AccountType) "
					+ "VALUES(?, ?, ?, ?, ? ,?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, account.getUserName());
			stmt.setString(2, account.getName());
			stmt.setString(3, account.getDOB());
			stmt.setString(4, account.getAddress());
			stmt.setString(5, account.getEmail());
			stmt.setString(6, account.getAccountType());
			stmt.executeUpdate();
			return 1;
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return 2;
		} catch (Exception e) {
			e.printStackTrace();
			return 3;
		}
	}
	
	//Check if user and password combination exists
	public boolean canLogin(String userID, String userPassword) {		
			try{
				//Get user information
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM user_pass WHERE userID = '" 
				+ userID + "' and userPassword = '" + userPassword + "' ");
				
				rs.next();
				if(rs.getString("userID").equalsIgnoreCase(userID) 
						&& rs.getString("userPassword").equals(userPassword)){
					return true;
				}
				return false;
			}
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
	}
	
	
	//Get all transactions
	public ArrayList<StringBuilder> getTransactions(String userName, String Query) {		
		ArrayList<StringBuilder> results = new ArrayList<StringBuilder>();
		
	    try {
	    	createConnection();
	    	String query = Query;
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
		  		StringBuilder oneResult = new StringBuilder("Transaction Date: ");
		    	
		    	oneResult.append(rs.getDate("TransactionDate"));
		    	oneResult.append(" [Amount=" + rs.getDouble("Amount") + ",");
		    	oneResult.append(" Type:" + rs.getString("TransactionType") + ",");
		    	oneResult.append(" TransactionID:" + rs.getInt("TransactionID") + ",");
		    	oneResult.append(" AccountNumber:" + rs.getInt("AccountNumber") + "]");
		    	
		    	results.add(oneResult);
	        }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    
	    return results;
	}
	
	public ArrayList<StringBuilder> noTransact(String userName) {
		return(getTransactions(userName, ("SELECT * FROM transactions WHERE userID = '" + userName + "'")));
	}
	
	public ArrayList<StringBuilder> fromTransact(String userName, String startDate) {
	    return(getTransactions(userName, ("SELECT * FROM transactions WHERE userID = '" + userName 
	    		+ "' AND TransactionDate >= '" + startDate + "'")));
	}
	
	public ArrayList<StringBuilder> toTransact(String userName, String endDate) {
	    return(getTransactions(userName, ("SELECT * FROM transactions WHERE userID = '" + userName 
	    		+ "' WHERE TransactionDate <= '" + endDate + "'")));
	}
	
	public ArrayList<StringBuilder> bothTransact(String userName, String startDate, String endDate) {
	    return(getTransactions(userName, ("SELECT * FROM transactions WHERE userID = '" + userName + "' AND TransactionDate >= '" 
	    		+ startDate + "' AND TransactionDate <= '" + endDate + "'")));
	}
	
	public int doTransaction(Transaction newTransaction) {
		try {		
			String sql = "INSERT INTO transactions(TransactionType, Amount, AccountNumber, TransactionDate, userID) "
					+ "VALUES(?, ?, ?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			String transType = newTransaction.getTransactionType();
			double amount = newTransaction.getAmount();
			int accountNum = newTransaction.getAccountNumber();
			String userName = newTransaction.getUserName();
			
			long millis=System.currentTimeMillis();  
			java.sql.Date todaysDate=new java.sql.Date(millis);  
			
			stmt.setString(1, transType);
			stmt.setDouble(2, amount);
			stmt.setInt(3, accountNum);
			stmt.setDate(4, todaysDate);
			stmt.setString(5, userName);
			stmt.executeUpdate();
			
			String query = "SELECT * FROM account_details WHERE AccountNumber = '" + accountNum + "';";
		    ResultSet rs = stmt.executeQuery(query);
		    rs.next(); 
		    
		    double accountTotal = rs.getDouble("AccountTotal");
			String update;
			double finalTotal;
			
			if(transType.equalsIgnoreCase("withdraw")) { //A withdrawal
				finalTotal = accountTotal - amount;
			}
			else { //It's a deposit
				finalTotal = accountTotal + amount;
			}
			
			if(finalTotal < 100) {
				return 2; //Send a warning message
			}
			update = "UPDATE account_details SET AccountTotal = '" + finalTotal + "' WHERE AccountNumber = '" + accountNum + "';";
			stmt.execute(update);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 3;
		}
	}
	
	//Displays accounts and their current balances
	public ArrayList<StringBuilder> getAccounts(String userName){
		ArrayList<StringBuilder> accounts = new ArrayList<StringBuilder>();
		
		try {
			createConnection();
			String query = "SELECT * FROM account_details WHERE userID = '" + userName + "'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				StringBuilder oneResult = new StringBuilder("Account Number: ");
				//Get account number
				int accountNumber = rs.getInt("AccountNumber");
				oneResult.append(accountNumber);
				//Get balance of account
				String balanceQuery = "SELECT * FROM account_details WHERE AccountNumber = " + accountNumber;
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery(balanceQuery);
				
				rs2.next();
				oneResult.append(" Balance:" + rs2.getDouble("AccountTotal"));
				accounts.add(oneResult);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return accounts;
	}
	
	//Close database connection
	public void closeConnection() {
		try {
			con.close();
			System.out.println(">> Database connection terminated");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}