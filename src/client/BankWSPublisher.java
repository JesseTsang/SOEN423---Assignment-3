package client;

import javax.xml.ws.Endpoint;

import common.BankServerWS;

public class BankWSPublisher
{

	public static void main(String[] args)
	{
		Endpoint.publish("http://localhost:8080/WS/A3", new BankServerWS());

	}

}
