package com.freightliner.pcsparsing.service;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.freightliner.pcsparsing.PCSParsingException;
import com.freightliner.pcsparsing.Truck;

/**
 * The class provide the finder service for truck.
 * 
 * @author jftl8v
 * 
 */
public class TruckFinder extends Finder {
	
	private static final Log log = LogFactory.getLog(TruckFinder.class);

 /**
  * Get the active trucks.
  * @return List	a list of active trucks
  * 
  * @throws PCSParsingException
  */
  public static List getActiveTrucks() throws PCSParsingException {
    List trucks = null;
    Session session = null;
    try {
      session = getSession();
      trucks = session.find("from TruckImpl truck order by truck.serialNumber asc");
    } catch (HibernateException e) {
      throw new PCSParsingException(e);
    } finally {
      close(session);
    }
    
    return trucks;
  }

 /**
  * Find a truck based on the serial number.
  * @param String serialNumber: the serial number
  * @return Truck	the truck object
  * 
  * @throws PCSParsingException
  */
  public static Truck find(String serialNumber) throws PCSParsingException {
    Truck truck = null;
    Session session = null;
    try {
      String maxTSOSequenceNumber = getMaxTSOSequenceNumber(serialNumber);
      session = getSession();
      Query query = session.createQuery(
          "select truck from TruckImpl as truck " +
          "where truck.serialNumber = :serialNumber " +
          "and truck.tsoSequenceNumber = :maxTSOSequenceNumber");
      query.setString("serialNumber", serialNumber);
      query.setString("maxTSOSequenceNumber", maxTSOSequenceNumber);
      if (log.isDebugEnabled()) {
		log.debug("Find truck " + serialNumber + " " + maxTSOSequenceNumber);
      }
      truck = (Truck) query.uniqueResult();
      if (log.isDebugEnabled()) {
		log.debug("Found: " + truck);
      }
    } catch (HibernateException e) {
      throw new PCSParsingException(e);
    } finally {
      close(session);
    }

    return truck;
  }

}
