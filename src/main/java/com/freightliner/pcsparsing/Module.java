package com.freightliner.pcsparsing;

import java.io.Serializable;
import java.util.*;

/**
 * This interface provides acsess to Module.
 *
 * @author   jftl8v
 * 
 */
public interface Module extends Serializable, Comparable {
	
   /**
    * Get all the departments and put them on a TreeSet.
    * @return SortedSet	the set of departments
    */
	public SortedSet getDepartments();

   /**
    * Get the description of the module.
    * @return String	the description of the module
    */
	public String getDescription();
	
   /**
    * Set the description of the module.
    * @param String description: the description of the module
    */
	public void setDescription(String description);

   /**
    * Get the module number .
    * @return String
    */
	public String getNumber();
	
   /**
    * Set the module number.
    * @param String module: the module ID
    */
	public void setNumber(String module);
	
   /**
    * Get the sequence number.
    * @return String	the sequence number
    */
	public String getSequenceNumber();
	
   /**
    * Set the sequence number.
    * @param String sequenceNumber: a string of sequence number
    */
	public void setSequenceNumber(String sequenceNumber);

   /**
    * Check if the module has been ordered.
    * @return boolean	true/false
    */
	public boolean isOrdered();
	
   /**
    * Set module as ordered.
    * @param boolean ordered: a boolean value
    */
	public void setOrdered(boolean ordered);

   /**
    * Check if the revision level is updated.
    * @return boolean	true/false
    */
	public boolean isRevisionLevelUpdated();

   /**
    * Get the associated truck.
    * @return Truck	the associated truck
    */
	public Truck getTruck();
	
   /**
    * Set associated truck.
    * @param Truck truck:	the associated truck
    */
	public void setTruck(Truck truck);

   /**
    * Get a set of BOMs.
    * @return Set	 a set of boms
    */
	public Set getBoms();
	
   /**
    * Set associated BOMS.
    * @param Set boms: a set of BOMs
    */
	public void setBoms(Set boms);
	
   /**
    * Add to the associated bom set.
    * @param BOM bom: a BOM
    */
	public void add(BOM bom);

   /**
    * Check if the status is valid.
    * @return boolean true/false
    */
  	boolean isStatusValid();
}
