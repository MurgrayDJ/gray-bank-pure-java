package com.banksystem.model;


public class Account {
	String userName;
	String name;
	String DOB; 
	String address;
	String email;
	String AccountType;
	
	public Account(String userName, String name, String DOB, String address, String email, String accountType) {
		super();
		this.userName = userName;
		this.name = name;
		this.DOB = DOB;
		this.address = address;
		this.email = email;
		this.AccountType = accountType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dOB) {
		DOB = dOB;
	}
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAccountType() {
		return AccountType;
	}
	public void setAccountType(String accountType) {
		AccountType = accountType;
	}
}