package domain;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "BranchID")
@XmlEnum
public enum BranchID
{
	@XmlEnumValue("BC")
	BC("BC"),
	@XmlEnumValue("MB")
	MB("MB"),
	@XmlEnumValue("NB")
	NB("NB"),
	@XmlEnumValue("QC")
	QC("QC");
	
	private String value;
	
	BranchID(String str)
	{
		value = str;
	}
	
	BranchID()
	{
		
	}

	public String value()
	{
		return value;
	}
	
	public static BranchID fromValue(String str)
	{
		for(BranchID branch: BranchID.values())
		{
			if(branch.value.equals(str)) 
			{
				return branch;				
			}
		}
		
		throw new IllegalArgumentException(str);
	}
}
