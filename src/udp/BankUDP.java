package udp;

import common.BankServerImpl;

public class BankUDP implements BankUDPInterface
{
	private static final long serialVersionUID = 1L;
	
	private String clientIDSource;
	private String clientIDDest;
	private double amount;
	

	private String operationType;
	private boolean transferStatus = false;
	private int totalClientsCount;
	
	//Constructor for inter-banks fund transfer
	public BankUDP(String clientIDSource, String clientIDDest, double amount)
	{
		this.clientIDSource = clientIDSource;
		this.clientIDDest 	= clientIDDest;
		this.amount 		= amount;
		
		this.operationType = "fundTransfer";
	}
	
	//Constructor for get total client numbers.
	public BankUDP()
	{
		this.operationType = "getTotalClients";
	}
	
	/********** Main Method **********/
	
	@Override
	public void execute(BankServerImpl server)
	{	
		try
		{
			if(this.operationType.equals("fundTransfer"))
			{
				transferStatus = server.transferFund(clientIDSource, clientIDDest, amount);	
			}
			else if(this.operationType.equals("getTotalClients"))
			{
				totalClientsCount = server.getLocalAccountCount();
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}		
	}
	
	/********** Getters **********/
	public boolean isTransferStatus()
	{
		return transferStatus;
	}

	public int getTotalClientsCount()
	{
		return totalClientsCount;
	}
}

