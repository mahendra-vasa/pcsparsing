package com.freightliner.pcsparsing.service;

import java.util.*;

import net.sf.hibernate.*;

import org.apache.commons.logging.*;

/**
 * This class give a backup checking on the table representing moduleOrder.
 * It will update the "order" status in the moduleOrder table after parsing.
 *
 * @author JFTL227
 * 
 */
import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.ModuleOrderImpl;
import com.freightliner.pcsparsing.impl.ModuleImpl;
import com.freightliner.pcsparsing.impl.ModulePK;

public class ModuleParsedUpdate extends Finder {
  
  private static final Log log = LogFactory.getLog(Finder.class);
  
 /**
  * Update the module as "ordered" if the module has been parsed.
  * @param Module module: the module
  * @throws PCSparsingException
  * 
  */
  public static void updateModuleParsed(Module module) 
  		throws PCSParsingException {
  	
  	Session session = null;
  	Transaction tx = null;  	   
    
    try {
      session = getSession();
      boolean isOrdered = module.isOrdered();
      if (!isOrdered) {
      	log.warn("Module" + module.getNumber() + " not ordered for Truck" + module.getTruck().getSerialNumber()); 
      	
      	tx = session.beginTransaction();       
        module.setOrdered(true);
        session.update(module);
        tx.commit();
      }     
      
  	} catch (Exception e) {
      Finder.attemptRollback(tx, session);
      throw new PCSParsingException("Could not set parsed status for "
          + module.getTruck().getSerialNumber() + " " + module.getNumber(), e);
    } finally {
      close(session);
    }
  }
	
}