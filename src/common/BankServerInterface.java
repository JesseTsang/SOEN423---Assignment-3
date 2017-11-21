package common;

import java.util.Hashtable;


public interface BankServerInterface
{
	//Manager Operations
	public boolean createAccount(String firstName, String lastName, String address, String phone, String customerID, String branch) throws Exception;
	public boolean editRecord(String customerID, String fieldName, String newValue) throws Exception;
	public Hashtable<String, Integer> getAccountCount() throws Exception;
	public boolean transferFund(String customerID1, String customerID2, double amt) throws Exception;
	
	//Core Operations
	public boolean deposit(String customerID, double amt) throws Exception;
	public boolean withdraw(String customerID, double amt) throws Exception;
	public double getBalance(String customerID) throws Exception;
	
	//Misc Operations
	public int getLocalAccountCount();
	
	//Server Operations
	public void shutdown();
}