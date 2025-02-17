package com.freightliner.pcsparsing.impl;

import net.sf.hibernate.*;
import com.freightliner.pcsparsing.Module;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.service.*;


public class LocationTestCase extends ModelTestCase {

  public LocationTestCase(String name) {
    super(name);
  }

  public void testCreate() throws Exception {
    String item1No = "11111";
    Truck truck = new TruckImpl("SW1231");
    Module module = new ModuleImpl(truck, "666", "");
    BOM bom = new BOMImpl(module, "111-1111", "");
    String locCode1 = "121";
    String locCode2 = "766";
    ComponentItem item = new ComponentItemImpl(bom, item1No, "GENERIC PART", Department.CHASSIS); 
    new LocationImpl(item, locCode1);
    new LocationImpl(item, locCode2);

    Transaction tx = null;
    Session session = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(truck);
      session.save(module);
      tx.commit();
    } finally {
      Finder.close(session);
    }

    Location locationFromDB = LocationFinder.find(item1No, locCode1);
    assertNotNull(locationFromDB);
    locationFromDB = LocationFinder.find(item1No, locCode2);
    assertNotNull(locationFromDB);
  }
}
