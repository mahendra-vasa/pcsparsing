package com.freightliner.pcsparsing.action;

import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * This class is the action form  on truck.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class TruckActionForm  extends ActionForm {
  private String stringMultibox[] = new String[0];

  private List serialNumbers;

  public List getSerialNumbers() {
    return serialNumbers;
  }

  public void setSerialNumbers(List serialNumbers) {
    this.serialNumbers = serialNumbers;
  }

  public String[] getStringMultibox() {
      return (this.stringMultibox);
  }

  public void setStringMultibox(String stringMultibox[]) {
      this.stringMultibox = stringMultibox;
  }

}
