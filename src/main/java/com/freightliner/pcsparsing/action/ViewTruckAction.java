package com.freightliner.pcsparsing.action;

import java.util.*;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.SerialNumber;
import com.freightliner.pcsparsing.service.SerialNumberFinder;

/**
 * This class controls the action of displaying all the trucks on truck.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class ViewTruckAction extends Action {
  private static final Log log = LogFactory.getLog(ViewTruckAction.class);
  private String SERIAL_NUMBER = "serialNumber";

  public ActionForward execute(
    ActionMapping actionMapping,
    ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response)
    throws Exception {

    log.debug("Getting active serial numbers");
    //List serialNumbers = SerialNumberFinder.findActive();
    List serialNumbers = new ArrayList(123);
    log.debug("Found " + serialNumbers.size() + " active serial numbers");

    request.setAttribute("serialNumbers", serialNumbers);
    SerialNumber serialNumber = getSerialNumber(request, serialNumbers);
    request.setAttribute("serialNumber", serialNumber);
    return actionMapping.findForward("viewTruck");
  }

	private SerialNumber getSerialNumber(HttpServletRequest request, List serialNumbers) {
		SerialNumber serialNumber = null;
		HttpSession session = request.getSession();
		Object object = session.getAttribute(SERIAL_NUMBER);
		if (object != null) {
			serialNumber = (SerialNumber) object;
		} else if (serialNumbers.size() > 0) {
			serialNumber = (SerialNumber) serialNumbers.get(0);
		}
		session.setAttribute(SERIAL_NUMBER, serialNumber);
		return serialNumber;
	}

}
