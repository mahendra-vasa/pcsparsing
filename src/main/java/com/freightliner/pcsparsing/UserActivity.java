package com.freightliner.pcsparsing;

import java.util.*;

/**
 * This class defines all types of UserActivity.
 *
 * @author   jftl8v
 * 
 */
public class UserActivity {

 /**
  * Map for storing all the user activities.
  */
  private static final Map map = new HashMap();

 /**
  * The user activity for DELETE
  */
  public static final UserActivity DELETE = new UserActivity("D");
 
 /**
  * The user activity for ORDER
  */
  public static final UserActivity ORDER = new UserActivity("O");
  
 /**
  * The user activity for WARN
  */
  public static final UserActivity WARN = new UserActivity("W");

 /**
  * The activity code
  */
  private final String activityCode;

 /**
  * Get the user actitity by the user activity code.
  * @param String userActivityCode: the user activity code
  * @return UserActivity	the user activity
  */
  public static UserActivity get(String userActivityCode) {
    return (UserActivity) map.get(userActivityCode);
  }
  
 /**
  * Constructor
  * @param String activityCode: the activity code
  */
  private UserActivity(String activityCode) {
    this.activityCode = activityCode;
    map.put(activityCode, this);
  }

 /**
  * Check if two objects (user activities) are equal.
  * @param Object o: an object (UserActivity)
  * @return boolean	true/false
  */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserActivity)) return false;
    if (o == null) return false;

    final UserActivity oUserAction = (UserActivity) o;
    if (activityCode != null ? !activityCode.equals(oUserAction.getActivityCode()) : oUserAction.getActivityCode() != null) return false;

    return true;
  }

 /**
  * Get the activity code.
  * @return String the activity code
  */
  public String getActivityCode() {
    return activityCode;
  }

 /**
  * Hashing the activity code.
  * @return int	a hash code
  */
  public int hashCode() {
		return 31 * activityCode.hashCode();
  }
  
 /**
  * Get the string of the object.
  * Return a string including the class name and activity code.
  * @return String
  */
  public String toString() {
    return "[" + getClass() + " " + activityCode + "]";
  }


}
