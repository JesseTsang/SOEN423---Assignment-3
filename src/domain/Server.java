package domain;

public class Server
{
	private String host;
	private int port;
	
	public Server(String host, int port)
	{		
		this.host = host;
		this.port = port;
	}
	
	/********** Getters & Setters & toString() **********/
	
	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	@Override
	public String toString()
	{
		return "Server [Host =" + host + ", Port =" + port + "]";
	}
}