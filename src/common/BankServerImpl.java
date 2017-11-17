package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;

import domain.BranchID;
import domain.Client;
import domain.EditRecordField;


public class BankServerImpl implements BankServerInterface
{
	//Variable for each separate bank server
	private Logger logger = null;
	private BranchID branchID;
	private int clientCount;
	private Map<String, ArrayList<Client>> clientList = new HashMap<String, ArrayList<Client>>();
	
	//UDP Server for listening incoming requests
	//private UDPServer UDPServer;
		
	//Holds other servers' addresses : ["ServerName", "hostName:portNumber"]
	HashMap<String, String> serversList = new HashMap();
		
	private static final int    CLIENT_NAME_INI_POS = 3;	

	@Override
	public Boolean createAccount(String firstName, String lastName, String address, String phone, String customerID,
	        BranchID branch) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean editRecord(String customerID, EditRecordField fieldName, String newValue) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getAccountCount() throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean transferFund(String customerID1, String customerID2, double amt) throws Exception
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deposit(String customerID, double amt) throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void withdraw(String customerID, double amt) throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	public double getBalance(String customerID) throws Exception
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLocalAccountCount() throws Exception
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void shutdown()
	{
		// TODO Auto-generated method stub

	}

	public BranchID getBranchID()
	{
		// TODO Auto-generated method stub
		return branchID;
	}

}
