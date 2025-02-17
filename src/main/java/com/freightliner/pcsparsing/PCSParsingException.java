package com.freightliner.pcsparsing;

import java.io.*;

import net.sf.hibernate.HibernateException;

/**
 * User expeception specific to this project.
 *
 * @author   jftl8v
 * 
 */
public class PCSParsingException extends Exception {
  private Exception e;

  public PCSParsingException(HibernateException e) {
    super(e.getMessage());
    this.e = e;
  }

  public PCSParsingException(String message) {
    super(message);
  }

  public PCSParsingException(String message, Exception e) {
    super(message);
    this.e = e;
  }

  public void printStackTrace() {
    super.printStackTrace();
  }

  public void printStackTrace(PrintStream s) {
  	if (e != null) {
      e.printStackTrace(s);
  	}
    super.printStackTrace(s);
  }

  public void printStackTrace(PrintWriter s) {
  	if (e != null) {
      e.printStackTrace(s);
  	}
    super.printStackTrace(s);   
  }
}
