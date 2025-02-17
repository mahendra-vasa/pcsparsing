package com.freightliner.pcsparsing.service;

import java.io.File;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import net.sf.hibernate.cfg.Configuration;

import com.freightliner.pcsparsing.SerialNumber;
import com.freightliner.pcsparsing.impl.SerialNumberImpl;
import com.freightliner.pcsparsing.util.PropertiesUtil;

public class ParseServiceTestCase extends TestCase {
//public class ParseServiceTestCase {

  /*public ParseServiceTestCase(String name) {
    super(name);
  }*/
  
  protected void setUp() throws Exception {
	    super.setUp();
	    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, PropertiesUtil.getProperty(PropertiesUtil.JNDI_CLASS));
	      System.setProperty("org.osjava.jndi.shared", "true");
	      Context ctx = new InitialContext();
	    //initializeDataSource();
	    //truncate();
	  }
  
 
  
  
  
  //new
  public void testParse() throws Exception
  {
	  System.out.println("HB configuratio");
	  /*File f=new File(Servlet.getServletContext()
			  .getRealPath("/WEB-INF/classes/hibernate.cfg.xml"));

			  Configuration c = new Configuration().configure(f);*/

	    String serialNumber1 = "E06801";
	    String projectNo1 = "AAAA";
	   //Add truck, module, BOM, Item
	    System.out.println("1");
	    SerialNumber serialNumberImpl1 = new SerialNumberImpl(serialNumber1, "?? description \" \' foo", "PW1245");
	    System.out.println("2");
	    serialNumberImpl1.setParsed(true);
	    System.out.println("3");
	    serialNumberImpl1.setTracked(true);
	    System.out.println("4");
	    //serialNumberImpl1.setLastUpdated(getDate(2008, 10, 2));
	    serialNumberImpl1.setLastUpdated( new Date(2008, 10, 2));
	    System.out.println("5");
	    serialNumberImpl1.setCmcsNumber(projectNo1);
	    System.out.println("6");
	    serialNumberImpl1.setDescription("123456789 123456789 123456789");
	    System.out.println("7");
	    ParseThread parseThread = new ParseThread();	   
	    System.out.println("8");
	    parseThread.run();
	    
	    System.out.println("9");
	  
  }
  
  //end new
  
  public void testNoParse() {
	    ParseService.parse();
	  }
	  /* Data is there go ahead and parse the truck
	   * Assumption : Data is present there in the DB2 tables and the Job Number is present in the Job Table(Syteline/SqlServer) 
	   */
   
}
