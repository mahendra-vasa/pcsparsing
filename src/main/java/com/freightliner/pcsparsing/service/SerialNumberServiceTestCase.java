package com.freightliner.pcsparsing.service;

import net.sf.hibernate.*;
import java.util.Iterator;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.*;


public class SerialNumberServiceTestCase extends ModelTestCase {

  public SerialNumberServiceTestCase(String name) {
    super(name);
  }
  
	public void testToggleIsParsed() throws Exception {
	  String number = "E00001";
    SerialNumber serialNumber = new SerialNumberImpl(number, "", "");

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(serialNumber);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    SerialNumber serialNumberFromDB = SerialNumberFinder.find(number);
    assertEquals("SerialNumber.isParsed()", false, serialNumberFromDB.isParsed());

    SerialNumberService.toggleIsParsed(number);
    SerialNumber serialNumberFromDB2 = SerialNumberFinder.find(number);
    assertEquals("SerialNumber.isParsed()", true, serialNumberFromDB2.isParsed());
    
    SerialNumberService.toggleIsParsed(number);
    SerialNumber serialNumberFromDB3 = SerialNumberFinder.find(number);
    assertEquals("SerialNumber.isParsed()", false, serialNumberFromDB3.isParsed());
    
    SerialNumberService.toggleIsParsed(number);
    SerialNumber serialNumberFromDB4 = SerialNumberFinder.find(number);
    assertEquals("SerialNumber.isParsed()", true, serialNumberFromDB4.isParsed());
  }

  
	public void testToggleIsTracked() throws Exception {
	  String number = "E00001";
    SerialNumber serialNumber = new SerialNumberImpl(number, "", "");

    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.save(serialNumber);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }

    SerialNumber serialNumberFromDB = SerialNumberFinder.find(number);
    assertEquals("SerialNumber.isTracked()", false, serialNumberFromDB.isTracked());

    SerialNumberService.toggleIsTracked(number);
    SerialNumber serialNumberFromDB2 = SerialNumberFinder.find(number);
    assertEquals("SerialNumber.isTracked()", true, serialNumberFromDB2.isTracked());
    
    SerialNumberService.toggleIsTracked(number);
    SerialNumber serialNumberFromDB3 = SerialNumberFinder.find(number);
    assertEquals("SerialNumber.isTracked()", false, serialNumberFromDB3.isTracked());
    
    SerialNumberService.toggleIsTracked(number);
    SerialNumber serialNumberFromDB4 = SerialNumberFinder.find(number);
    assertEquals("SerialNumber.isTracked()", true, serialNumberFromDB4.isTracked());
  }

  
	public void testAdd() throws Exception {
	  String number = "A44090";
    String description = "This is a description";
    String cmcsNumber = "JFL89";

    SerialNumber serialNumber = SerialNumberFinder.find(number);
    assertNull("SerialNumber should not exist", serialNumber);
    
    SerialNumberService.add(number, description, cmcsNumber);
    
    serialNumber = SerialNumberFinder.find(number);
    assertNotNull("SerialNumber should exist", serialNumber);
	}
	
	public void testDelete() throws Exception {
	  String number = "G77099";
    
    SerialNumberService.add(number, "", "");
    SerialNumber serialNumber = SerialNumberFinder.find(number);
    assertNotNull("SerialNumber should exist", serialNumber);
    assertEquals("Serial number " + number + " isMarkedForDeletion", false, serialNumber.isMarkedForDeletion());
    
    Truck truck = new TruckImpl(number);
    Module module1 = new ModuleImpl(truck, "85C","0");
    module1.setOrdered(false);
    BOM bom = new BOMImpl(module1, "129-C01248", "");
    new ComponentItemImpl(bom, "UUU-999         ", "WIDGET SEAL", Department.CHASSIS);

    Module module2 = new ModuleImpl(truck, "123", "0");
    module2.setOrdered(true);
    bom = new BOMImpl(module2, "129-C01248    ", "");
    new ComponentItemImpl(bom, "QQQ-999            ", "WIDGET SEAL", Department.BODY);

    Module module3 = new ModuleImpl(truck, "456", "0");
    module3.setOrdered(true);
    bom = new BOMImpl(module3, "G77099-456  ", "");
    bom.setType(ItemType.CWO);
    bom.setCwoIndicator("C");
    // BOM #2
    BOM bom2 = new BOMImpl(module3, "A01001-456   ", "");
    new ComponentItemImpl(bom2, "FFF-999", "WIDGET SEAL", Department.ELECTRICAL);
    
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(truck);
      session.saveOrUpdate(module1);
      session.saveOrUpdate(module2);
      session.saveOrUpdate(module3);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }
    
    SerialNumberService.delete(number);
    
    serialNumber = SerialNumberFinder.find(number);
    assertNotNull("Serial number " + number + " still exists after marked for deletion", serialNumber);
    assertTrue("Serial number " + number + " isMarkedForDeletion", serialNumber.isMarkedForDeletion());
    assertEquals("Serial number " + number + " isParsed", false, serialNumber.isParsed());
    assertEquals("Serial number " + number + " isTracked", false, serialNumber.isTracked());
    
    Module module1FromDB = null;
    Iterator moduleIt = ModuleFinder.find(number, module1.getNumber()).iterator();
    if (moduleIt.hasNext()) {
    	module1FromDB = (Module) moduleIt.next();
    }
    assertNotNull("Module " +  module1FromDB.getNumber() + " should exist after deletion", module1FromDB);
    assertEquals("Module " +  module1FromDB.getNumber() + " is ordered", false, module1FromDB.isOrdered());
    
    Module module2FromDB = null;
    Iterator moduleIt2 = ModuleFinder.find(number, module2.getNumber()).iterator();
    if (moduleIt2.hasNext()) {
    	module2FromDB = (Module) moduleIt2.next();
    }
    assertNotNull("Module " +  module2FromDB.getNumber() + " should exist after deletion", module2FromDB);
    assertEquals("Module " +  module2FromDB.getNumber() + " is ordered", false, module2FromDB.isOrdered());
   
    Module module3FromDB = null;
    Iterator moduleIt3 = ModuleFinder.find(number, module3.getNumber()).iterator();
    if (moduleIt3.hasNext()) {
    	module3FromDB = (Module) moduleIt3.next();
   
    }    
    assertNotNull("Module " + module3FromDB.getNumber() + " should exist after deletion", module3FromDB);
    assertEquals("Module " +  module3FromDB.getNumber() + " is ordered", false, module3FromDB.isOrdered());
    
    assertNotNull("Truck should exist after delete", TruckFinder.find(number));
    assertNotNull("Module should exist after delete", ModuleFinder.find(number, module1.getNumber()));
    assertNotNull("Module should exist after delete", ModuleFinder.find(number, module2.getNumber()));
    assertNotNull("Module should exist after delete", ModuleFinder.find(number, module2.getNumber()));
    // Could check the items and BOMS, too ...
	}
	
	public void testIsValidToAdd() throws Exception {
	  String number = "T77661";
    String description = "This is a description";
    String cmcsNumber = "GJFS";
    boolean isValid = SerialNumberService.isValidToAdd(number);
    assertTrue("Should be valid to add serial number " + number, isValid);

    number = "T77661";
    description = "This is a description";
    cmcsNumber = "GJFS";
    SerialNumberService.add(number, description, cmcsNumber);
    isValid = SerialNumberService.isValidToAdd(number);
    assertEquals("Should not be valid to add duplicate serial number " + number, false, isValid);

    number = "T7766";
    description = "This is a description";
    cmcsNumber = "GJFS";
    isValid = SerialNumberService.isValidToAdd(number);
    assertEquals("Should not be valid to add serial number with less than 6 digits" + number, false, isValid);

    number = "T776688";
    description = "This is a description";
    cmcsNumber = "GJFS";
    isValid = SerialNumberService.isValidToAdd(number);
    assertEquals("Should not be valid to add serial number with more than 6 digits" + number, false, isValid);

    number = "T77661";
    description = "This is a description";
    cmcsNumber = "GJFS";
    SerialNumber serialNumber = SerialNumberFinder.find(number);
    serialNumber.setMarkedForDeletion(true);
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.update(serialNumber);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }
    isValid = SerialNumberService.isValidToAdd(number);
    assertEquals("Should not be valid to add duplicate serial number marked for deletion" + number, false, isValid);
	}
	
	public void testGetReasonInvalidToAdd() throws Exception {
	  String number = "T77661";
    String description = "This is a description";
    String cmcsNumber = "GJFS";
    String reason = SerialNumberService.getReasonInvalidToAdd(number);
    assertEquals("Reason invalid", SerialNumberService.VALID, reason);

    number = "T77661";
    description = "This is a description";
    cmcsNumber = "GJFS";
    SerialNumberService.add(number, description, cmcsNumber);
    reason = SerialNumberService.getReasonInvalidToAdd(number);
    assertEquals("Reason invalid", SerialNumberService.DUPLICATE, reason);

    number = "T7766";
    description = "This is a description";
    cmcsNumber = "GJFS";
    reason = SerialNumberService.getReasonInvalidToAdd(number);
    assertEquals("Reason invalid", SerialNumberService.WRONG_LENGTH, reason);

    number = "T776688";
    description = "This is a description";
    cmcsNumber = "GJFS";
    reason = SerialNumberService.getReasonInvalidToAdd(number);
    assertEquals("Reason invalid", SerialNumberService.WRONG_LENGTH, reason);

    number = "T77661";
    description = "This is a description";
    cmcsNumber = "GJFS";
    SerialNumber serialNumber = SerialNumberFinder.find(number);
    serialNumber.setMarkedForDeletion(true);
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      tx = session.beginTransaction();
      session.update(serialNumber);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }
    reason = SerialNumberService.getReasonInvalidToAdd(number);
    assertEquals("Reason invalid", SerialNumberService.MARKED_FOR_DELETION, reason);
	}
}
