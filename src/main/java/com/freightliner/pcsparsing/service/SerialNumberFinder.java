package com.freightliner.pcsparsing.service;

import java.util.List;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.*;

/**
 * The class provide the finder service for the serial number object.
 * 
 * @author jftl8v
 * 
 */
public class SerialNumberFinder extends Finder {

   /**
    * Find the active serial number objects.
    * @return List	a list of active serial numbers
    * 
    * @throws PCSParsingException	if thers is Hibernate Exception
    */
	public static List findActive() throws PCSParsingException {
    List serialNumbers = null;
    Session session = null;
    try {
      session = getSession();
      serialNumbers = session.find(
          "from SerialNumberImpl sn where sn.explodeCode = 'Y' and sn.markedForDeletionCode <> 'Y' " + 
          " and sn.invalidCode <> 'Y' order by sn.number asc");
    } catch (HibernateException e) {
      throw new PCSParsingException(e);
    } finally {
      close(session);
    }
	    
    return serialNumbers;
  }

   /**
    * Find all the serial number objects that are not markd to delete.
    * @return List	a list of serial numbers
    * 
    * @throws PCSParsingException
    */
    public static List find() throws PCSParsingException {
        List serialNumbers = null;
        Session session = null;
        try {
          session = getSession();
          serialNumbers = session.find("from SerialNumberImpl sn where sn.markedForDeletionCode <> 'Y' order by sn.number asc");
        } catch (HibernateException e) {
          throw new PCSParsingException(e);
        } finally {
          close(session);
        }

	    return serialNumbers;
    }

   /**
    * Find the serial number object for the specific serial number.
    * @param String number: the serial number
    * @return SerialNumber	the serial number object
    * 
    * @throws PCSParsingException
    * 
    */
    public static SerialNumber find(String number) throws PCSParsingException {
      SerialNumber serialNumber = null;
      Session session = null;
      try {
        session = getSession();
        Query query = session.createQuery("from SerialNumberImpl sn where sn.number = :number");
        query.setString("number", number);
        serialNumber = (SerialNumber) query.uniqueResult();
      } catch (HibernateException e) {
        throw new PCSParsingException(e);
      } finally {
        close(session);
      }

      return serialNumber;
    }

   /**
    * Find all the serial number objects that can be parsed.
    * @return List	a list of serial number objects
    * 
    * @throws PCSParsingException
    * 
    */
    public static List findParseable() throws PCSParsingException {
      List serialNumbers = null;
      Session session = null;
      try {
        session = getSession();
        serialNumbers = session.find(
            "from SerialNumberImpl sn " + 
            " where sn.explodeCode = 'Y' and sn.markedForDeletionCode <> 'Y' and sn.invalidCode <> 'Y'" + 
            " order by sn.number asc");
      } catch (HibernateException e) {
        throw new PCSParsingException(e);
      } finally {
        close(session);
      }
  	    
      return serialNumbers;
    }

   /**
    * Find the serial number objects that have been parsed.
    * @return List	a list of serial number objects
    * 
    * @throws PCSParsingException
    */
    public static List findParsed() throws PCSParsingException {
      List serialNumbers = null;
      Session session = null;
      try {
        session = getSession();
        serialNumbers = session.find(
            "from SerialNumberImpl sn " + 
            " where sn.markedForDeletionCode <> 'Y' " +
            "		and sn.explodeCode = 'Y' " + 
            "   and sn.invalidCode <> 'Y' " + 
            "   and sn.parseCode = 'Y' ");
      } catch (HibernateException e) {
        throw new PCSParsingException(e);
      } finally {
        close(session);
      }
  	    
      return serialNumbers;
    }
}
