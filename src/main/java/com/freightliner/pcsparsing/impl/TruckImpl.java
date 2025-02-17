package com.freightliner.pcsparsing.impl;




import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sf.hibernate.Validatable;
import net.sf.hibernate.ValidationFailure;

import org.apache.commons.lang.StringUtils;

import com.freightliner.pcsparsing.Module;
import com.freightliner.pcsparsing.Truck;

/**
 * This class implements the Truck object associated truck table.
 *
 * @author   jftl8v
 *
 */
public class TruckImpl implements Truck, Validatable {

   /**
    * Default tso sequence number
    */
  	private String tsoSequenceNumber = "  0";  	
   
  	public static final Truck NULL = new TruckImpl("");
  	
   /**
    * Date Format
    */
  	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("M/d/y");

    // All the persistent data fields
  	private Date buildDate;
  	private String customerName = "";
  	private Set modules = new HashSet();
  	private String serialNumber = "";
  	private String buildLocation = "";
  	private String vehicleModelNumber = "";
  	private int quantitySplit;
  	private int cwoCount;
  	private String engineeringStatusCode = "";
  	private Date lastTranslateDate;
  	private int lastTcoNumber;
  	private int engineeringChangeLevel;
  	private String vehicleOrderClass = "";
  	private String customerAccountNumber = "";
  	private String respId = "";
  	private Date lastFrame;
  	private Date engToMfgReleaseDate;
  	private String orderStatusCode = "";
  	private Date lastUpdated;
  	
  	//new var for the created TS
  	private Timestamp createdTimeStamp = null; 

   /**
    * Default constructor.
    */
	TruckImpl() {
		setSerialNumber("");
		setTsoSequenceNumber("");
	}
	
	/**
	 * Constructor.
	 * @param String serialNumber:	a serial number.
	 * @throws IllegalArgumentException if serialNumber is null
	 */
	public TruckImpl(String serialNumber) {
	  this();
		if (serialNumber == null) {
			throw new IllegalArgumentException("Serial number cannot be null");
		}
		setSerialNumber(serialNumber);
	}

   /**
    * Get the customer's name.
    * @return String	the customer's name
    */
  	public String getCustomerName() {
    	return customerName;
  	}

   /**
    * Set the customer's name.
    * @param String name:	the customer's name
    */
  	public void setCustomerName(String name) {
    	this.customerName = name;
  	}
	
   /**
    * Add to the set of Modules.
    * @param Module module:	a Module
    */
  	public void add(Module module) {
    	getModules().add(module);
  	}
   
   /**
    * Get the serial number of the truck.
    * @return String the serial number of the truck.
    */
  	public String getSerialNumber() {
    	return serialNumber;
  	}

   /**
    * Set the serial number of the truck.
    * @param String serialNumber:	a serial number
    */
  	public void setSerialNumber(String serialNumber) {
    	this.serialNumber = StringUtils.trim(serialNumber);
  	}

   /**
    * Get the modules belong to the truck.
    * @return Set	a set of Modules.
    */
  	public Set getModules() {
    	return modules;
  	}

   /**
    * Set the modules for the truck.
    * @param Set modules:	a set of Modules
    */
  	public void setModules(Set modules) {
    	this.modules = modules;
  	}

   /**
    * Get the String of the truck.
    * @return String	a string of serial number and tso-sequence-number.
    */
  	public String toString() {
    	return "[Truck '" + serialNumber + "' '" + getTsoSequenceNumber() + "']";
  	}

   /**
    * Get the build date.
    * @return Date	the build date
    */
  	public Date getBuildDate() {
   		return buildDate;
  	}

   /**
    * Set the build date.
    * @param Date date:	the build date
    */
  	public void setBuildDate(Date date) {
    	this.buildDate = date;
  	}
   
   /**
    * Get the build location.
    * @return String	the build location
    */
  	public String getBuildLocation() {
    	return buildLocation;
  	}

   /**
    * Get the vehicle model number.
    * @return String	the vehicle model number
    */
  	public String getVehicleModelNumber() {
    	return vehicleModelNumber;
  	}

   /**
    * Get the quantity of split.
    * @return int	the quantity of split.
    */
  	public int getQuantitySplit() {
    	return quantitySplit;
  	}

   /**
    * Get the count of CWO (customer working order)
    * @return int	the total count of CWO of the truck
    */
  	public int getCwoCount() {
    	return cwoCount;
  	}

   /**
    * Get the engineering status code.
    * @return String	the engineering status code
    */
  	public String getEngineeringStatusCode() {
    	return engineeringStatusCode;
  	}

   /**
    * Get the last translate date.
    * @return Date	the last translate date
    */
  	public Date getLastTranslateDate() {
    	return lastTranslateDate;
  	}

   /**
    * Get the last TCO (Technicle Change Order) number.
    * @return int	the last TCO number.
    */
  	public int getLastTcoNumber() {
    	return lastTcoNumber;
  	}

   /**
    * Get the engineering change level.
    * @return int	the engineering change level
    */
  	public int getEngineeringChangeLevel() {
    	return engineeringChangeLevel;
  	}

   /**
    * Get the vehicle order class.
    * @return String	the vehicle order class.
    */
  	public String getVehicleOrderClass() {
    	return vehicleOrderClass;
  	}

   /**
    * Get the customer account number.
    * @return String	the customer account number.
    */
  	public String getCustomerAccountNumber() {
    	return customerAccountNumber;
  	}

   /**
    * Get the responsibility ID.
    * @return String	the responsibility ID.
    */
  	public String getRespId() {
    	return respId;
  	}

   /**
    * Get the date when the truck frame was changed last time.
    * @return Date		the date changing truck frame
    */
  	public Date getLastFrameDate() {
    	return lastFrame;
  	}

   /**
    * Get the date when the truck released from engineering to manufacturing.
    * @return Date	 the date the truck released from engineering to manufacturing
    */
  	public Date getEngToMfgReleaseDate() {
    	return engToMfgReleaseDate;
  	}

   /**
    * Get the order status code.
    * @return String	the order status code.
    */
  	public String getOrderStatusCode() {
    	return orderStatusCode;
  	}

   /**
    * Set the customer account number.
    * @param String customerAccountNumber: the customer account number
    */
  	public void setCustomerAccountNumber(String customerAccountNumber) {
    	this.customerAccountNumber = customerAccountNumber;
  	}
  	
   /**
    * Set the build location.
    * @param String buildLocation: the build location
    */
  	public void setBuildLocation(String buildLocation) {
    	this.buildLocation = buildLocation;
  	}
  	
   /**
    * Set the total count of CWO (customer working order)
    * @param int cwoCount: the count of CWO
    */
  	public void setCwoCount(int cwoCount) {
    	this.cwoCount = cwoCount;
  	}
   
   /**
    * Set the date when the truck frame was changed.
    * @param Date lastFrame: the date the truck frame changed
    */
  	public void setLastFrameDate(Date lastFrame) {
    	this.lastFrame = lastFrame;
  	}
  	
   /**
    * Set the engineering change level.
    * @param int engineeringChangeLevel: the engineering change level
    */
  	public void setEngineeringChangeLevel(int engineeringChangeLevel) {
    	this.engineeringChangeLevel = engineeringChangeLevel;
  	}
  	
   /**
    * Set the engineering status code.
    * @param String engineeringStatusCode: the engineering status code
    */
  	public void setEngineeringStatusCode(String engineeringStatusCode) {
    	this.engineeringStatusCode = engineeringStatusCode;
  	}
  	
   /**
    * Set the date when the truck released from engineering to manufacturing.
    * @param Date engToMfgReleaseDate: the release date from engineering to manufacturing
    */
  	public void setEngToMfgReleaseDate(Date engToMfgReleaseDate) {
    	this.engToMfgReleaseDate = engToMfgReleaseDate;
  	}
  	
   /**
    * Set the last TCO (Technicle Change Order) number.
    * @param int lastTcoNumber: the last TCO number
    */
  	public void setLastTcoNumber(int lastTcoNumber) {
    	this.lastTcoNumber = lastTcoNumber;
  	}
  	
   /**
    * Set the last translate date.
    * @param Date lastTranslateDate: the last translate date
    */	
  	public void setLastTranslateDate(Date lastTranslateDate) {
    	this.lastTranslateDate = lastTranslateDate;
  	}

   /**
    * Set the order status order.
    * @param String orderStatusCode: the order status code.
    */
  	public void setOrderStatusCode(String orderStatusCode) {
    	this.orderStatusCode = orderStatusCode;
  	}
  	
   /**
    * Set the quantity of split
    * @param int quantitySplit: the quantity of split 
    */
  	public void setQuantitySplit(int quantitySplit) {
    	this.quantitySplit = quantitySplit;
  	}
  	
   /**
    * Set the responsibility ID.
    * @param String respId: the responsibility ID	
    */
  	public void setRespId(String respId) {
    	this.respId = respId;
  	}
  	
   /**
    * Set the vehicle model number
    * @param String vehicleModelNumber: the vehicle model number
    */
  	public void setVehicleModelNumber(String vehicleModelNumber) {
    	this.vehicleModelNumber = vehicleModelNumber;
  	}
  	
   /**
    * Set the vehicle order class.
    * @param String vehicleOrderClass: the vehicle order class
    */
  	public void setVehicleOrderClass(String vehicleOrderClass) {
    	this.vehicleOrderClass = vehicleOrderClass;
  	}
   
   /**
  	* Get the last update date.
  	* @return Date the last update date
  	*/
  	public Date getLastUpdated() {
    	return lastUpdated;
  	}
  
   /**
    * Set the last update date.
    * @param Date lastUpdated: the last update date
    */
  	public void setLastUpdated(Date lastUpdated) {
    	this.lastUpdated = lastUpdated;
  	}
  
   /**
    * Formate the last update date.
    * @return String	the last update date
    */
	public String getLastUpdatedFormatted() {
	  if (lastUpdated != null) {
	    return DATE_FORMAT.format(lastUpdated);
	  } else {
	    return "Unknown";
	  }
	}
	
   /**
    * Get the tso sequence number.
    * @return String	the tso sequence number
    */
  	public String getTsoSequenceNumber() {
    	return tsoSequenceNumber;
  	}

   /**
    * Set the tso sequence number.
    * @param String sequenceNumber:  the tso sequence number
    */
  	public void setTsoSequenceNumber(String tsoSequenceNumber) {
    	this.tsoSequenceNumber = StringUtils.trim(tsoSequenceNumber);
  	}
  	

	/* get the TimeStamp for the created_ts filed
	 *  of DB
	 */
	public Timestamp getCreatedTimeStamp() {
		return createdTimeStamp;
	}
	/* Set the TimeStamp for the created_ts filed
	 *  of DB
	 */
	public void setCreatedTimeStamp(Timestamp createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

   /**
    * Inherited from interface Validatable.
    * Validate the truck's serialNumber.
    * @throws ValidationFailure 	if there's no serial number or serial number is illegle.
    */
  	public void validate() throws ValidationFailure {
    	String serialNumber = getSerialNumber();
    	if (serialNumber == null || StringUtils.strip(serialNumber).length() != 6) {
      	throw new ValidationFailure("Invalid serial number '" + serialNumber + "' Serial number must be exactly six digits.");
    	}
  	}

   /**
    * For Hibernate mapping purpose. 
    * Comparing all the primary keys of two trucks.
    * @param Object obj: an object (Truck) for comparing
    * @return boolean	true/false
    */
  	public boolean equals(Object obj) {
    	if (obj == null) return false;
    	if (!(obj instanceof Truck)) return false;
    	Truck oTruck = (Truck) obj;
    	return oTruck.getSerialNumber().equals(getSerialNumber()) && 
        	oTruck.getTsoSequenceNumber().equals(getTsoSequenceNumber());
  	}

   /**
    * For Hibernate mapping purpose.
    * Uses all the primary keys to generate a hashcode.
    * @return int	a hashcode
    */
  	public int hashCode() {
    	return getSerialNumber().hashCode() * 13 + getTsoSequenceNumber().hashCode();
  	}

   /**
    * Compare the primary keys of two objects (trucks).
    * @param Object o: an object (Truck)
    * @return int	an Integer representing less than (negative), equal (0) 
    * 				and more than(positive)
    */
  	public int compareTo(Object o) {
    	final Truck oTruck = (Truck) o;
    	return getSerialNumber().compareTo(oTruck.getSerialNumber()) * 10 + 
    		getTsoSequenceNumber().compareTo(oTruck.getTsoSequenceNumber());
  	}
  	

	



}
