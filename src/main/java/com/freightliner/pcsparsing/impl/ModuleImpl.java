package com.freightliner.pcsparsing.impl;

import java.util.*;

import net.sf.hibernate.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.Module;

/**
 * This class implements the Module object associated with module table.
 *
 * @author   jftl8v
 *
 */
public class ModuleImpl implements Module, Comparable, Validatable  {

  private static final Log log = LogFactory.getLog(ModuleImpl.class);
  
 /**
  * The module order
  */
  private ModuleOrderImpl order;	
  
  // All the persistent data fields
  // @TODO: Make constants that have states: T, F, Unitinitialized
  private boolean revisionLevelUpdated = false;
  private boolean statusValid = true;
  private ModulePK compID;
  private Truck truck;
  private Set boms = new HashSet();
	private String description;
	private Set departmentCodes = new HashSet();
	private String number;
	private String sequenceNumber;
  
  /**
   * Default constructor.
   * Required for persistence mechanism. Do not use.
   */
  ModuleImpl() {
    super();
  }

 /**
  * Constructor.
  * @param Truck truck: associated truck
  * @param String number: the module number
  * @param String sequenceNumber: the sequence number
  */
  public ModuleImpl(Truck truck, String number, String sequenceNumber) throws ValidationFailure {
    this();
    setNumber(number);
    setSequenceNumber(sequenceNumber);
    setTruck(truck);
    truck.add(this);
    compID = new ModulePK(truck.getSerialNumber(), number, sequenceNumber);
    validate();
    if (log.isDebugEnabled()) log.debug("Module() " + number + " order: " + order + " getOrder() " + getOrder() +
    		" is ordered: " + isOrdered());
  }

   /**
    * Check if the status is valid.
    * @return boolean true/false
    */
  	public boolean isStatusValid() {
    	return statusValid;
  	}

   /**
    * Set the status as statusValid.
    * @param boolean statusValid: true/false
    */
  	public void setStatusValid(boolean statusValid) {
    	this.statusValid = statusValid;
  	}

   /**
    * Check if the module has been ordered.
    * @return boolean	true/false
    */
  	public boolean isOrdered() {
    	if (log.isDebugEnabled()) {
    		log.debug("isOrdered(): " + (getOrder() != null));
     	}
    	return getOrder() != null;
  	}

   /**
    * Set module as ordered.
    * @param boolean ordered: a boolean value
    */
  	public void setOrdered(boolean isOrdered) {
  		if (isOrdered) {
      		setOrder(new ModuleOrderImpl(this));
    	} else {
    		if (order != null) {
      			order.setModule(null);
    		}
      		setOrder(null);
    	}
  		if (log.isDebugEnabled()) 
  			log.debug("setOrdered" + isOrdered + " order: " + order);
  	}

   /**
    * Get the module order of this module.
    * @return ModuleOrderImpl	the module order
    */
  	public ModuleOrderImpl getOrder() {
    	return order;
  	}

   /**
    * Set the module order of this module.
    * @param ModuleOrderImpl order: the module order
    */
  	public void setOrder(ModuleOrderImpl order) {
    	this.order = order;
  	}

   
 /**
  * For Hibernate mapping purpose.
  * Generate a hash code based on the primary keys.
  * @return int	a hash code
  */
  public int hashCode() {
  		int result = 19 * getNumber().hashCode();
  		if (getCompID() != null) {
  			result = result + getCompID().getSerialNumber().hashCode() +
  						getCompID().getSequenceNumber().hashCode();
  		}
  		return result;
  }

 /**
  * Compare the primary keys between this module and the other object.
  * @param Object o: an object (Module)
  * @return int	an Integer representing less than (negative), equal (0) 
  * 				and more than(positive)
  */
  public int compareTo(Object o) {
    if (getNumber() == null || getSequenceNumber() == null) {
      throw new RuntimeException("Module number should never be null");
    }
    Module otherModule = (Module) o;
    if (o == null) {
      return getNumber().compareToIgnoreCase(null);
    }
    int result = getNumber().compareToIgnoreCase(otherModule.getNumber());
    if (result == 0) {
    	return getSequenceNumber().compareToIgnoreCase(otherModule.getSequenceNumber());	
    }
    
    return result;
  }
  
 /**
  * For Hibernate mapping purpose.
  * Compare the composit primary keys between this module and the other object.
  * @param Object o: an object (Module)
  * @return boolean	true/false
  */
  public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Module)) return false;
    if (o == null) return false;

		final ModuleImpl module = (ModuleImpl) o;

  	return module.getCompID().equals(getCompID());
	}
 
 /**
  * Check the primary keys to validate the module.
  * @throws ValidateFailue	if the associated truck is null and the primary keys are null or of wrong pattern.
  */
  public void validate() throws ValidationFailure {
    Truck truck = getTruck();
    if (truck == null) {
      throw new ValidationFailure("Module " + getNumber() + " invalid, 'truck' is null. Module must be associated with a truck.");
    }
    if (getNumber() == null || getNumber().length() != 3) {
      throw new IllegalArgumentException("Module number must be exactly three characters");
    }
    if (getCompID().getSequenceNumber() == null) {
    	throw new IllegalArgumentException("Sequence number must not be null.");
    }
  }
  
   /**
    * Check if the revision level is updated.
    * @return boolean	true/false
    */
  	public boolean isRevisionLevelUpdated() {
    	return revisionLevelUpdated;
  	}

   /**
    * Set the revision level as updated.
    * @param boolean updated: true/false
    */
  	public void setRevisionLevelUpdated(boolean updated) {
    	revisionLevelUpdated = updated;
  	}

   /**
    * Get the primary key composit object.
    * @return ModulePK	the primary key
    */
	public ModulePK getCompID() {
		return compID;
	}
	/**
	 * @param compID The compID to set.
	 */
	public void setCompID(ModulePK compID) {
		this.compID = compID;
		setNumber(compID.getNumber());
	}
	
		
	
   /**
    * Get all the departments and put them on a TreeSet.
    * @return SortedSet	the set of departments
    */
	/* @TODO: Filter somehwere else? */
  	public SortedSet getDepartments() {
   		SortedSet sortedDepartments = new TreeSet();
    	if (!departmentCodes.isEmpty()) {
      		for (Iterator iterator = getDepartmentCodes().iterator(); iterator.hasNext();) {
        		String departmentCode = (String) iterator.next();
        		if (Department.BODY.getAbbreviation().equals(departmentCode) ||
                		Department.CHASSIS.getAbbreviation().equals(departmentCode) ||
                		Department.ELECTRICAL.getAbbreviation().equals(departmentCode)) {
            		sortedDepartments.add(Department.get(departmentCode));
        		}
      		}
    	}
    	return sortedDepartments;
  	}
  	
   /**
    * Get the department codes.
    * @return Set a set of department codes.
    */
  	Set getDepartmentCodes() {
		return departmentCodes;
	}

   /**
    * Set the department codes.
    * @param Set departmentCodes: a set of department codes.
    */
  	void setDepartmentCodes(Set departmentCodes) {
		this.departmentCodes = departmentCodes;
	}

   /**
    * Get the description of the module.
    * @return String	the description of the module
    */
	public String getDescription() {
		return description;
	}

   /**
    * Set the description of the module.
    * @param String description: the description of the module
    */
	public void setDescription(String description) {
		this.description = description;
	}

   /**
    * Get the module number .
    * @return String
    */
	public String getNumber() {
		return number;
	}

   /**
    * Set the module number.
    * @param String module: the module ID
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
    * @param String sequenceNumber: a string of sequence number
    */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;	
	}

   /**
    * Get the associated truck.
    * @return Truck	the associated truck
    */
	public Truck getTruck() {
		return truck;
	}

   /**
    * Set associated truck.
    * @param Truck truck:	the associated truck
    */
	public void setTruck(Truck truck) {
		this.truck = truck;
	}

   /**
    * Get a set of BOMs.
    * @return Set	 a set of boms
    */
	public Set getBoms() {
		if (boms == null) {
			boms = new HashSet();
		}
		return boms;
	}

   /**
    * Set associated BOMS.
    * @param Set boms: a set of BOMs
    */
	public void setBoms(Set boms) {
		this.boms = boms;
	}

   /**
    * Add to the associated bom set.
    * @param BOM bom: a BOM
    */
	public void add(BOM bom) {
		getBoms().add(bom);
	}

   /**
    * Get a string of the object.
    * Returns a string including the class name and the module number.
    */
	public String toString() {
		return getClass() + "[" + number + "]";
	}

}
