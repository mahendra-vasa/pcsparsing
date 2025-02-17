package com.freightliner.pcsparsing.service;

import java.util.*;

import net.sf.hibernate.*;

import org.apache.commons.logging.*;

import com.freightliner.pcsparsing.*;

/**
 * The class provide the finder service for BOM.
 * 
 * @author jftl8v
 * 
 */
public class BOMFinder extends Finder {

  private static final Log log = LogFactory.getLog(BOMFinder.class);

 /**
  * Find a BOM based on the bom number from the DB.
  * @param String number: the bom number
  * @return BOM	the bom
  * @throws PCSParsingException
  */
  public static BOM find(String number) throws PCSParsingException {
    BOM bom = null;
    Session session = null;
    try {
      session = getSession();
      Query query = session.createQuery(
          "from BOMImpl bom where bom.number = :number");
      query.setString("number", number);
      bom = (BOM) query.uniqueResult();
    } catch (HibernateException e) {
      throw new PCSParsingException(e);
    } finally {
      close(session);
    }

    return bom;
  }

 /**
  * Find a list of boms based on the serial number and the module number from DB.
  * @param String serialNumber: the serial number
  * @param String moduleNumber: the module number
  * @return List	a list of boms
  * @throws PCSParsingException
  */
  public static List find(String serialNumber, String moduleNumber) throws PCSParsingException {
    log.debug("Find BOMS for module " + serialNumber + " " + moduleNumber);
     Session session = null;
    try {
      session = Finder.getSession();
      Query query = session.createQuery(
          "from BOMImpl bom " +
          "left join fetch bom.items as item " +
          "where bom.compID.moduleNumber = :moduleNumber and " +
          "bom.compID.serialNumber = :serialNumber and " +
          "bom.compID.tsoSequenceNumber = :tsoSequenceNumber");
      query.setString("moduleNumber", moduleNumber);
      query.setString("serialNumber", serialNumber);
      query.setString("tsoSequenceNumber", getMaxTSOSequenceNumber(serialNumber));
      if (log.isDebugEnabled()) log.debug("Found BOMs");
      Set results = new HashSet(query.list());
      return new ArrayList(results);
    } catch (HibernateException e) {
      throw new PCSParsingException("Could not find BOMs for module ID " + serialNumber + " " + moduleNumber, e);
    } finally {
      Finder.close(session);
    }
  }
}
