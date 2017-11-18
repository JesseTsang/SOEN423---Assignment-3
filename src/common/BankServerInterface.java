package common;

import java.util.HashMap;

import domain.BranchID;
import domain.EditRecordField;

public interface BankServerInterface
{
	//Manager Operations
	public boolean createAccount(String firstName, String lastName, String address, String phone, String customerID, BranchID branch) throws Exception;
	public boolean editRecord(String customerID, EditRecordField fieldName, String newValue) throws Exception;
	public HashMap<String, String> getAccountCount() throws Exception;
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