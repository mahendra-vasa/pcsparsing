package com.freightliner.pcsparsing.impl;

import java.util.Date;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.UserActivity;

/**
 * This class implements the User Action object associated User Action table. 
 * It records the user actions performed on the specific serial number.
 *
 * @author   jftl8v
 *
 */
public class UserActionImpl implements UserAction {

  private String serialNumber;
  private Date timestamp;
  private String user;
  private String userActivityCode;

 /**
  * Default constructor.
  */
  public UserActionImpl() { }
  
 /**
  * Constructor.
  * @param String serialNumber: the serial number the user acts on
  * @param String user: the actual user
  * @param UserActivity userActivity:	the action
  */
  public UserActionImpl(String serialNumber, String user, UserActivity userActivity) {
    this.serialNumber = serialNumber;
    this.user = user;
    setUserActivity(userActivity);
    this.timestamp = new Date();
  }

 /**
  * Get the serial number.
  * @return String	the serial number
  */
  public String getSerialNumber() {
    return serialNumber;
  }
  
 /**
  * Set the serial number.
  * @param String serialNumber: the serial number
  */
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }
  
 /**
  * Get the actual user.
  * @return String the actual user.
  */
  public String getUser() {
    return user;
  }
  
 /**
  * Set the actual user.
  * @param String user: the actual user
  */
  public void setUser(String user) {
    this.user = user;
  }
  
 /**
  * Get the actual user activity.
  * @return UserActivity	the user action
  */
  public UserActivity getUserActivity() {
    return UserActivity.get(userActivityCode);
  }
  
 /**
  * Set the user activity.
  * @param UserActivity userActivity: the user action
  */
  public void setUserActivity(UserActivity userActivity) {
    userActivityCode = userActivity.getActivityCode();
  }
  
 /**
  * Get the code for the user's activity.
  * @return String	the code for the user's activity
  */
  String getUserActivityCode() {
    return userActivityCode;
  }
  
 /**
  * Set the code for the user's activity.
  * @param String userActivityCode: the code for the user's activity
  */
  void setUserActivityCode(String userActivityCode) {
    this.userActivityCode = userActivityCode;
  }

 /**
  * For Hibernate use only.
  * Compare all the primary keys of two user activities.
  * @param Object o:	an object (UserActivity)
  * @return boolean	true/false
  */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserAction)) return false;
    if (o == null) return false;

    final UserAction oUserAction = (UserAction) o;
    if (serialNumber != null ? !serialNumber.equals(oUserAction.getSerialNumber()) : oUserAction.getSerialNumber() != null) return false;
    if (getUser() != null ? !getUser().equals(oUserAction.getUser()) : oUserAction.getUser() != null) return false;
    if (getUserActivity() != null ? !getUserActivity().equals(oUserAction.getUserActivity()) : oUserAction.getUserActivity() != null) return false;

    return true;
  }

 /**
  * For Hibernate mapping purpose.
  * Uses all the primary keys to generate a hashcode.
  * @return int	a hashcode
  */ 
  public int hashCode() {
		int result = 23;
		if (serialNumber != null) {
			result = result + serialNumber.hashCode();
		}
		if (user != null) {
			result = result + user.hashCode();
		}
		if (getUserActivity() != null) {
			result = result + getUserActivity().hashCode();
		}
		return result;
  }
  
 /**
  * Get the time stamp of the user action.
  * @return Date	the time stamp
  */
  public Date getTimestamp() {
    return timestamp;
  }
 
 /**
  * Set the time stamp of the user action.
  * @param Date timestamp: the time stamp
  */
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
  
  
 /**
  * Get the string of this class. 
  * Return a string including class name, user, serial number, 
  * user activity code and time stamp.
  * @return String
  */
  public String toString() {
    return "[" + getClass() + " " + user + " " + serialNumber + " " 
    		+ userActivityCode + " " + timestamp + "]";
  }
}
