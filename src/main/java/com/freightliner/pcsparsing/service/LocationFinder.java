package com.freightliner.pcsparsing.service;

import java.util.Collection;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.freightliner.pcsparsing.Location;
import com.freightliner.pcsparsing.PCSParsingException;
import com.freightliner.pcsparsing.SerialNumber;

/**
 * The class provides the finder service for Location by forming the HQL queries.
 * 
 * @author jftl8v
 * 
 */
public class LocationFinder extends Finder {

 /**
  * Fin the loction according to the item number of location code.
  * 
  * @param String itemNumber: the item number
  * @param String locationCode: the location code
  * @return Location	the location object
  * 
  * @throws PCSParsingException	if there is a HibernateException
  * 
  */
  public static Location find(String itemNumber, String locationCode) throws PCSParsingException {
    Location location = null;
    Session session = null;
    try {
        session = getSession();
        Query query = session
                .createQuery("from LocationImpl as location " +
                		"where location.item.number = :itemNumber and " +
                		"loc = :locationCode");
        query.setString("itemNumber", itemNumber);
        query.setString("locationCode", locationCode);
        location = (Location) query.uniqueResult();
    } catch (HibernateException e) {
        throw new PCSParsingException(e);
    } finally {
        close(session);
    }

    return location;
  }

 /**
  * Find all the locations whose associated items are ordered.
  * 
  * @param SerialNumber serialNumber: the serial number
  * @return Collection	the collection of the locations
  * 
  * @throws PCSParsingException	if there is a HibernateException
  *  
  */
  public static Collection findOrdered(SerialNumber serialNumber) throws PCSParsingException {
    Query query = null;
    Session session = null;
    List resultList = null;
    try {
    	System.out.println("###### INSIDE findOrdered() #####");
      session = getSession();
      System.out.println("###### INSIDE findOrdered():: Session Object #####"+session);
      String queryString = "from LocationImpl location " +
      		" left join fetch location.item " + 
          " where location.item.bom.compID.serialNumber = :serialNumber " +
          " and location.item.bom.compID.tsoSequenceNumber = :maxTSOSequenceNumber " +
          " and exists(from ModuleImpl m join location.item.bom.module.order)";
      String maxTsoNum = getMaxTSOSequenceNumber(serialNumber.getNumber());
      System.out.println("###### INSIDE findOrdered():: 2 ###maxTsoNum##="+maxTsoNum);      
   
      query = session.createQuery(queryString);
      System.out.println("###### INSIDE findOrdered():: query Object #####"+query);
      query.setString("serialNumber", serialNumber.getNumber());
      System.out.println("###### INSIDE findOrdered():: 1 #####");
      query.setString("maxTSOSequenceNumber", getMaxTSOSequenceNumber(serialNumber.getNumber()));
      
      resultList = query.list();     
      System.out.println("###### INSIDE findOrdered():: resultList size #####"+resultList.size());
      return resultList;
     
    } catch (HibernateException e) {
    	System.out.println("###### INSIDE findOrdered():: HibernateException #####"+e+"  MSG:="+e.getMessage());
        throw new PCSParsingException("Could not get ordered locations for truck: "
                + serialNumber.getNumber(), e);
    } finally {
    	System.out.println("###### INSIDE findOrdered():: finally : closing the connection #####");
        close(session);
        System.out.println("###### INSIDE findOrdered():: finally : After closing the connection #####");
    }
  }

}
