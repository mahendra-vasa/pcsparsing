package com.freightliner.pcsparsing.service;

import java.util.List;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.SerialNumber;
import com.freightliner.pcsparsing.impl.*;

public class SerialNumberFinderTestCase extends ModelTestCase {

	public SerialNumberFinderTestCase(String name) {
		super(name);
	}
	
  public void testFindActive() throws Exception {
    // TODO: Actually return only trackable serialNumbers
    SerialNumber serialNumberA = new SerialNumberImpl("AX9900", "", "");
    serialNumberA.setTracked(true);
    serialNumberA.setMarkedForDeletion(false);

    SerialNumber serialNumberB = new SerialNumberImpl("AA9900", "", "");
    serialNumberB.setParsed(true);
    serialNumberB.setTracked(true);
    serialNumberB.setValid(true);

    SerialNumber serialNumberC = new SerialNumberImpl("CX8800", "", "");
    serialNumberC.setTracked(true);
    serialNumberC.setParsed(true);
    serialNumberC.setMarkedForDeletion(true);
    
    SerialNumber serialNumberD = new SerialNumberImpl("DD8800", "", "");
    serialNumberD.setTracked(true);
    serialNumberD.setMarkedForDeletion(false);
    serialNumberD.setValid(false);
    
    SerialNumber serialNumberE = new SerialNumberImpl("AEEEEE", "", "");
    serialNumberE.setTracked(false);
    serialNumberE.setMarkedForDeletion(false);
    serialNumberE.setValid(true);
    
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(serialNumberA);
      session.save(serialNumberB);
      session.save(serialNumberC);
      session.save(serialNumberD);
      session.save(serialNumberE);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    List serialNumbers = SerialNumberFinder.findActive();
    assertNotNull("SerialNumberDelegate should return List of serialNumbers", serialNumbers);
    assertEquals("SerialNumberDelegate.getTrackableSerialNumbers should return at trackable serial numbers", 2, serialNumbers.size());
    assertEquals("First serial number", serialNumberB, (SerialNumber) serialNumbers.get(0));
    assertEquals("Second serial number", serialNumberA, (SerialNumber) serialNumbers.get(1));
  }
	
  public void testFind() throws Exception {
    // TODO: Actually return only trackable serialNumbers
    SerialNumber serialNumberA = new SerialNumberImpl("AX9900", "", "");
    SerialNumber serialNumberB = new SerialNumberImpl("AA9900", "", "");
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(serialNumberA);
      session.save(serialNumberB);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    List serialNumbers = SerialNumberFinder.find();
    assertNotNull("SerialNumberDelegate should return List of serialNumbers", serialNumbers);
    assertTrue("SerialNumberDelegate.getTrackableSerialNumbers should return at least one trackable serialNumberA",
      serialNumbers.size() > 0);
    for (int i = 0; i < serialNumbers.size(); i++) {
        if (i > 0) {
          SerialNumber serialNumber1 = (SerialNumber) serialNumbers.get(i - 1);
          SerialNumber serialNumber2 = (SerialNumber) serialNumbers.get(i);
          assertTrue("SerialNumber List should be sorted in serial number order ascending. " + serialNumber1.getNumber() +
              " should precede " + serialNumber2.getNumber(),
              serialNumber1.getNumber().compareTo(serialNumber2.getNumber()) <= 0);
        }
    }
  }

}
