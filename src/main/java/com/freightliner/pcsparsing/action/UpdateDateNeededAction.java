package com.freightliner.pcsparsing.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.freightliner.pcsparsing.impl.*;
import com.freightliner.pcsparsing.service.SerialNumberFinder;

public class UpdateDateNeededAction extends Action{

	private static final Log log = LogFactory.getLog(ToggleIsParsedAction.class);
	
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String serialNumber = request.getParameter("serialNumber");
		String date = request.getParameter("date");
		boolean updateComplete = false;
	
	//   System.out.println("####Update Date Needed: Input Serial = "+ serialNumber + "  Dt Need " + date );
		if (log.isDebugEnabled()) log.debug("Update Date Needed for " + serialNumber);
		updateComplete = PCSParsingBusinessQueryImpl.updateDateNeeded(serialNumber,date);
	//  System.out.println("####Update Date Needed: After Update = "+ serialNumber + "  Dt Need " + date );
		if (log.isDebugEnabled()) log.debug("Date Needed Changed");
	//	response.sendRedirect("empty.html");
		
	    List serialNumbers = SerialNumberFinder.find();
	    request.setAttribute("serialNumbers", serialNumbers);
		if (updateComplete){
			return actionMapping.findForward("viewBOMs");
		}
		else {
			String reason = "Input Date Format is Invalid";
	 		    request.setAttribute("message", reason);
			return actionMapping.findForward("viewTrack");			
		}	
	}
}