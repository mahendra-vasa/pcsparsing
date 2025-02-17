package com.freightliner.pcsparsing;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * This interface provides access to the data fields of Truck.
 *
 * @author   jftl8v
 * 
 */
public interface Truck extends Serializable, Comparable {
  
   /**
  	* Get the last update date.
  	* @return Date	the last update date
  	*/
  	public Date getLastUpdated();
  
   /**
    * Set the last update date.
    * @param Date date: the last update date
    */
  	public void setLastUpdated(Date date);
  	
   /**
    * Formate the last update date.
    * @return String	the last update date
    */
  	public String getLastUpdatedFormatted();

   /**
    * Get the build date.
    * @return Date	the build date
    */
  	public Date getBuildDate();
  	
   /**
    * Set the build date.
    * @param Date date: the build date	
    */
  	public void setBuildDate(Date date);
  
   /**
    * Get the customer's name.
    * @return String	the customer's name
    */
  	public String getCustomerName();
  	
   /**
    * Set the customer's name.
    * @param String name:	the customer's name
    */
  	public void setCustomerName(String name);

   /**
    * Get the serial number of the truck.
    * @return	String	the serial number of the truck.
    */
	public String getSerialNumber();
	
   /**
    * Set the serial number of the truck.
    * @param String serialNumber:	a serial number
    */
	public void setSerialNumber(String serialNumber);	
	
   /**
    * Get the modules belong to the truck.
    * @return Set	a set of Modules.
    */
	public Set getModules();
	
   /**
    * Set the modules for the truck.
    * @param Set modules:	a set of Modules
    */
	public void setModules(Set modules);
	
   /**
    * For Hibernate mapping purpose. 
    * Comparing all the primary keys of two trucks.
    * @param Object obj:	an object (truck) for comparing
    * @return boolean	true/false
    */
	public boolean equals(Object obj);
	
   /**
    * For Hibernate mapping purpose.
    * Uses all the primary keys to generate hashcode.
    * @return int	a hashcode
    */
	public int hashCode();
	
   /**
    * Get the String of the truck.
    * @return String	a string of serial number and tso-sequence-number.
    */
	public String toString();

   /**
    * Add to the set of Modules.
    * @param Module module:	a Module
    */
  	public void add(Module module);
  	
   /**
    * Get the build location.
    * @return String	the build location
    */
  	public String getBuildLocation();
  
   /**
    * Get the vehicle model number.
    * @return String	the vehicle model number
    */
  	public String getVehicleModelNumber();
  	
   /**
    * Get the quantity of split.
    * @return int	the quantity of split.
    */
  	public int getQuantitySplit();
  	
   /**
    * Get the count of CWO (customer working order)
    * @return int	the total count of CWO of the truck
    */
  	public int getCwoCount();
  	
   /**
    * Get the engineering status code.
    * @return String	the engineering status code
    */
  	public String getEngineeringStatusCode();
  	
   /**
    * Get the last translate date.
    * @return Date		the last translate date
    */
  	public Date getLastTranslateDate();
  	
   /**
    * Get the last TCO (Technicle Change Order) number.
    * @return int	the last TCO number.
    */
  	public int getLastTcoNumber();
  	
   /**
    * Get the engineering change level.
    * @return int	the engineering change level
    */
  	public int getEngineeringChangeLevel();
  	
   /**
    * Get the vehicle order class.
    * @return String	the vehicle order class.
    */
  	public String getVehicleOrderClass();
  	
   /**
    * Get the customer account number.
    * @return String	the customer account number.
    */
  	public String getCustomerAccountNumber();
  	
   /**
    * Get the responsibility ID.
    * @return String	the responsibility ID.
    */
  	public String getRespId();
  	
   /**
    * Get the date when the truck frame was changed last time.
    * @return Date		the date changing truck frame
    */
  	public Date getLastFrameDate();
  	
   /**
    * Get the date when the truck released from engineering to manufacturing.
    * @return Date	 the date the truck released from engineering to manufacturing
    */
  	public Date getEngToMfgReleaseDate();
  	
   /**
    * Get the order status code.
    * @return String	the order status code.
    */
  	public String getOrderStatusCode();
  	
   /**
    * Set the build location.
    * @param String buildLocation: the build location
    */
  	public void setBuildLocation(String location);
  	
   /**
    * Set the vehicle model number
    * @param String vehicleModelNumber: the vehicle model number
    */
  	public void setVehicleModelNumber(String vehicleModelNumber);
  	
   /**
    * Set the quantity of split
    * @param int quantitySplit: the quantity of split 
    */
  	public void setQuantitySplit(int quantity);
  	
   /**
    * Set the total count of CWO (customer working order)
    * @param int cwoCount: the count of CWO
    */
  	public void setCwoCount(int cwoCount);
  	
   /**
    * Set the order status order.
    * @param String orderStatusCode: the order status code.
    */
  	public void setOrderStatusCode(String code);
  	
   /**
    * Set the engineering status code.
    * @param String engineeringStatusCode: the engineering status code
    */
  	public void setEngineeringStatusCode(String engineeringStatusCode);
  	
   /**
    * Set the last translate date.
    * @param Date lastTranslateDate: the last translate date
    */	
  	public void setLastTranslateDate(Date lastTranslateDate);
  	
   /**
    * Set the last TCO (Technicle Change Order) number.
    * @param int lastTcoNumber: the last TCO number
    */
  	public void setLastTcoNumber(int lastTcoNumber);
  
   /**
    * Set the engineering change level.
    * @param int engineeringChangeLevel: the engineering change level
    */
  	public void setEngineeringChangeLevel(int engineeringChangeLevel);
  	
   /**
    * Set the vehicle order class.
    * @param String vehicleOrderClass: the vehicle order class
    */
  	public void setVehicleOrderClass(String vehicleOrderClass);
  	
   /**
    * Set the customer account number.
    * @param String customerAccountNumber: the customer account number
    */
  	public void setCustomerAccountNumber(String customerAccountNumber);
  	
   /**
    * Set the responsibility ID.
    * @param String respId: the responsibility ID	
    */
  	public void setRespId(String respId);
  	
   /**
    * Set the date when the truck frame was changed.
    * @param Date lastFrame: the date the truck frame changed
    */
  	public void setLastFrameDate(Date lastFrameDate);
  	
   /**
    * Set the date when the truck released from engineering to manufacturing.
    * @param Date engToMfgReleaseDate: the release date from engineering to manufacturing
    */
  	public void setEngToMfgReleaseDate(Date releaseDate);
   
   /**
    * Set the tso sequence number.
    * @param String sequenceNumber:  the tso sequence number
    */
  	public void setTsoSequenceNumber(String sequenceNumber);
  	
   /**
    * Get the tso sequence number.
    * @return String	the tso sequence number
    */
  	public String getTsoSequenceNumber();
  	
  	/* get the TimeStamp for the created_ts filed
	 *  of DB
	 */
	public Timestamp getCreatedTimeStamp();
	/* Set the TimeStamp for the created_ts filed
	 *  of DB
	 */
	public void setCreatedTimeStamp(Timestamp createdTimeStamp) ;
}