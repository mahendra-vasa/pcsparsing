package com.freightliner.pcsparsing;

import java.util.Date;

/**
 * This class provides parse status and access methods.
 *
 * @author   jftl8v
 * 
 */
public class ParseStatus {
  

  // Three Status
  public static final int IDLE = 0; 
  public static final int PARSING = 1; 
  public static final int STOPPING = 2; 
  
 /**
  * Parsing completion date and time
  */
  private Date completion;
  private Exception error;
  
 /**
  * Number of items
  */
  private int items;
  
  private String modules="";
  
 /**
  * Number of locations
  */
  private int locations;
  
  
 /**
  * Number been parsed
  */
  private int parsed;
  
 /**
  * Actual state
  */
  private int state;
  
 /**
  * Number going to be parsed
  */
  private int toParse;
  
 /**
  * parsing starting date and time
  */
  private Date start;
  
 /**
  * Parsing detail
  */
  private String detail;

 /**
  * Get the parsing completion date and time.
  * @return Date	the parsing completion date
  */
  public Date getCompletion() {
    return completion;
  }
  
 /**
  * Set the completion date and time.
  * @param Date completion: the parsing completion date 
  */
  public void setCompletion(Date completion) {
    this.completion = completion;
  }
  
 /**
  * Get the number of items
  * @return int	the number of items
  */
  public int getItems() {
    return items;
  }
  
  public String getModules(){
	  return modules;
  }
  public void setModules(String module){
	  this.modules= module;
  }
 /**
  * Set the number of items.
  * @param int items: the number of items
  */
  public void setItems(int items) {
    this.items = items;
  }

 /**
  * Get the number of parsed.
  * @return int	 the number of parsed
  */
  public int getParsed() {
    return parsed;
  }
  
 /**
  * Set the number of parsed.
  * @param int parsed: the number of parsed
  */
  public void setParsed(int parsed) {
    this.parsed = parsed;
  }
  
 /**
  * Get the current state
  * @return int	the current state
  */
  public int getState() {
    return state;
  }
  
 /**
  * Set the current state.
  * @param int state: an integer indicating the state
  */
  public void setState(int state) {
    this.state = state;
  }
  
 /**
  * Get parsing start date and time.
  * @return Date	the starting date
  */
  public Date getStart() {
    return start;
  }
  
 /**
  * Set parsing start date and time.
  * @param Date start: the date parsing started
  */
  public void setStart(Date start) {
    this.start = start;
  }
  
 /**
  * Get the number of going to be parsed.
  * @return int	the number of going to be parsed	
  */
  public int getToParse() {
    return toParse;
  }
 
 /**
  * Set the number of going to be parsed.
  * @param int toParse: the number of going to be parsed
  */
  public void setToParse(int toParse) {
    this.toParse = toParse;
  }
  
 /**
  * Get the error.
  * @return Exception
  */
  public Exception getError() {
    return error;
  }
  
 /**
  * Set the error.
  * @param Exception error
  */
  public void setError(Exception error) {
    this.error = error;
  }
    
   /**
    * Get the parsing details.
    * @return String	the parsing detail
    */
	public String getDetail() {
		return detail;
	}
	
   /**
    * Set the parsing details.
    * @param String detail: the parsing details
    */
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
   /**
    * Get a very detailed message.
    * @return String	a detailed message
    */
	public String getMessage() {
		String msg = "";
		if (getDetail() != null && getDetail().length() > 0) {
			msg = msg + getDetail() + ". ";
		}
		//msg = msg + "Parsed " + items + " items, " + locations + " locations";
		msg = msg + "Module " + modules ;
		return msg;
	}
	
   /**
    * Set the number of locations have been parsed.
    * @param int locationsParsed: the number of locations
    */
	public void setLocations(int locationsParsed) {
		this.locations = locationsParsed;
	}

}
