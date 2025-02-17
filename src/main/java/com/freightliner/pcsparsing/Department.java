package com.freightliner.pcsparsing;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.logging.*;

/**
 * This class stores all types of Department in a map 
 * and provides access methods to it.
 *
 * @author   jftl8v
 * 
 */
public class Department implements Comparable, Serializable {
  	
  	/**
  	 *  A map for storing all departments
  	 */
  	public static final Map map = new HashMap();

    
   /**
    * Body department
    */
  	public static final Department BODY = new Department("Body", "N");
  	
   /**
    * Chassis department
    */
  	public static final Department CHASSIS = new Department("Chassis", "R");
   
   /**
    * Electrical department
    */
  	public static final Department ELECTRICAL = new Department("Electrical", "V");
  	
  	private static final Log log = LogFactory.getLog(Department.class);
  
  	/**
  	 * Full name of the department
  	 */
  	private final String name;
  	
   /**
    * Abbreviation of the department
    */
  	private final String abbreviation;

   /**
    * Get department according its abbreviation.
    * 
    * @param String abbreviation:	the department abbreviation
    * @return Department	the department
    * 
    */
  	public static Department get(String abbreviation) {
    	Department department = (Department) map.get(abbreviation);
    	if (department == null) {
        	log.warn("No Department found for " + abbreviation);
        	department = new Department(abbreviation, abbreviation);
        	map.put(abbreviation, department);
    	}
    	return department;
  	}  

   /**
    * Constructor.  Construct a department and stores it in the map.
    * @param String name: the full name of the department
    * @param String abbreviation: the abbreviation of the department
    */
  	private Department(String name, String abbreviation) {
    	this.name = name;
    	this.abbreviation = abbreviation;
    	map.put(abbreviation, this);
  	}

   /**
    * Implements the methods inherited from Comparable interface.
    * @param Object o: an object
    * @return int	less than (negative), equal (0), more than (positive)
    */
  	public int compareTo(Object o) {
    	if (this == o) return 0;
    	final Department department = (Department) o;
    	return hashCode() - department.hashCode();
  	}
	
   /**
    * For Hibernate mapping purpose.
    * Compare the composit primary keys between this department and the other object.
    * @param Object o: an object (Department)
    * @return boolean	true/false
    */
  	public boolean equals(Object o) {
    	if (this == o) return true;
    	if (!(o instanceof Department)) return false;

    	final Department department = (Department) o;

    	if (!abbreviation.equals(department.abbreviation)) return false;
    	if (abbreviation != null ? !abbreviation.equals(department.abbreviation) : department.abbreviation != null) return false;

    	return true;
  	}

   /**
    * Get the abbreviation.
    * @return String	the abbreviation representing the department 
    */
  	public String getAbbreviation() {
    	return abbreviation;
  	}

   /**
    * Get the full name.
    * @return String the full name of the department
    */
  	public String getName() {
    	return name;
  	}
   
   /**
  	* For Hibernate mapping purpose.
  	* Generate a hash code based on the primary keys.
  	* @return int	a hash code
  	*/
  	public int hashCode() {
    	if (abbreviation == null) {
        	return -1;
    	}
    	return abbreviation.hashCode();
  	}

   /**
    * Get a string of the object.
    * @return String the full name of the department
    */
  	public String toString() {
    	return name;
  	}

}
