package com.freightliner.pcsparsing;

import java.util.*;

/**
 * This class defines the customer-work-order search criteria and stores them in a map.
 *
 * @author   jftl8v
 * 
 */
public class HasCWOSearchCriteria extends SearchCriteria {

   /**
    * A map for storing all the department search criteria
   	*/
  	protected static final Map map = new HashMap();
  	
   /**
    * The search criteria for all, including CWO and not CWO.
    */
  	public static final HasCWOSearchCriteria ALL = new HasCWOSearchCriteria("ALL", "All");
  	
   /**
    * The search criteria for only CWO.
    */
  	public static final HasCWOSearchCriteria ONLY = new HasCWOSearchCriteria("ONLY", "Yes", Boolean.TRUE);
  	
   /**
    * The search criteria for not CWO.
    */
  	public static final HasCWOSearchCriteria NOT = new HasCWOSearchCriteria("NOT", "No", Boolean.FALSE);

   /**
    * Constructor. Inherited from SearchCriteria class.
    * @param String key: a search key
    * @param String displayName: a display name for the CWO search criteria
    */
  	protected HasCWOSearchCriteria(String key, String displayName) {
    	super(key, displayName);
  	}

   /**
    * Constructor. Inherited from SearchCriteria class.
    * @param String key: a search key
    * @param String displayName: a display name for the CWO search criteria
    * @param Boolean booleanValue: a boolean value
    */
  	protected HasCWOSearchCriteria(String key, String displayName, Boolean booleanValue) {
    	super(key, displayName, booleanValue);
   	}

   /**
    * Get the storage map.
    * @return Map	the map storing all the hasCWO search criterias
    */
  	protected Map getMap() {
    	return map;
  	}

   /**
    * Get the CWO search criteria from the map.
    * @param String key: a search key
    * @return HasCWOSearchCriteria	a hasCWO search criteria
    */
  	public static HasCWOSearchCriteria get(String key) {
    	return (HasCWOSearchCriteria) map.get(key);
  	}

}
