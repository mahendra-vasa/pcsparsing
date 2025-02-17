package com.freightliner.pcsparsing.impl;

import java.util.List;

import net.sf.hibernate.*;

import org.apache.commons.lang.RandomStringUtils;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.service.*;

public class ModuleOrderTestCase extends ModelTestCase {

	public ModuleOrderTestCase(String name) {
		super(name);
	}
	
	public void testIsOrdered() throws Exception {
    Truck truck = new TruckImpl("zz1234");
    Module module = new ModuleImpl(truck, "85C", "");
    module.setOrdered(true);
    BOM bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "199000-AAA");
    new ComponentItemImpl(bom, "QQQ-0000", "SPROCKET", Department.BODY);

    Session session = null;
    Transaction tx = null;
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

    List modules = ModuleFinder.getModulesOrderedByNumber(truck);
    assertEquals("Module count", 1, modules.size());
    Module moduleFromDB = (Module) modules.get(0);
    assertEquals("IsOrdered", true, moduleFromDB.isOrdered());
    truck = new TruckImpl("W00111");
    module = new ModuleImpl(truck, "555", "");
    bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "66666-SSS");
    new ComponentItemImpl(bom, "EWR-345565", "SPROCKET", Department.CHASSIS);

    session = null;
    tx = null;
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

    modules = ModuleFinder.getModulesOrderedByNumber(truck);
    assertEquals("Module count", 1, modules.size());
    moduleFromDB = (Module) modules.get(0);
    assertEquals("IsOrdered", false, moduleFromDB.isOrdered());

    truck = new TruckImpl("Z00567");
    module = new ModuleImpl(truck, "444", "");
    module.setOrdered(false);
    bom = new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "12333-SSS");
    new ComponentItemImpl(bom, "HYU-345565", "WIDGET", Department.CHASSIS);

    session = null;
    tx = null;
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

    modules = ModuleFinder.getModulesOrderedByNumber(truck);
    assertEquals("Module count", 1, modules.size());
    moduleFromDB = (Module) modules.get(0);
    assertEquals("IsOrdered", false, moduleFromDB.isOrdered());
	}
}
