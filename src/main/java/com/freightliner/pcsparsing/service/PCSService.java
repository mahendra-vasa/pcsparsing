package com.freightliner.pcsparsing.service;

import java.sql.*;

import javax.naming.*;
import javax.sql.DataSource;

import org.apache.commons.logging.*;

import com.freightliner.pcsparsing.PCSParsingException;
import com.freightliner.pcsparsing.util.PropertiesUtil;

/**
 * The class provide basic DB services, such as open/close connection, execute query
 * and get count.
 * 
 * @author jftl8v
 * 
 */
public class PCSService {
  
  static final Log log = LogFactory.getLog(PCSService.class);
  static DataSource dataSource;

 /**
  * Execute query.
  * @param String query: the SQLK query
  * 
  * @throws PCSParsingException
  * 
  */
  public static void executeSQL(String sql) throws PCSParsingException {
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = getConnection();
      stmt = conn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      throw new PCSParsingException("Could not execute SQL: " + sql, e);
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null && !conn.isClosed()) {
          conn.close();
        }
      } catch (SQLException e1) {
        throw new PCSParsingException("Could not close connection", e1);
      }
    }
  }

 /**
  * Get count.
  * @param String query: the SQLK query
  * 
  * @throws PCSParsingException
  */
  public static int getCount(String sql) throws PCSParsingException {
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = getConnection();
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return rs.getInt(1);
      } else {
        throw new PCSParsingException("Could not get count: ResultSet.next() was false. " + sql);
      }
    } catch (Exception e) {
      throw new PCSParsingException("Could not get count: " + sql, e);
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null && !conn.isClosed()) {
          conn.close();
        }
      } catch (SQLException e1) {
        throw new PCSParsingException("Could not close connection", e1);
      }
    }
  }

 /**
  * Connect to a data source through jndi
  * 
  * @throws NamingException
  * @throws SQLException
  * @throws PCSParsingException
  */
  public static Connection getConnection() throws NamingException, SQLException, PCSParsingException {
  	if (dataSource == null) {
	    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, PropertiesUtil.getProperty(PropertiesUtil.JNDI_CLASS));
	    System.setProperty("org.osjava.jndi.shared", "true");
	    Context ctx = new InitialContext();
	    //dataSource = (DataSource) ctx.lookup(PropertiesUtil.getProperty(PropertiesUtil.PCS_CONNECTION_DATASOURCE));
 
	    dataSource = (DataSource) ctx.lookup(PropertiesUtil.getProperty(PropertiesUtil.PCS_CONNECTION_DATASOURCE_MSSQlSYTELINEDB));
	    System.out.println("########Inside the DATASOURCE:="+dataSource);
	   
  	}
    return dataSource.getConnection();
  }

 /**
  * Close DB connection and statement.
  * @param Connection connection: the DB connection
  * @param Statement stmt: the statement.
  */
  public static void close(Connection connection, Statement stmt) {
    try {
	    if (stmt != null) {
	      stmt.close();
	    }
	    if (connection != null && !connection.isClosed()) {
	      connection.close();
	    }
    } catch (Exception e) {
      log.error("Could not close connection and/or statement", e);
    }
  }

}
