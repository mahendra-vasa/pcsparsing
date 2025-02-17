package com.freightliner.pcsparsing.impl;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.SerialNumber;
import com.freightliner.pcsparsing.service.*;


public class SerialNumberTestCase extends ModelTestCase {

  public SerialNumberTestCase(String name) {
    super(name);
  }

  public void testIsInvalid() throws Exception {
	  String goodNumber = "E00001";
	  String badNumber = "BADONE";
    SerialNumber serialNumber = new SerialNumberImpl(goodNumber, "", "");
    serialNumber.setValid(true);
    SerialNumber serialNumberBad = new SerialNumberImpl(badNumber, "", "");
    serialNumberBad.setValid(false);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(serialNumber);
      session.save(serialNumberBad);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    SerialNumber serialNumberFromDB = SerialNumberFinder.find(goodNumber);
    assertEquals("SerialNumber.isValid()", true, serialNumberFromDB.isValid());
    
    serialNumberFromDB = SerialNumberFinder.find(badNumber);
    assertEquals("SerialNumber.isValid()", false, serialNumberFromDB.isValid());
    
  }
  
}
