
package com.freightliner.pcsparsing.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.freightliner.pcsparsing.Location;
import com.freightliner.pcsparsing.Module;
import com.freightliner.pcsparsing.PCSParsingException;
import com.freightliner.pcsparsing.ParseStatus;
import com.freightliner.pcsparsing.SerialNumber;
import com.freightliner.pcsparsing.Truck;
import com.freightliner.pcsparsing.impl.PCSParsingBusinessQueryImpl;
import com.freightliner.pcsparsing.impl.SerialNumberImpl;


/**
 * The class provide defines a parsing thread that does all the insertion and updating.
 * 
 * @author jftl8v
 * 
 */
public class ParseThread extends Thread {
  
  private static final Log log = LogFactory.getLog(ParseService.class);

  private int state = ParseStatus.IDLE;
  private Date completion;
  private int itemsParsed;
  private int locationsParsed;
  private int parsed;
  private int toParse;
  private Date start;
  private Exception lastException;
  private String detail;
  private String moduleParsed;

 /**
  * Get the parsing status.
  * @return ParseStatus	the parsing status
  */
  public synchronized ParseStatus getParseStatus() {
    ParseStatus parseStatus = new ParseStatus();
    parseStatus.setState(state);
    parseStatus.setError(lastException);
    parseStatus.setCompletion(completion);
    parseStatus.setItems(itemsParsed);
    parseStatus.setLocations(locationsParsed);
    parseStatus.setStart(start);
    parseStatus.setParsed(parsed);
    parseStatus.setToParse(toParse);
    parseStatus.setDetail(detail);
    parseStatus.setModules(getModuleNumber());
    //for Exception
    
    return parseStatus;
  }

 /**
  * Cancel parsing.
  */
  public synchronized void cancel() {
  	detail = "Cancelling";
    log.info("Cancel parsing thread");
    state = ParseStatus.STOPPING;
  }

 /**
  * Parsing thread run() method.
  */
  public synchronized void run() {
    state = ParseStatus.PARSING;
    start = new Date();
  	detail = "Starting";	
  	
  	
  	PCSParsingBusinessQueryImpl pcsParsingBusinessQueryImpl = null;    
    Connection connection = null;    
    try {
    	//List the selected project# to be parsed.
      List serialNumbers = SerialNumberFinder.findParsed();
      
      log.info("Parsing " + serialNumbers.size() + " serial numbers");
      log.info(serialNumbers+" the 1st serial number is :="+ ((SerialNumberImpl)(serialNumbers.get(0))).getCmcsNumber());
      toParse = serialNumbers.size();         
    
      /*
       * Implementation of NEW Functionality 
       */
       
      //A. get the connection
       connection = PCSService.getConnection();       
       connection.setAutoCommit(false);
       
       pcsParsingBusinessQueryImpl = new PCSParsingBusinessQueryImpl();
       
      //B. Start the Parsing Operation - Sequentially - One by one
      for (Iterator iter = serialNumbers.iterator(); iter.hasNext();) {
        //B1. Get the serial number for the selected Job Number
        
        SerialNumber serialNumber = (SerialNumber) iter.next();
        log.info("ParseThread:serial Number to PARSE is:="+serialNumber.getNumber()+"### JOB No:="+serialNumber.getCmcsNumber());
      
       //Step 1. get the serial Number List
       //probably this method can be discarded as we already have the serial number object
          //--> commented for the moment -- if reqd. un comment it to use it
          //SerialNumber serialNum = pcsParsingBusinessQueryImpl.getSelectedSerialNumberDetails(serialNumber.getCmcsNumber());       
        checkStatus();
        //Step 1. get the SPLT (Truck) table details passing the Serial number as an parameter         
    	Truck truck = pcsParsingBusinessQueryImpl.getTruckDetails(serialNumber.getNumber());
        
        checkStatus();
        //Step 2. Insert the Data into the Job table of Syteline
        log.info("ParseThread:STEP 2.Update THE JOB TABLE");
        boolean doesJobExist =  pcsParsingBusinessQueryImpl.insertJobDetails(truck, serialNumber, connection);

        
        if(doesJobExist == true)
        { //if the job exist in the Job table proceed futher otherwise raise an error and Exit i.e Send Email  
          
          //Step 3. Retrieve PCI(Component item) details with Group By clause for the insert operation into JobRoute Table
        		  
          checkStatus();
           List componentitemListGroupBy = pcsParsingBusinessQueryImpl.retrieveComponentItemDetailsGroupBy(serialNumber.getNumber());
          System.out.println("####Group By list Size:="+componentitemListGroupBy.size());
           if(null==componentitemListGroupBy)
           {
        	   log.info("ParseThread: NO Modules Selected for Parse !! ##Show Message on Screen ###### ");
           	   PCSParsingException parseException =  new PCSParsingException(
                       "NO Modules Selected to Parse for "
                       + serialNumber.getNumber()
                       + "."
                       );
           	log.error("Could not parse", parseException);
               lastException = parseException;
               
           	throw parseException;
           }//end if
        checkStatus();     
        //Step 4. Insert data into JobRouter using the Storeprocedure
        log.info("ParseThread:STEP 3. INSERTION INTO THE JOB ROUTE #####");
        pcsParsingBusinessQueryImpl.insertJobRouteDetails(componentitemListGroupBy,serialNumber.getCmcsNumber(),connection);
        
        //step 5. Update table jrt table                 
             //pcsParsingBusinessQueryImpl.updateJRTTableDetailsForSerialNumber(serialNumber.getNumber(), connection);
             
        //Step 6. Retrieve the list of PCI(Component item) for insert operation into ITEM table and Jobmatl table
        checkStatus();                 
        log.info("ParseThread:STEP 4. GETTING THE COMPONENTLIST  #####");
        List componentitemList =  pcsParsingBusinessQueryImpl.retrieveComponentItemDetails(serialNumber.getNumber());
        
        //Step 7. Insert data into the Item table
         checkStatus();
         log.info("ParseThread:STEP 5. INSERTION INTO THE ITEM TABLE #####");
         pcsParsingBusinessQueryImpl.updateItemDetails(componentitemList, connection);          
        
         //change:
         //take the backup of the Jobmatl table
         log.info("ParseThread:INSERTION INTO THE DTNA_JobMatl Last TABLE #####");
         // pcsParsingBusinessQueryImpl.insertJobmatlLast(serialNumber, connection);
         
         
         //Change:
         //step 6. insert data into the job Material table
         log.info("ParseThread:STEP 6. Updating THE updateJobMatlQtyForModules()  #####");
         // checkStatus();
         // pcsParsingBusinessQueryImpl.updateJobMatlQtyForModules(serialNumber,componentitemList,connection);
                
        //step 7. insert data into the job Material table
          checkStatus();
          log.info("ParseThread:STEP 7. INSERTION INTO TEMPORARY JOBMATL TABLE #####");
         pcsParsingBusinessQueryImpl.insertJobMaterialDetails(serialNumber,componentitemList,connection);   

         //#### This method     insertOrUpdateSpecQuantities() is not to be used ot called any more
          log.info("ParseThread:STEP 8. insertOrUpdateSpecQuantities #####");
          // insertOrUpdateSpecQuantities(serialNumber,connection,pcsParsingBusinessQueryImpl);
          
          connection.commit();
//        step 8. insert data into the job Material table
          checkStatus();
          log.info("ParseThread:STEP 8. MOVE TEMPORARY JOBMATL TABLE INTO PROD #####");

           pcsParsingBusinessQueryImpl.moveTempJobMaterialToProd(serialNumber,connection);   
          
          checkStatus();      
            log.info("ParseThread:STEP 9. updateModuleParsed #####");
            Collection moduleNumbers = ItemFinder.findModules(serialNumber);
            updateModuleParsed(moduleNumbers);          
          
           checkStatus(); 
                 //increment the parse counter
                 parsed++;
                 
         } // end of if(doesJobExist == true)
        else{        	
        	log.info("ParseThread: JOB NO DOESNT EXIST ##Show Message on Screen ###### ");
        	PCSParsingException parseException =  new PCSParsingException(
                    "Could not parse serial number "
                    + serialNumber.getNumber()
                    + ". JOB Number does not Exist. "
                    );
        	log.error("Could not parse", parseException);
            lastException = parseException;
            
        	throw parseException;
        }
       
        log.info("ParseThread:### ##################");
        log.info("ParseThread:## ALL OPERATIONS COMPLETED #####");
        log.info("ParseThread:##################");
          /*************End of operations ************/
         
        
     }//end of For loop
      
      //comitting the transaction
      connection.commit();  
   }catch (PCSParsingException e) {
    	log.error("Could not parse", e);
        lastException = e;
		// TODO: handle exception
        log.error("PCSPARSING Exception in Run :"+e);
    	 
        try{
        	  //roll back the transaction
        	if (null!=connection  && !connection.isClosed()) {
        	     connection.rollback(); 
        		}    	    
          }catch (SQLException sqle) {
    		 // TODO: handle exception
        	 log.error( "#### Parsing Error : Rollback : could not roll back the Parsing transaction ####");
    	    }
	}catch (Exception e) {
      log.error("Could not parse", e);
      lastException = e;
      try{
    	  //roll back the transaction
    	  if (null!=connection  && !connection.isClosed()) {
    		  connection.rollback();   	
            }    	    
      }catch (SQLException sqle) {
		// TODO: handle exception
    	 log.error( "#### Parsing Error : Rollback : could not roll back the Parsing transaction ####");
	    }
    } finally {
	  state = ParseStatus.IDLE;
      try {        
        if (connection != null && !connection.isClosed()) {
          connection.close();
        }
      } catch (SQLException e) {
        log.error("Could not close connection", e);
      }
    }
  	detail = "Done";
    completion = new Date();
  }
  /**
   * Update the modules been parsed. In case, some one has un-ordered it during parsing.
   * @param Collection moduleCollection: the modules
   * @throws PCSParsingException
   */
   void updateModuleParsed(Collection moduleCollection) throws PCSParsingException 
   {
   	  Iterator iter = moduleCollection.iterator();
   	  while (iter.hasNext()) {   		 
    		Module module = (Module) iter.next();
    		ModuleParsedUpdate.updateModuleParsed(module);   		
     	}   	
   }
 

 /**
  * Check the parsing status code.
  * @throws PCSParsingException
  */
  private void checkStatus() throws PCSParsingException {
    if (state == ParseStatus.STOPPING) {
      throw new PCSParsingException("Parse cancelled by user");
    }
  }

 
 /*
  * get the module being parsed
  */public String getModuleNumber(){
     this.moduleParsed = PCSParsingBusinessQueryImpl.getModuleNumber();
	  //PCSParsingBusinessQueryImpl.
     return moduleParsed;
  }
 

 /**
  * Insert/update the spec quantity table.
  * @param SerialNumber serialNumbr: the serial number
  * @throws PCSParsingException
  */
  void insertOrUpdateSpecQuantities(SerialNumber serialNumber,Connection conn,PCSParsingBusinessQueryImpl pcsParsingBusinessQueryImpl)
      throws PCSParsingException {

    checkStatus();
    detail = "Finding locations (make take several minutes)";
    log.info(detail);    
    Collection locations = LocationFinder.findOrdered(serialNumber);    
    detail = "Found " + locations.size() + "  locations for " + serialNumber.getNumber();
    log.info(detail);
    
    for (Iterator iter = locations.iterator(); iter.hasNext();) 
    {
      Location location = (Location) iter.next();
      int count = pcsParsingBusinessQueryImpl.getSpecQuantityCount
                       (location.getItemNumber().trim(),location.getLoc().trim(),
                    		   location.getItem().getEngRevisionLevel().trim(),conn);
      
      if (log.isDebugEnabled()) {
        log.debug("Location " + location.getItemNumber() + " "
            + location.getLoc() + " "
            + location.getItem().getEngRevisionLevel() + " count: " + count);
      }
      
      if (count == 1) {
    	  pcsParsingBusinessQueryImpl.updateSpecQuantity(location,conn);
      } else if (count == 0) {
    	  pcsParsingBusinessQueryImpl.insertSpecQuantity(location,conn);
      } else {
        throw new PCSParsingException(
            "Could not parse serial number "
                + serialNumber.getNumber()
                + ". Expected 0 or 1 spec quantities with serial number in PCS, but found: "
                + count);
      }
      locationsParsed++;
    }//end of FOR loop
    
   }//end of method

}
