package com.freightliner.pcsparsing.impl;

import com.freightliner.pcsparsing.Components;


public class ComponentImpl implements Components{
	
	
	  /**
	 * 
	 */
	private String tsoSplitNumber= ""; //this is serialNumber
	  private String tsoSequenceNumber= "";
	  private String sequenceNumber= "";
	  private String moduleNumber= "";
	  
	  /**
	    * The ID number of the item
	    */
	  private String itemNumber= "";
	  private String compItemNo= "";
	  private String compQty= "";
	 
	 
	  /**
	    * The description of the item
	    */
	  private String itemDescription = "";
	  	
  	 /**
	   * The type code representing type of the item
	   */
	  private String itemType = "";
		  	
  	 /**
	   * The status
	   */
	  private String itemStatus = "";
		  	
	  	
	 /**
	   * The department code the item belong
	  */
	private String departmentCode = "";
	
	 /**
	   * The purchasing unit of measure
	   */
	 private String purchasingUnitOfMeasure = "";
	
	 
	 /**
	   * The Itar Indc
	   */
	 private String itarRestrIndc = "";
	
   /**
    * The MIP number.
    */
  	private String mipNumber = "";
	  	
  /**
	* The engineering revision level
	*/
	private String engRevisionLevel = ""; // denotes the drwg_rev_lvl
	  
	  	  	
   /**
    * The manufacturing revision level
    */
  	private String mfgRevisionLevel = "";	  	
  	
  
   /**
    * The account number
    */
  	private int accountNumber;


	public int getAccountNumber() {
		return accountNumber;
	}
	
	
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	
	public String getCompItemNo() {
		return compItemNo;
	}
	
	
	public void setCompItemNo(String compItemNo) {
		this.compItemNo = compItemNo;
	}
	
	
	public String getCompQty() {
		return compQty;
	}
	
	
	public void setCompQty(String compQty) {
		this.compQty = compQty;
	}
	
	
	public String getDepartmentCode() {
		return departmentCode;
	}
	
	
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	

	
	public String getEngRevisionLevel() {
		return engRevisionLevel;
	}
	
	
	public void setEngRevisionLevel(String engRevisionLevel) {
		this.engRevisionLevel = engRevisionLevel;
	}
	
	
	public String getItemNumber() {
		return itemNumber;
	}
	
	
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	
	
	public String getMfgRevisionLevel() {
		return mfgRevisionLevel;
	}
	
	
	public void setMfgRevisionLevel(String mfgRevisionLevel) {
		this.mfgRevisionLevel = mfgRevisionLevel;
	}
	
	
	public String getMipNumber() {
		return mipNumber;
	}
	
	
	public void setMipNumber(String mipNumber) {
		this.mipNumber = mipNumber;
	}
	
	
	public String getModuleNumber() {
		return moduleNumber;
	}
	
	
	public void setModuleNumber(String moduleNumber) {
		this.moduleNumber = moduleNumber;
	}
	
	
	public String getItarRestrIndc() {
		return itarRestrIndc;
	}
	
	
	public void setItarRestrIndc(String itarRestrIndc) {
		this.itarRestrIndc = itarRestrIndc;
	}
	
	public String getPurchasingUnitOfMeasure() {
		return purchasingUnitOfMeasure;
	}
	
	
	public void setPurchasingUnitOfMeasure(String purchasingUnitOfMeasure) {
		this.purchasingUnitOfMeasure = purchasingUnitOfMeasure;
	}
	
	
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	
	
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	
	
	
	
	public String getTsoSequenceNumber() {
		return tsoSequenceNumber;
	}
	
	
	public void setTsoSequenceNumber(String tsoSequenceNumber) {
		this.tsoSequenceNumber = tsoSequenceNumber;
	}
	
	
	public String getTsoSplitNumber() {
		return tsoSplitNumber;
	}
	
	
	public void setTsoSplitNumber(String tsoSplitNumber) {
		this.tsoSplitNumber = tsoSplitNumber;
	}
	
	
	public String getItemDescription() {
		return itemDescription;
	}
	
	
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	
	
	public String getItemStatus() {
		return itemStatus;
	}
	
	
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}
	
	
	public String getItemType() {
		return itemType;
	}
	
	
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	/**
	 * For Hibernate mapping purpose.
	 * Compare all the primary keys between this SerialNumber and the other object.
	 * @param Object obj: an object (SerialNumber)
	 * @return boolean	true/false
	 */
	 public boolean equals(Object obj) {
	   if (obj == null) return false;
	   if (!(obj instanceof Components)) return false;
	   Components oSN = (Components) obj;
	   return oSN.getTsoSplitNumber().equals(getTsoSplitNumber());
	 }

	/**
	 * For Hibernate mapping purpose.
	 * Generate a hash code based on the primary keys.
	 * @return int	a hash code
	 */
	 public int hashCode() {
	   return getTsoSplitNumber().hashCode();
	 }




	
}
