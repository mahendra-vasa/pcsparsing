package com.freightliner.pcsparsing.impl;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * This class represents the primary key of the BOM object.
 *
 * @author   jftl8v
 * 
 */
public class BOMPK implements Serializable {
  
  
  private String tsoSequenceNumber;
  private String sequenceNumber;
  private String serialNumber;
  private String moduleNumber;
  
  /**
   *  Item number associated with BOM
   */
  private String number;
  
 /**
  * Default constructor.
  */
  public BOMPK() { }

 /**
  * Constructor.
  * @param String tsoSequenceNumber: the tso sequence number
  * @param String sequenceNumber: the sequence number
  * @param String serialNumber: the serial number
  * @param String number: the item number
  * 
  */
  public BOMPK(String tsoSequenceNumber, String sequenceNumber, 
                String serialNumber, String moduleNumber, String number) {
    setTsoSequenceNumber(tsoSequenceNumber);
    setSequenceNumber(sequenceNumber);
    setSerialNumber(serialNumber);
    setModuleNumber(moduleNumber);
    setNumber(number);
  }
  
 /**
  * Get the module number.
  * @return String	the module number
  */
  public String getModuleNumber() {
    return moduleNumber;
  }
  
 /**
  * Set the module number.
  * @param String moduleNumber: the module number
  */
  public void setModuleNumber(String moduleNumber) {
    this.moduleNumber = StringUtils.trim(moduleNumber);
  }
 
 /**
  * Get the item number.
  * @return String	the item number
  */
  public String getNumber() {
    return number;
  }
  
 /**
  * Set the item number.
  * @param String number: the item number
  */
  public void setNumber(String number) {
    this.number = StringUtils.trim(number);
  }
  
 /**
  * Get the sequence number.
  * @return String	the sequence number
  */
  public String getSequenceNumber() {
    return sequenceNumber;
  }
  
 /**
  * Set the sequence number.
  * @param String sequenceNumber: the sequence number
  */
  public void setSequenceNumber(String sequenceNumber) {
    this.sequenceNumber = StringUtils.trim(sequenceNumber);
  }
  
 /**
  * Get the serial number.
  * @return String	the serial number
  */
  public String getSerialNumber() {
    return serialNumber;
  }
 
 /**
  * Set the serial number.
  * @param String serialNumber: the serial number
  */ 
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = StringUtils.trim(serialNumber);
  }
 
 /**
  * Get the tsosequence number.
  * @return String	the tso sequence number
  */
  public String getTsoSequenceNumber() {
    return tsoSequenceNumber;
  }
  
 /**
  * Set the tso sequence number.
  * @param String tsoSequenceNumber: the tso sequence number
  */
  public void setTsoSequenceNumber(String tsoSequenceNumber) {
    this.tsoSequenceNumber = StringUtils.trim(tsoSequenceNumber);
  }
  
 /**
  * Compare all the keys between this BOMPK and the other object.
  * @param Object o: an object (BOMPK)
  * @return boolean	true/false
  */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BOMPK)) return false;
    if (o == null) return false;

    final BOMPK oBOMPK = (BOMPK) o;
    if (getNumber() != null ? !getNumber().equals(oBOMPK.getNumber()) : oBOMPK.getNumber() != null) return false;
    if (getModuleNumber() != null ? !getModuleNumber().equals(oBOMPK.getModuleNumber()) : oBOMPK.getModuleNumber() != null) return false;
    if (getSerialNumber() != null ? !getSerialNumber().equals(oBOMPK.getSerialNumber()) : oBOMPK.getSerialNumber() != null) return false;
    if (getTsoSequenceNumber() != null ? !getTsoSequenceNumber().equals(oBOMPK.getTsoSequenceNumber()) : oBOMPK.getTsoSequenceNumber() != null) return false;
    if (getSequenceNumber() != null ? !getSequenceNumber().equals(oBOMPK.getSequenceNumber()) : oBOMPK.getSequenceNumber() != null) return false;

    return true;
  }

 /**
  * Generate a hash code based on all the keys.
  * @return int	a hash code
  */
  public int hashCode() {
    int result = (getNumber() != null ? getNumber().hashCode() : 0)
			+ (getModuleNumber() != null ? getModuleNumber().hashCode() : 0)
			+ (getSerialNumber() != null ? getSerialNumber().hashCode() : 0)
			+ (getTsoSequenceNumber() != null ? getTsoSequenceNumber().hashCode() : 0)
			+ (getSequenceNumber() != null ? getSequenceNumber().hashCode() : 0);
    return result;
  }

   /**
    * Compare all the keys between this object and the other object.
    * @param Object o: an object (BOMPK)
    * @return int	an Integer representing less than (negative), equal (0) 
    * 				and more than(positive)
    */
	public int compareTo(Object o) {
    	final BOMPK oBOMPK = (BOMPK) o;
    	int result = getNumber().compareTo(oBOMPK.getNumber()) * 10000;
    	result = result + getModuleNumber().compareTo(oBOMPK.getModuleNumber()) * 1000;
    	result = result + getSerialNumber().compareTo(oBOMPK.getSerialNumber()) * 100;
    	result = result + getTsoSequenceNumber().compareTo(oBOMPK.getTsoSequenceNumber()) * 10;
    	result = result + getSequenceNumber().compareTo(oBOMPK.getSequenceNumber());
		return result;
	}
  
}
