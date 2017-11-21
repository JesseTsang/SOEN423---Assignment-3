package common;

import java.util.HashMap;

import domain.BranchID;
import domain.EditRecordField;

public interface BankServerWSInterface
{
	//Manager Operations
	public boolean createAccount(String firstName, String lastName, String address, String phone, String customerID, BranchID branch) throws Exception;
	public boolean editRecord(BranchID branch, String customerID, EditRecordField fieldName, String newValue) throws Exception;
	public HashMap<String, Integer> getAccountCount() throws Exception;
	public boolean transferFund(String sourceID, String destID, double amt) throws Exception;
	
	//Core Operations
	public boolean deposit(BranchID branch, String customerID, double amt) throws Exception;
	public boolean withdraw(BranchID branch, String customerID, double amt) throws Exception;
	public double getBalance(BranchID branch, String customerID) throws Exception;

	//Server Operations
	public void shutdown(BranchID branch);
}
