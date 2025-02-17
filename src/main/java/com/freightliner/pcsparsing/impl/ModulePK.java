package com.freightliner.pcsparsing.impl;

import java.io.Serializable;

import org.apache.commons.lang.builder.*;

/**
 * This class implements the primary key class of Module object.
 * 
 * @author   jftl8v
 *
 */
public class ModulePK implements Serializable {
	
	//All the data fields
	private String serialNumber;
	private String number;
	private String sequenceNumber;

   /**
    * Default constructor
    */
	public ModulePK() {
		
	}

   /**
    * Constructor
    * @param String serialNumber: the serial number
    * @param String number: the module number
    * @param String sequenceNumber: the sequence number
    */
	public ModulePK(String serialNumber, String number, String sequenceNumber) {
		this.serialNumber = serialNumber;
		this.number = number;
		this.sequenceNumber = sequenceNumber;
	}
	
   /**
    * Get the module number.
    * @return String	the module number
    */
	public String getNumber() {
		return number;
	}
	
	/**
	 * Set the module number.
	 * @param String number: The number to set.
	 */
	public void setNumber(String number) {
		this.number = number;
	}

   /**
  	* Compare all the keys between this ModulePK and the other object.
  	* @param Object other: an object (ModulePK)
  	* @return boolean	true/false
  	*/
  	public boolean equals(Object other) {
    	if ( !(other instanceof ModulePK) ) return false;
    	ModulePK castOther = (ModulePK) other;
    	return new EqualsBuilder()
        			.append(this.getSerialNumber(), castOther.getSerialNumber())
        			.append(this.getNumber(), castOther.getNumber())
        			.append(this.getSequenceNumber(), castOther.getSequenceNumber())
        			.isEquals();
	}

   /**
    * Generate a hash code based on all the keys.
    * @return int	a hash code
    */
	public int hashCode() {
    	return new HashCodeBuilder()
        			.append(getSerialNumber())
        			.append(getNumber())
        			.append(getSequenceNumber())
        			.toHashCode();
	}
	/**
	 * Get the serial number
	 * @return String	the serialNumber.
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	
	/**
	 * Set the serial number.
	 * @param String serialNumber: the serialNumber to set.
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	/**
	 * Get the sequence number.
	 * @return String	the sequenceNumber.
	 */
	public String getSequenceNumber() {
		return sequenceNumber;	
	}	
	
	/**
	 * Set the sequence number.
	 * @param String sequenceNumber: the sequenceNumber to set.
	 */	
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;	
	}	
		
		
} 
