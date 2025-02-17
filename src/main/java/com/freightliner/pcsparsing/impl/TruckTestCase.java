package com.freightliner.pcsparsing.impl;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.Truck;
import com.freightliner.pcsparsing.service.*;

public class TruckTestCase extends ModelTestCase {

  public TruckTestCase(String name) {
    super(name);
  }

  public void testSaveTruck() throws Exception {
    String serialNumber = "AZ9900";
    Truck truck = new TruckImpl(serialNumber);
    Session session = null;
    Transaction transaction = null;
    try {
      session = Finder.getSession();
      transaction = session.beginTransaction();
      session.saveOrUpdate(truck);
      transaction.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(transaction, session);
    	throw e;
    } finally {
      Finder.close(session);
    }


    Session newSession = null;
    try {
      newSession = TruckFinder.getSession();
      Query query = newSession.createQuery("from TruckImpl truck where truck.serialNumber = :serialNumber");
      query.setString("serialNumber", serialNumber);
      Truck truckFromDB = (Truck) query.uniqueResult();
      assertNotNull("New truck " + serialNumber + " should be in database.", truckFromDB);
      assertEquals("New truck " + serialNumber + " should be in database.", truck, truckFromDB);
    } finally {
      TruckFinder.close(newSession);
    }
  }

}
