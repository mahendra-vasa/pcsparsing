package com.freightliner.pcsparsing.action;

import java.util.List;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.service.*;
import com.freightliner.pcsparsing.service.SerialNumberFinder;

/**
 * This class controls the action of displaying parsing info on parse.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class ViewParseAction extends Action {

    private static final Log log = LogFactory.getLog(ViewParseAction.class);

  public ActionForward execute(
      ActionMapping actionMapping,
      ActionForm actionForm,
      HttpServletRequest request,
      HttpServletResponse response)
      throws Exception {

    String parse = request.getParameter("parse");
    if (parse != null) {
      ParseService.parse();
    }
    
    String cancel = request.getParameter("cancel");
    if (cancel != null) {
      ParseService.cancel();
    }
    
    String forceQuit = request.getParameter("forceQuit");
    if (forceQuit != null) {
      ParseService.forceQuit();
    }
    
    List serialNumbers = SerialNumberFinder.findParseable();
    log.debug("Found " + serialNumbers.size() + " serial numbers");

    request.setAttribute("serialNumbers", serialNumbers);
    ParseStatus parseStatus = ParseService.getParseStatus();
    request.setAttribute("parseStatus", parseStatus);
    return actionMapping.findForward("viewParse");
  }

}
