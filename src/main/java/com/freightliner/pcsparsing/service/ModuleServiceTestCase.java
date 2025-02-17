package com.freightliner.pcsparsing.service;

import net.sf.hibernate.*;

import org.apache.commons.lang.RandomStringUtils;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.*;

public class ModuleServiceTestCase extends ModelTestCase {

	public ModuleServiceTestCase(String name) {
		super(name);
	}
	
	public void testSetOrdered() throws Exception {
    Truck truck = new TruckImpl("Q88777");
    Module module1 = new ModuleImpl(truck, "176", "");

    BOM bom = new BOMImpl(module1, RandomStringUtils.randomAlphanumeric(25), "My BOM");
    new ComponentItemImpl(bom, RandomStringUtils.randomAlphanumeric(25), "SPROCKET", Department.CHASSIS);

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(module1);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    Module moduleFromDB = (Module) ModuleFinder.getModulesOrderedByNumber(truck.getSerialNumber()).get(0);
    assertEquals("Module ordered", false, moduleFromDB.isOrdered());

    ModuleService.setOrdered(truck.getSerialNumber(), module1.getNumber(), "foo");
    Module moduleFromDB2 = (Module) ModuleFinder.getModulesOrderedByNumber(truck.getSerialNumber()).get(0);
    assertEquals("Module ordered", true, moduleFromDB2.isOrdered());
    
    ModuleService.setOrdered(truck.getSerialNumber(), module1.getNumber(), "foo");
    Module moduleFromDB3 = (Module) ModuleFinder.getModulesOrderedByNumber(truck.getSerialNumber()).get(0);
    assertEquals("Module ordered", false, moduleFromDB3.isOrdered());
    
    ModuleService.setOrdered(truck.getSerialNumber(), module1.getNumber(), "foo");
    Module moduleFromDB4 = (Module) ModuleFinder.getModulesOrderedByNumber(truck.getSerialNumber()).get(0);
    assertEquals("Module ordered", true, moduleFromDB4.isOrdered());
  }
}
