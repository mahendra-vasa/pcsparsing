package com.freightliner.pcsparsing.service;

import java.util.*;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.*;


public class LocationFinderTestCase extends ModelTestCase {

  public LocationFinderTestCase(String name) {
    super(name);
  }

  public void testFindOrdered() throws Exception {
    String item1No = "222222";
    SerialNumberImpl serialNumberImpl = new SerialNumberImpl("SW1231", "", "");
    Truck truck = new TruckImpl("SW1231");
    Module module1 = new ModuleImpl(truck, "666", "");
    module1.setOrdered(false);
    BOM bom = new BOMImpl(module1, "222-2222", "");
    String locCode1 = "121";
    String locCode2 = "766";
    ComponentItem item = new ComponentItemImpl(bom, item1No, "GENERIC PART", Department.CHASSIS); 
    new LocationImpl(item, locCode1);
    new LocationImpl(item, locCode2);

    Module module2 = new ModuleImpl(truck, "444", "");
    module2.setOrdered(true);
    bom = new BOMImpl(module2, "565-7655", "");
    locCode1 = "100";
    locCode2 = "766";
    String item2No = "AAAAAAAAA";
    item = new ComponentItemImpl(bom, item2No, "GENERIC PART", Department.CHASSIS); 
    Collection orderedLocations = new ArrayList();
    Location loc1 = new LocationImpl(item, locCode1);
    Location loc2 = new LocationImpl(item, locCode2);
    orderedLocations.add(loc1);
    orderedLocations.add(loc2);

    Transaction tx = null;
    Session session = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(serialNumberImpl);
      session.save(truck);
      session.save(module1);
      session.save(module2);
      tx.commit();
    } finally {
      Finder.close(session);
    }
    
    Collection orderedLocationsFromDB = LocationFinder.findOrdered(serialNumberImpl);
    assertNotNull("Ordered locations", orderedLocationsFromDB);
    assertEquals("Ordered locations count", 2, orderedLocationsFromDB.size());
    assertTrue("Ordered locations location 1", orderedLocationsFromDB.contains(loc1));
    assertTrue("Ordered locations location 2", orderedLocationsFromDB.contains(loc2));
  }
}
