package com.freightliner.pcsparsing;

import java.util.*;

/**
 * This class defines the ordering search criteria and stores them in a map.
 *
 * @author   jftl8v
 * 
 */
public final class IsOrderedSearchCriteria extends SearchCriteria {

   /** 
    * A map for storing all the department search criteria
   	*/
  	protected static final Map map = new HashMap();
  	
   /**
    * The criteria for searching all, including ordered and un-ordered.
    */
  	public static final IsOrderedSearchCriteria ALL = new IsOrderedSearchCriteria("ALL", "All");
  	
   /**
    * The criteria for searching ordered modules.
    */
  	public static final IsOrderedSearchCriteria ONLY = new IsOrderedSearchCriteria("ONLY", "Yes", Boolean.TRUE);
  	
   /**
    * The criteria for searching un-ordered the modules.
    */
  	public static final IsOrderedSearchCriteria NOT = new IsOrderedSearchCriteria("NOT", "No", Boolean.FALSE);

   /**
    * Constructor. Inherited from SearchCriteria class.
    * @param String key: a search key
    * @param String displayName: a display name for the order search criteria
    */
  	protected IsOrderedSearchCriteria(String key, String displayName) {
    	super(key, displayName, null);
  	}

   /**
    * Constructor. Inherited from SearchCriteria class.
    * @param String key: a search key
    * @param String displayName: a display name for the order search criteria
    * @param Boolean booleanValue: a boolean value
    */
	protected IsOrderedSearchCriteria(String key, String displayName, Boolean booleanValue) {
    	super(key, displayName, booleanValue);
  	}

   /**
    * Get the storage map.
    * @return Map	the map storing all the IsOrder search criterias
    */
  	protected Map getMap() {
    	return map;
  	}

   /**
    * Get the CWO search criteria from the map.
    * @param String key: a search key
    * @return IsOrderedSearchCriteria	an order search criteria
    */
  	public static IsOrderedSearchCriteria get(String key) {
    	return (IsOrderedSearchCriteria) map.get(key);
  	}

}
