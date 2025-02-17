package com.freightliner.pcsparsing;

import java.util.*;

/**
 * This class defines the department search criteria and stores them in a map.
 *
 * @author   jftl8v
 * 
 */
public class DepartmentSearchCriteria extends SearchCriteria {

   /** 
    * A map for storing all the department search criteria
    */
  	protected static final Map map = new HashMap();
  	
   /** 
    * The criteria for searching all the departments.  
    */
  	public static final DepartmentSearchCriteria ALL = new DepartmentSearchCriteria("ALL", "All");
   
   /**
    * The criteria for searching the body departments.
    */
  	public static final DepartmentSearchCriteria BODY = new DepartmentSearchCriteria(Department.BODY.getAbbreviation(), "Body", Boolean.TRUE);
  	
   /**
    * The criteria for searching the chassis departments.
    */
  	public static final DepartmentSearchCriteria CHASSIS = new DepartmentSearchCriteria(Department.CHASSIS.getAbbreviation(), "Chassis", Boolean.TRUE);
  	
   /**
    * The criteria for searching the electrical departments.
    */
  	public static final DepartmentSearchCriteria ELECTRICAL = new DepartmentSearchCriteria(Department.ELECTRICAL.getAbbreviation(), "Electrical", Boolean.TRUE);

   /**
    * Constructor. Inherited from SearchCriteria class.
    * @param String key: a search key
    * @param String displayName: a display name of the search criteria
    */
  	protected DepartmentSearchCriteria(String key, String displayName) {
    	super(key, displayName);
  	}

   /**
    * Constructor. Inherited from SearchCriteria class.
    * @param String key: a search key
    * @param String displayName: a display name of the search criteria
    * @param Boolean booleanValue: true/false		
    */
  	protected DepartmentSearchCriteria(String key, String displayName, Boolean booleanValue) {
    	super(key, displayName, booleanValue);
  	}
	
   /**
    * Get the storage map.
    * @return Map	the map storing all the department search criterias
    */
  	protected Map getMap() {
    	return map;
  	}

   /**
    * Get the department search criteria from the map.
    * @param String key: a search key
    * @return  DepartmentSearchCriteria	a department search criteria
    */
  	public static DepartmentSearchCriteria get(String key) {
    	return (DepartmentSearchCriteria) map.get(key);
  	}

}
