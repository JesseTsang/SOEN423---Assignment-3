package common;

import java.util.Map;


public interface BankServerWSInterface
{
	//Manager Operations
	public boolean createAccount(String firstName, String lastName, String address, String phone, String customerID, String branch) throws Exception;
	public boolean editRecord(String branch, String customerID, String fieldName, String newValue) throws Exception;
	//public Map<String, Integer> getAccountCount() throws Exception;
	public boolean transferFund(String sourceID, String destID, double amt) throws Exception;
	
	//Core Operations
	public boolean deposit(String branch, String customerID, double amt) throws Exception;
	public boolean withdraw(String branch, String customerID, double amt) throws Exception;
	public double getBalance(String branch, String customerID) throws Exception;

	//Server Operations
	public void shutdown(String branch);
}
