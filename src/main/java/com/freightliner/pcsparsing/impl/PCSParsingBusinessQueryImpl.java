package com.freightliner.pcsparsing.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.freightliner.pcsparsing.DepartmentSearchCriteria;
import com.freightliner.pcsparsing.HasCWOSearchCriteria;
import com.freightliner.pcsparsing.IsOrderedSearchCriteria;
import com.freightliner.pcsparsing.Location;
import com.freightliner.pcsparsing.Module;
import com.freightliner.pcsparsing.PCSParsingException;
import com.freightliner.pcsparsing.SerialNumber;
import com.freightliner.pcsparsing.Truck;
import com.freightliner.pcsparsing.service.Finder;
import com.freightliner.pcsparsing.service.ItemFinder;
import com.freightliner.pcsparsing.service.ModuleFinder;
import com.freightliner.pcsparsing.service.PCSService;
import com.freightliner.pcsparsing.service.SerialNumberFinder;
import com.freightliner.pcsparsing.util.ApplicationConstants;
import com.freightliner.pcsparsing.util.ApplicationUtilities;
import com.freightliner.pcsparsing.util.SytelineQueryUtil;


/**
 * This class Implements the DB operation funcation. All the DB operation would be done over here for the Parsing 
 * e.g : Insert,Update and select etc.
 * @author JF06201
 *
 */
public class PCSParsingBusinessQueryImpl {
	
	
	static final Log log = LogFactory.getLog(PCSService.class);
	private static String moduleNumber ="";
	/**
	 * This method is going to fetch the details for the 
	 * specific Job No(project# - on Screen.
	 * The serialNumber class correspondence to PXTSN table in Db
	 * @param String jobNo - the jobno to search for
	 * @return Serial Number - serial number object
	 * 
	 * @throws PCSParsingException
	 */
	// Reminder : - this method might not be needed and called up check in parsethread class
	public SerialNumber getSelectedSerialNumberDetails(String jobNo) throws PCSParsingException {
		
		SerialNumber serialNumber = null;
		//Hibernate session object
		Session session = null;
		
		String methodName= "getSelectedSerialNumberDetails()";
		
		log.info("Entry into method ="+methodName);
		
		try{
			//get the ref. to the session object
			session = Finder.getSession();
			
			 Query query = session.createQuery("from SerialNumberImpl sn where sn.cmcsNumber = :jobNumber");			 
		           query.setString("jobNumber", jobNo);
		           serialNumber = (SerialNumber) query.uniqueResult();
		        
		      } catch (HibernateException e) {
		    	  //TODO: handle exception
		        throw new PCSParsingException(e);
		        
		      } finally {
		    	  Finder.close(session);
		      }
		      log.info("Exit From Method ="+methodName);
		return serialNumber;
	}
	//end of method getSelectedSerialNumberDetails()
	
	
	/**
	 * 
	 */
	
	public Truck getTruckDetails(String serialNumber) throws PCSParsingException {
		
		//Hibernate session object
		Session session = null;

		 Truck truck =  null;
		 
		 String methodName= "getTruckDetails()";			
		 log.info("Entry into method ="+methodName);
		try{
			//get the ref. to the session object
			session = Finder.getSession();			
			String sql = "from TruckImpl truck where truck.serialNumber = '"+serialNumber+"' "+
	 					 "and truck.createdTimeStamp = ( select max(truck.createdTimeStamp) from TruckImpl truck where truck.serialNumber = '"+serialNumber+"')";
			 
			//this query will return one record
			Query query = session.createQuery(sql);
			
			truck = (Truck)query.uniqueResult();
			 
		} catch (HibernateException e) {
	    	  //TODO: handle exception
			log.error("PCSParsingBusinessQueryImpl:getTruckDetails():"+e);
	        throw new PCSParsingException(e);	        
	    } catch (Exception pe) {
 	    	   //TODO: handle exception
	    	  log.error("PCSParsingBusinessQueryImpl:getTruckDetails():"+pe);
		       throw new PCSParsingException("Serial Number Not Found for Truck,Please check the entry in Database",pe);
		        
		}finally{
				  Finder.close(session);
	            }
	      
	      log.info("Exit From Method ="+methodName);
	      //return the truck Object
		return truck;
		
	}//end of getTruckDetails()
	
	/**
	 * This method insert the data into the Job Table of SiteLine
	 * The data is feeded into the Job table is from two tables of DB2
	 *  a.PXTSN(SerialNumber Class), b. PXSPLT (Truck Class) 
	 * 
	 * @param truck
	 * @param serialNumber
	 */
	public boolean insertJobDetails(Truck truck,SerialNumber serialNum,Connection conn)throws PCSParsingException{
		
		 String methodName= "insertJobDetails()";
		 PreparedStatement pStat = null;
		 ResultSet rs = null;
		 StringBuffer strBuffer = null;
		 log.info("Entry into method ="+methodName);
		 boolean doesJobExist = false; 
		 int explodeCode = 0;
		 
		 try{
			 /*1. Check if the given Job number is present in the Job Table. If yes -> go ahead and Update the
			  * data into the Syteline DB, IF(Data is not there for the Job No. inside Job table). Raise an Exception 
			  * send a Email to the support team that the details are not present for this Job Number 
			  */
			 
			 //###Check if the Job Number is present			 
			 strBuffer = new StringBuffer();
			 strBuffer.append(SytelineQueryUtil.getQueryAppender());
			 strBuffer.append("Select job from job where job = ?");			 
			 pStat = conn.prepareStatement(strBuffer.toString());
			 
			 //CmcsNumber is the Job number
			 pStat.setString(1, get10DigJobNo(serialNum.getCmcsNumber().trim()));
			 
			 rs = pStat.executeQuery();
			 
			 while(rs.next()){
				 // The Job Number is present in the DB so, update the field against those Job numbers
				 
				 //clear the buffer to reuse it
				 strBuffer.delete(0, strBuffer.length());
				 strBuffer.append(SytelineQueryUtil.getQueryAppender());
				 strBuffer.append(" Update  job set " +//table name
				 				  "Uf_Spec_Explode_Date=?,"+  //Current date & time
				 				  "Uf_MT_Create=?,"+   //N  
				 				  "Uf_Tracked=?,"+     //PXTSN.expld_indc
				 				  "Uf_Customer_Name=?,"+ //PXSPLT.cust_name_abbr
				 				  "Uf_Build_Location=?,"+ //PXSPLT.loc
				 				  "Uf_CWO_Qty=?,"+ //PXSPLT.cwo_cnt
				 				  "Uf_Date_Last_Update=?"+ //PXTSN.ts_last_updt
				 				  " where job=?" // give the job no. against which to update the record				 				  
				 				 );
				 log.info("PCSParsingBusinessQueryImpl:insertJobDetails():2ND PARAMETERIZED QUERY 2 BE EXECUTED ##"+strBuffer.toString());
				 
				 pStat = conn.prepareStatement(strBuffer.toString());
				 pStat.setTimestamp(1,ApplicationUtilities.getCurrentJavaSqlTimestamp(null));//set the timestamp
				 pStat.setInt(2,ApplicationConstants.Const_ZERO);
				
				 if((serialNum.getExplodeCode()).trim().equalsIgnoreCase("Y")){
					 explodeCode = 1;
				 }else if((serialNum.getExplodeCode()).trim().equalsIgnoreCase("")){
					 explodeCode = 0;
				 }
				 pStat.setInt(3,explodeCode);
				 pStat.setString(4,truck.getCustomerName() );
				 pStat.setString(5, truck.getBuildLocation());//contains the location
				 pStat.setInt(6, truck.getCwoCount());
				 pStat.setTimestamp(7,ApplicationUtilities.getCurrentJavaSqlTimestamp(serialNum.getLastUpdated()));				
				 pStat.setString(8,get10DigJobNo(serialNum.getCmcsNumber().trim()));//set the job number
				 
				 //execute the query				 
				 int i = pStat.executeUpdate();
				 
				 if(i>0){
					 log.info("PCSParsingBusinessQueryImpl:Query Executed in "+methodName+" is Successful");
				 }
				
				 doesJobExist = true;
			 }
			 
		 } catch (Exception e) {
			 
			 if (log.isDebugEnabled()) {
			      log.debug(strBuffer.toString());
			    }
			  log.error("PCSParsingBusinessQueryImpl:insertJobDetails():"+e);
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
				    	  log.error("PCSParsingBusinessQueryImpl:insertJobDetails():"+e1);
				    	  throw new PCSParsingException("Could not close connection", e1);
				      }
		    }
		    
		  log.info("Exit From Method ="+methodName);
		  return doesJobExist;
	}
	
	/**
	 * This item retrieves the details from the PXPCI table to add the data into the item table
	 * @param serialNumber
	 * @return
	 */
	
	public List retrieveComponentItemDetailsGroupBy(String serialNumber)throws PCSParsingException{
	
		 List componentitemList      = null;		
		 ComponentImpl componentImpl = null;
	
		 String methodName= "retrieveComponentItemDetailsGroupBy()";		
		 //Hibernate session object
		 Session session = null;
		 List componentImplList = null;
			
		log.info("Entry into method ="+methodName);
	
		try{			
			//get the session
			session = Finder.getSession();
			
			//get the ordered module numbers
			String inClauseString = getOrderedModules(serialNumber);
			
			if(!((inClauseString.trim()).equalsIgnoreCase(""))){
			
			 componentImplList =  session.find("select item.tsoSplitNumber," +
	    	 		"item.moduleNumber,item.itemNumber from ComponentImpl item "+
	    	 		"where item.tsoSplitNumber = '"+serialNumber+"' and item.moduleNumber in ("+inClauseString +") and " +
	    	 		"item.itemType in ('A','P','M') and "+
		    	 	"item.compItemNo not like '13-130%' "+
	    	 		"group by item.tsoSplitNumber," +
	    	 		"item.moduleNumber,item.itemNumber order by item.moduleNumber asc"); 
			
			componentitemList = new ArrayList();
			
			for(int i=0 ;i<componentImplList.size();i++)
			{
				componentImpl = new ComponentImpl();
				
				Object obj[] = (Object[])componentImplList.get(i);
				componentImpl.setItemNumber(obj[2].toString());				
				componentImpl.setModuleNumber(obj[1].toString());
				componentImpl.setTsoSplitNumber(obj[0].toString());
				
				//add the object to the list
				componentitemList.add(componentImpl);
			}
		 }//end if
		}catch (HibernateException he) {
	    	//TODO: handle exception
			log.error("PCSParsingBusinessQueryImpl:retrieveComponentItemDetailsGroupBy():"+he);
	        throw new PCSParsingException(he);	        
	      }catch (Exception e) {
			//TODO: handle exception	
	    	  log.error("PCSParsingBusinessQueryImpl:retrieveComponentItemDetailsGroupBy():"+e);
		      throw new PCSParsingException("Could not execute Hibernate Query: " + e);
		      
		  }finally {
		    	  	Finder.close(session);
		    	  	log.info("Exit From Method ="+methodName);
		           }		    
		  
		  
		return componentitemList;
	}//end of method retrieveComponentItemDetails()
	
	/*
	 * This method gets the list of the Modules which are ordered 
	 */
	
	private String getOrderedModules(String serialNumber){			
	
		 String methodName= "getOrderedModules()";		 		 
		 String inclause ="";
		 StringBuffer inClauseBuffer=null;
		 log.info("Entry into method :"+methodName);
	
		try{
			//get the ordered serial number			
			List orderedModulesList = ModuleFinder.getModulesOrderedByNumber(
			        				  serialNumber,
			        				  IsOrderedSearchCriteria.ONLY,
			        				  DepartmentSearchCriteria.ALL,
			        				  HasCWOSearchCriteria.ALL);
			
			int orderedModulesListSize = orderedModulesList.size();
			inClauseBuffer = new StringBuffer();
			
			for(int j=0;j<orderedModulesListSize;j++){
				Module mod = (Module)orderedModulesList.get(j);
				inClauseBuffer.append("'"+mod.getNumber()+"'");
				if(j<orderedModulesListSize-1){
					inClauseBuffer.append(",");
				}			
			}//end of for Loop
			
			inclause = inClauseBuffer.toString();
			
		}catch (Exception e) {
			// TODO: handle exception
			log.error("PCSParsingBusinessQueryImpl:getOrderedModules():"+e);
		}finally{
			inClauseBuffer = null;
		}
		 log.info("Exit from method ="+methodName);
		return inclause;
	}//end of method
	
	/**
	 * This item retrieves the details from the PXPCI table to add the data into the item table
	 * @param serialNumber
	 * @return
	 */
	
	public List retrieveComponentItemDetails(String serialNumber)throws PCSParsingException
	{
	  //NOTE : Item Numbers in the PXPCI table are also known as BOM 
	  //Deviation BOMS are those BOM for which the Sequence Number is present
		
		 ComponentImpl componentImpl = null;
		 List masterResultList = null;
		 String methodName= "retrieveComponentItemDetails()";		 
		 Session session = null;
		
		 log.info("Entry into method ="+methodName);
	
		try{
			//get the ordered module numbers
			String inClauseString = getOrderedModules(serialNumber);
			session = Finder.getSession();				
			
			masterResultList = new ArrayList();
			
			//Step 1 : Pick one module at a time 
			StringTokenizer st = new StringTokenizer(inClauseString,",");
			
			while(st.hasMoreTokens())			
			{
				//Stept 2: Pick the First Token :
				String moduleNumber = st.nextToken();
				log.info("##The Module Number is :="+moduleNumber);
									
				
				Query query = session.
							    createQuery("select max(compImpl.tsoSequenceNumber) from ComponentImpl compImpl where " +
							    		    "compImpl.tsoSplitNumber = '"+serialNumber+"' and " +
							    		    "compImpl.moduleNumber in ("+moduleNumber +")");				
				
				List list = query.list();
				
				String maxtsoSeqNumber = (String)list.get(0);
				
				
				
				
				
				//Step 3 : Fire the query where seq no is blank
				List serNoBlankList = session.find("select item.tsoSplitNumber,item.tsoSequenceNumber,item.sequenceNumber" +
		    	 		",item.moduleNumber,item.itemNumber,item.compItemNo,item.compQty,item.itemDescription,item.itemType,item.itemStatus" +
		    	 		",item.departmentCode,item.purchasingUnitOfMeasure,item.mipNumber,item.engRevisionLevel"+ 
		    	 		",item.mfgRevisionLevel,item.accountNumber,item.itarRestrIndc "+
		    	    	"from ComponentImpl item "+
		    	 		"where item.tsoSplitNumber = '"+serialNumber+"' and item.moduleNumber in ("+moduleNumber +") and item.sequenceNumber='' and " +
		    	 	    "item.tsoSequenceNumber ='"+maxtsoSeqNumber+"' and "+
		    	 	    "item.itemType in ('A','P','M') and "+
		    	 	    "item.compItemNo not like '13-130%' "+
		    	 		"group by item.tsoSplitNumber,item.tsoSequenceNumber,item.sequenceNumber" +
		    	 		",item.moduleNumber,item.itemNumber,item.compItemNo,item.compQty,item.itemDescription,item.itemType,item.itemStatus" +
		    	 		",item.departmentCode,item.purchasingUnitOfMeasure,item.mipNumber,item.engRevisionLevel"+ 
		    	 		",item.mfgRevisionLevel,item.accountNumber,item.itarRestrIndc");
				
				
				
				List serNumberBlankList = new ArrayList();
				
				int listSize = serNoBlankList.size();
				
				for(int i=0;i<listSize;i++)
				{
					componentImpl = new ComponentImpl();				
					Object obj[] = (Object[])serNoBlankList.get(i);
					componentImpl.setTsoSplitNumber(obj[0].toString());					
					componentImpl.setTsoSequenceNumber(obj[1].toString());					
					componentImpl.setSequenceNumber(obj[2].toString());					
					componentImpl.setModuleNumber(obj[3].toString());					
					componentImpl.setItemNumber(obj[4].toString());					
					componentImpl.setCompItemNo(obj[5].toString());					
					componentImpl.setCompQty(obj[6].toString());					
					componentImpl.setItemDescription(obj[7].toString());					
					componentImpl.setItemType(obj[8].toString());					
					componentImpl.setItemStatus(obj[9].toString());					
					componentImpl.setDepartmentCode(obj[10].toString());					
					componentImpl.setPurchasingUnitOfMeasure(obj[11].toString());					
					componentImpl.setMipNumber(obj[12].toString());					
					componentImpl.setEngRevisionLevel(obj[13].toString());					
					componentImpl.setMfgRevisionLevel(obj[14].toString());					
					componentImpl.setAccountNumber(Integer.parseInt(obj[15].toString()));
					componentImpl.setItarRestrIndc(obj[16].toString());
					
					
					
					//add the object to the list
					serNumberBlankList.add(componentImpl);
				}
							
				
				List serNoNONBlankList = session.find("select item.tsoSplitNumber,item.tsoSequenceNumber,item.sequenceNumber" +
		    	 		",item.moduleNumber,item.itemNumber,item.compItemNo,item.compQty,item.itemDescription,item.itemType,item.itemStatus" +
		    	 		",item.departmentCode,item.purchasingUnitOfMeasure,item.mipNumber,item.engRevisionLevel"+ 
		    	 		",item.mfgRevisionLevel,item.accountNumber,item.itarRestrIndc "+
		    	    	"from ComponentImpl item "+
		    	 		"where item.tsoSplitNumber = '"+serialNumber+"' and item.moduleNumber in ("+moduleNumber +") and item.sequenceNumber<>'' " +
		    	 		"and item.itemType in ('A','P','M') and "+
		    	 	    "item.compItemNo not like '13-130%' "+
		    	 		"group by item.tsoSplitNumber,item.tsoSequenceNumber,item.sequenceNumber" +
		    	 		",item.moduleNumber,item.itemNumber,item.compItemNo,item.compQty,item.itemDescription,item.itemType,item.itemStatus" +
		    	 		",item.departmentCode,item.purchasingUnitOfMeasure,item.mipNumber,item.engRevisionLevel"+ 
		    	 		",item.mfgRevisionLevel,item.accountNumber,item.itarRestrIndc");
				
								
				List serNumberNONBlankList = new ArrayList();
				
				 listSize = serNoNONBlankList.size();
				
				for(int i=0;i<listSize;i++)
				{

					componentImpl = new ComponentImpl();				
					Object obj[] = (Object[])serNoNONBlankList.get(i);					
					componentImpl.setTsoSplitNumber(obj[0].toString());					
					componentImpl.setTsoSequenceNumber(obj[1].toString());					
					componentImpl.setSequenceNumber(obj[2].toString());					
					componentImpl.setModuleNumber(obj[3].toString());					
					componentImpl.setItemNumber(obj[4].toString());					
					componentImpl.setCompItemNo(obj[5].toString());					
					componentImpl.setCompQty(obj[6].toString());					
					componentImpl.setItemDescription(obj[7].toString());					
					componentImpl.setItemType(obj[8].toString());					
					componentImpl.setItemStatus(obj[9].toString());					
					componentImpl.setDepartmentCode(obj[10].toString());					
					componentImpl.setPurchasingUnitOfMeasure(obj[11].toString());					
					componentImpl.setMipNumber(obj[12].toString());					
					componentImpl.setEngRevisionLevel(obj[13].toString());					
					componentImpl.setMfgRevisionLevel(obj[14].toString());					
					componentImpl.setAccountNumber(Integer.parseInt(obj[15].toString()));	
					componentImpl.setItarRestrIndc(obj[16].toString());

					
					//add the object to the list
					serNumberNONBlankList.add(componentImpl);
				}
				
				//Step 4: iterate the serNoBlankList and create Three map objects
				
				Map mapQty = new HashMap();
				Map mapComponentImplObject = new HashMap();
				
				//Change:
				Map mapComponentImplDeviationBOMOjbect = new HashMap();

				
				//Put the iterator on the Deviation BOM'S
				Iterator it = serNumberBlankList.iterator();
				
				//Populate the Different Maps Being created
				while(it.hasNext())
				{
					componentImpl = (ComponentImpl)it.next();
					
					mapQty.put(componentImpl.getCompItemNo(), componentImpl.getCompQty());
					mapComponentImplObject.put(componentImpl.getCompItemNo(),componentImpl);
					
				}//end of while loop
				
			//	System.out.println("####size of serNumberNONBlankList:="+serNumberNONBlankList.size());
				//Step5 : Iterate thru the serNumberNONBlankList -- This list depicts the Deviation BOMS
				
				Iterator itr = serNumberNONBlankList.iterator();
				
				double quantity = 0.00;
				
				while(itr.hasNext())
				{
					//object from Deviation BOM, nonBlank Seq No List
					componentImpl = (ComponentImpl)itr.next();
					
					//retrieve the component item number from the List
					String compItemNumber = componentImpl.getCompItemNo();
					
					//Look if this Component Item Number is Present in the Map 
					//(which does not contains the Deviation Boms
					
					String  qty = (String)mapQty.get(compItemNumber);
					
					//add the object to the Map
					mapComponentImplDeviationBOMOjbect.put(compItemNumber, componentImpl);

					
					if(null!= qty)
					{   //the key is present in the table
						//Add the quantity to the Deviation Qty
												
							//get the quantity from Deviation Bom
							quantity = new Double(componentImpl.getCompQty()).doubleValue();							
							//add the quantity of Non Deviation Boms
							quantity = quantity + (new Double(qty).doubleValue());
							
							//Change:
							/*remove the element from the mapComponentImplDeviationBOMOjbect
							as both the DeviationBOM and NONDeviationBOM are evaluated */
			//				System.out.println("#####removing the element");
							mapComponentImplDeviationBOMOjbect.remove(compItemNumber);
							
						
						componentImpl.setCompQty(""+quantity);
						
					}//end if
					
					//check if the Quantity is >0 add them to the List
					if(quantity>0)
					{
						//add the object to the master List of the Deviation BOMs
						masterResultList.add(componentImpl);
						
					}
					
					//remove this Key from the Map which contains object of NON deviation BOMS
					mapComponentImplObject.remove(compItemNumber);
					mapQty.remove(compItemNumber);
					
				}//end of while Loop
				
				
				//Now we have iterated thru the Deviation BOM and would like to 
				// add all the other left elements in the Maps
				
				Iterator iter = mapComponentImplObject.keySet().iterator();
				while (iter.hasNext())
				{
					Object key =iter.next();
					 componentImpl =(ComponentImpl)(mapComponentImplObject.get(key));
					
					//componentImpl = (ComponentImpl)componentImplObject;
					if((new Double(componentImpl.getCompQty()).doubleValue())>0){
						//add all the elements to the master ResultList
						masterResultList.add(componentImpl);
					}
					
				}//end while
				
		//		System.out.println("#####Iterating Deviation Bom List");
				//Change:
				//Iterate the Map of Deviation BOM for which NO match was found in NON Deviation BOM
				//list
				Iterator iterator = mapComponentImplDeviationBOMOjbect.keySet().iterator();
				while (iterator.hasNext())
				{
					Object key =iterator.next();
					 componentImpl =(ComponentImpl)(mapComponentImplDeviationBOMOjbect.get(key));					
					
					if((new Double(componentImpl.getCompQty()).doubleValue())>0){
						//add all the elements to the master ResultList
						masterResultList.add(componentImpl);
					}
					
				}//end while

				
			}
			 
			/*componentImplList = session.find("select item.tsoSplitNumber,item.tsoSequenceNumber,item.sequenceNumber" +
		    	 		",item.moduleNumber,item.itemNumber,item.compItemNo,item.compQty,item.itemDescription,item.itemType,item.itemStatus" +
		    	 		",item.departmentCode,item.purchasingUnitOfMeasure,item.mipNumber,item.engRevisionLevel"+ 
		    	 		",item.mfgRevisionLevel,item.accountNumber "+
		    	    	"from ComponentImpl item "+
		    	 		"where item.tsoSplitNumber = '"+serialNumber+"' and item.moduleNumber in ("+inClauseString +") group by item.tsoSplitNumber,item.tsoSequenceNumber,item.sequenceNumber" +
		    	 		",item.moduleNumber,item.itemNumber,item.compItemNo,item.compQty,item.itemDescription,item.itemType,item.itemStatus" +
		    	 		",item.departmentCode,item.purchasingUnitOfMeasure,item.mipNumber,item.engRevisionLevel"+ 
		    	 		",item.mfgRevisionLevel,item.accountNumber");
						
			componentitemList = new ArrayList();
			
			int listSize = componentImplList.size();
			
			for(int i=0;i<listSize;i++)
			{
				componentImpl = new ComponentImpl();				
				Object obj[] = (Object[])componentImplList.get(i);
				componentImpl.setTsoSplitNumber(obj[0].toString());				
				componentImpl.setTsoSequenceNumber(obj[1].toString());
				componentImpl.setSequenceNumber(obj[2].toString());
				componentImpl.setModuleNumber(obj[3].toString());				
				componentImpl.setItemNumber(obj[4].toString());
				componentImpl.setCompItemNo(obj[5].toString());
				componentImpl.setCompQty(obj[6].toString());
				componentImpl.setItemDescription(obj[7].toString());
				componentImpl.setItemType(obj[8].toString());
				componentImpl.setItemStatus(obj[9].toString());
				componentImpl.setDepartmentCode(obj[10].toString());
				componentImpl.setPurchasingUnitOfMeasure(obj[11].toString());
				componentImpl.setMipNumber(obj[12].toString());
				componentImpl.setEngRevisionLevel(obj[13].toString());
				componentImpl.setMfgRevisionLevel(obj[14].toString());
				componentImpl.setAccountNumber(Integer.parseInt(obj[15].toString()));
				
				//add the object to the list
				componentitemList.add(componentImpl);
			}	*/		
		
		}catch (HibernateException he) {
	    	  //TODO: handle exception
			log.error("PCSParsingBusinessQueryImpl:retrieveComponentItemDetails():"+he);
	        throw new PCSParsingException(he);
	        
	      }catch (Exception e) {
			//TODO: handle exception
			  log.error("PCSParsingBusinessQueryImpl:retrieveComponentItemDetails():"+e);
		      throw new PCSParsingException("Could not execute Hibernate Query: " + e);
		      
		    }finally{
		    	  		Finder.close(session);	
		    	  		 log.info("Exit From Method ="+methodName);
		      		}		    
		 
		return masterResultList;
	}//end of method retrieveComponentItemDetails()
	
	
	/**
	 * This method insert the data into the JobRoute table by calling the stored procedure
	 * @param List componentitemList
	 * @param Connection conn
	 * @return void
	 * This method is not being called any more
	 */
	public void insertJobRouteDetails(List componentitemList,String jobNo,Connection conn)throws PCSParsingException
	{	
		 ComponentImpl componentImpl = null;		
		 String methodName = "insertJobRouteDetails()";
		 CallableStatement cStat = null;
		 log.info("Entry into method ="+methodName);
		 
		try{
			 /**1. prepare the callable statement and insert the data into JobRoute table
			  * by calling the 'insertJobRoute' storedprocedure   
			  */ 
			 //Three values to be inserted : Job=PXPCI.tso_split_no, Module=PXPCI.module_no, BOM number=PXPCI.item_no 
			 Iterator itr =  componentitemList.iterator();
			 
			 cStat = conn.prepareCall("{call DTNA_OperNumByWc (?,?,?,?)}");
			
			 while(itr.hasNext())
			 {				 
				 componentImpl = (ComponentImpl)itr.next();
				 cStat.setString(1, get10DigJobNo(jobNo.trim()));				 
				 cStat.setInt(2, 0);
				 cStat.setString(3, componentImpl.getModuleNumber());
				 cStat.registerOutParameter(4, Types.INTEGER);
				 //execute the Batch
				 boolean val = cStat.execute();				 
				 
				 int output = cStat.getInt(4);
				 
			 }//end of while Loop			 
		 }catch (Exception e) {
				//TODO: handle exception
			 	log.error("PCSParsingBusinessQueryImpl:insertJobRouteDetails():"+e);
		      	throw new PCSParsingException("Could not execute SQL: " +e);		      
		    } finally {
				      try {			    	 
				    	  if (null!=cStat){ 
				    		  cStat.close();
				    	  }			    	  
				      } catch (SQLException e1) {
				    	  throw new PCSParsingException("Could not close connection", e1);
				      }
		    }	//end finally	
		 
		 log.info("Exit From Method ="+methodName);
	}//end of method insertJobRouteDetails()
	   
	
	/**
	 * This method inserts the detials for the Items into the item table (for which the key is missing in the table0
	 * 
	 * @param componentitemList
	 * @param conn
	 * @throws PCSParsingException
	 */
	public synchronized void updateItemDetails(List componentitemList,Connection conn)throws PCSParsingException
	{
		 String methodName= "updateItemDetails()";
		 ComponentImpl componentImpl = null;
		 CallableStatement cStat = null;
		 log.info("Entry into method ="+methodName);
		 String returnItem = " ";
		 String returnMessage = " ";
		 
		 //for testing
		 Set uniqueKeySet = new HashSet(); 
		try{
			
			/* TO DO : Operation work like this : for each and every record from PXPCI object make a key,  
			 * key="PXPCI.comp_item_no/ PXPCI.drwg_rev_lvl", check in the DB if this key is present, 
			 * if yes -  do nothing, if not - insert the details into the item table with key as the primary key
			 * ans other revelant details.
			 * Plan : to use the stored procedures with executebatch mode
			 */ 
			 Iterator itr =  componentitemList.iterator();
			 
			 cStat = conn.prepareCall("{call DTNA_insertItemSp (?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				
			 while(itr.hasNext()){				 
				 componentImpl = (ComponentImpl)itr.next();		 				
				 
				 //SET THE values for the insertion				
				 cStat.setString(1, ((componentImpl.getCompItemNo()).trim()));
				 String uniqueKeys = ((componentImpl.getCompItemNo()).trim())+"#TEST";
				 uniqueKeySet.add(uniqueKeys);
				 
				 cStat.setString(2, componentImpl.getItemDescription());				 
				 cStat.setString(3, componentImpl.getEngRevisionLevel());				 
				 cStat.setString(4, calcUnitOfmeasure(componentImpl.getPurchasingUnitOfMeasure()));
				 cStat.setString(5, ApplicationConstants.CUR_MAT_COST);
				 cStat.setString(6, ApplicationConstants.CHGOVER_ACTION);
				 cStat.setString(7, ApplicationConstants.CHGOVER_DATE);				 
				 cStat.setString(8, ApplicationConstants.UF_HAZ_MAT);
				 cStat.setString(9, componentImpl.getItemType());
				 cStat.setString(10, componentImpl.getMfgRevisionLevel());
				 cStat.setString(11, componentImpl.getItemStatus());
				 cStat.registerOutParameter(12, Types.VARCHAR);
				 cStat.registerOutParameter(13, Types.VARCHAR);
				 cStat.setString(14, componentImpl.getItarRestrIndc());

				
				 cStat.execute();
				 
				 returnItem = cStat.getString(12);
				 returnMessage = cStat.getString(13);
				 log.info(methodName+": Item "+returnItem+" Added : "+returnMessage);
			 }
			 
 			 
			 
		/**	 cStat = conn.prepareCall("{call DTNA_insertItem (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			
			 while(itr.hasNext()){				 
				 componentImpl = (ComponentImpl)itr.next();		 				
				 
				 //SET THE values for the insertion				
				 cStat.setString(1, ((componentImpl.getCompItemNo()).trim())+"/"+((componentImpl.getEngRevisionLevel()).trim()));
				 String uniqueKeys = ((componentImpl.getCompItemNo()).trim())+"/"+componentImpl.getEngRevisionLevel()+"#TEST";
				 uniqueKeySet.add(uniqueKeys);
				 
				 cStat.setString(2, componentImpl.getItemDescription());				 
				 cStat.setString(3, componentImpl.getEngRevisionLevel());				 
				 cStat.setString(4, calcUnitOfmeasure(componentImpl.getPurchasingUnitOfMeasure()));				 
				 cStat.setString(5, ApplicationConstants.MATERIAL_TYPE);
				 cStat.setString(6, ApplicationConstants.PURCHASE_CODE);
				 cStat.setString(7, ApplicationConstants.PRODUCT_CODE);
				 cStat.setString(8, ApplicationConstants.ABC_CODE);
				 cStat.setString(9, ApplicationConstants.COST_TYPE);
				 cStat.setString(10, ApplicationConstants.COST_METHOD);
				 cStat.setString(11, ApplicationConstants.DAYS_SUPPLY);
				 cStat.setString(12, ApplicationConstants.LOCATION_CODE);
				 cStat.setString(13, ApplicationConstants.STATUS);
				 cStat.setString(14, ApplicationConstants.UF_HAZ_MAT);
				 cStat.setString(15, ApplicationConstants.STOCKED);
				 cStat.setString(16, ApplicationConstants.ACTV_FOR_DATA_MIG);
				 cStat.setString(17, componentImpl.getItemType());				
				 cStat.setString(18, componentImpl.getItemStatus());				
				 cStat.setString(19, ApplicationConstants.LEAD_TIME);
				 cStat.setString(20, ApplicationConstants.U_WS_PRICE);
				 cStat.setString(21, ApplicationConstants.TAX_FREE_DAYS);
				 cStat.setString(22, ApplicationConstants.COMPO_MATL);
				 cStat.setString(23, ApplicationConstants.CUR_MAT_COST);
				 cStat.setString(24, ApplicationConstants.BUYER);
				 cStat.setString(25, ApplicationConstants.PLAN_CODE);
				 cStat.setInt(26, ApplicationConstants.Const_ONE);
				 cStat.setInt(27, ApplicationConstants.Const_ONE);
				 cStat.setString(28,((componentImpl.getCompItemNo()).trim()));//partnum
				 cStat.setString(29, ApplicationConstants.CONST_FASTENER);//fastener code
				
				 cStat.addBatch();			
			
			 }	
			 
			 //execute the callable statement in a batch mode
			 int [] noOfRowsAffected = cStat.executeBatch();
			 log.info(methodName+": No of Rows Updates: "+noOfRowsAffected.length); */
			
		}catch (Exception e) {			 
			 log.error("PCSParsingBusinessQueryImpl:updateItemDetails():"+e);
			//TODO: handle exception
			throw new PCSParsingException("Could not execute Callable in Batch: " + e);
	    } finally {
			      try {			    	 
			    	  if (null!=cStat){ 
			    		  cStat.close();
			    	  } 		    	  
			      } catch (SQLException e1) {			    	
			    	  throw new PCSParsingException("Could not close connection", e1);
			      }
	    }	//end finally	    
	  log.info("Exit From Method ="+methodName);
		
	}//end of method updateItemDetails
	
	/**
	 * This method insert the Records into the Job material table
	 * @param serialNum
	 * @param componentitemList
	 * @param conn
	 */


  	public synchronized void insertJobMaterialDetails(SerialNumber serialNum,List<ComponentImpl> componentitemList,Connection conn)throws PCSParsingException  
	{
		 String methodName= "insertJobMaterialDetails()";
		 ComponentImpl componentImpl = null;
		// CallableStatement cStat = null;
		 PreparedStatement cStat = null;
		 PreparedStatement pStat = null;
		 log.info("Entry into method ="+methodName);
		 ResultSet rs = null;
		 StringBuffer strBuffer = null;
		 strBuffer = new StringBuffer();		 
		 //	for testing
		 Set uniqueKeySet = new HashSet(); 
		 Set tempset = new HashSet();
		 
		 String zeroString="0";
		 
		try{
			 Iterator<ComponentImpl> itr =  componentitemList.iterator();			 
			 cStat = conn.prepareCall("{call DTNA_insertJobmatl (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");	
			 String itemRev = null;
			 
			 while(itr.hasNext())
			 {	 
				 componentImpl = (ComponentImpl)itr.next();	
				/* itemRev=" ";

				 //clear the buffer to reuse it
				 strBuffer.delete(0, strBuffer.length());
				 strBuffer.append(SytelineQueryUtil.getQueryAppender());
				 strBuffer.append(" Select item from item where item=? and product_code=?");
				 pStat = conn.prepareStatement(strBuffer.toString());

				 pStat.setString(1, ((componentImpl.getCompItemNo()).trim())+"/ ");
				 pStat.setString(2, ApplicationConstants.CONST_FASTENER);
 
				try{
					 rs = pStat.executeQuery();
					 if (rs.next()){
				  		 log.info("ParsingBusinessQueryImpl:STEP 3.2 INSERTION INTO THE JOBMATL TABLE ##### "+rs.getString(1));
					 }
					 else
					 {
						itemRev=componentImpl.getEngRevisionLevel();
			        //    log.info("ParsingBusinessQueryImpl:STEP 3.3 INSERTION INTO THE JOBMATL TABLE ##### "+itemRev);						 
					 }	 
				}catch (SQLException se) {
					log.error("PCSParsingBusinessQueryImpl:insertJobMaterialDetails():Could not execute prepared statement:"+se);
				//	TODO: handle exception
					throw new PCSParsingException("Could not execute prepared statement: " + se);
				}*/
				
	            // log.info("ParsingBusinessQueryImpl:STEP 4. INSERTION INTO THE JOBMATL TABLE ##### "+(componentImpl.getCompItemNo()).trim());				

				 //SET THE values for the insertion 
				 cStat.setString(1, get10DigJobNo(serialNum.getCmcsNumber().trim()));//job number - PVR			
				 cStat.setString(2, ((componentImpl.getCompItemNo()).trim()));
					//	+ "/" + itemRev);				 
				 String uniqueKeys = ((componentImpl.getCompItemNo()).trim())+"/"+componentImpl.getEngRevisionLevel()+"#TEST";
				 uniqueKeySet.add(uniqueKeys);
				 tempset.add(uniqueKeys+"/"+componentImpl.getItemNumber());
				 cStat.setString(3, calcUnitOfmeasure(componentImpl.getPurchasingUnitOfMeasure()));
				 cStat.setString(4, ApplicationConstants.MATERIAL_TYPE);				 
				 cStat.setString(5, componentImpl.getCompQty());				 
				 cStat.setString(6, componentImpl.getModuleNumber());
				 moduleNumber = componentImpl.getModuleNumber();				 
				 cStat.setString(7, componentImpl.getMfgRevisionLevel());				 
				 cStat.setString(8, componentImpl.getEngRevisionLevel());				 
				 cStat.setString(9, componentImpl.getItemStatus());				 
				 cStat.setString(10, ApplicationConstants.DELETE_REASON_INIT);// reason_code -- to be added into th table				 
				 cStat.setString(11, componentImpl.getTsoSplitNumber());//serial number
				 cStat.setString(12, componentImpl.getItemNumber());//bom
				 cStat.setInt(13, ApplicationConstants.Const_ONE);//bom qty				 
				 cStat.setString(14, (componentImpl.getCompItemNo()).trim());//partNum				 
				 cStat.setString(15,zeroString);//suffix
/*			       System.out.println("#%%%CSTAT 1:  Job Number " +get10DigJobNo(serialNum.getCmcsNumber().trim()));
			       System.out.println("#%%%CSTAT 2:  Comp Item Number " +(componentImpl.getCompItemNo()).trim()+"/"+itemRev);
			       System.out.println("#%%% uniqueKeys "+uniqueKeys);
			       System.out.println("#%%% uniqueKeySet "+uniqueKeySet);
			       System.out.println("#%%%CSTAT 3:  calc Unit of Measure " +calcUnitOfmeasure(componentImpl.getPurchasingUnitOfMeasure()));
			       System.out.println("#%%%CSTAT 4:  Material Type " +ApplicationConstants.MATERIAL_TYPE);
			       System.out.println("#%%%CSTAT 5:  Component Qty " +componentImpl.getCompQty());
			       System.out.println("#%%%CSTAT 6:  Module Number " +componentImpl.getModuleNumber());
			       System.out.println("#%%%CSTAT 7:  Mfg Rev Lvl " +componentImpl.getMfgRevisionLevel());
			       System.out.println("#%%%CSTAT 8:  Eng Rev Lvl " +componentImpl.getEngRevisionLevel());
			       System.out.println("#%%%CSTAT 9:  Item Status " +componentImpl.getItemStatus());
			       System.out.println("#%%%CSTAT 10:  Delete Reason  " +ApplicationConstants.DELETE_REASON_INIT);
			       System.out.println("#%%%CSTAT 11:  TSO Split Number " +componentImpl.getTsoSplitNumber());
			       System.out.println("#%%%CSTAT 12:  Item/BOM Number " +componentImpl.getItemNumber());
			       System.out.println("#%%%CSTAT 13:  Constant 1 " +ApplicationConstants.Const_ONE);
			       System.out.println("#%%%CSTAT 14:  Component Item Number " +(componentImpl.getCompItemNo()).trim());
			       System.out.println("#%%%CSTAT 15:  Zero String " +zeroString);
*/			       

				 cStat.execute();

		
			 }//end while
			 
			 // conn.commit();	 
	
		 }catch (SQLException se) {
			 	// TODO: handle exception
			 	log.error("PCSParsingBusinessQueryImpl:insertJobMaterialDetails():Could not execute Callable in Batch:"+se);
		      throw new PCSParsingException("Could not execute Callable in Batch: " + se);
		}catch (Exception e){
				log.error("PCSParsingBusinessQueryImpl:insertJobMaterialDetails():Could not execute Callable in Batch:"+e);
				//TODO: handle exception
			    throw new PCSParsingException("Could not execute Callable in Batch: " + e);
		} finally {				       
				   try {
					     if (null!=cStat){ 
				    		  cStat.close();
				    	  }
					     try{
					   	   if (null!=pStat){ 
						    	  pStat.close();
						    	 } 		    	  
						      } catch (SQLException e1) {
						    	  throw new PCSParsingException("Could not close connection", e1);
						      }
				   	
				       } catch (SQLException e1) {
				    	  throw new PCSParsingException("Could not close connection", e1);
				       }

			    }	//end finally	    
			  log.info("Exit From Method ="+methodName);
				
		}//end of method insertJobMaterialDetails
	
	/*
	 * method to take the backup of the JobmatlTable before inserting upting the records in the Jobmatl
	 */
	
	public void insertJobmatlLast(SerialNumber serialNum,Connection conn)throws PCSParsingException{
		
		 String methodName= "updateItemDetails()";		 
		 CallableStatement cStat = null;		
		 log.info("Entry into method ="+methodName); 
		try{
			 cStat = conn.prepareCall("{call DTNA_JobMatl_Backup (?)}");
			 cStat.setString(1, get10DigJobNo(serialNum.getCmcsNumber().trim()));//job number - PVR			 
			 cStat.execute();			 
		}catch (SQLException se) {
				log.error("PCSParsingBusinessQueryImpl:insertJobmatlLast():Could not execute StoredProcedure:"+se);
				//TODO: handle exception
		      throw new PCSParsingException("Could not execute StoredProcedure: " + se);
		}catch (Exception e){
				log.error("PCSParsingBusinessQueryImpl:insertJobmatlLast():Could not execute StoredProcedure:"+e);
				//TODO: handle exception
			    throw new PCSParsingException("Could not execute StoredProcedure: " + e);
			      
	    } finally{
				   try{			    	 
				   	   if (null!=cStat){ 
				    	  cStat.close();
				    	 } 		    	  
				      } catch (SQLException e1) {
				    	  throw new PCSParsingException("Could not close connection", e1);
				      }
			    }	//end finally	    
		log.info("Exit From Method ="+methodName);
	}//end of method
	
	/**
	  * Get the spec quantity count.
	  * @param String itemNumber: the item number
	  * @param String loc: the location ID
	  * @param String engRevLevel: the engineering revision level
	  * 
	  * @throws PCSParsingException
	  * 
	  */
	  public int getSpecQuantityCount(String itemNumber, String loc,
	      String engRevLevel,Connection conn) throws PCSParsingException {
		  
		     String methodName= "getSpecQuantityCount()";
			 PreparedStatement pStat = null;
			 ResultSet rs = null;			 
			 int returnValue = 0;
			 log.info("Entry into method ="+methodName);			 
	    
	  try {
		      pStat = conn.prepareStatement(SytelineQueryUtil.getQueryAppender()+" select count(*) from dtna_specquantity where part_no=? and rev_level=? and location=? ");
		      pStat.setString(1,itemNumber );
		      pStat.setString(2,engRevLevel );
		      pStat.setString(3,loc );
		      
		      rs = pStat.executeQuery();
		       
		      while (rs.next()){
		    	  returnValue =  rs.getInt(1);
		      }
	        
	    } catch (SQLException se) {
	    	log.error("PCSParsingBusinessQueryImpl:getSpecQuantityCount():"+se);
	    	throw new PCSParsingException("Could not get spec qty count", se);
	    } catch (Exception e) {
	    	log.error("PCSParsingBusinessQueryImpl:getSpecQuantityCount():"+e);
	    	throw new PCSParsingException("Could not get spec qty count", e);
	    }
	    return returnValue;
	    
	 }//end of method getSpecQuantityCount
	  
	  /**
	   * Insert a location.
	   * @param Location location: the collection
	   * @throws PCSParsingException
	   * 
	   */
	   public void insertSpecQuantity(Location location,Connection conn) throws PCSParsingException {
	     
	     
	     String methodName= "insertSpecQuantity()";
		 PreparedStatement pStat = null;		 
		 log.info("Entry into method ="+methodName);
		 
	     try{
	    	 pStat = conn.prepareStatement(SytelineQueryUtil.getQueryAppender()+" insert into dtna_specquantity(" 
			         	 + "part_no,rev_level,location,plant_id,"
			             + "fa_code,comp_code,matl_planner,on_hand_qty, "
			             + "minimum_qty,three_day_peg_qty,lead_time_peg_qty,lead_time, "
			             + "co_date,vendor_id1,vendor_id2,vendor_id3,shop_address, "
			             + "entry_date,cost"
			             + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	    	 
		    	   pStat.setString(1, location.getItemNumber());//part_no
		    	   pStat.setString(2, location.getItem().getEngRevisionLevel());//rev_level
		    	   pStat.setString(3, location.getLoc());//location
		           String crfa = location.getCrfa();
			       pStat.setString(4, crfa.substring(2, 4));//plant id     
			       pStat.setString(5, crfa.substring(4, 6));//fa_code
			       pStat.setString(6, crfa.substring(0, 2));//comp_code
			       pStat.setString(7, location.getMatlPlanner());//matl_planner
		       
			       int onHandQuantity = location.getNetAvailable();
			       if (onHandQuantity == 0) {
			         onHandQuantity = location.getOhTotalQuantity();
			       }
			       pStat.setInt(8, onHandQuantity);//on_hand_qty
			      
			       pStat.setDouble(9, location.getSafetyStock());//minimum_qty
			       pStat.setDouble(10, location.getPeg3DayQuantity());//three_day_peg_qty
			       pStat.setDouble(11, location.getPegLeadTimeQuantity());//lead_time_peg_qty
			       pStat.setInt(12, location.getLeadTime());//12 lead_time
			       Date changeOverDate = location.getChangeoverDate();
			       if (changeOverDate != null) {
			    	   pStat.setTimestamp(13, new Timestamp(changeOverDate.getTime()));//co_date
			       } else {
			    	   pStat.setTimestamp(13, null);//co_date
			       }
			       pStat.setString(14, location.getPrimaryVendorAbbr());//vendor_id1
			       pStat.setString(15, location.getSecondaryVendorId());//vendor_id2
			       pStat.setString(16, location.getAltVendorId());//vendor_id3
			       pStat.setString(17, crfa.substring(2, 4));//shop_address
			       
			       pStat.setTimestamp(18, new Timestamp(new Date().getTime()));//entry_date
			       
			       double cost = location.getOrdsTotalCost();
			       if (cost == 0) {
			         cost = location.getTotalCost();
			       }
			       pStat.setDouble(19, cost);//cost
			       
			       pStat.executeUpdate();
	       
	     } catch (Exception e) {
	       throw new PCSParsingException("Could not insert spec quantity: "
	           + location.getItemNumber(), e);
	     }try{
	    	 	if(null!=pStat){ 
	    	 		pStat.close();
	    	 	}
    	 }catch (Exception e) {
			// TODO: handle exception
    		 log.error("PCSParsingBusinessQueryImpl:insertSpecQuantity():"+e);
		} 
	     if (log.isDebugEnabled()) {
	       log.debug(location.getItemNumber());
	     }
	  }//end of insertSpecQuantity
	  
	  /**
	   * Update the location.
	   * @param Location location: the location
	   * @throws PCSParsingException
	   */
	   public void updateSpecQuantity(Location location,Connection conn) throws PCSParsingException {
		   
		   String methodName= "insertSpecQuantity()";
		   PreparedStatement pStat = null;		 
		   log.info("Entry into method ="+methodName);
		   
	     try{
	    	 
	    	  pStat = conn.prepareStatement(SytelineQueryUtil.getQueryAppender()+" update dtna_specquantity set" 
			         	 + " plant_id=?,"
			             + " fa_code=?, comp_code=?, matl_planner=?,on_hand_qty=?, "
			             + " minimum_qty=?,three_day_peg_qty=?,lead_time_peg_qty=?, lead_time=?, "
			             + " co_date=?,vendor_id1=?,vendor_id2=?,vendor_id3=?,shop_address=?, "
			             + " entry_date=?,cost=?"
			             + "where part_no=? and rev_level=? and location=?");    	  
	    	  	  
	      
		           String crfa = location.getCrfa();
			       pStat.setString(1, crfa.substring(2, 4));//plant id     
			       pStat.setString(2, crfa.substring(4, 6));//fa_code
			       pStat.setString(3, crfa.substring(0, 2));//comp_code
			       pStat.setString(4, location.getMatlPlanner());//matl_planner
		       
			       int onHandQuantity = location.getNetAvailable();
			       if (onHandQuantity == 0) {
			         onHandQuantity = location.getOhTotalQuantity();
			       }
			       pStat.setInt(5, onHandQuantity);//on_hand_qty
			      
			       pStat.setDouble(6, location.getSafetyStock());//minimum_qty
			       pStat.setDouble(7, location.getPeg3DayQuantity());//three_day_peg_qty
			       pStat.setDouble(8, location.getPegLeadTimeQuantity());//lead_time_peg_qty
			       pStat.setInt(9, location.getLeadTime());//12 lead_time
			       Date changeOverDate = location.getChangeoverDate();
			       if (null!=changeOverDate) {
			    	   pStat.setTimestamp(10, new Timestamp(changeOverDate.getTime()));//co_date			    	  
			       } else {
			    	   pStat.setTimestamp(10, null);//co_date			    	   
			       }
			       pStat.setString(11, location.getPrimaryVendorAbbr());//vendor_id1
			       pStat.setString(12, location.getSecondaryVendorId());//vendor_id2
			       pStat.setString(13, location.getAltVendorId());//vendor_id3
			       pStat.setString(14, crfa.substring(2, 4));//shop_address
			       
			       pStat.setTimestamp(15, new Timestamp(new Date().getTime()));//entry_date			      
			       
			       double cost = location.getOrdsTotalCost();
			       if (cost == 0) {
			         cost = location.getTotalCost();
			       }
			       pStat.setDouble(16, cost);//cost			       
			       pStat.setString(17, location.getItemNumber());//part_no
			       pStat.setString(18, location.getItem().getEngRevisionLevel());//rev_level
			       pStat.setString(19, location.getLoc());//location	       
			       
			       pStat.executeUpdate();
	       
	     }catch(SQLException se){
	    	 log.error("PCSParsingBusinessQueryImpl:updateSpecQuantity():Could not update spec quantity:"+se);
	    	  throw new PCSParsingException("Could not update spec quantity: ", se);
	     }catch (Exception e) {
	    	 log.error("PCSParsingBusinessQueryImpl:updateSpecQuantity():Could not update spec quantity:"+e);
	         throw new PCSParsingException("Could not update spec quantity: "+ location.getItemNumber(), e);
	     }finally{
	    	 try{
	    		 if (null!=pStat){ 
	    			 pStat.close();
	    		 }
	    	 }catch (Exception e){
				// TODO: handle exception
			 } 
	     }
	     if (log.isDebugEnabled()) {
	       log.debug(location.getItemNumber());
	     }
	   }
	
	/*
	 * to get the embedded number
	 */
	public String get10DigJobNo(String jobNo){
		
		StringBuffer sb = null; 
		String zeroString="0000" ;		
		try{
			//1. determine if the char at 3 place is a Digit or a Letter
			char ch = jobNo.charAt(2);
			
			boolean isDigit = Character.isDigit(ch);
			
			if(isDigit)
			{								
				sb =  new StringBuffer(jobNo);
				sb.insert(2, zeroString);
			}else
			{
				//Its a Letter				
				sb =  new StringBuffer(jobNo);
				sb.insert(3, zeroString);
			}
					
		}catch (Exception e){
			// TODO: handle exception
		}
		return sb.toString();
	}
	
	/*
	 * To calculate the unit of measure
	 */
	
	public String calcUnitOfmeasure(String unitOfMeasure){
		
		/*@UNIT_OF_MEASURE = '' THEN 'EA'
        WHEN @UNIT_OF_MEASURE = '0' THEN 'EA'
        WHEN @UNIT_OF_MEASURE = '00' THEN 'EA'
        WHEN @UNIT_OF_MEASURE = 'E' THEN 'EA'
        WHEN @UNIT_OF_MEASURE = 'M' THEN 'EA'
        ELSE @UNIT_OF_MEASURE*/

		
		if( (unitOfMeasure.equalsIgnoreCase("")) ||  (unitOfMeasure.equalsIgnoreCase("0")) ||  
			(unitOfMeasure.equalsIgnoreCase("00 ")) || (unitOfMeasure.equalsIgnoreCase("E")) || 
			(unitOfMeasure.equalsIgnoreCase("M")))
		{
			unitOfMeasure = "EA";
		}
		
		return unitOfMeasure;
	}//end of calcUnitOfmeasure
	
	/*
	 * To get the current Module Number being Parsed.	 * 
	 */
	
	public static String getModuleNumber(){
		
		return moduleNumber;
	}

	  /**
	   * Toggle the serial number object field: MTCreate.
	   * @param String number: the serial number
	   * 
	   * @throws PCSParsingException 
	   */
	   public static boolean toggleMTCreate(String number) throws PCSParsingException{

	//   System.out.println("####PCSParsingBusinessQueryImpl - toggleMTCreate:  Input Serial ="+ number);
	     SerialNumber serialNumber = SerialNumberFinder.find(number);
	     
	 	 Connection connection = null;
	 	 TrackTruckImpl trackTruckImpl = null;  
	 	 boolean updateComplete = false;
	     
	     try {

	   	   connection = PCSService.getConnection();
	// 	   System.out.println("#####Conn from the MSSwerver:="+connection);
	   	   connection.setAutoCommit(false);
	   	   trackTruckImpl = new TrackTruckImpl();	

	       updateComplete = trackTruckImpl.updateMTCreate(serialNumber,connection);
	     
	     }catch(SQLException se){
	 //  	 System.out.println("$$$$$ SQL Exception :="+se);
	    	  throw new PCSParsingException("SQL - Could not toggle 'MTCreate for: "
	   	          , se);
	     } catch (Exception e) {
	//    	 System.out.println("$$$$$ non-SQL Exception :="+e);	 
	       throw new PCSParsingException("Could not toggle 'MTCreate' for "
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
	       return updateComplete;
	   }
	   
	   /**
	    * Modify the serial number object field: dateNeeded.
	    * @param String number: the serial number dateNeeded
	    * 
	    * @throws PCSParsingException 
	    */
	    public static boolean updateDateNeeded(String number,String date) throws PCSParsingException {

	   // 	 SerialNumber serialNumber = SerialNumberFinder.find(number);


	  	 Connection connection = null;
	  	 TrackTruckImpl trackTruckImpl = null;  
	  	 boolean updateComplete = false;
	  // System.out.println("####PCSParsingBusinessQueryImpl: 1.1 Update Date Needed: Input Serial = "+ number + "  Dt Need " + date );	

	      try {
	       
	    	   connection = PCSService.getConnection();
	 //   	   System.out.println("#####Conn from the MSSwerver:="+connection);
	    	   connection.setAutoCommit(false);
	    	   trackTruckImpl = new TrackTruckImpl();	

	        updateComplete = trackTruckImpl.updateDateNeeded(number, date, connection);
	        if (log.isDebugEnabled()) log.debug(number + "  Date Needed updated: ");

		     
	      }catch(SQLException se){
		// 	 System.out.println("$$$$$ SQL Exception :="+se);
		   	  throw new PCSParsingException("Could not Update DateNeeded for: "
		  	          , se);
	      } catch (Exception e) {
	        throw new PCSParsingException("Could not update 'DateNeeded' for "
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
	       return updateComplete;
	  }//end of method
	    
	    
    /* This method Updates the JobMatl table for all the records present for the Module Number
	 *  and have the delete reason as NULL 
	 */
   public void updateJobMatlQtyForModules(SerialNumber serialNum,List componentitemList,Connection conn) throws PCSParsingException
   {
	   PreparedStatement ps = null;	   
	   
	   try{
		   String sql = SytelineQueryUtil.getQueryAppender()+" update jobmatl set matl_qty=0,matl_qty_conv=0,uf_bom_qty=0 where job = ?"+ 
						" AND uf_module = ?"+
						" AND suffix    = '0'"+
						" AND uf_delete_reason IS NULL";
		   
		   ps = conn.prepareStatement(sql);
		   
		  Iterator it = componentitemList.iterator();
		  
		  while(it.hasNext())
		  {
			  ComponentImpl componentImpl = (ComponentImpl)it.next();
			  
			  ps.setString(1, get10DigJobNo(serialNum.getCmcsNumber().trim()));
			  ps.setString(2, componentImpl.getModuleNumber());			  		 	  

			 ps.addBatch();
		  }	//end while	  
		 
		  //update the database
		 ps.executeBatch();
		  
		   
		   
   }catch (Exception e) {
        throw new PCSParsingException("Could not update Qty for Modules in JOBmatl tbale using ExecuteBatch"+e);
    }finally{
	         try {        
	             if (ps != null) {
	            	ps.close();
	             }
	           } catch (SQLException e) {
	            log.error("Could not close prepared statement", e);
	          }
	        }//end finally
   }//end of method updateJobMatlQtyForModules
   
	/**  
	 * Call stored pricedure to move jobmatl entries from temporary tables to prod jobmatl table 
	 * 
	 * 
	 */
	public synchronized void moveTempJobMaterialToProd(SerialNumber serialNum, Connection conn)throws PCSParsingException
	{
		 String methodName= "moveTempJobMaterialToProd()";
		 PreparedStatement cStat = null;
		 log.info("Entry into method ="+methodName);

		 
		 String zeroString="0";
		 
		try{
			 			 
			 cStat = conn.prepareCall("{call DTNA_PostJobmatl (?,?)}");	
			 
	
	            // log.info("ParsingBusinessQueryImpl:STEP 4. INSERTION INTO THE JOBMATL TABLE ##### "+(componentImpl.getCompItemNo()).trim());				

				 //SET THE values for the insertion 
				 cStat.setString(1, get10DigJobNo(serialNum.getCmcsNumber().trim()));//job number - PVR			
				 cStat.setString(2,zeroString);//suffix
				 cStat.execute();

	
		 }catch (SQLException se) {
			 	// TODO: handle exception
			 	log.error("PCSParsingBusinessQueryImpl:moveTempJobMaterialToProd():Could not execute Callable in Batch:"+se);
		      throw new PCSParsingException("Could not execute Callable in Batch: " + se);
		}catch (Exception e){
				log.error("PCSParsingBusinessQueryImpl:moveTempJobMaterialToProd():Could not execute Callable in Batch:"+e);
				//TODO: handle exception
			    throw new PCSParsingException("Could not execute Callable in Batch: " + e);
		} finally {				       
				   try {
					     if (null!=cStat){ 
				    		  cStat.close();
				    	  }
			   	   } catch (SQLException e1) {
				    	  throw new PCSParsingException("Could not close connection", e1);
			       }

			    }	//end finally	    
			  log.info("Exit From Method ="+methodName);
				
		}//end of method moveTempJobMaterialToProd
	   
   
	
}//class closing	


	
	

