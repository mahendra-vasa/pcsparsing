package com.freightliner.pcsparsing.service;

import java.util.*;

import net.sf.hibernate.*;

import org.apache.commons.lang.RandomStringUtils;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.*;

public class ModuleFinderTestCase extends ModelTestCase {

  public ModuleFinderTestCase(String name) throws Exception {
    super(name);
  }

  public void testGetModulesOrderedByNumber() throws Exception {
    Truck truck = new TruckImpl("Q88777");
    Set modules = new HashSet();
    Module module1 = new ModuleImpl(truck, "176", "000");
    modules.add(module1);

    BOM bom = new BOMImpl(module1, RandomStringUtils.randomAlphanumeric(25), "My BOM");
    ComponentItem item = new ComponentItemImpl(bom, RandomStringUtils.randomAlphanumeric(25), "SPROCKET", Department.CHASSIS);
    item.setEngRevisionLevel("P");
    item.setMfgRevisionLevel("X");

    item = new ComponentItemImpl(bom, RandomStringUtils.randomAlphanumeric(25), "SPROCKET", Department.ELECTRICAL);
    item.setEngRevisionLevel("P");
    item.setMfgRevisionLevel("X");

    item = new ComponentItemImpl(bom, RandomStringUtils.randomAlphanumeric(25), "SPROCKET", Department.BODY);
    item.setEngRevisionLevel("P");
    item.setMfgRevisionLevel("X");

    Module module2 = new ModuleImpl(truck, "999", "000");
    modules.add(module2);
    Module module4 = new ModuleImpl(truck, "20C", "000");
    modules.add(module4);
    truck.setModules(modules);

    List expectedModules = new ArrayList();
    expectedModules.add(module1);
    expectedModules.add(module4);
    expectedModules.add(module2);

    Truck truck2 = new TruckImpl("AAABBB");
    Module module3 = new ModuleImpl(truck2, "20A", "000");
    Module module5 = new ModuleImpl(truck2, "20B", "000");

    List expectedModules2 = new ArrayList();
    expectedModules2.add(module3);
    expectedModules2.add(module5);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
    
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      session.saveOrUpdate(module3);
      session.saveOrUpdate(module4);
      session.saveOrUpdate(module5);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    List modulesFromDB = ModuleFinder.getModulesOrderedByNumber(truck);
    assertEquals("Module count for " + truck.getSerialNumber(), expectedModules.size(), modulesFromDB.size());
    assertEquals("Finder should return ordered modules for " + truck.getSerialNumber(), expectedModules, modulesFromDB);

    List modulesFromDB2 = ModuleFinder.getModulesOrderedByNumber(truck2);
    assertEquals("Module count for " + truck2.getSerialNumber(), expectedModules2.size(), modulesFromDB2.size());
    assertEquals("Finder should return ordered modulesfor " + truck2.getSerialNumber(), expectedModules2, modulesFromDB2);

  }

  public void testGetModulesOrderedByNumberWithOrderedFilter() throws Exception {
    Truck truck1 = new TruckImpl("A01000");
    Module module1 = new ModuleImpl(truck1, "85C", "000");
    module1.setOrdered(true);

    Module module2 = new ModuleImpl(truck1, "123", "000");
    module2.setOrdered(false);

    Module module3 = new ModuleImpl(truck1, "456", "000");
    module3.setOrdered(true);


    Truck truck2 = new TruckImpl("A02000");
    Module module4 = new ModuleImpl(truck2, "111", "000");
    module4.setOrdered(false);

    Module module5 = new ModuleImpl(truck2, "ZZZ", "000");
    module5.setOrdered(true);

    Module module6 = new ModuleImpl(truck2, "90Z", "000");
    module6.setOrdered(true);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      //session.saveOrUpdate(truck1);
      //session.saveOrUpdate(truck2);
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      session.saveOrUpdate(module3);
      session.saveOrUpdate(module4);
      session.saveOrUpdate(module5);
      session.saveOrUpdate(module6);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    List expectedModules = new ArrayList();
    expectedModules.add(module2);
    expectedModules.add(module3);
    expectedModules.add(module1);
    List actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(), IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("All modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("All modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("All modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module3);
    expectedModules.add(module1);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(), IsOrderedSearchCriteria.ONLY, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("Only ordered modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("Only ordered modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only ordered modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module2);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(), IsOrderedSearchCriteria.NOT, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("Only unordered modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("Only unordered modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only unordered modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module4);
    expectedModules.add(module6);
    expectedModules.add(module5);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(), IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("All modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("All modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("All modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module6);
    expectedModules.add(module5);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(), IsOrderedSearchCriteria.ONLY, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("Only ordered modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("Only ordered modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only ordered modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module4);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(), IsOrderedSearchCriteria.NOT, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("Only unordered modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("Only unordered modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only unordered modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);
  }

  public void testGetModulesOrderedByNumberWithDepartmentFilter() throws Exception {
    Truck truck1 = new TruckImpl("A01000");
    Module module1 = new ModuleImpl(truck1, "85C", "000");
    addItem(module1, Department.CHASSIS);

    Module module2 = new ModuleImpl(truck1, "123", "000");
    addItem(module2, Department.BODY);

    Module module3 = new ModuleImpl(truck1, "456", "000");
    addItem(module3, Department.ELECTRICAL);

    Truck truck2 = new TruckImpl("A02000");
    Module module4 = new ModuleImpl(truck2, "111", "000");
    addItem(module4, Department.ELECTRICAL);

    Module module5 = new ModuleImpl(truck2, "123", "000");
    addItem(module5, Department.ELECTRICAL);

    Module module6 = new ModuleImpl(truck2, "90Z", "000");
   addItem(module6, Department.CHASSIS);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck1);
      session.saveOrUpdate(truck2);
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      session.saveOrUpdate(module3);
      session.saveOrUpdate(module4);
      session.saveOrUpdate(module5);
      session.saveOrUpdate(module6);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    List expectedModules = new ArrayList();
    expectedModules.add(module2);
    expectedModules.add(module3);
    expectedModules.add(module1);
    List actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("All modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("All modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("All modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module2);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.BODY, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Body modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("Only Body modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Body modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module1);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.CHASSIS, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Chassis modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("Only Chassis modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Chassis modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module3);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ELECTRICAL, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Electrical modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("Only Electrical modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Electrical modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module4);
    expectedModules.add(module5);
    expectedModules.add(module6);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("All modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("All modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("All modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.BODY, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Body modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("Only Body modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Body modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module6);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.CHASSIS, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Chassis modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("Only Chassis modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Chassis modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module4);
    expectedModules.add(module5);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ELECTRICAL, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Electrical modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("Only Electrical modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Electrical modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

  }

  public void testGetModulesOrderedByNumberWithMultipleDepartmentFilter() throws Exception {
    Truck truck1 = new TruckImpl("A01000");
    Module module1 = new ModuleImpl(truck1, "85C", "000");
    addItem(module1, Department.CHASSIS);
    addItem(module1, Department.BODY);

    Module module2 = new ModuleImpl(truck1, "123", "000");
    addItem(module2, Department.BODY);

    Module module3 = new ModuleImpl(truck1, "456", "000");
    addItem(module3, Department.ELECTRICAL);

    Truck truck2 = new TruckImpl("A02000");
    Module module4 = new ModuleImpl(truck2, "111", "000");
    addItem(module4, Department.ELECTRICAL);
    addItem(module4, Department.CHASSIS);

    Module module5 = new ModuleImpl(truck2, "ZZZ", "000");
    addItem(module5, Department.ELECTRICAL);

    Module module6 = new ModuleImpl(truck2, "90Z", "000");
    addItem(module6, Department.BODY);
    addItem(module6, Department.CHASSIS);
    addItem(module6, Department.ELECTRICAL);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck1);
      session.saveOrUpdate(truck2);
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      session.saveOrUpdate(module3);
      session.saveOrUpdate(module4);
      session.saveOrUpdate(module5);
      session.saveOrUpdate(module6);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    List expectedModules = new ArrayList();
    expectedModules.add(module2);
    expectedModules.add(module3);
    expectedModules.add(module1);
    List actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("All modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("All modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("All modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module2);
    expectedModules.add(module1);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.BODY, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Body modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("Only Body modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Body modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module1);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.CHASSIS, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Chassis modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("Only Chassis modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Chassis modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module3);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ELECTRICAL, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Electrical modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("Only Electrical modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Electrical modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module4);
    expectedModules.add(module6);
    expectedModules.add(module5);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("All modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("All modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("All modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module6);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.BODY, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Body modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("Only Body modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Body modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module4);
    expectedModules.add(module6);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.CHASSIS, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Chassis modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("Only Chassis modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Chassis modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module4);
    expectedModules.add(module6);
    expectedModules.add(module5);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ELECTRICAL, HasCWOSearchCriteria.ALL);
    assertNotNull("Only Electrical modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("Only Electrical modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Electrical modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);
  }

  public void testGetModulesOrderedByNumberRevLevel() throws Exception {
    Truck truck1 = new TruckImpl("A01000");
    Module module1 = new ModuleImpl(truck1, "85C", "");
    addItem(module1, Department.CHASSIS);
    addItem(module1, Department.BODY);

    Module module2 = new ModuleImpl(truck1, "123", "");
    addItem(module2, Department.BODY);

    Module module3 = new ModuleImpl(truck1, "456", "");
    addItem(module3, Department.ELECTRICAL);

    Truck truck2 = new TruckImpl("A02000");
    Module module4 = new ModuleImpl(truck2, "111", "");
    addItem(module4, Department.ELECTRICAL);
    addItem(module4, Department.CHASSIS);

    Module module5 = new ModuleImpl(truck2, "ZZZ", "");
    addItem(module5, Department.ELECTRICAL);

    Module module6 = new ModuleImpl(truck2, "90Z", "");
    addItem(module6, Department.BODY);
    addItem(module6, Department.CHASSIS);
    addItem(module6, Department.ELECTRICAL);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck1);
      session.saveOrUpdate(truck2);
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      session.saveOrUpdate(module3);
      session.saveOrUpdate(module4);
      session.saveOrUpdate(module5);
      session.saveOrUpdate(module6);
     tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

  }

  public void testGetModulesOrderedByNumberWithMultipleCriteria() throws Exception {
    Truck truck1 = new TruckImpl("A01000");
    Module module1 = new ModuleImpl(truck1, "85C", "000");
    module1.setOrdered(false);
    BOM bom1 = new BOMImpl(module1, "129-C01248", "");
    ComponentItemImpl com1=new ComponentItemImpl(bom1, "UUU-999         ", "WIDGET SEAL", Department.CHASSIS);

    Module module2 = new ModuleImpl(truck1, "123", "000");
    module2.setOrdered(true);
    BOM bom2 = new BOMImpl(module2, "129-C01248    ", "");
    ComponentItemImpl com2= new ComponentItemImpl(bom2, "QQQ-999            ", "WIDGET SEAL", Department.BODY);

    Module module3 = new ModuleImpl(truck1, "456", "000");
    module3.setOrdered(true);
    BOM bom3 = new BOMImpl(module3, "A01000-456  ", "");
    bom3.setType(ItemType.CWO);
    bom3.setCwoIndicator("C");
    // BOM #4
    BOM bom4 = new BOMImpl(module3, "A01001-456   ", "");
    ComponentItemImpl com3 = new ComponentItemImpl(bom4, "FFF-999", "WIDGET SEAL", Department.ELECTRICAL);

    Truck truck2 = new TruckImpl("A02000");
    Module module4 = new ModuleImpl(truck2, "111", "000");
    module4.setOrdered(false);
    BOM bom5 = new BOMImpl(module4, "129-C01248        ", "");
    ComponentItemImpl com4 = new ComponentItemImpl(bom5, "EEE-999          ", "WIDGET SEAL", Department.ELECTRICAL);

    Module module5 = new ModuleImpl(truck2, "ZZZ", "000");
    module5.setOrdered(true);
    BOM bom6 = new BOMImpl(module5, "A02000-ZZZ", "");
    bom6.setType(ItemType.CWO);
    bom6.setCwoIndicator("C");

    Module module6 = new ModuleImpl(truck2, "90Z", "000");
    module6.setOrdered(true);
    BOM bom7 = new BOMImpl(module6, "A02000-90Z", "");
    bom7.setType(ItemType.CWO);
    bom7.setCwoIndicator("C");

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
   //   session.saveOrUpdate(truck1);
   //   session.saveOrUpdate(truck2);
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      session.saveOrUpdate(module3);
      session.saveOrUpdate(module4);
      session.saveOrUpdate(module5);
      session.saveOrUpdate(module6);
      
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    List expectedModules = new ArrayList();
    expectedModules.add(module2);
    expectedModules.add(module3);
    expectedModules.add(module1);
    List actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("All modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("All modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("All modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module3);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck1.getSerialNumber(),
        IsOrderedSearchCriteria.ONLY, DepartmentSearchCriteria.ELECTRICAL, HasCWOSearchCriteria.ONLY);
    assertNotNull("Only Electrical, CWO modules for truck " + truck1.getSerialNumber(), actualModules);
    assertEquals("Only Electrical, CWO modules for truck " + truck1.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only Electrical, CWO modules for truck " + truck1.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(module4);
    expectedModules.add(module6);
    expectedModules.add(module5);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck2.getSerialNumber(),
        IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("All modules for truck " + truck2.getSerialNumber(), actualModules);
    assertEquals("All modules for truck " + truck2.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("All modules for truck " + truck2.getSerialNumber(), expectedModules, actualModules);

    // TODO: Add more permutations
  }

  public void testGetModulesOrderedByNumberNonexistantTruck() throws Exception {
    List modules = ModuleFinder.getModulesOrderedByNumber("E07913");
    assertTrue("Finder should return empty list if truck does not exist", modules.isEmpty());
  }

  public void testFindByModuleID() throws Exception {
    Truck truck = new TruckImpl("123456");
    Module[] moduleToFind = new Module[2];
    moduleToFind[0] = new ModuleImpl(truck, "L10", "000");
    moduleToFind[1] = new ModuleImpl(truck, "L10", "001");
    
    new ModuleImpl(truck, "727", "000");
    new ModuleImpl(truck, "777", "000");
    new ModuleImpl(truck, "707", "000");
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
    //  session.saveOrUpdate(truck);
      session.saveOrUpdate(moduleToFind[0]);
      session.saveOrUpdate(moduleToFind[1]);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }
	List list = ModuleFinder.find(truck.getSerialNumber(), moduleToFind[0].getNumber());
	assertEquals("Truck "+ truck.getSerialNumber(), 2, list.size());
	
	Iterator moduleIt= list.iterator();
    int i = 0;
    
    
    if (moduleIt.hasNext()) {
    	Module moduleFromDB = (Module) moduleIt.next();
    	String seqnumber = moduleFromDB.getSequenceNumber();
    	if (seqnumber == "001" ) {    	
    		assertEquals("ModuleFinder.find(id) should return module with ID of module ID", moduleToFind[1], moduleFromDB);
    		i = 0;
    		
    	} else {
    		assertEquals("ModuleFinder.find(id) should return module with ID of module ID", moduleToFind[0], moduleFromDB);
    		i = 1;
    	}
    	while (moduleIt.hasNext()) {
    			moduleFromDB = (Module) moduleIt.next();
    			assertEquals("ModuleFinder.find(id) should return module with ID of module ID", moduleToFind[i], moduleFromDB);
    	}
    }
  }

  public void testGetModulesOrderedByNumberWithCWOFilter() throws Exception {
    Truck truck = new TruckImpl("M24557");
    Module bomModule = new ModuleImpl(truck, "129", "000");
    BOM bom = new BOMImpl(bomModule, "129-C01248", "");
    bomModule.add(bom);

    Module cwoModule = new ModuleImpl(truck, "123", "000");
    bom = new BOMImpl(cwoModule, "M24557-123", "");
	// Leaving item type blank
    bom.setCwoIndicator("C");
    bom.setType(ItemType.EMPTY);
    cwoModule.add(bom);

    Module cwoAndBOMModule = new ModuleImpl(truck, "456", "000");
    bom = new BOMImpl(cwoAndBOMModule, "456-D60004", "");
    cwoAndBOMModule.add(bom);
    bom = new BOMImpl(cwoAndBOMModule, "M24557-456", "");
    bom.setType(ItemType.CWO);
    bom.setType(ItemType.CWO);
    bom.setCwoIndicator("C");
    cwoAndBOMModule.add(bom);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(bomModule);
      session.saveOrUpdate(cwoModule);
      session.saveOrUpdate(cwoAndBOMModule);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    List expectedModules = new ArrayList();
    expectedModules.add(cwoModule);
    expectedModules.add(bomModule);
    expectedModules.add(cwoAndBOMModule);
    List actualModules = ModuleFinder.getModulesOrderedByNumber(truck.getSerialNumber(), IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ALL);
    assertNotNull("All modules for truck " + truck.getSerialNumber(), actualModules);
    assertEquals("All modules for truck " + truck.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("All modules for truck " + truck.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(cwoModule);
    expectedModules.add(cwoAndBOMModule);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck.getSerialNumber(), IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.ONLY);
    assertNotNull("Only modules with CWOs for truck " + truck.getSerialNumber(), actualModules);
    assertEquals("Only modules with CWOs for truck " + truck.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only modules with CWOs for truck " + truck.getSerialNumber(), expectedModules, actualModules);

    expectedModules = new ArrayList();
    expectedModules.add(bomModule);
    actualModules = ModuleFinder.getModulesOrderedByNumber(truck.getSerialNumber(), IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL, HasCWOSearchCriteria.NOT);
    assertNotNull("Only modules without CWOs for truck " + truck.getSerialNumber(), actualModules);
    assertEquals("Only modules without CWOs  for truck " + truck.getSerialNumber(), expectedModules.size(), actualModules.size());
    assertEquals("Only modules without CWOs  for truck " + truck.getSerialNumber(), expectedModules, actualModules);
  }

}
