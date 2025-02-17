package com.freightliner.pcsparsing.action;

import java.util.List;

import javax.servlet.http.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.service.*;

/**
 * This class controls the action of displaying all the modules 
 * assocaited with a specific serial number on modules.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class ViewModulesAction extends Action {
  private String SERIAL_NUMBER = "serialNumber";
  private static final Log log = LogFactory.getLog(ViewModulesAction.class);

  public ActionForward execute(
    ActionMapping actionMapping,
    ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response)
    throws Exception {

    // @TODO: make string literals constants (form?)
    String serialNumber = request.getParameter("serialNumber");
    String ordered = request.getParameter("ordered");
    String department = request.getParameter("department");
    String cwo = request.getParameter("cwo");
    if (log.isDebugEnabled()) {
      log.debug("Getting modules for " + serialNumber);
      log.debug("IsOrdered param " + ordered);     
      log.debug("IsOrderedSearchCriteria " + IsOrderedSearchCriteria.get(ordered));      
      log.debug("department param " + department);      
      log.debug("DepartmentSearchCriteria " + DepartmentSearchCriteria.get(department));
      log.debug("HasCWOSearchCriteria param " + cwo);      
      log.debug("HasCWOSearchCriteria " + HasCWOSearchCriteria.get(cwo));
    }
    
    if (serialNumber == null || serialNumber.equals("")) {
      SerialNumber serialNumberImpl = getDefaultSerialNumber(request);
      if (serialNumberImpl != null) {
        serialNumber = serialNumberImpl.getNumber();
      }
    }

    Truck truck = TruckFinder.find(serialNumber);
    if (truck != null) {
      request.setAttribute("dlu", truck.getLastUpdatedFormatted());
    } else {
      request.setAttribute("dlu", "");
    }
    
    List modules = ModuleFinder.getModulesOrderedByNumber(
        serialNumber,
        IsOrderedSearchCriteria.get(ordered),
        DepartmentSearchCriteria.get(department),
        HasCWOSearchCriteria.get(cwo));    
    
    log.debug("Found " + modules.size() + " modules");
    request.setAttribute("modules", modules);

    String user = StringUtils.right(request.getRemoteAddr(), 8);
    List concurrentUsers = UserActionFinder.findConcurrentUsers(serialNumber, user);
    request.setAttribute("concurrentUsers", concurrentUsers);
    log.debug("Concurrent users (" + concurrentUsers.size() + "): " + concurrentUsers);
    UserActionService.recordWarning(serialNumber, user, concurrentUsers);
    
    return actionMapping.findForward("viewModules");
  }

  private SerialNumber getDefaultSerialNumber(HttpServletRequest request) throws PCSParsingException {
		SerialNumber serialNumber = null;
		HttpSession session = request.getSession();
		Object object = session.getAttribute(SERIAL_NUMBER);
		if (object != null) {
			serialNumber = (SerialNumber) object;
		} else {
		  List serialNumbers = SerialNumberFinder.findActive();
		  if (serialNumbers.size() > 0) {
		    serialNumber = (SerialNumber) serialNumbers.get(0);
		  }
		}
		session.setAttribute(SERIAL_NUMBER, serialNumber);
		if (serialNumber != null) {
			return serialNumber;
		} else {
		  return null;
		}
  }

}
