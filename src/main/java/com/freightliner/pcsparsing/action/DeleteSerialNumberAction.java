package com.freightliner.pcsparsing.action;

import java.util.List;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.service.*;

/**
 * This class controls the action of deleting a serial number on track.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class DeleteSerialNumberAction extends Action {
  
	private static final Log log = LogFactory.getLog(DeleteSerialNumberAction.class);
	
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// @TODO: make string literals constants (form?)
		String serialNumber = request.getParameter("serialNumber");
		if (log.isDebugEnabled()) log.debug("Deleting " + serialNumber);
		SerialNumberService.delete(serialNumber);
		if (log.isDebugEnabled()) log.debug("Deleting");

    List serialNumbers = SerialNumberFinder.find();
    request.setAttribute("serialNumbers", serialNumbers);

    return actionMapping.findForward("viewTrack");
	}


}
