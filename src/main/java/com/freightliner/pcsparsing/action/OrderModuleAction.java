package com.freightliner.pcsparsing.action;
import javax.servlet.http.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.service.ModuleService;

/**
 * This class controls the action of order/un-order a module on module.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class OrderModuleAction extends Action {
	
	private static final Log log = LogFactory.getLog(ViewModulesAction.class);
	
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// @TODO: make string literals constants (form?)
		String serialNumber = request.getParameter("serialNumber");
		String moduleNumber = request.getParameter("moduleNumber");
		System.out.println("Setting ordered status for " + serialNumber + " "
				+ moduleNumber);
		if (log.isDebugEnabled()) {
			log.debug("Setting ordered status for " + serialNumber + " "
					+ moduleNumber);
		}
    String user = StringUtils.right(request.getRemoteAddr(), 8);
		ModuleService.setOrdered(serialNumber, moduleNumber, user);
		if (log.isDebugEnabled()) log.debug("Ordered status set");
		response.sendRedirect("empty.html");
		return null;
	}
	
}
