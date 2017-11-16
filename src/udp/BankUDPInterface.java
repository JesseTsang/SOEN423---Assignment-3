package udp;

import java.io.Serializable;

import common.BankServerImpl;

public interface BankUDPInterface extends Serializable
{
	public void execute(BankServerImpl bankServer);
}
