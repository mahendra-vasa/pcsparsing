package com.freightliner.pcsparsing.action;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.service.SerialNumberService;

/**
 * This class controls the action of toggling isParsed on parse.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class ToggleIsParsedAction extends Action {
	
	private static final Log log = LogFactory.getLog(ToggleIsParsedAction.class);
	
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// @TODO: make string literals constants (form?)
		String serialNumber = request.getParameter("serialNumber");
		if (log.isDebugEnabled()) log.debug("Toggle isParsed for " + serialNumber);
		SerialNumberService.toggleIsParsed(serialNumber);
		if (log.isDebugEnabled()) log.debug("isParsed set");
		response.sendRedirect("empty.html");
		return null;
	}

}
