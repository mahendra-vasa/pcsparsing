package com.freightliner.pcsparsing.impl;

import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.freightliner.pcsparsing.*;

/**
 * This abstract class implements the Item interface .
 *
 * @author   jftl8v
 *
 * 
 */
public abstract class ItemImpl implements Item {

   /**
    * The department code the item belong
  	*/
  	protected String departmentCode = "";
  
   /**
    * The description of the item
    */
  	private String description = "";
  	
   /**
    * The manufacturing revision level
    */
  	protected String mfgRevisionLevel = "";
  	
   /**
    * The engineering revision level
    */
  	protected String engRevisionLevel = "";
  	
   /**
    * The status
    */
  	private String status = "";
  	
   /**
    * The purchasing unit of measure
    */
  	private String purchasingUnitOfMeasure = "";
  	
  	/**
     * The ITAR Indc
     */
   	private String itarRestrIndc = "";
  	
   /**
    * The ID number of the item
    */
  	private String number;
  	
   /**
    * The type code representing type of the item
    */
  	protected String typeCode = null;
  	
   /**
    * The account number
    */
  	private int accountNumber;
  	
   /**
    * The MIP number.
    */
  	private String mipNumber = "";
  	
   /**
    * The set of the locations
    */
  	private Set locations = new HashSet();

   /**
    * Default Constructor.
    */   
  	public ItemImpl() {
    	setNumber("");
    	typeCode = ItemType.EMPTY.getCode();
  	}

   /**
    * Access method. Get the description.
    * @ return String the description of the item.
    */
  	public String getDescription() {
    	return description;
  	}

   /**
    * Set the description of the item.
    * @param String description: the description of the item.
    */
  	public void setDescription(String description) {
    	this.description = description;
  	}

   /**
    * Get the ID number of the item
    * @return String	the item number
    */
  	public String getNumber() {
    	return number;
  	}

   /**
    * Set the ID number of the item.
    * @param String number: the item ID number
    */
	public void setNumber(String number) {
     	this.number = StringUtils.trim(number);
  	}
  	
   
   /**
    * Get the manufacturing revision level.
    * @return String	 the manufacturing revision level
    */
  	public String getMfgRevisionLevel() {
    	return mfgRevisionLevel;
  	}

   /**
    * Set the manufacturing revision level.
    * @param String mfgRevisionLevel: the manufacturing revision level
    */
  	public void setMfgRevisionLevel(String mfgRevisionLevel) {
    	this.mfgRevisionLevel = mfgRevisionLevel;
  	}

   /**
    * Get the engineering revision level.
    * @return String	the engineering revision level
    */
  	public String getEngRevisionLevel() {
    	return engRevisionLevel;
  	}

   /**
    * Set the engineering revision level.
    * @param String engRevisionLevel: the engineering revision level
    */    
  	public void setEngRevisionLevel(String engRevisionLevel) {
    	this.engRevisionLevel = engRevisionLevel;
  	}

   /**
    * Get the item status
    * @return String the item status
    */
  	public String getStatus() {
    	return status;
  	}

   /**
    * Set the status of the item.
    * @param String status: the status of the item
    */
  	public void setStatus(String status) {
    	this.status = status;
  	}
  	
   /**
    * Get the department that the item belong.
    * @return Department	the department .
    */
  	public Department getDepartment() {
    	return Department.get(departmentCode);
  	}

   /**
    * Set the department that the item belong.
    * @param Department department: the department that hte item belong
    * 
    */
  	public void setDepartment(Department department) {
    	if (department == null) {
      	throw new IllegalArgumentException("Department cannot be null");
    	}
    	this.departmentCode = department.getAbbreviation();
  	}

   /**
    * Get the department code.
    * @return String	the department code
    */
  	public String getDepartmentCode() {
    	return departmentCode;
  	}

   /**
    * Set the department code that the item belong.
    * @param String departmentCode: the department code 
    */
  	public void setDepartmentCode(String departmentCode) {
    	this.departmentCode = departmentCode;
  	}

   /**
    * Get the item type.
    * @return ItemType	the item type
    */
  	public ItemType getType() {
    	return ItemType.get(typeCode);
  	}

   /**
    * Set the item type.
    * @param ItemType type: an item type
    */
  	public void setType(ItemType itemType) {
    	if (itemType == null) {
      	throw new IllegalArgumentException("ItemType cannot be null");
    	}
    	typeCode = itemType.getCode();
  	}

  
   /**
    * Get the type code.
    * @return String	the type code
    */
  	public String getTypeCode() {
    	return typeCode;
  	}

   /**
    * Set the type code
    * @param String typeCode: the type code
    */
  	public void setTypeCode(String typeCode) {
    	this.typeCode = typeCode;
  	}
  	
   /**
    * Get the purchasing unit of measure.
    * @return String	the purchasing unit of measure
    */
  	public String getPurchasingUnitOfMeasure() {
    	return purchasingUnitOfMeasure;
  	}

   /**
    * Set the purching unit of measure.
    * @param String purchasingUnitOfMeasure: the purchasing unit of measure
    */
  	public void setPurchasingUnitOfMeasure(String purchasingUnitOfMeasure) {
    	this.purchasingUnitOfMeasure = purchasingUnitOfMeasure;
  	}

    /**
     * Get the ITAR Indc.
     * @return String the ITAR Indc
     */
   	public String getItarRestrIndc() {
     	return itarRestrIndc;
   	}

    /**
     * Set the ITAR Indc.
     * @param String itarRestrIndc: the ITAR Indc
     */
   	public void setItarRestrIndc(String itarRestrIndc) {
     	this.itarRestrIndc = itarRestrIndc;
   	}

  	
  	
   /**
    * Get the account number.
    * @return int	the account number
    */
  	public int getAccountNumber() {
    	return accountNumber;
  	}
  
   /**
    * Set the account number
    * @param int accountNumber: the account number
    */
  	public void setAccountNumber(int accountNumber) {
    	this.accountNumber = accountNumber;
  	}

   /**
    * Get the MIP number.
    * @return String	the MIP number
    */
  	public String getMipNumber() {
    	return mipNumber;
  	}
  
   /**
    * Set the MIP number
    * @param String mipNumber: the MIP number
    */
  	public void setMipNumber(String mipNumber) {
    	this.mipNumber = mipNumber;
  	}
  
   /**
    * Add a location to the associated set of locations.
    * @param Location location	a location
    */
  	public void add(Location location) {
    	getLocations().add(location);
  	}

   /**
    * Get a set of locations.
    * @return Set	a set of locations
    */
  	public Set getLocations() {
    	return locations;
  	}
  
   /**
    * Set Locations for the item.
    * @param Set locations: a set of locations
    */
  	public void setLocations(Set locations) {
    	this.locations = locations;
  	}
 	

}
