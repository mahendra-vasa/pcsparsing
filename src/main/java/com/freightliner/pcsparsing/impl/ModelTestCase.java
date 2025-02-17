package com.freightliner.pcsparsing.impl;

import java.io.*;
import com.freightliner.pcsparsing.Module;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.naming.*;

import junit.framework.*;
import net.sf.hibernate.HibernateException;

import org.apache.commons.lang.*;
import org.apache.commons.logging.*;

import com.ibm.db2.jcc.DB2DataSource;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.util.PropertiesUtil;
import com.microsoft.jdbcx.sqlserver.SQLServerDataSource;

public abstract class ModelTestCase extends TestCase {

  public static String truncateSQL;
  private static final Log log = LogFactory.getLog(ModelTestCase.class);

  private static DB2DataSource db2ds;
  private static SQLServerDataSource pcsds;

  public ModelTestCase(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    //initializeDataSource();
    //truncate();
  }

  private void initializeDataSource() throws Exception {
    // @TODO: read all from config files
    if (pcsds == null || db2ds == null) {
      System.setProperty(Context.INITIAL_CONTEXT_FACTORY, PropertiesUtil.getProperty(PropertiesUtil.JNDI_CLASS));
      System.setProperty("org.osjava.jndi.shared", "true");
      Context ctx = new InitialContext();

      pcsds = new SQLServerDataSource();
      pcsds.setDatabaseName(PropertiesUtil.getProperty(PropertiesUtil.PCS_DB_NAME));
      pcsds.setUser(PropertiesUtil.getProperty(PropertiesUtil.PCS_DB_USER));
      pcsds.setPassword(PropertiesUtil.getProperty(PropertiesUtil.PCS_DB_PASSWORD));
      pcsds.setServerName(PropertiesUtil.getProperty(PropertiesUtil.PCS_DB_SERVER));
      pcsds.setSelectMethod("cursor");
      ctx.bind(PropertiesUtil.getProperty(PropertiesUtil.PCS_CONNECTION_DATASOURCE), pcsds);

      db2ds = new DB2DataSource();
      db2ds.setDatabaseName(PropertiesUtil.getProperty(PropertiesUtil.DB_NAME));
      db2ds.setUser(PropertiesUtil.getProperty(PropertiesUtil.DB_USER));
      db2ds.setPassword(PropertiesUtil.getProperty(PropertiesUtil.DB_PASSWORD));
      ctx.bind(PropertiesUtil.getProperty(PropertiesUtil.CONNECTION_DATASOURCE), db2ds);
    }
  }

  private void truncate() throws SQLException, IOException {
    if (truncateSQL == null) {
      InputStream input = null;
      try {
        File file = new File("build/classes/truncate.sql");
        if (file.exists()) {
          input = new FileInputStream(file);
        } else {
          input = getClass().getResourceAsStream("/truncate.sql");
        }
        if (input == null) {
          throw new RuntimeException("Could not find truncate.sql script");
        }
        StringBuffer buffer = new StringBuffer();
        int c;
        while ((c = input.read()) != -1) {
          buffer.append((char)c);
        }
        truncateSQL = buffer.toString();
      } finally {
        if (input != null) input.close();
      }
    }
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = db2ds.getConnection();
      log.debug("Running /truncate.sql in " + db2ds.getDatabaseName());
      stmt = conn.createStatement();
      // DB2 needs this as separate statements
      // SQL Server barfs on whitespace
      String[] sql = StringUtils.split(truncateSQL, ";");
      for (int i = 0; i < sql.length; i++) {
        String line = StringUtils.strip(sql[i]);
        if (line.indexOf(".PX") > 0) {
        	throw new RuntimeException("Suspicious line " + line + ". Will not truncate");
        }
        if (line != null && !line.equals("")) {
          line += ";";
          log.debug(line);
          stmt.addBatch(line);
        }
      }
      int[] results = stmt.executeBatch();
      if (log.isDebugEnabled()) {
        for (int i = 0; i < results.length; i++) {
          int result = results[i];
          if (result == -3) {
            System.out.println("EXECUTE_FAILED");
            System.out.println(stmt.getWarnings());
          } else if (result == -2) {
            System.out.println("SUCCESS_NO_INFO");
          } else {
            System.out.println(result);
          }
        }
        log.debug("Truncated");
      }
    } finally {
      if (conn != null) {
        conn.commit();
        conn.close();
      }
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  protected void addItem(Module module, Department department) throws HibernateException {
    String itemNumber = RandomStringUtils.randomAlphanumeric(25);
    ComponentItem item = new ComponentItemImpl(new BOMImpl(module, RandomStringUtils.randomAlphanumeric(25), "My BOM"), itemNumber, "SPROCKET", department);
    item.setEngRevisionLevel("P");
    item.setMfgRevisionLevel("X");
  }

  protected void assertEqualsTrimmed(String message, String expected, String actual) {
    if (expected != null) {
      expected = expected.trim();
    }
    if (actual != null) {
      actual = actual.trim();
    }
    assertEquals(message, expected, actual);
  }

  protected void assertEqualsToDay(String message, Date expectedDate, java.sql.Date actualDate) {
    if (expectedDate == null || actualDate == null) {
      return;
    }
    if (expectedDate == null && actualDate != null) {
      throw new AssertionFailedError(message + ". Actual date was null.");
    }
    if (expectedDate != null && actualDate == null) {
      throw new AssertionFailedError(message + ". Actual date was not null.");
    }
    Calendar expected = GregorianCalendar.getInstance();
    expected.setTime(expectedDate);
    Calendar actual = GregorianCalendar.getInstance();
    actual.setTime(actualDate);
    if (expected.get(Calendar.YEAR) != actual.get(Calendar.YEAR)) {
      throw new AssertionFailedError(message + ". Expected year <" + 
          expected.get(Calendar.YEAR) + "> but was <" + 
          actual.get(Calendar.YEAR) + ">");
    }
    if (expected.get(Calendar.MONTH) != actual.get(Calendar.MONTH)) {
      throw new AssertionFailedError(message + ". Expected month <" + 
          expected.get(Calendar.MONTH) + "> but was <" + 
          actual.get(Calendar.MONTH) + ">");
    }
    if (expected.get(Calendar.DATE) != actual.get(Calendar.DATE)) {
      throw new AssertionFailedError(message + ". Expected date <" + 
          expected.get(Calendar.DATE) + "> but was <" + 
          actual.get(Calendar.DATE) + ">");
    }
  }

  protected Date getDate(int year, int month, int date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.set(year, month, date);
    return calendar.getTime();
  }

}
