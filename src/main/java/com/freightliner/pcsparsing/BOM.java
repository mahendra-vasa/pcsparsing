package com.freightliner.pcsparsing;

import java.util.Set;

/**
 * This interface provides access to BOM. 
 *
 * @author   jftl8v
 * 
 */
public interface BOM extends Item {
	
	/**
	 *	Get a set of items.  
	 *  @return Set	the set of items.
	 */ 
	public Set getItems();
	
   /**
	* Set a set of items.  
	*  @param Set items: the set of items.
	*/ 
	public void setItems(Set items);

  	
  	/**
	 *	Add a componentItem to the associated items  
	 *  @param ComponentItem item: a ComponentItem.
	 */ 
	 public void add(ComponentItem item);
	 
	/**
	 * Check if this bom is a CWO (customer-work-order).  
	 * @return boolean	true/false.
	 */ 
	 public boolean isCwo();
	
	/**
	 * Set the indicator of CWO (customer-work-order).  
	 * @param String cwoIndicator: the indicator of CWO.
	 */ 
	 public void setCwoIndicator(String cwoIndicator);
  
}
