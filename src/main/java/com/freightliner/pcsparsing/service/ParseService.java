package com.freightliner.pcsparsing.service;


import com.freightliner.pcsparsing.PCSParsingException;
import com.freightliner.pcsparsing.ParseStatus;





/**
 * The class provide the main thread the services including starting parsing, checking the status of the parsing thread,
 * cancelling parsing and quitting the parsing thread during parsing
 * 
 * @author jftl8v
 * 
 */
public class ParseService {

  private static ParseThread parseThread;
  private static ParseStatus parseStatus = new ParseStatus();  
  public static final String AUTO_PROJECT_NAME = "AUTO-GENERATED DURING PARSE";
  
 /**
  * Start the parsing thread.
  */
  public static synchronized void parse() {
    if (getParseStatus().getState() != ParseStatus.IDLE) {    	
      return;
    }
    
    parseThread = new ParseThread();
    parseThread.setDaemon(true);   
    parseThread.start();    
    
  }

 /**
  * Get thestatus of the parsing thread.
  * @return ParseStatus	the status of parsing
  */
  public static synchronized ParseStatus getParseStatus() {
    if (parseThread != null) {
      parseStatus = parseThread.getParseStatus();
    }
    return parseStatus;
  }

 /**
  * Cancell parsing.
  */
  public static synchronized void cancel() {
    if (parseThread != null) {
      parseStatus = parseThread.getParseStatus();
      if (parseStatus.getState() == ParseStatus.PARSING) {
        parseThread.cancel();
      }
    }
  }

 /**
  * Quit the parsing thread.
  */
  public static void forceQuit() {
    if (parseThread != null) {
      parseThread.interrupt();
    }
    ParseStatus newParseStatus = new ParseStatus();
    newParseStatus.setState(ParseStatus.IDLE);
    newParseStatus.setError(new PCSParsingException("User forced parsing to quit"));
  }

   

}
