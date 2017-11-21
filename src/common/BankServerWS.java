package common;

import java.util.Hashtable;
import java.util.Map;

import javax.jws.WebService;

import client.BankServerDriver;


@WebService(
		name = "BankServerWS",
		serviceName = "BankServerWS",
		portName = "BankServerWS",
		targetNamespace = "http://localhost/BankWS"
		)

public class BankServerWS implements BankServerWSInterface
{
	static Hashtable<String, BankServerImpl> branchDirectory = new Hashtable<String, BankServerImpl>();
	
	//Constructor
	public BankServerWS()
	{
		BankServerDriver newWS = new BankServerDriver();
		//HashMap == not synchronized
		//HashTable == synchronized
		Map <String, BankServerImpl> serversList = newWS.getServersList();
	
		for(String serverName : serversList.keySet())
		{
			BankServerImpl server = serversList.get(serverName);
			branchDirectory.put(serverName, server);
			
			System.out.println("WS Server Log: | WS Instance Creation | Branch: " + server.getBranchID() + " | Port : " + server.getUDPPort());	
		}	
	}

	@Override
	public boolean createAccount(String firstName, String lastName, String address, String phone, String customerID,
			String branchID) throws Exception
	{
		boolean result = branchDirectory.get(branchID).createAccount(firstName, lastName, address, phone, customerID, branchID);
		
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
	public boolean editRecord(String branchID, String customerID, String fieldName, String newValue)
	        throws Exception
	{
		boolean result = branchDirectory.get(branchID).editRecord(customerID, fieldName, newValue);
		
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
	public Hashtable<String, Integer> getAccountCount() throws Exception
	{
		Hashtable<String, Integer> totalActCount = new Hashtable<String, Integer>();
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
	public boolean deposit(String branchID, String customerID, double amount) throws Exception
	{
		boolean result = branchDirectory.get(branchID).deposit(customerID, amount);
		
		if(result == true)
		{
			System.out.println("WS Server Log: | Deposit Log: | Deposit Successfully | Client ID: " + customerID 
	                  + " | Branch ID: " + branchID + " | Amount: $" + amount);
			
			return true;
		}
		else
		{
			System.out.println("WS Server Log: | Deposit Log: | Deposit Unsuccessfully | Client ID: " + customerID 
	                  + " | Branch ID: " + branchID + " | Amount: $" + amount);
			
			return false;
		}
	}

	@Override
	public boolean withdraw(String branchID, String customerID, double amount) throws Exception
	{
		boolean result = branchDirectory.get(branchID).withdraw(customerID, amount);
		
		if(result == true)
		{
			System.out.println("WS Server Log: | Withdraw Log: | Withdraw Successfully | Client ID: " + customerID 
	                  + " | Branch ID: " + branchID + " | Amount: $" + amount);
			
			return true;
		}
		else
		{
			System.out.println("WS Server Log: | Withdraw Log: | Withdraw Unsuccessfully | Client ID: " + customerID 
	                  + " | Branch ID: " + branchID + " | Amount: $" + amount);
			
			return false;
		}
	}

	@Override
	public double getBalance(String branchID, String customerID) throws Exception
	{		
		double result = 0; 
		
		try
		{
			result = branchDirectory.get(branchID).getBalance(customerID);
		}
		catch (Exception e)
		{
			System.out.println("WS Server Log: | Withdrawl Error: | Unable to locate account. | Customer ID: " + customerID + " | Branch ID: " + branchID);
			throw new Exception("WS Server Log: | Withdrawl Error: | Unable to locate account. | Customer ID: " + customerID + " | Branch ID: " + branchID);
		}

		return result;
	}

	@Override
	public void shutdown(String branch)
	{
		branchDirectory.get(branch.toString()).shutdown();
	}
}
