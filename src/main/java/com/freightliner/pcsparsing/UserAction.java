package com.freightliner.pcsparsing;

import java.io.Serializable;
import java.util.Date;


/**
 * This interface provides acsess to UserAction.
 *
 * @author   jftl8v
 * 
 */
public interface UserAction extends Serializable {

 /**
  * Get the serial number.
  * @return String	the serial number
  */
  public String getSerialNumber();
  
 /**
  * Set the serial number.
  * @param String serialNumber: the serial number
  */
  public void setSerialNumber(String serialNumber);

 /**
  * Get the actual user.
  * @return String the actual user.
  */
  public String getUser();
  
 /**
  * Set the actual user.
  * @param String user: the actual user
  */
  public void setUser(String user);

 /**
  * Get the actual user activity.
  * @return UserActivity	the user action
  */
  public UserActivity getUserActivity();
  
 /**
  * Set the user activity.
  * @param UserActivity userActivity: the user action
  */
  public void setUserActivity(UserActivity userActivity);
  
 /**
  * Get the time stamp of the user action.
  * @return Date	the time stamp
  */
  public Date getTimestamp();
 
 /**
  * Set the time stamp of the user action.
  * @param Date timestamp: the time stamp
  */
  public void setTimestamp(Date timestamp);
}
