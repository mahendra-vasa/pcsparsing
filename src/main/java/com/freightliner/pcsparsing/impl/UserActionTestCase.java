package com.freightliner.pcsparsing.impl;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.UserActivity;
import com.freightliner.pcsparsing.service.*;
import com.freightliner.pcsparsing.service.Finder;


public class UserActionTestCase extends ModelTestCase {

  public UserActionTestCase(String name) {
    super(name);
  }
   
  public void testSave() throws Exception {
    String serialNumber ="OOP500";
    String user = "JFTL8V";
    UserActivity userActivity = UserActivity.ORDER;
    UserAction userAction = new UserActionImpl(serialNumber, user, userActivity); 
     
    Session session = null;
    Transaction tx = null;
    try {
      session = Finder.getSession();
      tx = session.beginTransaction();
      session.save(userAction);
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw e;
    } finally {
      Finder.close(session);
    }
    
    UserAction actionFromDB = UserActionFinder.find(serialNumber);
    assertNotNull("UserAction should not be null", actionFromDB);
    assertEquals("UserAction serial number", serialNumber, actionFromDB.getSerialNumber());
    assertEqualsTrimmed("UserAction user", user, actionFromDB.getUser());
    assertEquals("UserAction userActivity", userActivity, actionFromDB.getUserActivity());
    assertTrue("UserAction timestamp should be within 10 seconds of now", 
        System.currentTimeMillis() - actionFromDB.getTimestamp().getTime() < 10 * 1000);
  }

}
