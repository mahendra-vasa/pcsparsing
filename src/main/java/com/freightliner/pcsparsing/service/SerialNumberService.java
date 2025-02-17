package com.freightliner.pcsparsing.service;

import java.util.*;

import net.sf.hibernate.*;

import org.apache.commons.logging.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.*;

/**
 * The class provides the services for the serial number object, 
 * which includes adding, deleting and changing the data fields
 * 
 * @author jftl8v
 * 
 */
public class SerialNumberService {

  public static final String VALID = "Valid";
  public static final String DUPLICATE = "Duplicate of existing serial number";
  public static final String WRONG_LENGTH = "Serial number must have exactly six letters or numbers";
  public static final String MARKED_FOR_DELETION = "Serial number already exists and is marked for deletion";

  private static final Log log = LogFactory.getLog(SerialNumberService.class);
	
 /**
  * Toggle the serial number object field: isTracked.
  * @param String number: the serial number
  * 
  * @throws PCSParsingException 
  */
  public static void toggleIsTracked(String number) throws PCSParsingException {

    SerialNumber serialNumber = SerialNumberFinder.find(number);

    Session session = null;
    Transaction tx = null;
    try {
      session = SerialNumberFinder.getSession();
      tx = session.beginTransaction();
      session.refresh(serialNumber);
      boolean isTracked = serialNumber.isTracked();
      if (log.isDebugEnabled()) log.debug(number + "  isTracked: " + serialNumber.isTracked());
      serialNumber.setTracked(!isTracked);
      if (log.isDebugEnabled()) log.debug(number + "  isTracked toggled: " + serialNumber.isTracked());
      serialNumber.setLastUpdated(new Date());
      
      session.update(serialNumber);
      tx.commit();
    } catch (Exception e) {
      Finder.attemptRollback(tx, session);
      throw new PCSParsingException("Could not toggle 'isTracked' for "
          + number, e);
    } finally {
      SerialNumberFinder.close(session);
    }
  }

 /**
  * Add a serial number object.
  * @param String serialNumber: the serial number
  * @param String description: the description
  * @param String cmcsNumber: the CMCS number
  * 
  * @throws PCSParsingException
  */
  public static void add(String serialNumber, String description, String cmcsNumber) throws PCSParsingException {
    Session session = null;
    Transaction tx = null;
    try {
      session = SerialNumberFinder.getSession();
      tx = session.beginTransaction();
      SerialNumberImpl newSerialNumber = new SerialNumberImpl(serialNumber, description, cmcsNumber);
      newSerialNumber.setLastUpdated(new Date());
      session.save(newSerialNumber);
      tx.commit();
    } catch (Exception e) {
      Finder.attemptRollback(tx, session);
      throw new PCSParsingException("Could not add new serial number: " + serialNumber, e);
    } finally {
      SerialNumberFinder.close(session);
    }
  }

  /**
   * Mark serial number for deletion and delete module orders. Mainframe batch job
   * responsible for full DB delete.
   * @param String number: the serial number
   * 
   * @throws PCSParsingException
   * 
   */
  public static void delete(String number) throws PCSParsingException {

    SerialNumber serialNumber = SerialNumberFinder.find(number);

    Session session = null;
    Transaction tx = null;
    try {
      session = SerialNumberFinder.getSession();
      tx = session.beginTransaction();
      session.refresh(serialNumber);
      serialNumber.setMarkedForDeletion(true);
      serialNumber.setParsed(false);
      serialNumber.setTracked(false);
      serialNumber.setLastUpdated(new Date());
      deleteOrders(session, number);
      session.update(serialNumber);
      tx.commit();
    } catch (Exception e) {
      Finder.attemptRollback(tx, session);
      throw new PCSParsingException("Could not delete "
          + number, e);
    } finally {
      SerialNumberFinder.close(session);
    }
  }

 /**
  * Delete the module orders associated with the serial number.
  * @param Session session: the Hibernate session
  * @param String number: the serial number
  * 
  * @throws PCSParsingException
  */
  private static void deleteOrders(Session session, String number) throws PCSParsingException, HibernateException {
    List modules = ModuleFinder.getModulesOrderedByNumber(number);
    for (Iterator iter = modules.iterator(); iter.hasNext();) {
      ModuleImpl module = (ModuleImpl) iter.next();
      if (module.getOrder() != null) {
        session.refresh(module);
        session.delete(module.getOrder());
        module.setOrdered(false);
        session.update(module);
      }
    }
  }

 /**
  * Toggle the serial number data field: isParsed.
  * @param String serialNumber: the serial number
  * 
  * @throws PCSParsingException
  */
  public static void toggleIsParsed(String serialNumber) throws PCSParsingException {

    SerialNumber serialNumberImpl = SerialNumberFinder.find(serialNumber);

    Session session = null;
    Transaction tx = null;
    try {
      session = SerialNumberFinder.getSession();
      tx = session.beginTransaction();
      session.refresh(serialNumberImpl);
      boolean isParsed = serialNumberImpl.isParsed();
      if (log.isDebugEnabled()) log.debug(serialNumber + "  isParsed: " + serialNumberImpl.isParsed());
      serialNumberImpl.setParsed(!isParsed);
      if (log.isDebugEnabled()) log.debug(serialNumber + "  isParsed toggled: " + serialNumberImpl.isParsed());
      serialNumberImpl.setLastUpdated(new Date());
      session.update(serialNumberImpl);
      tx.commit();
    } catch (Exception e) {
      Finder.attemptRollback(tx, session);
      throw new PCSParsingException("Could not toggle 'isParsed' for "
          + serialNumber, e);
    } finally {
      SerialNumberFinder.close(session);
    }
  }

 /**
  * Validate the serial number.
  * @param String serialNumber: the serial number
  * @return boolean	true/false
  * 
  * @throws PCSParsingException
  * 
  */
  public static boolean isValidToAdd(String serialNumber) throws PCSParsingException {
    if (serialNumber == null || serialNumber.length() != 6) {
      return false;
    }
    SerialNumber serialNumberImpl = SerialNumberFinder.find(serialNumber);
    if (serialNumberImpl != null) {
      return false;
    }
    return true;
  }

 /**
  * Validat the serial number and return an explanation.
  * @param String serialNumber: the serial number
  * @return String	an explanation of validation
  * 
  * @throws PCSParsingException
  */
  public static String getReasonInvalidToAdd(String serialNumber) throws PCSParsingException {
    if (serialNumber == null || serialNumber.length() != 6) {
      return WRONG_LENGTH;
    }
    SerialNumber serialNumberImpl = SerialNumberFinder.find(serialNumber);
    if (serialNumberImpl != null && !serialNumberImpl.isMarkedForDeletion()) {
      return DUPLICATE;
    }
    if (serialNumberImpl != null && serialNumberImpl.isMarkedForDeletion()) {
      return MARKED_FOR_DELETION;
    }
    return VALID;
  }

}
