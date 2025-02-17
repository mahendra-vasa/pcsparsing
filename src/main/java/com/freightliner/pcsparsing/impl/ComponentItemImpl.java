package com.freightliner.pcsparsing.impl;

import java.util.Iterator;

import net.sf.hibernate.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.*;

import com.freightliner.pcsparsing.*;

/**
 * This class implements ComponentItem object associated componentItem table.
 *
 * @author   jftl8v
 * 
 */
public class ComponentItemImpl extends ItemImpl implements ComponentItem, Validatable {

  private static final Log log = LogFactory.getLog(ComponentItemImpl.class);
  
  // All the persistent data fields.
  private BOM bom;
  private double quantity;
  private double canadianCost;
  private double usaCost;
  private String deleteReason = "";
  
  
  private boolean calculatedCanadianCost = false;
  private boolean calculatedUsaCost = false;

   /**
    * Default constructor.
    */
	ComponentItemImpl() {
	  super();
	}
	
   /**
    * Constructor.
    * @param BOM bom: the BOM
    * @param String itemNumber: the item number
    * @param String description: the description of the component item
    * @param Department department: the department
    * 
    * @throws ValidationFailure
    */
	public ComponentItemImpl(BOM bom, String itemNumber, 
							String description, Department department) 
				throws ValidationFailure {
	  	this();
    	setBom(bom);
    	setDescription(description);
    	setNumber(itemNumber);
    	setDepartment(department);
    	bom.add(this);
    	validate();
	}

   /**
    * Check if the status is valid
    * @return boolean	true/false
    */
  	public boolean isStatusValid() {
    	// Coded for speed. Logic duplicated elsewhere
    	if ("D".equals(getStatus()) || "S".equals(getStatus()) || "O".equals(getStatus())) {
      		return false;
    	} else {
      		return true;
    	}
  	}

 /**
  * Check the primary keys to validate the component item.
  * @throws ValidateFailue	if the primary keys are null or of wrong pattern.
  */
  public void validate() throws ValidationFailure {
    if (getNumber() == null) {
      throw new ValidationFailure("Item number cannot be null");
    }
    if (departmentCode == null) {
      throw new ValidationFailure("Department code cannot be null");
    }
    if (getBom() == null) {
      throw new ValidationFailure("BOM cannot be null");
    }
  }

 /**
  * For Hibernate mapping purpose.
  * Compare the composit primary keys between this component item and the other object.
  * @param Object o: an object (ComponentItem)
  * @return boolean	true/false
  */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ComponentItem)) return false;
    if (o == null) return false;

    final ComponentItem item = (ComponentItem) o;
    if (getBom() != null ? !getBom().equals(item.getBom()) : item.getBom() != null) return false;
    if (getNumber() != null ? !getNumber().equals(item.getNumber()) : item.getNumber() != null) return false;

    return true;
  }

   /**
    * For Hibernate mapping purpose.
    * Generate a hash code based on the primary keys.
    * @return int	a hash code
    */
  	public int hashCode() {
  		int result = getNumber().hashCode();
    	result = 29 * (getBom() != null ? getBom().hashCode() : 0);
    	return result;
  	}

   /**
    * Get a string of the object.
    * Returns a string including the class name, BOM and the component item number.
    * @return String
    */
  	public String toString() {
		return "[" + getClass() + getBom() + " '" + getNumber() + "]";
	}

   /**
  	* Get the BOM.
  	* @return BOM	the BOM
  	*/
  	public BOM getBom() {
    	return bom;
  	}

   /**
    * Set the BOM
    * @param BOM bom: the BOM
    */
  	public void setBom(BOM bom) {
    	this.bom = bom;
  	}

   /**
    * Get quantity.
    * @return double	quantity.
    */
  	public double getQuantity() {
    	return quantity;
  	}

   /**
    * Set the quantity
    * @param double quantity: the quantity of the componentItem
    */
 	public void setQuantity(double quantity) {
    	this.quantity = quantity;
  	}
 	
  /**
   * Get the Delete Reason.
   * @return String the Delete Reason
   */
 	public String getDeleteReason() {
	   	return deleteReason;
 	}

  /**
   * Set the Delete Reason.
   * @param String deleteReason
   */
 	public void setDeleteReason(String deleteReason) {
 	   	this.deleteReason = deleteReason;
 	 	}
 	
 	
 	
   /**
    * Check if the revision level is different
    * @return boolean	true/false
    */
  	public boolean isRevisionLevelDifferent() {
    	return !StringUtils.defaultString(mfgRevisionLevel).equals(StringUtils.defaultString(engRevisionLevel));
  	}

   /**
    * Get the module number.
    * @return String the module number
    */
  	public String getModuleNumber() {
    	return getBom().getModuleNumber();
  	}
   
   /**
    * Get the BOM level.
    * @return int	the BOM level
    */
  	public int getBOMLevel() {
    	return 2;
  	}

   /**
    * Calculate the cost in USA.
    * @return double	the cost in USA.
    */
  	public double getUsaCost() {
    	if (!calculatedUsaCost) {
      		calculatedUsaCost = true;
      		for (Iterator iter = getLocations().iterator(); iter.hasNext();) {
        		Location location = (Location) iter.next();
        		if (Integer.parseInt(location.getLoc().trim()) == 9) {
          			usaCost = location.getOrdsTotalCost();
          			
          			if (usaCost == 0) {
            			usaCost = location.getTotalCost();
          			}
          			return usaCost;
        		}
      		}
      		if (log.isDebugEnabled()) 
      			log.debug("Could not find USA location (009) for component item " + getNumber());
    	}
    	return usaCost;
  	}

   /**
    * Calculate the cost in Canada.
    * @return double	the cost in Canada.
    */
  	public double getCanadianCost() {
    	if (!calculatedCanadianCost) {
      		calculatedCanadianCost = true;
      		for (Iterator iter = getLocations().iterator(); iter.hasNext();) {
        		Location location = (Location) iter.next();
        		
        		if (Integer.parseInt(location.getLoc().trim()) == 8) {
          			double canadianCost = location.getOrdsTotalCost();
          			if (canadianCost == 0) {
            			canadianCost = location.getTotalCost();
          			}
          			return canadianCost;
        		}
      		}
       		if (log.isDebugEnabled()) 
       			log.debug("Could not find Canadian location (008) for component item " + getNumber());
    	}
    	return canadianCost;
  	}

   /**
  	* Compare the primary keys between this component item and the other object.
  	* @param Object o: an object (ComponentItem)
    * @return int	an Integer representing less than (negative), equal (0) 
    * 				and more than(positive)
    */
	public int compareTo(Object o) {
    	final ComponentItem oItem = (ComponentItem) o;
    	int result = getNumber().compareTo(oItem.getNumber()) * 1000;
    	result = result + getBom().compareTo(oItem.getBom());
		return result;
	}


}
