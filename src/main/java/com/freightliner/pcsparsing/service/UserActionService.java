package com.freightliner.pcsparsing.service;

import java.util.*;
import java.util.Date;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.UserActionImpl;

/**
 * The class provide the adding service for user action.
 * 
 * @author jftl8v
 * 
 */
public class UserActionService {

 /**
  * Add the user action if there's no such activity associated with a specific user.
  * If the record already exists, update the time stamp.
  * @param String serialNumber: the serial number
  * @param String user: the user
  * @param UserActivity activity: the user activity type
  * 
  * @throws PCSParsingException 
  */
  public static void add(String serialNumber, String user, UserActivity activity) throws PCSParsingException {
    UserAction userAction = UserActionFinder.find(serialNumber, user, activity);
    
    Session session = null;
    Transaction tx = null;
    try {
      session = Finder.getSession();
      tx = session.beginTransaction();
      if (userAction == null) {
        userAction = new UserActionImpl(serialNumber, user, activity);
        session.save(userAction);
      } else {
        userAction.setTimestamp(new Date());
        session.update(userAction);
     }
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw new PCSParsingException("Could not add new user action " + serialNumber + " " + user + " " + activity, e);
    } finally {
      Finder.close(session);
    }
  }

  /**
   * For testing only
   */
  static void add(String serialNumber, String user, UserActivity activity, Date timestamp) throws PCSParsingException {
    UserAction userAction = UserActionFinder.find(serialNumber, user, activity);
    
    Session session = null;
    Transaction tx = null;
    try {
      session = Finder.getSession();
      tx = session.beginTransaction();
      if (userAction == null) {
        userAction = new UserActionImpl(serialNumber, user, activity);
        userAction.setTimestamp(timestamp);
        session.save(userAction);
      } else {
        userAction.setTimestamp(timestamp);
        session.update(userAction);
     }
      tx.commit();
    } catch (Exception e) {
    	Finder.attemptRollback(tx, session);
    	throw new PCSParsingException("Could not add new user action " + serialNumber + " " + user + " " + activity, e);
    } finally {
      Finder.close(session);
    }
  }

 /**
  * Add "ORDER" activity to user action
  * @param String serialNumber: the serial number
  * @param String user: the user
  * 
  * @throws PCSParsingException 
  */
  public static void recordOrder(String serialNumber, String user) throws PCSParsingException {
    add(serialNumber, user, UserActivity.ORDER);
  }

 /**
  * Add "WARN" activity to the user if there are other users doing the same operation on the same serial number.
  *
  * @param String serialNumber: the serial number
  * @param String user: the user
  * @param List concurrentUsers: a list of concurrent users
  * 
  * @throws PCSParsingException 
  */
  public static void recordWarning(String serialNumber, String user, List concurrentUsers) throws PCSParsingException {
    if (concurrentUsers != null && !concurrentUsers.isEmpty()) {
      add(serialNumber, user, UserActivity.WARN);
    }
  }

}
