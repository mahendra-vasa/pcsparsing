package com.freightliner.pcsparsing.impl;

import java.util.Date;

import com.freightliner.pcsparsing.*;

/**
 * This class implements the Location object associated location table.
 *
 * @author   jftl8v
 *
 */
public class LocationImpl implements Location {

  // All persistent data fields
  private ComponentItem item;
  private String loc = "";
  private String shopAddress = "";
  private String crfa = "";
  private String matlPlanner = "";
  private int ohTotalQuantity;
  private int safetyStock;
  private int peg3DayQuantity;
  private int pegLeadTimeQuantity;
  private Date changeoverDate;
  private String primaryVendorAbbr = "";
  private String secondaryVendorId = "";
  private String altVendorId = "";
  private double totalCost;
  private double ordsTotalCost;
  private int leadTime;
  private int netAvailable;
  
 /**
  * Default constructor.
  */
  public LocationImpl() { }
  
 /**
  * Constructor.
  * @param ComponentItem item: the associated item
  * @param String loc: the location ID
  */
  public LocationImpl(ComponentItem item, String loc) {
    setItem(item);
    item.add(this);
    this.loc = loc;
  }

 /**
  * Get the item number.
  * @return String	the item number  
  */
  public String getItemNumber() {
    return getItem().getNumber();
  }

 /**
  * Get the location ID.
  * @return String	the location ID
  */
  public String getLoc() {
    return loc;
  }
  
 /**
  * Set the location ID
  * @param String loc: the location ID
  */
  public void setLoc(String loc) {
    this.loc = loc;
  }

 /**
  * Get the associated item
  * @return ComponentItem	the associated item
  */
  public ComponentItem getItem() {
    return item;
  }

 /**
  * Set theassociated item.
  * @param ComponentItem item: a component item
  */ 
  public void setItem(ComponentItem item) {
    this.item = item;
  }

 /**
  * Get a string for the object.
  * Returns a string including the class name, item number and the location ID.
  * @return String
  */
  public String toString() {
		return getClass() + " " + getItemNumber() + " " + getLoc() + " ";
	}
 
 /**
  * For Hibernate mapping purpose.
  * Compare the composit primary keys between this location and the other object.
  * @param Object o: an object (Location)
  * @return boolean	true/false
  */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Location)) return false;
    if (o == null) return false;

    final Location oLocation = (Location) o;
    if (getItem() != null ? !getItem().equals(oLocation.getItem()) : oLocation.getItem() != null) return false;
    if (getLoc() != null ? !getLoc().equals(oLocation.getLoc()) : oLocation.getLoc() != null) return false;

    return true;
  }

 /**
  * For Hibernate mapping purpose.
  * Generate a hash code based on the primary keys.
  * @return int	a hash code
  */
  public int hashCode() {
		int result = 7;
		if (getItem() != null) {
			result = result + 19 * getItem().hashCode();
		}
		if (getLoc() != null) {
			result = result + getLoc().hashCode();
		}
		return result;
  }
  
 /**
  * Get the CRFA (comp code, plant ID, FA code).
  * @return String	the CRFA
  */
  public String getCrfa() {
    return crfa;
  }
  
 /**
  * Set the CRFA (comp code, plant ID, FA code).
  * @param String crfa: a string representing the CRFA
  */
  public void setCrfa(String crfa) {
    this.crfa = crfa;
  }
  
 /**
  * Get the shop address
  * @return String	the shop address
  */
  public String getShopAddress() {
    return shopAddress;
  }
  
 /**
  * Set the shop address
  * @param String shopAddress: the shop address string
  */
  public void setShopAddress(String shopAddress) {
    this.shopAddress = shopAddress;
  }
  
 /**
  * Get the alternate vendor ID.
  * @return String	the alternate vendor ID
  */
  public String getAltVendorId() {
    return altVendorId;
  }
  
 /**
  * Set the alternate vendor ID.
  * @param String altVendorId: the alternate vendor ID
  */
  public void setAltVendorId(String altVendorId) {
    this.altVendorId = altVendorId;
  }
  
 /**
  * Get the change-over date.
  * @return Date the change-over date
  */
  public Date getChangeoverDate() {
    return changeoverDate;
  }
  
 /**
  * Set the change-over date.
  * @param Date changeoverDate: the change-over date
  */
  public void setChangeoverDate(Date changeoverDate) {
    this.changeoverDate = changeoverDate;
  }
  
 /**
  * Get the material planner.
  * @return String the material planner
  */
  public String getMatlPlanner() {
    return matlPlanner;
  }
  
 /**
  * Set the material planner.
  * @param String matlPlanner: the ID of the material planner
  */
  public void setMatlPlanner(String matlPlanner) {
    this.matlPlanner = matlPlanner;
  }
  
 /**
  * Get the orders total cost.
  * @return double	the amount of the orders total cost
  */
  public double getOrdsTotalCost() {
    return ordsTotalCost;
  }
  
 /**
  * Set the total cost of all the orders.
  * @param double ordsTotalCost: the amount of total orders cost
  */
  public void setOrdsTotalCost(double ordsTotalCost) {
    this.ordsTotalCost = ordsTotalCost;
  }
  
 /**
  * Get the on-hand total quantity.
  * @return int	the on-hand total quantity
  */
  public int getOhTotalQuantity() {
    return ohTotalQuantity;
  }
  
 /**
  * Set the on-hand total quantity.
  * @param int ohTotalQuantity: the total on hand quantity 
  */
  public void setOhTotalQuantity(int ohTotalQuantity) {
    this.ohTotalQuantity = ohTotalQuantity;
  }
  
 /**
  * Get the three-day peg quantity
  * @return int	the three-day peg quantity
  */
  public int getPeg3DayQuantity() {
    return peg3DayQuantity;
  }
  
 /**
  * Set the three-day peg quantity.
  * @param int peg3DayQuantity: the three-day peg quantity
  */
  public void setPeg3DayQuantity(int peg3DayQuantity) {
    this.peg3DayQuantity = peg3DayQuantity;
  }
  
 /**
  * Get the lead-time peg quantity.
  * @return int	the lead-time peg quantity
  */
  public int getPegLeadTimeQuantity() {
    return pegLeadTimeQuantity;
  }
  
 /**
  * Set the lead-time peg quantity
  * @param int pegLeadTimeQuantity: the lead-time peg quantity
  */
  public void setPegLeadTimeQuantity(int pegLeadTimeQuantity) {
    this.pegLeadTimeQuantity = pegLeadTimeQuantity;
  }
  
 /**
  * Get the primary vendor abbreviation.
  * @return String	 the primary vendor abbreviation
  */ 
  public String getPrimaryVendorAbbr() {
    return primaryVendorAbbr;
  }
  
 /**
  * Set the primary vendor abbreviation.
  * @param String primaryVendorAbbr: the primary vendor abbreviation
  */
  public void setPrimaryVendorAbbr(String primaryVendorAbbr) {
    this.primaryVendorAbbr = primaryVendorAbbr;
  }
  
 /**
  * Get the safty stock (minimum quantity).
  * @return int	the quantity of the safty stock
  */
  public int getSafetyStock() {
    return safetyStock;
  }
  
 /**
  * Set the safe minimum quantity
  * @param int saftyStock: the minimum quantity of safety stock.
  */
  public void setSafetyStock(int safetyStock) {
    this.safetyStock = safetyStock;
  }
  
 /**
  * Get the secondary vendor ID.
  * @return String	the secondary vendor ID
  */
  public String getSecondaryVendorId() {
    return secondaryVendorId;
  }
  
 /**
  * Set the secondary vendor ID.
  * @param	String secondaryVendorId: the secondary vendor ID
  */
  public void setSecondaryVendorId(String secondaryVendorId) {
    this.secondaryVendorId = secondaryVendorId;
  }
  
 /**
  * Get the total cost.
  * @return double	the amount of total cost
  */
  public double getTotalCost() {
    return totalCost;
  }
  
 /**
  * Set the total cost.
  * @param double totalCost: the amount of total cost
  */
  public void setTotalCost(double totalCost) {
    this.totalCost = totalCost;
  }
  
 /**
  * Get the lead time.
  * @return int	the lead time
  */
  public int getLeadTime() {
    return leadTime;
  }
  
 /**
  * Set the total lead time.
  * @param int leadTime: an integer for lead time	
  */
  public void setLeadTime(int leadTime) {
    this.leadTime = leadTime;
  }

 /**
  * Get the available net quantity.
  * @return int	the available net quantity
  */
  public int getNetAvailable() {
    return netAvailable;
  }

 /**
  * Set the available net quantity.
  * @param int netAvailable: the avaiable net quantity  
  */
  public void setNetAvailable(int netAvailable) {
    this.netAvailable = netAvailable;
  }
}
