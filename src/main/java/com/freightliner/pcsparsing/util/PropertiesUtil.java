package com.freightliner.pcsparsing.util;

import java.io.*;
import java.util.Properties;

import com.freightliner.pcsparsing.PCSParsingException;

/**
 * This class loads the property file. 
 *
 * @author   jftl8v
 *
 */
public class PropertiesUtil {

  // All the property names
  public static final String DB_NAME = "db.name";
  private static final String PROPERTIES_FILE_PATH = "/px.properties";
  private static Properties props;
  public static final String DB_PASSWORD = "db.password";
  public static final String DB_USER = "db.user";
  public static final String JNDI_CLASS = "jndi.class";
  public static final String CONNECTION_DATASOURCE = "connection.datasource";
  public static final String PCS_DB_NAME = "pcs.db.name";
  public static final String PCS_DB_USER = "pcs.db.user";
  public static final String PCS_DB_PASSWORD = "pcs.db.password";
  public static final String PCS_DB_SERVER = "pcs.db.server";
  public static final String PCS_CONNECTION_DATASOURCE = "pcs.connection.datasource";
  
//entry of syteline Db
  public static final String PCS_CONNECTION_DATASOURCE_MSSQlSYTELINEDB = "pcs.connection.datasource.mssqlserver";  
  
  
 
 /**
  * Get a specific property.
  * @param String property: the property name
  * @return String	the property value.
  * 
  * @throws PCSParsingException
  * 
  */
  public static String getProperty(String property) throws PCSParsingException {
    initProps();
    return props.getProperty(property);
  }

 /**
  * Load the property file.
  *
  * @throws PCSParsingException
  */ 
  private static void initProps() throws PCSParsingException {
    if (props == null) {
      props = new Properties();
      InputStream inStream;
      try {
        inStream = PropertiesUtil.class.getResourceAsStream(PROPERTIES_FILE_PATH);
        if (inStream == null) {
          throw new RuntimeException("Could not find " + PROPERTIES_FILE_PATH);
        }
        props.load(inStream);
      } catch (IOException e) {
        throw new PCSParsingException("Could not load " + PROPERTIES_FILE_PATH, e);
      }
    }
  }

}
