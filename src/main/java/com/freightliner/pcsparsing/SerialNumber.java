package com.freightliner.pcsparsing;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * This interface provides acsess to the data fields of SerialNumber.
 *
 * @author   jftl8v
 * 
 */
public interface SerialNumber extends Serializable {
  
 /**
  * Get the date last updated.
  * @return Date	the last update date
  */
  public Date getLastUpdated();
  
 /**
  * Set the date of last updated
  * @param Date date: the date of last updated
  */
  public void setLastUpdated(Date date);
  
 /**
  * Get the last update date if the date is not null.
  * @return String	reformatted the last update date
  */
  public String getSpecLastUpdatedFormatted();
  
 /**
  * Get the serial number
  * @return String	the serial number
  */
  public String getNumber();
  
 /**
  * Set the serial number
  * @param String serialNumber: the serial number
  */
  public void setNumber(String serialNumber);

 /**
  * Check if it is tracked.
  * @return boolean	true/false
  */
  public boolean isTracked();
  
 /**
  * Set as tracked.
  * @param boolean tracked: a boolean value
  */
  public void setTracked(boolean tracked);

 /**
  * Check if it is valid.
  * @return boolean true/false
  */
  public boolean isValid();
  
 /**
  * Set as valid.
  * @param boolean valid: a boolean value
  */
  public void setValid(boolean valid);

 /**
  * Get the CMCS number(job_no)
  * @return String	the CMCS number
  */
  public String getCmcsNumber();
  
 /**
  * Set the CMCS number
  * @param String number: the CMCS number
  */
  public void setCmcsNumber(String number);

 /**
  * Get the description of the serial number.
  * @return String	the description
  */
  public String getDescription();
  
 /**
  * Set the description of the serial number
  * @param String description: the description of the serial number
  */
  public void setDescription(String description);

 /**
  * Check if it has been parsed.
  * @return boolean	true/false
  */
  public boolean isParsed();
  
 /**
  * Set as parsed.
  * @param boolean parsed
  */
  public void setParsed(boolean parsed);
  
 /**
  * Check if it has been marked for deletion.
  * @return boolean true/false
  */
  public boolean isMarkedForDeletion();
  
 /**
  * Set mark for deletion.
  * @param boolean markedForDeletion
  */
  public void setMarkedForDeletion(boolean markedForDeletion);
  
  /**
   * Get the explode code.
   * @return String	the explode code
   */
   String getExplodeCode() ;
   
  /**
   * Set the explode code.
   * @return String explodeCode: the explode code
   */
   public void setExplodeCode(String explodeCode);
}
