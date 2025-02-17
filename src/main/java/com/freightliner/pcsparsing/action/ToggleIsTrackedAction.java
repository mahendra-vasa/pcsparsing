package com.freightliner.pcsparsing.action;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.service.SerialNumberService;

/**
 * This class controls the action of toggling isTracked on track.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class ToggleIsTrackedAction extends Action {
	
	private static final Log log = LogFactory.getLog(ToggleIsTrackedAction.class);
	
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// @TODO: make string literals constants (form?)
		String serialNumber = request.getParameter("serialNumber");
		if (log.isDebugEnabled()) log.debug("Toggle isTracked for " + serialNumber);
		SerialNumberService.toggleIsTracked(serialNumber);
		if (log.isDebugEnabled()) log.debug("isTracked set");
		response.sendRedirect("empty.html");
		return null;
	}

}
