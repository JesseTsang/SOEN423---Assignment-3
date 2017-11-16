package domain;

public class Server
{
	private int portNum;
	private String ipAddress;
	
	public Server(String ip, int port)
	{
		this.portNum = port;
		this.ipAddress = ip;
	}
	
	/********** Getters & Setters & toString() **********/
	
	public int getPortNum()
	{
		return portNum;
	}

	public void setPortNum(int portNum)
	{
		this.portNum = portNum;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	@Override
	public String toString()
	{
		return "Server [portNum=" + portNum + ", ipAddress=" + ipAddress + "]";
	}
}