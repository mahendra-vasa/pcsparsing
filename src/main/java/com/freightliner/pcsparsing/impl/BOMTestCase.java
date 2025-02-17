package com.freightliner.pcsparsing.impl;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.service.*;
import com.freightliner.pcsparsing.Module;

public class BOMTestCase extends ModelTestCase {

  public BOMTestCase(String name) {
    super(name);
  }

  public void testGetPurchasingUnitOfMeasure() throws Exception {
    String number = "AABB-000111";
    Truck truck = new TruckImpl("QQ1234");
    Module module = new ModuleImpl(truck, "000", "");
    BOM bom = new BOMImpl(module, number, "");
    String purchasingUnitOfMeasure = "in";
    bom.setPurchasingUnitOfMeasure(purchasingUnitOfMeasure);

    Transaction tx = null;
    Session session = null;
    try {
      session = Finder.getSession();
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

    BOM bomFromDB = BOMFinder.find(number);

    assertNotNull("BOM from database", bomFromDB);
    assertEquals("BOM number", bom.getNumber(), bomFromDB.getNumber());
    assertEquals("BOM purchasingUnitOfMeasure", purchasingUnitOfMeasure, bomFromDB.getPurchasingUnitOfMeasure());
  }

  public void testGetBOM() throws Exception {
    String number = "AABB-000111";
    Truck truck = new TruckImpl("QQ1234");
    Module module = new ModuleImpl(truck, "000", "");
    BOM bom = new BOMImpl(module, number, "");

    Transaction tx = null;
    Session session = null;
    try {
      session = Finder.getSession();
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

    BOM bomFromDB = BOMFinder.find(number);

    assertNotNull("BOM from database", bomFromDB);
    assertEquals("BOM number", bom.getNumber(), bomFromDB.getNumber());
  }

  public void testEquals() throws Exception {
    // TODO: make more robust and handle transients
    // TODO: Add DB constraints
    String bom1Number ="foo";
    Truck truck = new TruckImpl("pp9900");
    Module module = new ModuleImpl(truck, "222", "");
    BOM bom1 = new BOMImpl(module, bom1Number, "desc1");
    BOM bom2 = new BOMImpl(module, "bar", "desc2");

    Transaction tx = null;
    Session session = null;
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

    BOM bomFromDB = BOMFinder.find(bom1Number);
    assertNotNull(bomFromDB);
    assertEquals("BOMs with same item number should be equal", bom1, bomFromDB);
    assertTrue("BOMs with different item number should not be equal", !bom1.equals(bom2));
    assertTrue("BOMs with different item number should not be equal", !bom2.equals(bom1));
    assertTrue("BOMs with different item number should not be equal", !bomFromDB.equals(bom2));
  }
}
