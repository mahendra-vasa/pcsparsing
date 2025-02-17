package com.freightliner.pcsparsing.action;

import java.util.List;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.PCSParsingException;
import com.freightliner.pcsparsing.impl.*;
import com.freightliner.pcsparsing.service.*;

/**
 * This class controls the action of adding serial number on track.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class AddSerialNumberAction extends Action {
  
	private static final Log log = LogFactory.getLog(AddSerialNumberAction.class);
	public static final String ACTION_UPDATE = "updateDateNeeded";
	public static final String ACTION_ADD_SERIAL = "addSerial";
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String actionCode = request.getParameter("actionCode");
		log.info("actionCode="+actionCode);

		if (actionCode == null || actionCode.equals("")){
			return addSerialNumber(actionMapping, actionForm, request, response);
		} else {
			log.info("Entry to updateDateNeeded - 1 ");			
			if (actionCode.equals(ACTION_UPDATE)) {
				log.info("Entry to updateDateNeeded - 2 ");
				return updateDateNeeded(actionMapping, actionForm, request, response);
			}
		}
		
		return actionMapping.findForward("viewTrack");
	}
	
	public ActionForward addSerialNumber(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// @TODO: make string literals constants (form?)
		String serialNumber = request.getParameter("serialNumber");
		String description = request.getParameter("description");
		String cmcsNumber = request.getParameter("cmcsNumber");
		if (log.isDebugEnabled()) log.debug("Adding " + serialNumber);
		if (SerialNumberService.isValidToAdd(serialNumber)) {
			SerialNumberService.add(serialNumber, description, cmcsNumber);
			if (log.isDebugEnabled()) log.debug("Added");
		} else {
		  String reason = SerialNumberService.getReasonInvalidToAdd(serialNumber);
	    request.setAttribute("message", reason);
			if (log.isDebugEnabled()) log.debug("Could not add " + serialNumber + ". " + reason);
		}

    List serialNumbers = SerialNumberFinder.find();
    request.setAttribute("serialNumbers", serialNumbers);

     return actionMapping.findForward("viewTrack");
	}
	
	
	public ActionForward updateDateNeeded(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String serialNumber = request.getParameter("serialNumber");
		String date = request.getParameter("date");
		String reason = "";
		boolean updateComplete = false;
	        
	
	//   System.out.println("####Update Date Needed: Input Serial = "+ serialNumber + "  Dt Need " + date );
		if (log.isDebugEnabled()) log.debug("Update Date Needed for " + serialNumber);
		
		try{
		updateComplete = PCSParsingBusinessQueryImpl.updateDateNeeded(serialNumber,date);
	//  System.out.println("####Update Date Needed: After Update = "+ serialNumber + "  Dt Need " + date );
		if (log.isDebugEnabled()) log.debug("Date Needed Changed");
		
	      List serialNumbers = SerialNumberFinder.find();
	      request.setAttribute("serialNumbers", serialNumbers);
		
		if (!updateComplete){
	       	reason = "Date could not be updated for serial " + serialNumber + " --serial not loaded to Syteline";
	 		request.setAttribute("message", reason);
	       	log.info("UpdateDateNeeded Action: MSG " + reason);	 	
			}

		}
		catch (PCSParsingException e) {
			
			PCSParsingException parseException =  new PCSParsingException(
                    "Date Could Not be updated for serial "
                    + serialNumber + ". JOB Number does not Exist. "
                    );   
        	log.error("Could not update Date " + reason); 
            
            throw parseException;  			
		}
					
		return actionMapping.findForward("viewTrack");
	}
}
