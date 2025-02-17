package com.freightliner.pcsparsing.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.freightliner.pcsparsing.SerialNumber;

/**
 * This class implements the Serial Number object associated Serial Number table.
 *
 * @author   jftl8v
 *
 */
public class SerialNumberImpl implements SerialNumber {
  
 /**
  * Simple date format
  */
  static final DateFormat DATE_FORMAT = new SimpleDateFormat("M/d/y");

  // All persistent data fields
  private String cmcsNumber = "";
  private String description;
  private String number;
  private Date lastUpdated;
  private String explodeCode = "";
  private String markedForDeletionCode = "";
  private String parseCode = "";
  private String invalidCode = "N";

 /**
  * Default constructor.
  */
  public SerialNumberImpl() {  }
  
 /**
  * Constructor.
  * @param String number: the serial number
  * @param String description: the description of the serial number.
  * @param String cmcsNumber: the CMCS number(job_no)
  */
  public SerialNumberImpl(String number, String description, String cmcsNumber) {
  	setNumber(number);
  	setDescription(description);
  	setCmcsNumber(cmcsNumber);
  }
  
   /**
  	* Get the date last updated.
  	* @return Date	the last update date
  	*/
	public Date getLastUpdated() {
			return lastUpdated;
		}

   /**
    * Set the date of last updated
    * @param Date date: the date of last updated
    */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

   /**
    * Get the serial number
    * @return String	the serial number
    */
	public String getNumber() {
		return number;
	}
	
   /**
    * Set the serial number
    * @param String serialNumber: the serial number
    */
	public void setNumber(String number) {
		this.number = number;
	}

 /**
  * Check if it is tracked.
  * @return boolean	true/false
  */
  public boolean isTracked() {
      return "Y".equals(getExplodeCode());
  }
 
 /**
  * Set as tracked.
  * @param boolean tracked: a boolean value
  */
  public void setTracked(boolean tracked) {
    if (tracked) {
      setExplodeCode("Y");
    } else {
      setExplodeCode("N");
    }
  }

 /**
  * Get the CMCS number(job_no)
  * @return String	the CMCS number
  */
  public String getCmcsNumber() {
      return cmcsNumber;
  }

 /**
  * Set the CMCS number
  * @param String number: the CMCS number
  */
  public void setCmcsNumber(String cmcsNumber) {
    this.cmcsNumber = cmcsNumber;
  }

 /**
  * Get the description of the serial number.
  * @return String	the description
  */
  public String getDescription() {
      return description;
  }

 /**
  * Set the description of the serial number
  * @param String description: the description of the serial number
  */
  public void setDescription(String description) {
    this.description = description;
  }

 /**
  * For Hibernate mapping purpose.
  * Compare all the primary keys between this SerialNumber and the other object.
  * @param Object obj: an object (SerialNumber)
  * @return boolean	true/false
  */
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof SerialNumber)) return false;
    SerialNumber oSN = (SerialNumber) obj;
    return oSN.getNumber().equals(getNumber());
  }

 /**
  * For Hibernate mapping purpose.
  * Generate a hash code based on the primary keys.
  * @return int	a hash code
  */
  public int hashCode() {
    return getNumber().hashCode();
  }

 /**
  * Check if it has been parsed.
  * @return boolean	true/false
  */
  public boolean isParsed() {
    return "Y".equals(parseCode);
  }

 /**
  * Set as parsed. 
  * @param boolean parsed
  */
  public void setParsed(boolean parsed) {
    if (parsed) {
      parseCode = "Y";
    } else {
      parseCode = "N";
    }
  }
 
 /**
  * Get the explode code.
  * @return String	the explode code
  */
  public String getExplodeCode() {
    return explodeCode;
  }
  
 /**
  * Set the explode code.
  * @return String explodeCode: the explode code
  */
  public void setExplodeCode(String explodeCode) {
    this.explodeCode = explodeCode;
  }
 
 /**
  * Get the code for parsing.
  * @return String	the code for parsing
  */ 
  String getParseCode() {
    return parseCode;
  }
 
 /**
  * Set the code for parsing.
  * @param String parseCode: the code for parsing
  */ 
  void setParseCode(String parseCode) {
    this.parseCode = parseCode;
  }

 /**
  * Check if it has been marked for deletion.
  * @return boolean true/false
  */
  public boolean isMarkedForDeletion() {
    return "Y".equals(markedForDeletionCode);
  }

 /**
  * Set mark for deletion.
  * @param boolean markedForDeletion
  */
  public void setMarkedForDeletion(boolean markedForDeletion) {
    if (markedForDeletion) {
      markedForDeletionCode = "Y";
    } else {
      markedForDeletionCode = "N";
    }
  }
  
 /**
  * Get the code for marked-for-deletion.
  * @return String	the code for marked-for-deletion
  */
  String getMarkedForDeletionCode() {
    return markedForDeletionCode;
  }
 
 /**
  * Set the code for marked-for-deletion.
  * @param String markedForDeletionCode: the code for marked-for-deletion
  */
  void setMarkedForDeletionCode(String markedForDeletionCode) {
    this.markedForDeletionCode = markedForDeletionCode;
  }

 /**
  * Check if it is valid.
  * @return boolean true/false
  */
  public boolean isValid() {
    return !"Y".equals(invalidCode);
  }

 /**
  * Set as valid.
  * @param boolean valid: a boolean value
  */
  public void setValid(boolean valid) {
    if (valid) {
      invalidCode = "N";
    } else {
      invalidCode = "Y";
    }
    validateState();
  }
 
 /**
  * Get the invalid code.
  * @return String	the invalid code
  */
  String getInvalidCode() {
    return invalidCode;
  }
  
 /**
  * Set the invalid code and validate the state.
  * @param String invalidCode: the invalid code
  */
  void setInvalidCode(String invalidCode) {
    this.invalidCode = invalidCode;
    validateState();
  }

 /**
  * Validate the state of the serial number. 
  * If it is not valid, set the tracked and parsed as false.
  */
  void validateState() {
    if (!isValid()) {
      setTracked(false);
      setParsed(false);
    }
  }
  
 /**
  * Get the string of the object.
  * Returns a string including the class name and the serial number.
  * @return String
  */
  public String toString() {
    return "[" + getClass() + " " + getNumber()+ "]";
  }

 /**
  * Get the last update date if the date is not null.
  * @return String	reformatted the last update date
  */
  public String getSpecLastUpdatedFormatted() {
  	if (lastUpdated != null) {
  		return DATE_FORMAT.format(lastUpdated);
  	} else {
  		return "";
  	}
  }
  
}
