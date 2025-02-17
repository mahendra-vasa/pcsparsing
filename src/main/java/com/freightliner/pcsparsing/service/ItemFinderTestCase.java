package com.freightliner.pcsparsing.service;

import java.util.*;
import java.util.Collection;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.*;

public class ItemFinderTestCase extends ModelTestCase {

  public ItemFinderTestCase(String name) {
    super(name);
  }

  public void testFind() throws Exception {
    Truck truck = new TruckImpl("pp0099");
    Module module = new ModuleImpl(truck, "111", "");
    BOM bom = new BOMImpl(module, "34141", "Description");
    ComponentItem item = new ComponentItemImpl(bom, "900", "GENERIC PART", Department.CHASSIS);
    Transaction tx = null;
    Session session = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    ComponentItem itemFromDB = ItemFinder.find(item.getNumber());
    assertEquals("Item should exist in database", item, itemFromDB);
  }

  public void testFindOrderedPartsWithLocation() throws Exception {
    SerialNumber serialNumber = new SerialNumberImpl("pp0099", "", "");
    Truck truck = new TruckImpl("pp0099");
    Module module = new ModuleImpl(truck, "111", "");
    module.setOrdered(true);
    BOM bom = new BOMImpl(module, "34141", "Description");
    ComponentItem item1 = new ComponentItemImpl(bom, "900", "GENERIC PART", Department.CHASSIS);
    Location location = new LocationImpl(item1, "009");
    location.setCrfa("123456");
    location.setShopAddress("789012");
    location.setMatlPlanner("090");
    location.setOhTotalQuantity(10900);
    location.setSafetyStock(200);
    location.setPeg3DayQuantity(340);
    location.setPegLeadTimeQuantity(500);
    location.setChangeoverDate(getDate(1969, 4, 30));
    location.setPrimaryVendorAbbr("MRMX");
    location.setSecondaryVendorId("0001110");
    location.setAltVendorId("99999");
    location.setTotalCost(10.20);
    location.setOrdsTotalCost(1999.99);
    location.setNetAvailable(7200);

    location = new LocationImpl(item1, "008");
    
    assertEquals("Item location count ", 2, item1.getLocations().size());
    
    Transaction tx = null;
    Session session = null;
    try {
      session = Finder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(serialNumber);
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }
	
	Collection modules = ItemFinder.findModules(serialNumber);
    Collection items = ItemFinder.findOrderedItems(serialNumber, modules);
    assertEquals("Items count", 2, items.size());
    assertTrue("BOM " + bom.getNumber() + " should be in results", items.contains(bom));
    assertTrue("Item " + item1.getNumber() + " should be in results", items.contains(item1));
    Item itemFromDB = null;
    for (Iterator iter = items.iterator(); iter.hasNext();) {
      Item item = (Item) iter.next();
      if (item instanceof ComponentItem) {
        itemFromDB = item;
        break;
      }
    }
    assertEquals("USA cost ", 1999.99, itemFromDB.getUsaCost(), 0.001);
    assertEquals("Item " + itemFromDB.getNumber() + "should have locations", 2, itemFromDB.getLocations().size());
  }

  public void testFindOrderedParts() throws Exception {
    SerialNumber serialNumber = new SerialNumberImpl("pp0099", "", "");
    Truck truck = new TruckImpl("pp0099");
    Module module = new ModuleImpl(truck, "111", "");
    module.setOrdered(true);
    BOM bom = new BOMImpl(module, "34141", "Description");
    ComponentItem item1 = new ComponentItemImpl(bom, "900", "GENERIC PART", Department.CHASSIS);
    new LocationImpl(item1, "008");
    new LocationImpl(item1, "009");
    new LocationImpl(item1, "010");
    BOM bom2 = new BOMImpl(module, "1233", "DESC");
    ComponentItem item2 = new ComponentItemImpl(bom2, "120", "Mug", Department.CHASSIS);
    new LocationImpl(item2, "010");
    ComponentItem item3 = new ComponentItemImpl(bom2, "222", "Cup", Department.CHASSIS);
    ComponentItem item4 = new ComponentItemImpl(bom2, "333", "Saucer", Department.CHASSIS);
    new LocationImpl(item3, "010");
    new LocationImpl(item3, "008");
    new LocationImpl(item3, "090");
    Transaction tx = null;
    Session session = null;
    try {
      session = Finder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(serialNumber);
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    Collection modules = ItemFinder.findModules(serialNumber);
    Collection items = ItemFinder.findOrderedItems(serialNumber, modules);
    assertTrue("BOM " + bom.getNumber() + "should be in results", items.contains(bom));
    assertTrue("BOM " + bom2.getNumber() + "should be in results", items.contains(bom2));
    assertTrue("Item " + item1.getNumber() + "should be in results", items.contains(item1));
    assertTrue("Item 2 " + item1.getNumber() + "should be in results", items.contains(item2));
    assertTrue("Item 3 " + item1.getNumber() + "should be in results", items.contains(item3));
    assertTrue("Item 4 " + item1.getNumber() + "should be in results", items.contains(item4));
    assertEquals("Items count", 6, items.size());
  }
}
