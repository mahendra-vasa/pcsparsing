package com.freightliner.pcsparsing.action;

import java.util.List;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.service.BOMFinder;

/**
 * This class controls the action of displaying all the BOMs 
 * assocaited with a specific module on boms.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class ViewBOMsAction extends Action {
  private static final Log log = LogFactory.getLog(ViewBOMsAction.class);

  public ActionForward execute(
    ActionMapping actionMapping,
    ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response)
    throws Exception {

    String moduleNumber = request.getParameter("moduleNumber");
    String serialNumber = request.getParameter("serialNumber");
    List boms = BOMFinder.find(serialNumber, moduleNumber);
    request.setAttribute("boms", boms);
    if (boms != null && log.isDebugEnabled()) {
      log.debug("Found " + boms.size() + " BOMs");
    }

    return actionMapping.findForward("viewBOMs");
  }

}
