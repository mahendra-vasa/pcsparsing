package com.freightliner.pcsparsing.impl;

import java.util.*;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.Module;

/**
 * This class implements the BOM object associated with the BOM table.
 *
 * @author   jftl8v
 * 
 */
public class BOMImpl extends ItemImpl implements BOM, Validatable {
  private BOMPK compID;
  private Set items = new TreeSet();
  private Module module;
  private String cwoIndicator = "";

  private String deleteReason = "";


  /**
   * Default constructor.
   * Required for persistence mechanism. Do not use.
   */
	BOMImpl() {
		super();
		typeCode = ItemType.BOM.getCode();
	}
   
   /**
    * Constructor.
    * @param Module module: the associated module
    * @param String number: the bom number
    * @param String description: the bom description
    */
	public BOMImpl(Module module, String number, String description) {
		this();
    if (module == null) {
        throw new IllegalArgumentException("Module cannot be null");
    }
		setNumber(number);
		setDescription(description);
    Truck truck = module.getTruck();
    compID = new BOMPK(truck.getTsoSequenceNumber(), 
    					module.getSequenceNumber(), 
    					truck.getSerialNumber(), 
    					module.getNumber(), number);
    module.add(this);
	}

   /**
    * Set the bom type.
    * @param ItemType type: an item type
    */
    public void setType(ItemType type) {
    	if (type != null) {
      		typeCode = type.getCode();
    	}
  	}

 /**
  * Get the type code.
  * @return String	 the type code
  */
  public String getTypeCode() {
    return typeCode;
  }

 /**
  * Set the type code.
  * @param String typeCode: the type code
  */
  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
    setType(ItemType.get(typeCode));
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
  * Check the primary keys to validate the bom.
  * @throws ValidateFailue	if the primary keys are null or of wrong pattern.
  */
  public void validate() throws ValidationFailure {
    if (isCwo() && getItems() != null && !getItems().isEmpty()) {
      throw new ValidationFailure("Invalid BOM " + getNumber() + ". CWO cannot contain any Items");
    }
    if (!isCwo() && ItemType.CWO.getCode().equals(getTypeCode())) {
      throw new ValidationFailure("Invalid BOM " + getNumber() + ". Must be CWO if type code is '" + ItemType.CWO.getCode() + "'");
    }
  }

   /**
	*	Add a componentItem to the associated items  
	*  @param ComponentItem item: a ComponentItem.
	*/ 
  	public void add(ComponentItem item) {
    	getItems().add(item);
  	}

 	/**
	 * Get a set of items.  
	 * @return Set	the set of items.
	 */ 
  	public Set getItems() {
    	return items;
  	}
  
   /**
	* Set a set of items.  
	* @param Set items: the set of items.
	*/
  	public void setItems(Set items) {
    	this.items = items;
  	}
  
   /**
	* Check if this bom is a CWO (customer-work-order).  
	* @return boolean	true/false.
	*/ 
  	public boolean isCwo() {
    	return "C".equals(cwoIndicator);
  	}
  
   /**
	* Set the indicator of CWO (customer-work-order).  
	* @param String cwoIndicator: the indicator of CWO.
	*/
  	public void setCwoIndicator(String cwoIndicator) {
  		this.cwoIndicator = cwoIndicator;
  	}

   /**
    * Get the module number.
    * @return String	the module number
  	public String getModuleNumber() {
    	return compID.getModuleNumber();
  	}

   /**
    * Get the BOM level.
    * @return int	the BOM level
    */
  	public int getBOMLevel() {
    	return 1;
  	}

   /**
    * Get the cost in USA.
    * @return double	the cost in USA.
    */
  	public double getUsaCost() {
    	return 0;
  	}
   
   /**
    * Get the cost in Canada.
    * @return double	the cost in Canada.
    */
  	public double getCanadianCost() {
    	return 0;
  	}

  /**
   * Always return 0. BOM does not have quantity. Avoids seperate handling for BOMs
   * @return double
   */
  public double getQuantity() {
    return 0;
  }

 /**
  * Get a string of the object.
  * Returns a string including the class name, the serial number, the tso sequence number, the module name,
  * the sequence number,the item number, the description, the manufacturing revision level,
  * the engineering revision level, the status,the type code and the department.
  * @return String
  */
  public String toString() {
		return "[" + getClass() + " '" 
		+ compID.getSerialNumber() + " '" + compID.getTsoSequenceNumber() + "' '" + compID.getModuleNumber() + "' '" 
		+ compID.getSequenceNumber() + "' '" + compID.getNumber() + "' '" 
		+ getDescription() + " " + mfgRevisionLevel + " " + engRevisionLevel + " " 
		+ getStatus() + " " + typeCode + " " + getDepartment() + "]";
	}

 /**
  * For Hibernate mapping purpose.
  * Compare the composit primary keys between this bom and the other object.
  * @param Object o: an object (BOM)
  * @return boolean	true/false
  */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BOM)) return false;
    if (o == null) return false;
    final BOMImpl oBOM = (BOMImpl) o;
    return compID.equals(oBOM.getCompID());
  }

 /**
  * For Hibernate mapping purpose.
  * Generate a hash code based on the primary keys.
  * @return int	a hash code
  */
  public int hashCode() {
    return compID.hashCode();
  }

   /**
  	* Compare the primary keys between this bom and the other object.
  	* @param Object o: an object (BOM)
    * @return int	an Integer representing less than (negative), equal (0) 
    * 				and more than(positive)
    */
	public int compareTo(Object o) {
    final BOMImpl oBOM = (BOMImpl) o;
    return compID.compareTo(oBOM.getCompID());
	}

 /**
  * Get the primary key object.
  * @return BOMPK the primary key
  */
  public BOMPK getCompID() {
    return compID;
  }
  
 /**
  * Set the primary key object.
  * @param BOMPK compID: the primary key
  */
  public void setCompID(BOMPK compID) {
    this.compID = compID;
  }
  
 /**
  * Get the ID number of the item
  * @return String	the item number
  */
  public String getNumber() {
    return compID.getNumber();
  }
  
 /**
  * Get the associated module.
  * @return Module	 the associated module
  */
  public Module getModule() {
    return module;
  }
  
 /**
  * set the associated module.
  * @param Module module: the associated module
  */
  public void setModule(Module module) {
    this.module = module;
  }
   
   /**
    * Get the indicator of CWO (customer working order).
    * @return String the CWO indicator
    */
	public String getCwoIndicator() {
		return cwoIndicator;
	}
	
   /**
    * Get the module number.
    * @return String	the module number
    */
    public String getModuleNumber() {
    	return compID.getModuleNumber();
  	}


}
