package com.freightliner.pcsparsing;

import java.io.Serializable;
import java.util.Set;

/**
 * This interface provides acsess to the data fields of Item.
 *
 * @author   jftl8v
 * 
 */
public interface Item extends Comparable, Serializable {

   /**
    * Get the description.
    * @return String	the description of the item.
    */
	public String getDescription();
	
   /**
    * Set the description of the item.
    * @param String description: the description of the item.
    */
  	public void setDescription(String description);

   /**
    * Get the department that the item belong.
    * @return Department	the department .
    */
	public Department getDepartment();
  	
   /**
    * Set the department that the item belong.
    * @param Department department: the department that hte item belong
    * 
    */
  	public void setDepartment(Department department);

   /**
    * Get the module number.
    * @return String the module number
    */
  	public String getModuleNumber();
  
   /**
    * Get the ID number of the item
    * @return String	the item number
    */
	public String getNumber();
	
   /**
    * Set the ID number of the item.
    * @param String number: the item ID number
    */
	public void setNumber(String number);

   /**
    * Get the item type.
    * @return ItemType	the item type
    */
	public ItemType getType();
	
   /**
    * Set the item type.
    * @param ItemType type: an item type
    */
	public void setType(ItemType type);

   /**
    * Get the manufacturing revision level.
    * @return String	 the manufacturing revision level
    */
  	public String getMfgRevisionLevel();
  	
   /**
    * Set the manufacturing revision level.
    * @param String mfgRevisionLevel: the manufacturing revision level
    */
  	public void setMfgRevisionLevel(String mfgRevisionLevel);

   /**
    * Get the engineering revision level.
    * @return String	the engineering revision level
    */
  	public String getEngRevisionLevel();
  	
   /**
    * Set the engineering revision level.
    * @param String engRevisionLevel: the engineering revision level
    */  
  	public void setEngRevisionLevel(String engRevisionLevel);

   /**
    * Get the item status
    * @return String the item status
    */
  	public String getStatus();
  	
   /**
    * Set the status of the item.
    * @param String status: the status of the item
    */
  	public void setStatus(String status);

   /**
    * Get the purchasing unit of measure.
    * @return String	the purchasing unit of measure
    */
  	public String getPurchasingUnitOfMeasure();
  	
   /**
    * Set the purching unit of measure.
    * @param String purchasingUnitOfMeasure: the purchasing unit of measure
    */
  	public void setPurchasingUnitOfMeasure(String purchasingUnitOfMeasure);

  	
    /**
     * Get the ITAR Indc.
     * @return String	the ITAR Indc
     */
   	public String getItarRestrIndc();
   	
    /**
     * Set the ITAR Indc.
     * @param String itarRestrIndc: the ITAR Indc
     */
   	public void setItarRestrIndc(String itarRestrIndc);
  	
   /**
    * Calculate the cost in USA.
    * @return double	the cost in USA.
    */
  	public double getUsaCost();

   /**
    * Calculate the cost in Canada.
    * @return double	the cost in Canada.
    */
  	public double getCanadianCost();
  
   /**
    * Get the BOM level.
    * @return int	the BOM level
    */
  	public int getBOMLevel();

   /**
    * Get the account number.
    * @return int	the account number
    */
  	public int getAccountNumber();
  	
   /**
    * Set the account number
    * @param int accountNumber: the account number
    */
  	public void setAccountNumber(int accountNumber);
  
   /**
    * Get the MIP number.
    * @return String	the MIP number
    */
  	public String getMipNumber();
  	
   /**
    * Set the MIP number
    * @param String mipNumber: the MIP number
    */
  	public void setMipNumber(String mipNumber);
	
   /**
    * Add a location to the associated set of locations.
    * @param Location location	a location
    */
  	public void add(Location location);
  	
   /**
    * Get a set of locations.
    * @return Set	a set of locations
    */
    public Set getLocations();
    
   /**
    * Set Locations for the item.
    * @param Set locations: a set of locations
    */
  	public void setLocations(Set locations);
  
   /**
    * Get quantity.
    * @return double	quantity.
    */
  	public double getQuantity();
   /**
     * Get Item No Order Reason.
     * @return double	No Order Reason.
     */
   	public String getDeleteReason();  
	
    /**
     * Set the description of the item.
     * @param String description: the description of the item.
     */
   	public void setDeleteReason(String deleteReason); 
  
 	
}
