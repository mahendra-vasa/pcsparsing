package com.freightliner.pcsparsing.service;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;

import org.apache.commons.logging.*;

import com.freightliner.pcsparsing.PCSParsingException;
import java.io.File; 


/**
 * This class configure, find and close theHibernate sessions.
 * 
 * @author jftl8v
 */
public class Finder {

  private static SessionFactory sessionFactory;
  private static final Log log = LogFactory.getLog(Finder.class);
  private static int openSessions;
  private static int closedSessions;

 /**
  * Get the session factory.
  * 
  * @return SessionFactory	the session factory
  * @throws HibernateException
  */
  protected static synchronized SessionFactory getSessionFactory() throws HibernateException {
    if (sessionFactory == null) {
      System.setProperty(Environment.USE_REFLECTION_OPTIMIZER, "false");
      sessionFactory = new Configuration().configure(new File("/config/hibernate.cfg.xml")).buildSessionFactory();
    }
    return sessionFactory;
  }

  /**
   * Close session. Catch and log, but do NOT throw Exception.
   * 
   * @param Session session: the hibernate session
   */
  public static void close(Session session) {
    if (session != null && session.isOpen()) {
      try {
        session.close();
        if (log.isInfoEnabled()) {
          closedSessions++;
          log.debug("close() -- " + openSessions + "/" + closedSessions);
        }
     } catch (HibernateException e) {
        log.error("Could not end Hibernate session cleanly", e);
      } finally {
        session = null;
      }
    } else if (log.isWarnEnabled()) {
      if (session == null) {
        log.info("close() -- session is null");
      } else if (session.isOpen()) {
        log.info("close() -- session was not open");
      }
      log.debug("close() -- " + openSessions + "/" + closedSessions);
    }
  }

  /**
   * Get the session from the session factory.
   * 
   * @return New Hibernate Session, independent of shared static Session
   * @throws HibernateException
   */
  public static Session getSession() throws HibernateException {
    if (log.isInfoEnabled()) {
      openSessions++;
      log.debug("getSession() -- " + openSessions + "/" + closedSessions);
    }
    return getSessionFactory().openSession();
  }

  /**
   * Rollback transaction, if uncommitted transaction exists.
   * Log any exceptions, but do not throw
   * Close session on exceptions
   * 
   * @param Transaction tx
   * @param Session session
   */
  public static void attemptRollback(Transaction tx, Session session) {
    boolean canRollback = false;
    try {
      if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
        canRollback = true;
      }
    } catch (HibernateException e) {
      log.warn("Could not test transaction state before rollback attempt", e);
      close(session);
    }
    try {
      if (canRollback) {
        tx.rollback();
      }
    } catch (HibernateException e) {
      log.error("Could not rollback transaction", e);
      close(session);
    }
  }

 /**
  * Get the maximum tso sequence number of the same serial number.  
  * It means get the latest version.
  * 
  * @param String serialNumber: the serial number
  * @return String	the max tso sequence number
  * @throws PCSParsingException
  */
  static String getMaxTSOSequenceNumber(String serialNumber) throws PCSParsingException {
    Session session = null;
    try {
      session = getSession();
      Query query = session.createQuery("select max(truck.tsoSequenceNumber) from TruckImpl truck " +
      		"where truck.serialNumber = :serialNumber");
      query.setString("serialNumber", serialNumber);
      return (String) query.uniqueResult();
    } catch (HibernateException e) {
      throw new PCSParsingException(e);
    } finally {
      close(session);
    }
  }
  
}
