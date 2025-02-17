package com.freightliner.pcsparsing;


/**
 * This interface provides some access to ComponentItem.
 *
 * @author   jftl8v
 *
 * 
 */
public interface ComponentItem extends Item {

   /**
    * Get the BOM.
    * @return BOM	the BOM
    */
	public BOM getBom();
	
   /**
    * Set the BOM
    * @param BOM bom: the BOM
    */
	public void setBom(BOM bom);

   /**
    * Check if the revision level is different
    * @return boolean	true/false
    */
  	public boolean isRevisionLevelDifferent();

   /**
    * Check if the status is valid
    * @return boolean	true/false
    */
  	public boolean isStatusValid();
 
   /**
    * Set the quantity
    * @param double quantity: the quantity of the componentItem
    */
  	public void setQuantity(double quantity);

}
