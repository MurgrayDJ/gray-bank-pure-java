package com.banksystem.model;

public class Transaction {
	String transactionType;
	double amount;
	int accountNumber;
	String userName;
	
	public Transaction(String transactionType, double amount, int accountNumber, String userName) {
		super();
		this.transactionType = transactionType;
		this.amount = amount;
		this.accountNumber = accountNumber;
		this.userName = userName;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
