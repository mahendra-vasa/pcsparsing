<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.freightliner.pcsparsing.*,
                 java.util.*" %>

<%@ taglib uri="/jstl/core" prefix="c" %>
<%@ taglib uri="/struts/html" prefix="html" %>

<HTML>
<HEAD>

  <META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <META http-equiv="Content-Style-Type" content="text/css">
  <LINK href="theme/ftl.css" rel="stylesheet" type="text/css">
  <TITLE>Modules</TITLE>
  <script language="JavaScript">
    function selectModule(row, module) {
      if (window.selectedRow) {
        window.selectedRow.bgColor = 'white';
      }
      window.selectedRow = row;
      window.selectedRow.bgColor = 'silver';
			parent.boms_frame.document.write("<p><p align='center'><i>loading ...</i></p>");
			parent.boms_frame.document.close();
			parent.document.all.boms_progress.width=200;
			parent.document.all.boms_progress.height=13;
      parent.boms_frame.location='boms.do';
      parent.boms_frame.location='boms.do?moduleNumber=' + module + '&serialNumber=' + parent.truckForm.serialNumbers.value;
    }

    function orderModule(module) {
			parent.empty_frame.location.href='orderModule.do?serialNumber=' + parent.truckForm.serialNumbers.value + '&moduleNumber=' + module;
    }

    function onLoad(concurrentUsers) {
      stopProgress();
      parent.document.all.dlu.innerText='<%= request.getAttribute("dlu") %>';
      if (concurrentUsers != null && concurrentUsers != "") {
        var warning = "Warning. Other users have ordered modules from this serial number in the past 30 minutes. The user's remote address is " + concurrentUsers;
        alert(warning);
      }
    }

    function stopProgress() {
    	parent.document.all.modules_progress.width=0;
    	parent.document.all.modules_progress.height=0;
    }


  </script>
</HEAD>
<%
  String concurrentUsersList = "";
  Collection concurrentUsers = (Collection) request.getAttribute("concurrentUsers");
  String delimiter = "";
  for (Iterator iterator = concurrentUsers.iterator(); iterator.hasNext();) {
    UserAction userAction = (UserAction) iterator.next();
    concurrentUsersList = concurrentUsersList + delimiter + userAction.getUser().trim();
    delimiter = ", ";
  }
%>
<body id="iframe" onLoad="onLoad('<%= concurrentUsersList %>')">
  <html:form action="modules">
    <TABLE width="470" id="header_table">
        <thead>
          <TR>
            <th width="24"></th>
            <th width="24"></th>
            <th width="12"></th>
            <th width="23">Mod</th>
            <th width="12"></th>
            <th width="235">Description</th>
            <th width="12"></th>
            <th width="64">Dept</th>
            <th width="12"></th>
            <th width="24" id="title_centered">Ord</th>
            <th width="12">&nbsp;</th>
          </TR>
        </thead>
</TABLE>

    <div id="tbl-container">
    <TABLE width="442" id="grid">
        <tbody>

        <%-- Iterate with scriptlet code to make <tr> onclick
              TODO: make this a tag --%>
        <%
          Collection modules = (Collection) request.getAttribute("modules");
          Iterator iter = modules.iterator();
          while (iter.hasNext()) {
            Module module = (Module) iter.next();
        %>
					<tr onClick="selectModule(this, '<%= module.getNumber() %>');" class="module">
 						<TD width="24" id="icon">
              <% if (!module.isStatusValid()) { %>
                <img src="warn.gif">
              <% } %>
            </TD>
 						<TD width="24" id="icon">
              <% if (module.isRevisionLevelUpdated()) { %>
                <img src="caution.gif">
              <% } %>
            </TD>
						<TD width="12"></TD>
						<TD width="23">
              <%= module.getNumber() %>
            </TD>
						<TD width="12"></TD>
						<TD width="235"><%= module.getDescription() %></TD>
						<TD width="12"></TD>
						<TD width="64">
              <%
                Collection depts = module.getDepartments();
                Iterator iter2 = depts.iterator();
                while (iter2.hasNext()) {
                  Department department = (Department) iter2.next();
              %>
                <%= department.getName() %><br>
              <% } %>
            </TD>
						<TD width="12"></TD>
						<TD width="24" id="icon">
                <input type="checkbox" <% if (module.isOrdered()) { %>checked<% } %>
                  onClick="orderModule('<%= module.getNumber() %>');">
            </TD>
					</TR>
					<tr>
						<TD height="4"></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
					</tr>


        <% } %>
        </tbody>
			</TABLE>
	
      </div>
  </html:form>
</BODY>
</HTML>