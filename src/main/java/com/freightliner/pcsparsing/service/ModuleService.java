package com.freightliner.pcsparsing.service;

import net.sf.hibernate.*;
import java.util.*;
import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.ModuleImpl;

/**
 * The class provide the update service for module.
 * 
 * @author jftl8v
 * 
 */
public class ModuleService {

 /**
  * Update the module order table and the user action table 
  * as the user check/uncheck the module order.
  * 
  * @param String serialNumber: the serial number
  * @param String moduleNumber: the module number
  * @param String user: the user
  * 
  * @throws PCSParsingException
  * 
  */
  public static void setOrdered(String serialNumber, String moduleNumber, String user)
      throws PCSParsingException {
    //Module module = ModuleFinder.find(serialNumber, moduleNumber);
	Module module = null;
	Iterator moduleIterator = ModuleFinder.find(serialNumber, moduleNumber).iterator();
	
    Session session = null;
    Transaction tx = null;
    try {
      session = ModuleFinder.getSession();
      
      while (moduleIterator.hasNext()) {
      	module = (Module) moduleIterator.next();
      	
      	tx = session.beginTransaction();
      	session.refresh(module);
      	boolean isOrdered = module.isOrdered();
      	if (isOrdered) {
        	session.delete(((ModuleImpl) module).getOrder());
        	module.setOrdered(false);
        //@TODO: hide delete details
      	} else {
        	module.setOrdered(true);
      	}
      	session.update(module);
      	tx.commit();
      }
    } catch (Exception e) {
      Finder.attemptRollback(tx, session);
      throw new PCSParsingException("Could not set ordered status for "
          + serialNumber + " " + moduleNumber, e);
    } finally {
      ModuleFinder.close(session);
    }
    UserActionService.recordOrder(serialNumber, user);
  }
  
}
