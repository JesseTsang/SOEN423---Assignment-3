package common;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import domain.BranchID;
import domain.Client;
import domain.EditRecordField;
import domain.Server;

import udp.UDPServer;


public class BankServerImpl implements BankServerInterface
{
	//Variable for each separate bank server
	private BranchID branchID;
	private String UDPHost;
	private int UDPPort;
	private Logger logger = null;
	
	//Variable to contact other servers
	private UDPServer UDPServer;
	
	//Holds other servers' addresses : ["ServerName", "hostName:portNumber"]
	HashMap<String, Server> serversList = new HashMap<String, Server>();
	
	private int clientCount;
	private Map<String, ArrayList<Client>> clientList = new HashMap<String, ArrayList<Client>>();
		
	private static final int CLIENT_NAME_INI_POS = 3;
	
	//Constructor
	public BankServerImpl(BranchID branchID, String host, int port, HashMap<String, Server> serversDir)
	{
		this.branchID = branchID;
		this.UDPHost = host;
		this.UDPPort = port;	
		this.serversList = serversDir;
			
		this.UDPServer = new UDPServer(UDPHost, UDPPort, this);
		
		this.clientCount = 0;
		
		//1.1 Logging Initiation
		this.logger = this.initiateLogger();
		
		this.logger.info("Server Log: | BankServerImpl Server Instance Creation | Branch: " + branchID + " | Port : " + UDPPort);
		System.out.println("Server Log: | BankServerImpl Server Instance Creation | Branch: " + branchID + " | Port : " + UDPPort);	
	}
	
	private Logger initiateLogger() 
	{
		Logger logger = Logger.getLogger("Server Logs/" + this.branchID + "- Server Log");
		FileHandler fh;
		
		try
		{
			//FileHandler Configuration and Format Configuration
			fh = new FileHandler("Server Logs/" + this.branchID + " - Server Log.log");
			
			//Disable console handling
			logger.setUseParentHandlers(false);
			logger.addHandler(fh);
			
			//Formatting configuration
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		}
		catch (SecurityException e)
		{
			System.err.println("Server Log: Error: Security Exception " + e);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("Server Log: Error: IO Exception " + e);
			e.printStackTrace();
		}
		
		System.out.println("Server Log: | BankServerImpl Initialization | Logging Initialization Successed.");
		System.out.println("Server Log: | BankServerImpl Initialization | Server ID: " + branchID.toString() + " | Port : " + UDPPort);
		
		return logger;
	}

	@Override
	public synchronized boolean createAccount(String firstName, String lastName, String address, String phone, String customerID,
	        BranchID branchID) throws Exception
	{
		this.logger.info("Initiating user account creation for " + firstName + " " + lastName);
		
		//If the user IS at the right branch ... we start the operation.
		if (this.branchID == branchID)
		{
			//Each character will be a key, each key will starts with 10 buckets.
			String key = Character.toString((char)customerID.charAt(CLIENT_NAME_INI_POS));
			ArrayList<Client> values = new ArrayList<Client>(10);
			
			//If a key doesn't exist ... for example no client with last name Z ... then ...
			if(!clientList.containsKey(key))
			{
				//Adds a key and 10 buckets
				clientList.put(key, values);
			}
			
			//This is to test if the account is already exist.
			//1. Extract the buckets of the last Name ... for example, we will get all the clients with last name start with Z
			values = clientList.get(key);
			
			//2. After extracted the buckets, we loops to check if the customerID is already exist.
			for (Client client: values)
			{
				if (client.getCustomerID().equals(customerID))
				{
					this.logger.severe("Server Log: | Account Creation Error: Account Already Exists | Customer ID: " + customerID);
					System.out.println("Server Log: | Account Creation Error: Account Already Exists | Customer ID: " + customerID);
					
					return false;
				}
			}
			
			//3. If no existing account found, then we create a new account.
			Client newClient;
			
			try
			{
				newClient = new Client(firstName, lastName, address, phone, customerID, branchID);
				values.add(newClient);
				clientList.put(key, values);
				
				this.logger.info("Server Log: | Account Creation Successful | Customer ID: " + customerID);
				System.out.println("Server Log: | Account Creation Successful | Customer ID: " + customerID);
				this.logger.info(newClient.toString());
				
				clientCount++;
				
				return true;
			}
			catch (Exception e)
			{
				this.logger.severe("Server Log: | Account Creation Error. | Customer ID: " + customerID + " | " + e.getMessage());
				System.out.println("Server Log: | Account Creation Error. | Customer ID: " + customerID + " | " + e.getMessage());
				
				return false;
			}	
		}//end if clause ... if not the same branch
		else
		{
			this.logger.severe("Server Log: | Account Creation Error: BranchID Mismatch | Customer ID: " + customerID);
			System.out.println("Server Log: | Account Creation Error: BranchID Mismatch | Customer ID: " + customerID);
			
			return false;
		}
	}

	@Override
	public synchronized boolean editRecord(String customerID, EditRecordField fieldName, String newValue) throws Exception
	{
		//1. Check if such client exist.
		String key = Character.toString((char)customerID.charAt(CLIENT_NAME_INI_POS));
		ArrayList<Client> values = clientList.get(key);
		
		for (Client client: values)
		{
			//1.1 Client Found
			if (client.getCustomerID().equals(customerID))
			{		
				switch(fieldName)
				{
					case address:
						client.setAddress(newValue);
						this.logger.info("Server Log: | Edit Record Log: Address Record Modified Successful | Customer ID: " + client.getCustomerID());
						return true;
					
					case phone:
						try
						{
							if(client.verifyPhoneNumber(newValue))
							{
								client.setPhoneNumber(newValue);
								this.logger.info("Server Log: | Edit Record Log: Phone Record Modified Successful | Customer ID: " + client.getCustomerID());
								
								return true;
							}
						}
						catch (Exception e)
						{
							this.logger.severe("Server Log: | Edit Record Error: Invalid Phone Format | Customer ID: " + client.getCustomerID());
							System.out.println("Server Log: | Edit Record Error: Invalid Phone Format | Customer ID: " + client.getCustomerID());
							e.printStackTrace();
							
							return false;
						}
					
					case branch:
						try
						{
							for(BranchID enumList : BranchID.values())
							{
								if(enumList.name().equalsIgnoreCase(newValue))
								{
									/*
									 * Todo: Maybe we should also modify the customer ID here because their ID has BranchID as part of their ID.
									 */
									client.setBranchID(enumList);
									this.logger.info("Server Log: | Edit Record Log: Branch ID Modified Successful | Customer ID: " + client.getCustomerID());
									System.out.println("Server Log: | Edit Record Log: Branch ID Modified Successful | Customer ID: " + client.getCustomerID());
									
									return true;
								}
								else
								{
									this.logger.severe("Server Log: | Edit Record Error: Invalid Branch ID | Customer ID: " + client.getCustomerID());
									System.out.println("Server Log: | Edit Record Error: Invalid Branch ID | Customer ID: " + client.getCustomerID());
									
									return false;
								}
							}
						}
						catch (Exception e)
						{
							this.logger.severe("Server Log: | Edit Record Error: Unknow Branch Error | Customer ID: " + client.getCustomerID());
							System.out.println("Server Log: | Edit Record Error: Unknow Branch Error | Customer ID: " + client.getCustomerID());
							
							return false;
						}					
				}//end switch statements
			}//end if clause (customer found)
			else
			{
				this.logger.severe("Server Log: | Record Edit Error: Account Not Found | Customer ID: " + customerID);
				System.out.println("Server Log: | Edit Record Error: Account Not Found | Customer ID: " + customerID);
				
				return false;
			}
		}
				
		return false;
	}

	@Override
	public synchronized HashMap<String, String> getAccountCount() throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized boolean transferFund(String customerID1, String customerID2, double amt) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized boolean deposit(String customerID, double amount) throws Exception
	{
		//0. Preliminary Check
		if (amount <= 0)
		{
			this.logger.info("Server Log: | Deposit Error: Attempted to deposit incorrect amount. | Amount: " + amount + " | Customer ID: " + customerID);
			System.out.println("Server Log: | Deposit Error: Attempted to deposit incorrect amount. | Amount: " + amount + " | Customer ID: " + customerID);
			
			return false;
		}
		
		//1. Verify the customerID is valid.
		try
		{
			//Maybe move the verification process to a separate method
			String key = Character.toString((char)customerID.charAt(CLIENT_NAME_INI_POS));
			ArrayList<Client> values = clientList.get(key);
			
			for (Client client : values)
			{
				if (client.getCustomerID().equals(customerID))
				{
					client.deposit(amount);
					double newBalance = client.getBalance();
					this.logger.info("Server Log: | Deposit Log: | Deposit: " + amount + " | Balance: " + newBalance + " | Customer ID: " + customerID);
					System.out.println("Server Log: | Deposit Log: | Deposit: " + amount + " | Balance: " + newBalance + " | Customer ID: " + customerID);
						
					return true;
				}
			}
		}
		catch (Exception e)
		{
			this.logger.severe("Server Log: | Deposit Error: | Unable to locate account. | Customer ID: " + customerID);
			System.out.println("Server Log: | Deposit Error: | Unable to locate account. | Customer ID: " + customerID);
			
			return false;
		}
		
		return false;
	}

	@Override
	public synchronized boolean withdraw(String customerID, double amount) throws Exception
	{
		//0. Preliminary Check
		if (amount <= 0)
		{
			this.logger.info("Server Log: | Withdrawl Error: Attempted to withdraw incorrect amount. | Amount: " + amount + " | Customer ID: " + customerID);
			System.out.println("Server Log: | Withdrawl Error: Attempted to withdraw incorrect amount. | Amount: " + amount + " | Customer ID: " + customerID);
			
			return false;
		}
		
		//1. Verify the customerID is valid.
		try
		{
			//Maybe move the verification process to a separate method
			String key = Character.toString((char)customerID.charAt(CLIENT_NAME_INI_POS));
			ArrayList<Client> values = clientList.get(key);
			
			for (Client client : values)
			{
				if (client.getCustomerID().equals(customerID))
				{
					double oldBalance = client.getBalance();
					double newBalance = oldBalance - amount;				
					
					if (newBalance < 0 )
					{				
						this.logger.severe("Server Log: | Withdrawl Error: Attempted to withdraw more than current balance. | Amount: " 
								+ amount + " | Customer Balance: " + oldBalance + " | Customer ID: " + customerID);
						System.out.println("Server Log: | Withdrawl Error: Attempted to withdraw more than current balance. | Amount: " 
								+ amount + " | Customer Balance: " + oldBalance + " | Customer ID: " + customerID);
						
						return false;		
					}
					else
					{
						client.withdraw(amount);
						
						this.logger.info("Server Log: | Withdrawl Log: | Withdrawl: " + amount + " | Balance: " + newBalance + " | Customer ID: " + customerID);
						System.out.println("Server Log: | Withdrawl Log: | Withdrawl: " + amount + " | Balance: " + newBalance + " | Customer ID: " + customerID);
						
						return true;
					}					
				}
			}
		}
		catch (Exception e) //Cannot find client
		{
			this.logger.severe("Server Log: | Withdrawl Error: | Unable to locate account. | Customer ID: " + customerID);
			System.out.println("Server Log: | Withdrawl Error: | Unable to locate account. | Customer ID: " + customerID);
			
			return false;
		}
		
		return false;
	}

	@Override
	public synchronized double getBalance(String customerID) throws Exception
	{
		double customerBalance = 0;
		
		//1. Verify the customerID is valid.
		try
		{
			//Maybe move the verification process to a separate method
			String key = Character.toString((char)customerID.charAt(CLIENT_NAME_INI_POS));
			
			ArrayList<Client> values = clientList.get(key);
					
			for (Client client : values)
			{
				if (client.getCustomerID().equals(customerID))
				{
					customerBalance = client.getBalance();
							
					this.logger.info("Server Log: | Balance Log: | Balance: " + customerBalance + " | Customer ID: " + customerID);
					System.out.println("Server Log: | Balance Log: | Balance: " + customerBalance + " | Customer ID: " + customerID);
					
					return customerBalance;
				}
			}
		}
		catch (Exception e)
		{
			this.logger.severe("Server Log: | Withdrawl Error: | Unable to locate account. | Customer ID: " + customerID);
			throw new Exception("Server Log: | Withdrawl Error: | Unable to locate account. | Customer ID: " + customerID);
		}
		
		return customerBalance;
	}

	@Override
	public synchronized int getLocalAccountCount()
	{
		return clientCount;
	}

	@Override
	public void shutdown()
	{
		this.UDPServer.stop();
	}

	public BranchID getBranchID()
	{
		return branchID;
	}
}
