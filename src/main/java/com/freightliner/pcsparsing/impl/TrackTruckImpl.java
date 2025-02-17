package com.freightliner.pcsparsing.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.freightliner.pcsparsing.PCSParsingException;
import com.freightliner.pcsparsing.SerialNumber;
import com.freightliner.pcsparsing.service.PCSService;
import com.freightliner.pcsparsing.service.SerialNumberService;
import com.freightliner.pcsparsing.util.ApplicationUtilities;
import com.freightliner.pcsparsing.util.SytelineQueryUtil;
import com.freightliner.pcsparsing.service.SerialNumberFinder;


public class TrackTruckImpl {
	
	static final Log log = LogFactory.getLog(PCSService.class);

	   /**
	    * Date Format
	    */
	  	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");

	    // All the persistent data fields


	  	private Date dateNeeded;
	  	
	  	//new var for the created TS
	  	private static Timestamp rawDate = null; 

	
	/**
	 * This method insert the data into the Job Table of SyteLine
	 * The data is fed into the Job table is from screen input.
	 * 
	 * @param truck
	 * @param serialNumber
	 */
	public boolean updateMTCreate(SerialNumber serialNum,Connection conn)throws PCSParsingException{
		
		 String methodName= "updateMTCreate()";
		 PreparedStatement pStat = null;
		 ResultSet rs = null;
		 StringBuffer strBuffer = null;
		 log.info("Entry into method ="+methodName);
		 int MTCreateOn  = 0; 
		 boolean updateComplete = false;
		 
	//	 System.out.println("###### TrackTruckImpl: INSIDE updateMTCreate() #####");
		 try{
			 /*1. Find MT_Create for the given Job number in the Job Table. If found -> go ahead and Update the
			  * data into the Syteline DB, IF(Data is not there for the Job No. inside Job table).  
			  */
			 
			 //###Find the MTCreate attribute			 
			 strBuffer = new StringBuffer();
			 strBuffer.append(SytelineQueryUtil.getQueryAppender());
			 strBuffer.append(" select Uf_MT_Create from job where job = ? and suffix='0'");
		// strBuffer.append("select Uf_MT_Create from job where job = ? and suffix='0' and stat='F'");			 
			 pStat = conn.prepareStatement(strBuffer.toString());
			 
		//	 System.out.println("##### updateMTCreate(): QUERY FORMED IS #####"+strBuffer.toString());
			 //CmcsNumber is the Job number
		//	 System.out.println("##### updateMTCreate():  job=" + get10DigJobNo(serialNum.getCmcsNumber().trim()));
			 pStat.setString(1,get10DigJobNo(serialNum.getCmcsNumber().trim()));//set the job number 
		//	 System.out.println("##### updateMTCreate(): GOING TO EXECUTE THE QUERY");
			 rs = pStat.executeQuery();
		//	 System.out.println("##### updateMTCreate():QUERY EXECUTED ##");
			 
			 while(rs.next()){
				 // The Job Number is present in the DB so, update the field against those Job numbers
		//		 System.out.println("##### updateMTCreate(): 1. INSIDE THE RESULT SET");
				 //clear the buffer to reuse it
				 strBuffer.delete(0, strBuffer.length());
				 MTCreateOn=(rs.getInt("Uf_MT_Create"));
		//		 System.out.println("##### updateMTCreate():  MT_Create (before)=" + MTCreateOn);				 
				 if (MTCreateOn == 1){
					 MTCreateOn = 0;
				 } 
				 else
				 {
					 MTCreateOn = 1;
				 }
		//		 System.out.println("##### updateMTCreate():  MT_Create (after)=" + MTCreateOn);
				 
		//		 System.out.println("##### updateMTCreate(): 2. strBuffer CLEANED ###"+strBuffer.toString());
				 strBuffer.append(SytelineQueryUtil.getQueryAppender());
				 strBuffer.append(" update  job set " +//table name
				 				  "Uf_MT_Create=?,"+   //N  
				 				  "Uf_Date_Last_Update=?"+ //PXTSN.ts_last_updt
				 				  " where job=? and suffix='0'" // give the job no. against which to update the record
				 				//  " where job=? and suffix='0' and stat='F'" // give the job no. against which to update the record
				 				 );
		//		 System.out.println("##### updateMTCreate(): 2ND PARAMETERIZED QUERY 2 BE EXECUTED ##"+strBuffer.toString());
				 
				 pStat = conn.prepareStatement(strBuffer.toString());
				 pStat.setInt(1,MTCreateOn); 
				 pStat.setTimestamp(2,new Timestamp(new Date().getTime()));
		//		 System.out.println("----^^^^MODI^^^"+new Timestamp(new Date().getTime()));
		//		 System.out.println("----^^^^ORGI^^^"+serialNum.getLastUpdated());
				 pStat.setString(3,get10DigJobNo(serialNum.getCmcsNumber().trim()));//set the job number

				 
				 //execute the query				 
				 int i = pStat.executeUpdate();
				 
				 if(i>0){
		//			 System.out.println("##### updateMTCreate():if(i>0):UPDATE IS SUCCESSFUL ");
					 log.info("Query Executed in "+methodName+" is Successful");
					 conn.commit();
					 updateComplete = true;
				 }
				
			 }
			 

	     }catch(SQLException se){
	  //  	 System.out.println("$$$$$ SQL Exception :="+se);
	    	  throw new PCSParsingException("TrackTruck -Could not toggle 'MTCreate for: "
	   	          , se);			 
		 } catch (Exception e) {
	 //		 System.out.println("@@@@@ TrackTruckImpl: INSIDE updateMTCreate() : EXCEPTION OCCURRED :"+e);
			 if (log.isDebugEnabled()) {
			      log.debug(strBuffer.toString());
			    }
		      throw new PCSParsingException("Could not execute SQL: " + e);
		      
		    } finally {
			      try {
			    	  strBuffer = null;
			    	  if (pStat != null){ 
			    		  pStat.close();
			    	  }	  
			    	  if(null!=rs){
			    		  rs.close();
			    	  }
			      } catch (SQLException e1) {
			    	  throw new PCSParsingException("Could not close connection", e1);
			      }
		    }
		    
		  log.info("Exit From Method ="+methodName);
		  return updateComplete;
	}

	/**
	 * This method gets data from the job table of SyteLine
	 * 
	 * @param serialNumber
	 */
	public static int getMTCreate(String number)throws PCSParsingException, SQLException{
 
// 	 System.out.println("####TrackTruckImpl - getMTCreate:  Input Serial ="+ number);
	 SerialNumber serialNum = SerialNumberFinder.find(number);

	 Connection connection = null;
	 String methodName= "getMTCreate()";
	 PreparedStatement pStat = null;
	 ResultSet rs = null;
	 StringBuffer strBuffer = null;
	 log.info("Entry into method ="+methodName);
	 int mtCreate = 0; 
	 
//	 System.out.println("###### TrackTruckImpl: INSIDE getMTCreate() #####");
	
	 try {

		connection = PCSService.getConnection();
//		System.out.println("#####Conn from the MSServer:="+connection);
		connection.setAutoCommit(false);

		 /*1. Find MT_Create for the given Job number in the Job Table. If found -> go ahead and Update the
		  * data into the Syteline DB, IF(Data is not there for the Job No. inside Job table).  
		  */
		 
		 //###Find the MTCreate attribute			 
		 strBuffer = new StringBuffer();
		 strBuffer.append(SytelineQueryUtil.getQueryAppender());
		 strBuffer.append(" select Uf_MT_Create from job where job = ? and suffix='0'");
			 
		 pStat = connection.prepareStatement(strBuffer.toString());
		 
//		 System.out.println("##### getMTCreate(): QUERY FORMED IS #####"+strBuffer.toString());
		 //CmcsNumber is the Job number
//		 System.out.println("##### getMTCreate():  job=" + get10DigJobNoStatic(serialNum.getCmcsNumber().trim()));
		 pStat.setString(1,get10DigJobNoStatic(serialNum.getCmcsNumber().trim()));//set the job number 
//		 System.out.println("##### getMTCreate(): GOING TO EXECUTE THE QUERY");
		 rs = pStat.executeQuery();
//		 System.out.println("##### getMTCreate():QUERY EXECUTED ##");
		 		 
		 if(rs.next()){
			 // The Job Number is present in the DB so, update the field against those Job numbers
//			 System.out.println("##### gettMTCreate(): 1. INSIDE THE RESULT SET");
			 //clear the buffer to reuse it
			 strBuffer.delete(0, strBuffer.length());
			 mtCreate=(rs.getInt("Uf_MT_Create"));
//			 System.out.println("##### getMTCreate():  MT_Create (before)=" + mtCreate);				 
			 }

	 	}catch(SQLException se){
//	 		System.out.println("$$$$$ SQL Exception :="+se);
	 		throw new SQLException("SQL - Could not get 'MTCreate for: " +
	 				 number);
	 	} catch (Exception e) {
//	 		System.out.println("$$$$$ non-SQL Exception :="+e);	 
	 		throw new PCSParsingException("Could not get 'MTCreate' for "
	 				+ number, e);
	 	} finally {
   
	 		try {        
	 			if (connection != null && !connection.isClosed()) {
	 				connection.close();
	 			}
	 		} catch (SQLException e) {
	 			log.error("Could not close connection", e);
	 		}
	 	}
	 	return mtCreate;
	}

	
	/**
	 * This method insert the data into the Job Table of SyteLine
	 * The data is fed into the Job table is from screen input.
	 * 
	 * @param truck
	 * @param serialNumber
	 */
	public boolean updateDateNeeded(String number, String date,Connection conn)throws PCSParsingException{

		 
		 String methodName= "updateDateNeeded()";
		 PreparedStatement pStat = null;
		 ResultSet rs = null;
		 StringBuffer strBuffer = null;
		 log.info("Entry into method ="+methodName);
		 boolean updateComplete = false;
		 Date formattedDateNeeded = null;
		 
		 
	//	 System.out.println("###### TrackTruckImpl: INSIDE updateDateNeeded() #####");
		 try{
			SerialNumber serialNum = SerialNumberFinder.find(number);
			
			 /*1. Check if the given Job number is present in the Job Table. If yes -> go ahead and Update the
			  * data into the Syteline DB, IF(Data is not there for the Job No. inside Job table). Raise an Exception 
			  * send a Email to the support team that the details are not present for this Job Number 
			  */

			 //###Find the Job Number and MTCreate Indicator			 
			 strBuffer = new StringBuffer();
			// strBuffer.append("select end_date from job_sch where job = ? and suffix='0' and stat='F'");
			 strBuffer.append(SytelineQueryUtil.getQueryAppender());
			 strBuffer.append(" Select job_sch.job, job_sch.end_date from job_sch, job where job_sch.job = ? and job_sch.suffix='0'"); 
			// 		" and job.job = job_sch.job and job.stat='F'");
			 
			 pStat = conn.prepareStatement(strBuffer.toString());
			 
	//		 System.out.println("##### updateDateNeeded(): QUERY FORMED IS #####"+strBuffer.toString());
			 //CmcsNumber is the Job number
			 pStat.setString(1, get10DigJobNo(serialNum.getCmcsNumber().trim()));
	//		 System.out.println("##### updateDateNeeded(): GOING TO EXECUTE THE QUERY");
			 rs = pStat.executeQuery();
	//		 System.out.println("##### updateDateNeeded():QUERY EXECUTED ##");
			 
			 if(rs.next()){
				 // The Job Number is present in the DB so, update the field against those Job numbers
	//			 System.out.println("##### updateDateNeeded(): 1. INSIDE THE RESULT SET");
				 //clear the buffer to reuse it
				 strBuffer.delete(0, strBuffer.length());
	//			 System.out.println("##### updateDateNeeded(): 2. strBuffer CLEANED ###"+strBuffer.toString());
				 dateNeeded=(rs.getTimestamp("end_date"));
				 
					
				if (date != null) {
					try
				       {
				        formattedDateNeeded = DATE_FORMAT.parse(date);            
	//			        System.out.println("Today = " + formattedDateNeeded);
				       } catch (Exception e){
	//			    	   System.out.println("@@@@@ TrackTruckImpl: Input Date Format Invalid: "+e);
						 if (log.isDebugEnabled()) {
						     log.debug(strBuffer.toString());
						  }
					      throw new PCSParsingException("Input Date Format Invalid : " + e);
				       }
				  }
				  
	//			 System.out.println("##### updateDateNeeded(): 5. formatted date from GUI    ###"+formattedDateNeeded);
				 strBuffer.append(SytelineQueryUtil.getQueryAppender());
				 strBuffer.append(" Update job_sch set " + //table name
				 				  "end_date=?"+   //N  
				 				  " where job=? and suffix='0' ");// give the job no. against which to update the record);
	//			 System.out.println("##### updateDateNeeded(): 2ND PARAMETERIZED QUERY 2 BE EXECUTED ##"+strBuffer.toString());
				 
				 pStat = conn.prepareStatement(strBuffer.toString());

				 pStat.setTimestamp(1,ApplicationUtilities.getCurrentJavaSqlTimestamp(formattedDateNeeded)); 
	//			 System.out.println("----^^^^ New Date ^^^"+ApplicationUtilities.getCurrentJavaSqlTimestamp(formattedDateNeeded));
	//			 System.out.println("----^^^^MODI^^^"+new Timestamp(new Date().getTime()));
	//			 System.out.println("----^^^^ORGI^^^"+serialNum.getLastUpdated());
				 pStat.setString(2,get10DigJobNo(serialNum.getCmcsNumber().trim()));//set the job number
				 
				 //execute the query				 
				 int i = pStat.executeUpdate();
				 
				 if(i>0){
	//				 System.out.println("##### updateDateNeeded():if(i>0):UPDATE IS SUCCESSFUL ");
					 log.info("Query Executed in "+methodName+" is Successful");
					 conn.commit();
					 updateComplete = true;					 
				 }
				
			 }

			 

	     }catch(SQLException se){
	//    	 System.out.println("$$$$$ SQL Exception :="+se);
		      throw new PCSParsingException("Could not execute SQL: " + se);
			 
		 } catch (Exception e) {
	//		 System.out.println("@@@@@ TrackTruckImpl: INSIDE updateDateNeeded() : EXCEPTION OCCURRED :"+e);
			 if (log.isDebugEnabled()) {
			      log.debug(strBuffer.toString());
			    }
		      
		    } finally {
			      try {
			    	  strBuffer = null;
			    	  if (pStat != null){ 
			    		  pStat.close();
			    	  }	  
			    	  if(null!=rs){
			    		  rs.close();
			    	  }
			      } catch (SQLException e1) {
			    	  throw new PCSParsingException("Could not close connection", e1);
			      }
		    }
		    
		  log.info("Exit From Method ="+methodName);
		  return updateComplete;
	}

	/**
	 * This method gets data from the job table of SyteLine
	 * 
	 * @param serialNumber
	 */
	public static String getDateNeeded(String number)throws PCSParsingException{
 
// 	 System.out.println("####TrackTruckImpl - getDateNeeded:  Input Serial ="+ number);
	 SerialNumber serialNum = SerialNumberFinder.find(number);

	 Connection connection = null;
	 String methodName= "getDateNeeded()";
	 PreparedStatement pStat = null;
	 ResultSet rs = null;
	 StringBuffer strBuffer = null;
	 log.info("Entry into method ="+methodName);
	 String formattedDateNeeded = null;
	 Date dateNeeded = null; 
	 
//	 System.out.println("###### TrackTruckImpl: INSIDE getDateNeeded() #####");
	
	 try {

		connection = PCSService.getConnection();
//		System.out.println("#####Conn from the MSServer:="+connection);
		connection.setAutoCommit(false);
	

		 /*1. Find MT_Create for the given Job number in the Job Table. If found -> go ahead and Update the
		  * data into the Syteline DB, IF(Data is not there for the Job No. inside Job table).  
		  */
		 
		 //###Find the dateNeeded attribute			 
		 strBuffer = new StringBuffer();
		 strBuffer.append(SytelineQueryUtil.getQueryAppender());
		 strBuffer.append(" select end_date from job_sch where job = ? and suffix='0'");
			 
		 pStat = connection.prepareStatement(strBuffer.toString());
		 
//		 System.out.println("##### getDateNeeded(): QUERY FORMED IS #####"+strBuffer.toString());
		 //CmcsNumber is the Job number
//		 System.out.println("##### getDateNeeded():  job=" + get10DigJobNoStatic(serialNum.getCmcsNumber().trim()));
		 pStat.setString(1,get10DigJobNoStatic(serialNum.getCmcsNumber().trim()));//set the job number 
//		 System.out.println("##### getDateNeeded(): GOING TO EXECUTE THE QUERY");
		 rs = pStat.executeQuery();
//		 System.out.println("##### getDateNeeded():QUERY EXECUTED ##");
		 
		 if(rs.next()){
			 // The Job Number is present in the DB so, update the field against those Job numbers
//			 System.out.println("##### getDateNeeded(): 1. INSIDE THE RESULT SET");
			 //clear the buffer to reuse it
			 strBuffer.delete(0, strBuffer.length());
			 dateNeeded = (rs.getDate("end_date"));

			if (dateNeeded != null) {
				try
			       {
			        SimpleDateFormat dateformatMMDDYY = new SimpleDateFormat("MM/dd/yy");

			        formattedDateNeeded = dateformatMMDDYY.format(dateNeeded);
	//		        System.out.println("Today = " + formattedDateNeeded);
			       } catch (Exception e){
			    	   System.out.println("@@@@@ TrackTruckImpl: Input Date Format Invalid: "+e);
					 if (log.isDebugEnabled()) {
					     log.debug(strBuffer.toString());
					  }
				      throw new PCSParsingException("Input Date Format Invalid : " + e);
			       }
			  }		
		 }
	//	 System.out.println("##### getDateNeeded():  dateNeeded= " + dateNeeded);
	//	 System.out.println("##### getDateNeeded():  formattedDateNeeded= " + formattedDateNeeded);
		 
		 if (formattedDateNeeded == null){
			 formattedDateNeeded = "";
		 } 


	 	}catch(SQLException se){
	// 		System.out.println("$$$$$ SQL Exception :="+se);
	 		throw new PCSParsingException("SQL - Could not get dateNeeded for: "
	 				, se);
	 	} catch (Exception e) {
	// 		System.out.println("$$$$$ non-SQL Exception :="+e);	 
	 		throw new PCSParsingException("Could not get dateNeeded for "
	 				+ number, e);
	 	} finally {
   
	 		try {        
	 			if (connection != null && !connection.isClosed()) {
	 				connection.close();
	 			}
	 		} catch (SQLException e) {
	 			log.error("Could not close connection", e);
	 		}
	 	}
	 	return formattedDateNeeded;
	}
	
	/*
	 * to get the embedded number
	 */
	public String get10DigJobNo(String jobNo){
		
		StringBuffer sb = null; 
		String spaceString="0000" ;
		try{
			sb =  new StringBuffer(jobNo);
			
			if (isInteger(sb.substring(2,3))){
				sb.insert(2, spaceString);		
			}
			else{
				sb.insert(3,spaceString);
			}							
						
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return sb.toString();
	}
	
	/*
	 * to get the embedded number
	 */
	public static String get10DigJobNoStatic(String jobNo){
		
		StringBuffer sb = null; 
		String spaceString="0000" ;
		try{
			sb =  new StringBuffer(jobNo);

			 if (isInteger(sb.substring(2,3))){
				sb.insert(2, spaceString);		
			}
			else{
				sb.insert(3,spaceString);
			}			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return sb.toString();
	}

	public static boolean isInteger ( String input ) 
	{
	   try
	   {
	      Integer.parseInt( input );
	      return true;
	   }
	   catch( Exception e)
	   {
	      return false;
	   }
	}
	  
}
