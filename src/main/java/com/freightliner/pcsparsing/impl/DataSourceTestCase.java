package com.freightliner.pcsparsing.impl;

import java.sql.*;

import javax.naming.*;

import junit.framework.TestCase;
import com.ibm.db2.jcc.DB2DataSource;

import com.freightliner.pcsparsing.util.PropertiesUtil;

public class DataSourceTestCase extends TestCase {

  public DataSourceTestCase(String name) {
    super(name);
  }

  public void testCreate() throws Exception {
    DB2DataSource db2ds = new DB2DataSource();

    db2ds.setDatabaseName(PropertiesUtil.getProperty(PropertiesUtil.DB_NAME));
    db2ds.setUser(PropertiesUtil.getProperty(PropertiesUtil.DB_USER));
    db2ds.setPassword(PropertiesUtil.getProperty(PropertiesUtil.DB_PASSWORD));

    System.setProperty(Context.INITIAL_CONTEXT_FACTORY,PropertiesUtil.getProperty(PropertiesUtil.JNDI_CLASS));

    Context ctx = new InitialContext();
    ctx.rebind(PropertiesUtil.getProperty(PropertiesUtil.CONNECTION_DATASOURCE), db2ds);

    db2ds = (DB2DataSource) ctx.lookup(PropertiesUtil.getProperty(PropertiesUtil.CONNECTION_DATASOURCE));
    Connection conn = db2ds.getConnection();

    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM FIM.PXSPLT");
    ResultSet results = stmt.executeQuery();
    // Just check that no errors are thrown
    results.next();
  }
}
