package com.freightliner.pcsparsing.action;

import java.util.List;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.struts.action.*;

import com.freightliner.pcsparsing.service.SerialNumberFinder;

/**
 * This class controls the action of displaying tracking info track.jsp.
 * 
 * @author   jftl8v
 *  
 */
public class ViewTrackAction extends Action {

  private static final Log log = LogFactory.getLog(ViewTrackAction.class);

  public ActionForward execute(
      ActionMapping actionMapping,
      ActionForm actionForm,
      HttpServletRequest request,
      HttpServletResponse response)
      throws Exception {

      List serialNumbers = SerialNumberFinder.find();
      log.debug("Found " + serialNumbers.size() + " serial numbers");

      request.setAttribute("serialNumbers", serialNumbers);
      return actionMapping.findForward("viewTrack");
    }
}
