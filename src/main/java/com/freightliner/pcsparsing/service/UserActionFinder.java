package com.freightliner.pcsparsing.service;

import java.sql.Timestamp;
import java.util.*;

import net.sf.hibernate.*;

import com.freightliner.pcsparsing.*;

/**
 * The class provide the finder service for user actions.
 * 
 * @author jftl8v
 * 
 */
public class UserActionFinder extends Finder {

 /**
  * Find the latest user actions associated with the serial number.
  * It is only used in test.
  * 
  * @param String serialNumber: the serial number
  * @return UserAction	the recent user action
  * 
  * @throws PCSParsingException
  */
  // TODO: It may not return the unique result?
  public static UserAction find(String serialNumber) throws PCSParsingException {
    UserAction userAction = null;
    Session session = null;
    try {
      session = getSession();
      Query query = session.createQuery(
        "from UserActionImpl userAction " +
        " where userAction.serialNumber = :serialNumber "
      );
      query.setString("serialNumber", serialNumber);
      userAction = (UserAction) query.uniqueResult();
    } catch (HibernateException e) {
      throw new PCSParsingException(e);
    } finally {
      close(session);
    }

    return userAction;
  }

 /**
  * Find the user's action on a specific serial number and of a specific type.
  * @param String serialNumber: the serial number
  * @param String user: the user
  * @param UserActivity activity: the user activity
  * @return UserAction		the user action
  * 
  * @throws PCSParsingException
  */
  public static UserAction find(String serialNumber, String user, UserActivity activity) throws PCSParsingException {
    UserAction userAction = null;
    Session session = null;
    try {
      session = getSession();
      Query query = session.createQuery(
        "from UserActionImpl userAction " +
        " where userAction.serialNumber = :serialNumber " +
        "   and userAction.user = :user " + 
        "   and userAction.userActivityCode = :userActivityCode"
      );
      query.setString("serialNumber", serialNumber);
      query.setString("user", user);
      query.setString("userActivityCode", activity.getActivityCode());
      userAction = (UserAction) query.uniqueResult();
    } catch (HibernateException e) {
      throw new PCSParsingException(e);
    } finally {
      close(session);
    }

    return userAction;
  }

 /**
  * Find the concurrent users working on the same serial number and of same activity.
  * @param String serialNumber: the serial number
  * @param String user: the user
  * @return List		a list of user actions
  * 
  * @throws PCSParsingException
  */
  public static List findConcurrentUsers(String serialNumber, String user) throws PCSParsingException {
    Session session = null;
    try {
      session = getSession();
      Query query = session.createQuery(
        "from UserActionImpl userAction " +
        " where userAction.serialNumber = :serialNumber " +
        "   and userAction.user <> :user " + 
        "   and userAction.userActivityCode = :userActivityCode" +
        "   and userAction.timestamp >= :cutoff" +
        "   and not exists(from UserActionImpl warning " +
        "      where warning.userActivityCode = :warningCode and warning.user = :user and warning.timestamp >= :cutoff)"
      );
      query.setString("serialNumber", serialNumber);
      query.setString("user", user);
      query.setString("userActivityCode", UserActivity.ORDER.getActivityCode());
      query.setString("warningCode", UserActivity.WARN.getActivityCode());
      Calendar calendar = GregorianCalendar.getInstance();
      calendar.add(Calendar.MINUTE, -30);
      query.setTimestamp("cutoff", new Timestamp(calendar.getTime().getTime()));
      return query.list();
    } catch (HibernateException e) {
      throw new PCSParsingException(e);
    } finally {
      close(session);
    }
  }

}
