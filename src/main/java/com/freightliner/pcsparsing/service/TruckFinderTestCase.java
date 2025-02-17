package com.freightliner.pcsparsing.service;
import net.sf.hibernate.*;

import com.freightliner.pcsparsing.Truck;
import com.freightliner.pcsparsing.impl.*;

public class TruckFinderTestCase extends ModelTestCase {

	public TruckFinderTestCase(String name) {
		super(name);
	}
  
  public void testGetTruck() throws Exception {
    String serialNumber = "M24557";
    Truck truck = new TruckImpl(serialNumber);
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(truck);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    Truck truckFromDB = TruckFinder.find(serialNumber);
    assertNotNull("TruckFinder.getTruck should return non-null Truck for serial number '" + serialNumber +"'", truckFromDB);
    assertEquals("Truck serial number", serialNumber, truckFromDB.getSerialNumber());
  }

}
