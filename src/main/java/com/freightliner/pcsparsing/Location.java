package com.freightliner.pcsparsing;

import java.io.Serializable;
import java.util.Date;

/**
 * This interface provides acsess to Location.
 *
 * @author   jftl8v
 * 
 */
public interface Location extends Serializable {

 /**
  * Get the item number.
  * @return String	the item number  
  */
  public String getItemNumber();

 /**
  * Get the location ID.
  * @return String	the location ID
  */
  public String getLoc();
  
 /**
  * Set the location ID
  * @param String loc: the location ID
  */
  public void setLoc(String loc);

 /**
  * Get the associated item
  * @return ComponentItem	the associated item
  */
  public ComponentItem getItem();
  
 /**
  * Set theassociated item.
  * @param ComponentItem item: a component item
  */
  public void setItem(ComponentItem item);

 /**
  * Get the CRFA (comp code, plant ID, FA code).
  * @return String	the CRFA
  */
  public String getCrfa();
  
 /**
  * Set the CRFA (comp code, plant ID, FA code).
  * @param String crfa: a string representing the CRFA
  */
  public void setCrfa(String crfa);

 /**
  * Get the shop address
  * @return String	the shop address
  */
  public String getShopAddress();
  
 /**
  * Set the shop address
  * @param String shopAddress: the shop address string
  */
  public void setShopAddress(String shopAddress);
  
 /**
  * Get the material planner.
  * @return String the material planner
  */
  public String getMatlPlanner();

 /**
  * Set the material planner.
  * @param String matlPlanner: the ID of the material planner
  */
  public void setMatlPlanner(String matlPlanner);
 
 /**
  * Get the on-hand total quantity.
  * @return int	the on-hand total quantity
  */
  public int getOhTotalQuantity();
 
 /**
  * Set the on-hand total quantity.
  * @param int ohTotalQuantity: the total on hand quantity 
  */
  public void setOhTotalQuantity(int ohTotalQuantity);

 /**
  * Get the safty stock (minimum quantity).
  * @return int	the quantity of the safty stock
  */
  public int getSafetyStock();
  
 /**
  * Set the safe minimum quantity
  * @param int saftyStock: the minimum quantity of safety stock.
  */
  public void setSafetyStock(int safetyStock);
  
 /**
  * Get the three-day peg quantity
  * @return int	the three-day peg quantity
  */
  public int getPeg3DayQuantity();
 
 /**
  * Set the three-day peg quantity.
  * @param int peg3DayQuantity: the three-day peg quantity
  */
  public void setPeg3DayQuantity(int peg3DayQuantity);

 /**
  * Get the lead-time peg quantity.
  * @return int	the lead-time peg quantity
  */
  public int getPegLeadTimeQuantity();
  
 /**
  * Set the lead-time peg quantity
  * @param int pegLeadTimeQuantity: the lead-time peg quantity
  */
  public void setPegLeadTimeQuantity(int peqLeadTimeQuantity);

 /**
  * Get the change-over date.
  * @return Date the change-over date
  */
  public Date getChangeoverDate();  
  
 /**
  * Set the change-over date.
  * @param Date changeoverDate: the change-over date
  */
  public void setChangeoverDate(Date changeoverDate);

 /**
  * Get the primary vendor abbreviation.
  * @return String	 the primary vendor abbreviation
  */ 
  public String getPrimaryVendorAbbr();
  
 /**
  * Set the primary vendor abbreviation.
  * @param String primaryVendorAbbr: the primary vendor abbreviation
  */
  public void setPrimaryVendorAbbr(String primaryVendorAbbr);

 /**
  * Get the secondary vendor ID.
  * @return String	the secondary vendor ID
  */
  public String getSecondaryVendorId();
  
 /**
  * Set the secondary vendor ID.
  * @param	String secondaryVendorId: the secondary vendor ID
  */
  public void setSecondaryVendorId(String secondaryVendorId);  
 
 /**
  * Get the alternate vendor ID.
  * @return String	the alternate vendor ID
  */
  public String getAltVendorId();
 
 /**
  * Set the alternate vendor ID.
  * @param String altVendorId: the alternate vendor ID
  */
  public void setAltVendorId(String altVendorId);
    
 /**
  * Get the total cost.
  * @return double	the amount of total cost
  */
  public double getTotalCost();
   
 /**
  * Set the total cost.
  * @param double totalCost: the amount of total cost
  */
  public void setTotalCost(double totalCost);
  
 /**
  * Set the total cost of all the orders.
  * @param double ordsTotalCost: the amount of total orders cost
  */
  public void setOrdsTotalCost(double ordsTotalCost);
    
 /**
  * Get the orders total cost.
  * @return double	the amount of the orders total cost
  */
  public double getOrdsTotalCost();
 
 /**
  * Get the lead time.
  * @return int	the lead time
  */
  public int getLeadTime();
  
 /**
  * Set the total lead time.
  * @param int leadTime: an integer for lead time	
  */
  public void setLeadTime(int leadTime);

 /**
  * Get the available net quantity.
  * @return int	the available net quantity
  */
  public int getNetAvailable();
  
 /**
  * Set the available net quantity.
  * @param int netAvailable: the avaiable net quantity  
  */
  public void setNetAvailable(int netAvailable);

}
