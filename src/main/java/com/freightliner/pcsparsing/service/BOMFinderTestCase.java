package com.freightliner.pcsparsing.service;

import java.util.*;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.*;


public class BOMFinderTestCase extends ModelTestCase {

  public BOMFinderTestCase(String name) {
    super(name);
  }

  public void testFindByID() throws Exception {
    String description = "BUNK-MOUNTED DVD PLAYER";
    String bomNumber = "860-C00902";
    Truck truck = new TruckImpl("ss9701");
    Module module = new ModuleImpl(truck, "711", "");
    BOM bom = new BOMImpl(module, "860-C00902", description);
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(module);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    BOM bomFromDB = BOMFinder.find(bomNumber);
    assertEquals("BOM " + description + " should exist in database", bom, bomFromDB);
    assertEquals("BOM description", description, bomFromDB.getDescription().trim());
  }

  public void testFindByIDWithItems() throws Exception {
    String description = "BUNK-MOUNTED DVD PLAYER";
    String bomNumber = "860-C00902";
    Truck truck = new TruckImpl("ss9701");
    Module module = new ModuleImpl(truck, "711", "");
    BOMImpl bom = new BOMImpl(module, "860-C00902", description);
    BOMPK pk = bom.getCompID();
    new ComponentItemImpl(bom, "9000", "GENERIC PART", Department.CHASSIS);
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(module);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    BOM bomFromDB = BOMFinder.find(bomNumber);
    assertNotNull("BOM should be found in DB", bomFromDB);
    Session session2 = null;
    try {
      session2 = Finder.getSession();
      session2.load(bomFromDB, pk);
      assertEquals("BOM " + description + " should exist in database", bom, bomFromDB);
      assertEquals("BOM description", description, bomFromDB.getDescription().trim());
      assertTrue("BOM should have items", !bomFromDB.getItems().isEmpty());
    } finally {
      Finder.close(session2);
    }

  }

  public void testFindForModule() throws Exception {
    Truck truck1 = new TruckImpl("ZZ1234");
    String module1Number = "777";
    String seqNumber = "";
    Module module1 = new ModuleImpl(truck1, module1Number, seqNumber);
    
    String sequenseNumber1 = "002";
    truck1.setTsoSequenceNumber(sequenseNumber1);

    BOM module1BOM1 = new BOMImpl(module1, "XXX-000111", "");
    List module1BOMItems = new ArrayList();
    module1BOMItems.add(new ComponentItemImpl(module1BOM1, "11", "GENERIC PART", Department.CHASSIS));
    module1BOMItems.add(new ComponentItemImpl(module1BOM1, "22", "GENERIC PART", Department.CHASSIS));
    module1BOMItems.add(new ComponentItemImpl(module1BOM1, "33", "GENERIC PART", Department.CHASSIS));
    module1BOMItems.add(new ComponentItemImpl(module1BOM1, "44", "GENERIC PART", Department.CHASSIS));
    module1BOMItems.add(new ComponentItemImpl(module1BOM1, "55", "GENERIC PART", Department.CHASSIS));
    module1BOMItems.add(new ComponentItemImpl(module1BOM1, "66", "GENERIC PART", Department.CHASSIS));

    BOM module1BOM2 = new BOMImpl(module1, "AAA-000111", "");
    List module1BOM2Items = new ArrayList();
    module1BOM2Items.add(new ComponentItemImpl(module1BOM2, "77", "GENERIC PART", Department.CHASSIS));
    module1BOM2Items.add(new ComponentItemImpl(module1BOM2, "88", "GENERIC PART", Department.CHASSIS));

    List module1BOMs = new ArrayList();
    module1BOMs.add(module1BOM1);
    module1BOMs.add(module1BOM2);
	
	Truck truck3 = new TruckImpl("ZZ1234");
    Module module3 = new ModuleImpl(truck3, module1Number,seqNumber);
    String sequenseNumber3 = "001";
    truck3.setTsoSequenceNumber(sequenseNumber3);
    
    BOM module3BOM1 = new BOMImpl(module3, "XXX-000111", "");
    List module3BOMItems = new ArrayList();
    module3BOMItems.add(new ComponentItemImpl(module3BOM1, "11", "GENERIC PART", Department.CHASSIS));
    module3BOMItems.add(new ComponentItemImpl(module3BOM1, "22", "GENERIC PART", Department.CHASSIS));
    module3BOMItems.add(new ComponentItemImpl(module3BOM1, "33", "GENERIC PART", Department.CHASSIS));
    module3BOMItems.add(new ComponentItemImpl(module3BOM1, "44", "GENERIC PART", Department.CHASSIS));
    module3BOMItems.add(new ComponentItemImpl(module3BOM1, "55", "GENERIC PART", Department.CHASSIS));
    module3BOMItems.add(new ComponentItemImpl(module3BOM1, "66", "GENERIC PART", Department.CHASSIS));
    
    
    Truck truck2 = new TruckImpl("ZZ1255");
    String module2Number = "11A";
    Module module2 = new ModuleImpl(truck2, module2Number, "");

    BOM module2BOM1 = new BOMImpl(module2, "CCC-999999", "");
    List module2BOMItems = new ArrayList();
    module2BOMItems.add(new ComponentItemImpl(module2BOM1, "11", "GENERIC PART", Department.CHASSIS));
    module2BOMItems.add(new ComponentItemImpl(module2BOM1, "22", "GENERIC PART", Department.CHASSIS));
    module2BOMItems.add(new ComponentItemImpl(module2BOM1, "33", "GENERIC PART", Department.CHASSIS));
    module2BOMItems.add(new ComponentItemImpl(module2BOM1, "44", "GENERIC PART", Department.CHASSIS));
    module2BOMItems.add(new ComponentItemImpl(module2BOM1, "55", "GENERIC PART", Department.CHASSIS));
    module2BOMItems.add(new ComponentItemImpl(module2BOM1, "66", "GENERIC PART", Department.CHASSIS));

    BOM module2BOM2 = new BOMImpl(module2, "DDD-2222222", "");
    List module2BOM2Items = new ArrayList();
    module2BOM2Items.add(new ComponentItemImpl(module2BOM2, "77", "GENERIC PART", Department.CHASSIS));
    module2BOM2Items.add(new ComponentItemImpl(module2BOM2, "88", "GENERIC PART", Department.CHASSIS));
    List module2BOMs = new ArrayList();
    module2BOMs.add(module2BOM1);
    module2BOMs.add(module2BOM2);

    Session session = null;
    Transaction transaction = null;
    try {
      session = Finder.getSession();
      transaction = session.beginTransaction();
      session.saveOrUpdate(truck1);
      session.saveOrUpdate(truck2);
      session.saveOrUpdate(truck3);
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      session.saveOrUpdate(module3BOM1);
      transaction.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(transaction, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    List actualModule1BOMs = BOMFinder.find(truck1.getSerialNumber(), module1Number);

    assertNotNull("Module " + module1Number + " should have BOMs", actualModule1BOMs);
    assertEquals("Module " + module1Number + " BOMs", module1BOMs.size(), actualModule1BOMs.size());
    // TODO: assert collections equals
    //assertEquals("Module " + module1Number + " BOMs", module1BOMs, actualModule1BOMs);
    for (Iterator iterator = actualModule1BOMs.iterator(); iterator.hasNext();) {
      BOM bom = (BOM) iterator.next();
      assertNotNull("Module " + module1Number + " BOM items", bom.getItems());
      assertTrue("Module " + module1Number + " BOM items should not be empty", !bom.getItems().isEmpty());
      for (Iterator iterator2 = bom.getItems().iterator(); iterator2.hasNext();) {
        ComponentItem item = (ComponentItem) iterator2.next();
        assertNotNull("Item ID", item);
        assertNotNull("Description should not be null", item.getDescription());
      }
    }

    List actualModule2BOMs = BOMFinder.find(truck2.getSerialNumber(), module2Number);
    assertNotNull("Module " + module2Number + " should have BOMs", actualModule2BOMs);
    assertEquals("Module " + module2Number + " BOMs", module2BOMs.size(), actualModule2BOMs.size());
    //assertEquals("Module " + module2Number + " BOMs", module2BOMs, actualModule2BOMs);
    for (Iterator iterator = actualModule2BOMs.iterator(); iterator.hasNext();) {
      BOM bom = (BOM) iterator.next();
      assertNotNull("Module " + module2Number + " BOM items", bom.getItems());
      assertTrue("Module " + module2Number + " BOM items should not be empty", !bom.getItems().isEmpty());
      for (Iterator iterator2 = bom.getItems().iterator(); iterator2.hasNext();) {
        ComponentItem item = (ComponentItem) iterator2.next();
        assertNotNull("Item ID", item);
        assertNotNull("Description should not be null", item.getDescription());
      }
    }
  }
}
