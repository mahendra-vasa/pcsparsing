package com.freightliner.pcsparsing.action;

import java.util.List;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.impl.*;
import com.freightliner.pcsparsing.service.SerialNumberFinder;
import com.freightliner.pcsparsing.service.SerialNumberService;

public class ToggleMTCreate extends Action {
	private static final Log log = LogFactory.getLog(ToggleIsParsedAction.class);
	
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// @TODO: make string literals constants (form?)
		String serialNumber = request.getParameter("serialNumber");
		boolean updateComplete = false;
		if (log.isDebugEnabled()) log.debug("Toggle MTCreate for " + serialNumber);
		updateComplete = PCSParsingBusinessQueryImpl.toggleMTCreate(serialNumber);
		System.out.println("@@@@@ ToggleMTCreateAction: Update Complete value: " + updateComplete);
		if (log.isDebugEnabled()) log.debug("MTCreate set");
	
		
		if (updateComplete){
			System.out.println("@@@@@ ToggleMTCreateAction: True !!! ");
			response.sendRedirect("empty.html");

			}
		else {
			System.out.println("@@@@@ ToggleMTCreateAction: False !!! ");
			  String reason = "Database Error. MT Create not toggled";
			    request.setAttribute("message", reason);
			}	

	    List serialNumbers = SerialNumberFinder.find();
	    request.setAttribute("serialNumbers", serialNumbers);
	    
		return actionMapping.findForward("viewTrack");			
	}
}
