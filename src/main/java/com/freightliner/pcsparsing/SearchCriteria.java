package com.freightliner.pcsparsing;

import java.util.Map;


/**
 * This abstract class defines some basic methods in forming the search criteria.
 *
 * @author   jftl8v
 *
 * 
 */
public abstract class SearchCriteria {

 /**
  * The display name of the search criteria.
  */
  protected final String displayName;
  
 /**
  * The search key.
  */
  protected final String key;
  
 /**
  * The boolean value associated with the search criteria.
  */
  protected final Boolean booleanValue;

 /** 
  * Constructor.
  * @param String key: a search key
  * @param String displayName: a display name of the search criteria
  */
  protected SearchCriteria(String key, String displayName) {
    this(key, displayName, null);
  }

 /**
  * Constructor.
  * @param String key: a search key
  * @param String displayName: a display name of the search criteria
  * @param String booleanValue: a boolean value
  */
  protected SearchCriteria(String key, String displayName, Boolean booleanValue) {
    this.key = key;
    this.displayName = displayName;
    this.booleanValue = booleanValue;
    getMap().put(key, this);
    if (booleanValue == null) {
      getMap().put(null, this);
      getMap().put("", this);
    }
  }

 /**
  * Abstract method. Get the storage map.
  * @return Map: the map storing all the search criterias
  */
  protected abstract Map getMap();

 /**
  * Get the booleanValue. If there's associated boolean value throw an exception.
  * @return boolean 	true/false
  * @throws PCSParsingException 	if the boolean value is null
  */
  public boolean booleanValue() throws PCSParsingException {
    if (booleanValue == null) {
      throw new PCSParsingException("SearchCriteria" + displayName + " (" + key + ") has no boolean value");
    }
    return booleanValue.booleanValue();
  }

 /**
  * Get display name of the search criteria.
  * @return String	the display name
  */
  public String getDisplayName() {
    return displayName;
  }

 /**
  * Get the search key
  * @return String	the search key
  */
  public String getKey() {
    return key;
  }

 /** 
  * Get the string of the object.
  * Returns a string including the class name, display name, search key and the boolean value.
  * @return String
  */
  public String toString() {
    return getClass() + "[" + displayName + " " + key + " " + booleanValue + "]";
  }

}
