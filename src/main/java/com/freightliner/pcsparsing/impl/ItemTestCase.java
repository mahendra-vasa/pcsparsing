package com.freightliner.pcsparsing.impl;

import net.sf.hibernate.*;

import org.apache.commons.lang.RandomStringUtils;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.service.*;

public class ItemTestCase extends ModelTestCase {

  public ItemTestCase(String name) {
    super(name);
  }

  public void testEquals() throws Exception {
    // TODO: make more robust and handle transients
    // TODO: Add DB constraints
    Truck truck = new TruckImpl("SW1231");
    Module module = new ModuleImpl(truck, "666", "");
    BOM bom = new BOMImpl(module, "111-1111", "");
    String itemNumber = "24";
    ComponentItem item1 = new ComponentItemImpl(bom, itemNumber, "GENERIC PART", Department.CHASSIS);
    ComponentItem item2 = new ComponentItemImpl(bom, "25", "GENERIC PART", Department.CHASSIS);
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(truck);
      session.save(module);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    ComponentItem itemFromDB = ItemFinder.find(itemNumber);
    assertEquals("Items with same number should be equal", item1, itemFromDB);
    assertTrue("Items with different ids should not be equal", !item1.equals(item2));
    assertTrue("Items with different ids should not be equal", !item2.equals(item1));
    assertTrue("Items with different ids should not be equal", !itemFromDB.equals(item2));
  }

  public void testStatusInvalid() throws Exception {
    Truck truck = new TruckImpl("zz1234");
    Module module = new ModuleImpl(truck, "85C", "");
    BOM bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "199000-AAA");
    ComponentItem item = new ComponentItemImpl(bom, "QQQ-0000", "SPROCKET", Department.BODY);
    item.setEngRevisionLevel("P");
    item.setMfgRevisionLevel("P");
    item.setStatus("Z");

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module);
      tx.commit();
      session.refresh(module, LockMode.UPGRADE);
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    ComponentItem itemFromDB = ItemFinder.find(item.getNumber());
    assertTrue("IsStatusValid", itemFromDB.isStatusValid());

    truck = new TruckImpl("W00111");
    module = new ModuleImpl(truck, "555","");
    bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "66666-SSS");
    item = new ComponentItemImpl(bom, "EWR-345565", "SPROCKET", Department.CHASSIS);
    item.setEngRevisionLevel("Z");
    item.setMfgRevisionLevel("P");
    item.setStatus("D");

    session = null;
    tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module);
      tx.commit();
      session.refresh(module, LockMode.UPGRADE);
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    itemFromDB = ItemFinder.find(item.getNumber());
    assertTrue("IsStatusValid", !itemFromDB.isStatusValid());
  }

  public void testPurchasingUnitOfMeasure() throws Exception {
    Truck truck = new TruckImpl("zz1234");
    Module module = new ModuleImpl(truck, "85C", "");
    BOM bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "199000-AAA");
    String itemDesc = "SPROCKET";
    ComponentItem item = new ComponentItemImpl(bom, "QQQ-0000", itemDesc, Department.BODY);
    String purchasingUnitOfMeasure = "ft";
    item.setPurchasingUnitOfMeasure(purchasingUnitOfMeasure);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module);
      tx.commit();
      session.refresh(module, LockMode.UPGRADE);
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    ComponentItem itemFromDB = ItemFinder.find(item.getNumber());
    assertEqualsTrimmed("Component item desc", itemDesc, itemFromDB.getDescription());
    assertEqualsTrimmed("Component item purchasingUnitOfMeasure", purchasingUnitOfMeasure, itemFromDB.getPurchasingUnitOfMeasure());
  }

  public void testItemType() throws Exception {
    Truck truck = new TruckImpl("zz1234");
    Module module = new ModuleImpl(truck, "85C", "");
    BOM bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "199000-AAA");
    ComponentItem item = new ComponentItemImpl(bom, "QQQ-0000", "SPROCKET", Department.BODY);
    item.setStatus("Z");
    ItemType type = ItemType.L;
    item.setType(type);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module);
      tx.commit();
      session.refresh(module, LockMode.UPGRADE);
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    ComponentItem itemFromDB = ItemFinder.find(item.getNumber());
    assertEquals("ItemType", type, itemFromDB.getType());
  }

  public void testLocations() throws Exception {
    Truck truck = new TruckImpl("zz1234");
    Module module = new ModuleImpl(truck, "85C","");
    BOM bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "199000-AAA");
    ComponentItem item = new ComponentItemImpl(bom, "QQQ-0000", "SPROCKET", Department.BODY);
    new LocationImpl(item, "007");
    
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module);
      tx.commit();
      session.refresh(module, LockMode.UPGRADE);
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    ComponentItem itemFromDB = ItemFinder.findWithLocations(item.getNumber());
    assertNotNull("Item should not be null", itemFromDB);
    assertEquals("Item locations", 1, itemFromDB.getLocations().size());
    
    
  }

}
