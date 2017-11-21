package common;

import java.util.HashMap;
import java.util.Hashtable;

import domain.BranchID;
import domain.EditRecordField;
import domain.Server;

public class BankServerWS implements BankServerWSInterface
{
	static Hashtable<String, BankServerImpl> branchDirectory = new Hashtable<String, BankServerImpl>();
	
	//Constructor
	public BankServerWS(BranchID branchID, String host, int port, HashMap<String, Server> serversDir)
	{
		branchDirectory.put(branchID.toString(), new BankServerImpl(branchID, host, port, serversDir));
		
		System.out.println("WS Server Log: | WS Instance Creation | Branch: " + branchID + " | Port : " + port);	
	}

	@Override
	public boolean createAccount(String firstName, String lastName, String address, String phone, String customerID,
	        BranchID branchID) throws Exception
	{
		boolean result = branchDirectory.get(branchID.toString()).createAccount(firstName, lastName, address, phone, customerID, branchID);
		
		if(result == true)
		{
			System.out.println("WS Server Log: | Account Creation Successful | Branch: " + branchID + " | Customer ID: " + customerID);
			
			return true;
		}
		else
		{
			System.out.println("WS Server Log: | Account Creation Error. | Branch: " + branchID + " | Customer ID: " + customerID);
			
			return false;
		}
	}

	@Override
	public boolean editRecord(BranchID branchID, String customerID, EditRecordField fieldName, String newValue)
	        throws Exception
	{
		boolean result = branchDirectory.get(branchID.toString()).editRecord(customerID, fieldName, newValue);
		
		if(result == true)
		{
			System.out.println("WS Server Log: | Account Modify Successful | Branch: " + branchID + " | Customer ID: " + customerID);
			
			return true;
		}
		else
		{
			System.out.println("WS Server Log: | Account Modify Error. | Branch: " + branchID + " | Customer ID: " + customerID);
			
			return false;
		}
	}

	@Override
	public HashMap<String, Integer> getAccountCount() throws Exception
	{
		HashMap<String, Integer> totalActCount = new HashMap<String, Integer>();
		String branchID = "";
		
		for(String branch : branchDirectory.keySet())
		{
			branchID = branch;
		}
		
		totalActCount = branchDirectory.get(branchID).getAccountCount();
		
		return totalActCount;
	}

	@Override
	public boolean transferFund(String sourceID, String destID, double amount) throws Exception
	{
		String sourceBranchID = sourceID.substring(0, 2);
		boolean result = branchDirectory.get(sourceBranchID).transferFund(sourceID, destID, amount);
		
		if(result == true)
		{
			System.out.println("WS Server Log: | Transfer Fund Log: | Fund Transfer Successfully | Source Client ID: " + sourceID 
	                  + " | Destination Client ID: " + destID + " | Amount: $" + amount);
			
			return true;
		}
		else
		{
			System.out.println("WS Server Log: | Transfer Fund Log: | Fund Transfer Unsuccessful | Source Client ID: " + sourceID 
	                  + " | Destination Client ID: " + destID + " | Amount: $" + amount);
			
			return false;
		}
	}

	@Override
	public boolean deposit(BranchID branch, String customerID, double amount) throws Exception
	{
		boolean result = branchDirectory.get(branch).deposit(customerID, amount);
		
		if(result == true)
		{
			System.out.println("WS Server Log: | Deposit Log: | Deposit Successfully | Client ID: " + customerID 
	                  + " | Branch ID: " + branch.toString() + " | Amount: $" + amount);
			
			return true;
		}
		else
		{
			System.out.println("WS Server Log: | Deposit Log: | Deposit Unsuccessfully | Client ID: " + customerID 
	                  + " | Branch ID: " + branch.toString() + " | Amount: $" + amount);
			
			return false;
		}
	}

	@Override
	public boolean withdraw(BranchID branch, String customerID, double amount) throws Exception
	{
		boolean result = branchDirectory.get(branch).withdraw(customerID, amount);
		
		if(result == true)
		{
			System.out.println("WS Server Log: | Withdraw Log: | Withdraw Successfully | Client ID: " + customerID 
	                  + " | Branch ID: " + branch.toString() + " | Amount: $" + amount);
			
			return true;
		}
		else
		{
			System.out.println("WS Server Log: | Withdraw Log: | Withdraw Unsuccessfully | Client ID: " + customerID 
	                  + " | Branch ID: " + branch.toString() + " | Amount: $" + amount);
			
			return false;
		}
	}

	@Override
	public double getBalance(BranchID branch, String customerID) throws Exception
	{		
		double result = 0; 
		
		try
		{
			result = branchDirectory.get(branch).getBalance(customerID);
		}
		catch (Exception e)
		{
			System.out.println("WS Server Log: | Withdrawl Error: | Unable to locate account. | Customer ID: " + customerID + " | Branch ID: " + branch.toString());
			throw new Exception("WS Server Log: | Withdrawl Error: | Unable to locate account. | Customer ID: " + customerID + " | Branch ID: " + branch.toString());
		}

		return result;
	}

	@Override
	public void shutdown(BranchID branch)
	{
		branchDirectory.get(branch).shutdown();
	}
}
