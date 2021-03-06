package udp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MarshallService implements Serializable
{
	private static final long serialVersionUID = 1L;

	//We will translate the data object to byte array for UDP requests 
	public static final byte[] marshall(BankUDPInterface data)
	{	
		byte[] dataMarshalled = null;
		
		try
		{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutput output = new ObjectOutputStream(outputStream);
			
			output.writeObject(data);
			output.close();
			
			dataMarshalled = outputStream.toByteArray();
			
			return dataMarshalled;
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return dataMarshalled;
	}
	
	//We will translate the byte array to BankUDPInterface object from UDP requests 
	public static final BankUDPInterface unmarshall(byte[] data)
	{
		BankUDPInterface dataUnmarshall = null;
		try
		{
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
			ObjectInputStream input = new ObjectInputStream(inputStream);
			
			dataUnmarshall = (BankUDPInterface)input.readObject();
			input.close();
			
			return dataUnmarshall;
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return dataUnmarshall;
	}
}