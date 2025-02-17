package com.freightliner.pcsparsing.impl;

import java.io.Serializable;

/**
 * This class implements the Module Order object associated module order table.
 *
 * @author   jftl8v
 *
 */
public class ModuleOrderImpl implements Serializable {
	
	// All the data fields
	private ModuleImpl module;
	private ModulePK compID;

 /**
  * Default constructor
  */
  public ModuleOrderImpl() { }

 /**
  * Constructor.
  * @param ModuleImpl module: the module
  */
  public ModuleOrderImpl(ModuleImpl module) {
    setModule(module);
    setCompID(new ModulePK(module.getCompID().getSerialNumber(), 
    						module.getCompID().getNumber(),
    					  	module.getCompID().getSequenceNumber()));
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
    * For Hibernate mapping purpose.
    * Compare the composit primary keys between this module order and the other object.
    * @param Object o: an object (ModuleOrderImpl)
    * @return boolean	true/false
    */
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ModuleOrderImpl)) return false;
	  if (o == null) return false;
	
		final ModuleOrderImpl moduleOrder = (ModuleOrderImpl) o;
	
		return moduleOrder.getCompID().equals(getCompID());
	
	}

   /**
    * Get the associated module.
    * @return ModuleImpl	the associated module
    */
	public ModuleImpl getModule() {
		return module;
	}

   /**
    * Set the associated module.
    * @param ModuleImpl module: the associated module
    */
	public void setModule(ModuleImpl module) {
		this.module = module;
	}
	
 /**
  * Get the primary key object.
  * @return ModulePK	the primary key object
  */
  ModulePK getCompID() {
    return compID;
  }
  
 /**
  * Set the primary key object.
  * @param ModulePK compID: the primary key object
  */
  void setCompID(ModulePK compID) {
    this.compID = compID;
  }
}
