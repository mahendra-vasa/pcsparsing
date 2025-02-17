package com.freightliner.pcsparsing.service;

import java.util.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.ModelTestCase;


public class UserActionServiceTestCase extends ModelTestCase {

  public UserActionServiceTestCase(String name) {
    super(name);
  }

  public void testAdd() throws Exception {
    String serialNumber ="117888";
    String user = "2123";
    UserActivity userActivity = UserActivity.WARN;

    UserActionService.add(serialNumber, user, userActivity);
        
    UserAction actionFromDB = UserActionFinder.find(serialNumber);
    assertNotNull("UserAction should be in database", actionFromDB);
    assertEquals("UserAction serial number", serialNumber, actionFromDB.getSerialNumber());
    assertEqualsTrimmed("UserAction user", user, actionFromDB.getUser());
    assertEquals("UserAction userActivity", userActivity, actionFromDB.getUserActivity());
    assertTrue("UserAction timestamp should be within 10 seconds of now", 
        System.currentTimeMillis() - actionFromDB.getTimestamp().getTime() < 10 * 1000);
    
  }
  
  public void testAddWithExisting() throws Exception {
    String serialNumber ="1212";
    String user = "3333";
    UserActivity userActivity = UserActivity.WARN;

    UserActionService.add(serialNumber, user, userActivity);
    // Just assert this works with no exception
    UserActionService.add(serialNumber, user, userActivity);
        
    UserAction actionFromDB = UserActionFinder.find(serialNumber);
    assertNotNull("UserAction should be in database", actionFromDB);
    assertEqualsTrimmed("UserAction serial number", serialNumber, actionFromDB.getSerialNumber());
    assertEqualsTrimmed("UserAction user", user, actionFromDB.getUser());
    assertEquals("UserAction userActivity", userActivity, actionFromDB.getUserActivity());
    assertTrue("UserAction timestamp should be within 10 seconds of now", 
        System.currentTimeMillis() - actionFromDB.getTimestamp().getTime() < 10 * 1000);
    
  }
  
  public void testGetConcurrentUsers() throws Exception {
    String serialNumber ="1212";
    String user = "3333";
    String user2 = "66666";
    String user3 = "9999";
    List concurrentUsers = UserActionFinder.findConcurrentUsers(serialNumber, user);
    assertTrue("Should have no concurrent users", concurrentUsers.isEmpty());
    
    UserActionService.recordOrder(serialNumber, user);
    concurrentUsers = UserActionFinder.findConcurrentUsers(serialNumber, user);
    UserActionService.recordWarning(serialNumber, user, concurrentUsers);
    assertTrue("Should have no concurrent users (same user)", concurrentUsers.isEmpty());
    
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.add(Calendar.MINUTE, -31);
    Date oldDate = calendar.getTime();
    UserActionService.add(serialNumber, user2, UserActivity.ORDER, oldDate);
    concurrentUsers = UserActionFinder.findConcurrentUsers(serialNumber, user);
    UserActionService.recordWarning(serialNumber, user, concurrentUsers);
    assertTrue("Should have no concurrent users (old date)", concurrentUsers.isEmpty());
    
    UserActionService.recordOrder(serialNumber, user2);
    UserActionService.recordOrder(serialNumber, user3);
    concurrentUsers = UserActionFinder.findConcurrentUsers(serialNumber, user);
    UserActionService.recordWarning(serialNumber, user, concurrentUsers);
    assertEquals("Concurrent users", 2, concurrentUsers.size());
    
    UserActionService.recordOrder(serialNumber, user);
    concurrentUsers = UserActionFinder.findConcurrentUsers(serialNumber, user);
    UserActionService.recordWarning(serialNumber, user, concurrentUsers);
    assertTrue("Should have no concurrent users (already warned)", concurrentUsers.isEmpty());
    
    UserActionService.add(serialNumber, user, UserActivity.DELETE);
    concurrentUsers = UserActionFinder.findConcurrentUsers(serialNumber, user);
    UserActionService.recordWarning(serialNumber, user, concurrentUsers);
    assertTrue("Should have no concurrent users (not an order action)", concurrentUsers.isEmpty());
    
    UserActionService.recordOrder(serialNumber, user3);
    UserActionService.recordOrder(serialNumber, user);
    calendar = GregorianCalendar.getInstance();
    calendar.add(Calendar.MINUTE, -31);
    oldDate = calendar.getTime();
    UserActionService.add(serialNumber, user, UserActivity.WARN, oldDate);
    concurrentUsers = UserActionFinder.findConcurrentUsers(serialNumber, user);
    assertEquals("Concurrent users", 2, concurrentUsers.size());
  }
  
  public void testRecordOrder() throws Exception {
    String serialNumber ="117888";
    String user = "2123";
    UserActivity userActivity = UserActivity.ORDER;

    UserActionService.recordOrder(serialNumber, user);
        
    UserAction actionFromDB = UserActionFinder.find(serialNumber);
    assertNotNull("UserAction should be in database", actionFromDB);
    assertEquals("UserAction serial number", serialNumber, actionFromDB.getSerialNumber());
    assertEqualsTrimmed("UserAction user", user, actionFromDB.getUser());
    assertEquals("UserAction userActivity", userActivity, actionFromDB.getUserActivity());
    assertTrue("UserAction timestamp should be within 10 seconds of now", 
        System.currentTimeMillis() - actionFromDB.getTimestamp().getTime() < 10 * 1000);
  }
  
  public void testRecordWarning() throws Exception {
    String serialNumber ="117888";
    String user = "2123";
    UserActivity userActivity = UserActivity.WARN;

    UserActionService.recordWarning(serialNumber, user, new ArrayList());
    UserAction actionFromDB = UserActionFinder.find(serialNumber);
    assertTrue("UserAction should not be in database", actionFromDB == null);

    List concurrentUsers = new ArrayList();
    concurrentUsers.add("user 1");
    UserActionService.recordWarning(serialNumber, user, concurrentUsers);
    actionFromDB = UserActionFinder.find(serialNumber);
    assertNotNull("UserAction should be in database", actionFromDB);
    assertEquals("UserAction serial number", serialNumber, actionFromDB.getSerialNumber());
    assertEqualsTrimmed("UserAction user", user, actionFromDB.getUser());
    assertEquals("UserAction userActivity", userActivity, actionFromDB.getUserActivity());
    assertTrue("UserAction timestamp should be within 10 seconds of now", 
        System.currentTimeMillis() - actionFromDB.getTimestamp().getTime() < 10 * 1000);
  }
}
