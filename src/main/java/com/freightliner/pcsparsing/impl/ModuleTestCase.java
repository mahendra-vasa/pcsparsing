package com.freightliner.pcsparsing.impl;

import java.util.*;

import net.sf.hibernate.*;

import org.apache.commons.lang.RandomStringUtils;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.service.*;

public class ModuleTestCase extends ModelTestCase {

  public ModuleTestCase(String name) {
    super(name);
  }

  public void testSortByNumber() throws Exception {
    List modules = new ArrayList();
    Module module1 = new ModuleImpl();
    module1.setNumber("ZZ1133");
    module1.setSequenceNumber("0");
    modules.add(module1);

    Module module2 = new ModuleImpl();
    module2.setNumber("AA9999");
    module2.setSequenceNumber("0");
    modules.add(module2);

    Module module3 = new ModuleImpl();
    module3.setNumber("123456");
    module3.setSequenceNumber("0");
    modules.add(module3);

    Module module4 = new ModuleImpl();
    module4.setNumber("AA9990");
    module4.setSequenceNumber("0");
    modules.add(module4);

    Collections.sort(modules);

    List expectedSortedModules = new ArrayList();
    expectedSortedModules.add(module3);
    expectedSortedModules.add(module4);
    expectedSortedModules.add(module2);
    expectedSortedModules.add(module1);

    assertEquals("Module.sortByNumber should sort modules by number", expectedSortedModules, modules);
  }

  public void testDuplicateModuleNumbers() throws Exception {
    String moduleNumber = "652";
    Truck truck = new TruckImpl("zz1234");
    String seqNumber1 = "000";
    Module module = new ModuleImpl(truck, moduleNumber, seqNumber1);

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
	
	String seqNumber2 = "001";
    Module module2 = new ModuleImpl(truck, moduleNumber, seqNumber2);
    tx = null;
    session = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
     // session.saveOrUpdate(truck);
      session.saveOrUpdate(module2);
     
      tx.commit();
     // fail("Attempt to insert duplicate module number should throw exception");
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	// Expected
    } finally {
      Finder.close(session);
    }

    session = null;
    try {
      session = ModuleFinder.getSession();
      List result = session.find(
          "select module.compID.sequenceNumber from ModuleImpl module where module.compID.number = ?",
          moduleNumber,
          Hibernate.STRING);
     // Integer count = (Integer) result.get(0);
     
      assertEquals("Should have two modules with number " + moduleNumber, 2, result.size());
    } finally {
      ModuleFinder.close(session);
    }
  }

  public void testEqualsPersistedObjects() throws Exception {
    // @TODO: Use full equals assert
    String module1Number = "456";
    Truck truck = new TruckImpl("AO1234");
    Module module1 = new ModuleImpl(truck, module1Number, "000");
    Module module2 = new ModuleImpl(truck, "613", "000");
    Transaction tx = null;
    Session session = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    assertTrue("Different modules should not be equal", !module1.equals(module2));

    Session session2 = null;
    try {
      session2 = Finder.getSession();
      Query query = session2.createQuery("from ModuleImpl module where module.compID.number = :number");
      query.setString("number", module1Number);
      Module module1DifferentSession = (Module) query.uniqueResult();
      assertEquals("Same module from different sessions should be equal", module1, module1DifferentSession);
    } finally {
      Finder.close(session2);
    }
  }

  public void testEqualsTransientObjects() throws HibernateException {
    String module1Number = "111";
    Truck truck = new TruckImpl("zz1234");
    Module module1 = new ModuleImpl(truck, module1Number, "");
    Module module2 = new ModuleImpl(truck, "222", "");

    assertTrue("Different modules should not be equal", !module1.equals(module2));
  }

  public void testGetDepartments() throws Exception {
    Truck truck = new TruckImpl("zz1234");
    Module module1 = new ModuleImpl(truck, "85C", "000");
    addItem(module1, Department.CHASSIS);

    Module module2 = new ModuleImpl(truck, "123", "000");
    addItem(module2, Department.BODY);
    addItem(module2, Department.ELECTRICAL);

    Module module3 = new ModuleImpl(truck, "456", "000");

    Module module4 = new ModuleImpl(truck, "111", "000"  );
    addItem(module4, Department.ELECTRICAL);
    addItem(module4, Department.BODY);
    addItem(module4, Department.CHASSIS);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      session.saveOrUpdate(module3);
      session.saveOrUpdate(module4);
      tx.commit();
      session.refresh(module1, LockMode.UPGRADE);
      session.refresh(module2, LockMode.UPGRADE);
      session.refresh(module3, LockMode.UPGRADE);
      session.refresh(module4, LockMode.UPGRADE);
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    SortedSet expectedDepts = new TreeSet();
    expectedDepts.add(Department.CHASSIS);
    assertEquals("Department names for module " + module1.getNumber(), expectedDepts, module1.getDepartments());

    expectedDepts = new TreeSet();
    expectedDepts.add(Department.BODY);
    expectedDepts.add(Department.ELECTRICAL);
    assertEquals("Department names for module " + module2.getNumber(), expectedDepts, module2.getDepartments());

    expectedDepts = new TreeSet();
    assertEquals("Department names for module " + module3.getNumber(), expectedDepts, module3.getDepartments());

    expectedDepts = new TreeSet();
    expectedDepts.add(Department.BODY);
    expectedDepts.add(Department.CHASSIS);
    expectedDepts.add(Department.ELECTRICAL);
    assertEquals("Department names for module " + module4, expectedDepts, module4.getDepartments());
  }

  public void testSimpleIsRevisionLevelUpdated() throws Exception {
    Truck truck = new TruckImpl("zz1234");
    Module module = new ModuleImpl(truck, "85C", "");
    BOM bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "199000-AAA");
    ComponentItem item = new ComponentItemImpl(bom, "EEE-999          ", "SPROCKET", Department.BODY);
    item.setEngRevisionLevel("P");
    item.setMfgRevisionLevel("P");

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

    Module moduleFromDB = (Module) ModuleFinder.getModulesOrderedByNumber(truck).get(0);
    assertTrue("IsRevisionLevelUpdated", !moduleFromDB.isRevisionLevelUpdated());

    truck = new TruckImpl("W00111");
    module = new ModuleImpl(truck, "555", "");
    bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "66666-SSS");
    item = new ComponentItemImpl(bom, "EWR-345565", "SPROCKET", Department.CHASSIS);
    item.setEngRevisionLevel("Z");
    item.setMfgRevisionLevel("P");

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

    moduleFromDB = (Module) ModuleFinder.getModulesOrderedByNumber(truck).get(0);
    assertTrue("IsRevisionLevelUpdated", moduleFromDB.isRevisionLevelUpdated());
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

    Module moduleFromDB = (Module) ModuleFinder.getModulesOrderedByNumber(truck).get(0);
    assertTrue("IsStatusValid", moduleFromDB.isStatusValid());

    truck = new TruckImpl("W00111");
    module = new ModuleImpl(truck, "555", "");
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

    moduleFromDB = (Module) ModuleFinder.getModulesOrderedByNumber(truck).get(0);
    assertTrue("IsStatusValid", !moduleFromDB.isStatusValid());
  }

}
