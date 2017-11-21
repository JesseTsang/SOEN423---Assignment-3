package client;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import common.BankServerImpl;
import domain.BranchID;
import domain.Server;

public class BankServerDriver
{
	Map<String, Server> serversDetails = new HashMap<String, Server>();
	Hashtable<String, BankServerImpl> serversList = new Hashtable<String, BankServerImpl>();
	
	public BankServerDriver()
	{	
		BranchID bc = BranchID.BC;
		String bcHost = "localhost";
		int bcPort = 30000;
		
		BranchID mb = BranchID.MB;
		String mbHost = "localhost";
		int mbPort = 30100; 
		
		BranchID nb = BranchID.NB;
		String nbHost = "localhost";
		int nbPort = 30200; 
		
		BranchID qc = BranchID.QC;
		String qcHost = "localhost";
		int qcPort = 30300; 
		
		serversDetails.put(bc.toString(), new Server(bcHost, bcPort));
		serversDetails.put(mb.toString(), new Server(mbHost, mbPort));
		serversDetails.put(nb.toString(), new Server(nbHost, nbPort));
		serversDetails.put(qc.toString(), new Server(qcHost, qcPort));
		
		BankServerImpl BCServer = new BankServerImpl(bc.toString(), bcHost, bcPort, serversDetails);
		BankServerImpl MBServer = new BankServerImpl(mb.toString(), mbHost, mbPort, serversDetails);
		BankServerImpl NBServer = new BankServerImpl(nb.toString(), nbHost, nbPort, serversDetails);
		BankServerImpl QCServer = new BankServerImpl(qc.toString(), qcHost, qcPort, serversDetails);
		
		serversList.put(bc.toString(), BCServer);
		serversList.put(mb.toString(), MBServer);
		serversList.put(nb.toString(), NBServer);
		serversList.put(qc.toString(), QCServer);
		
	}

	public Map<String, BankServerImpl> getServersList()
	{
		return serversList;
	}
}
