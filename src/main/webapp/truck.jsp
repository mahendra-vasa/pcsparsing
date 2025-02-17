<%@ page import="com.freightliner.pcsparsing.*"%>

<%@ taglib uri="/jstl/core" prefix="c" %>
<%@ taglib uri="/struts/html" prefix="html" %>

<%-- TODO: use EL instead of scriplet values  --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>PCS Parsing</title>
	<link href="theme/ftl.css" rel="stylesheet" type="text/css">
  <script language="JavaScript">
    function selectTruck() {
      serialNumber = document.truckForm.serialNumbers.value;
      cwo = document.truckForm.cwo.value;
      department = document.truckForm.department.value;
      ordered = document.truckForm.ordered.value;
      url = 'modules.do?serialNumber=' + serialNumber +
        '&cwo=' + cwo +
        '&department=' + department +
        '&ordered=' + ordered;
			window.modules_frame.document.write("<p><p align='center'><i>loading ...</i></p>");
			window.modules_frame.document.close();
			document.all.modules_progress.width=200;
			document.all.modules_progress.height=13;
      window.modules_frame.location=url;
			window.boms_frame.document.write("<p><p align='center'><i>loading ...</i></p>");
			window.boms_frame.document.close();
			document.all.boms_progress.width=200;
			document.all.boms_progress.height=13;
      window.boms_frame.location='boms.do';
    }
  </script>
</head>

<body>

<html:form action="/truck">
<div class="tabs">
  <span class="tab" id="selected_tab">Browse Spec</span><span class="tab"><a href="track.do">Track Truck</a></span><span class="tab"><a href="parse.do">Parse Truck</a></span>
</div><div class="c_bluebar"><span class="bluebar_label">Truck #</span><span></span>
    <span style="vertical-align: ;"><html:select
            property="serialNumbers"
            onchange="javascript:selectTruck()"
            styleId="t_combo">
          <html:options collection="serialNumbers" property="number"/>
        </html:select></span>
    <span class="bluebar_label">DLU Spec:</span>
    <span class="bluebar_label" id="dlu"></span></div><TABLE>
		<TR>
      <td>
        <table style="width: 100px">
          <tr>
            <TD>
              <span id="t_dialog_info_label">CWO:</span>
            </TD>
            <td>
              <SELECT id="t_combo" onchange="javascript:selectTruck()" name="cwo">
                <OPTION value="<%= HasCWOSearchCriteria.ALL.getKey() %>" selected><%= HasCWOSearchCriteria.ALL.getDisplayName() %></OPTION>
                <OPTION value="<%= HasCWOSearchCriteria.ONLY.getKey() %>"><%= HasCWOSearchCriteria.ONLY.getDisplayName() %></OPTION>
                <OPTION value="<%= HasCWOSearchCriteria.NOT.getKey() %>"><%= HasCWOSearchCriteria.NOT.getDisplayName() %></OPTION>
              </SELECT>
             </TD>
            <td>
             <span id="t_dialog_info_label" style="margin-left: 8px">Dept:</span>
            </TD>
            <td>
              <SELECT id="t_combo" onchange="javascript:selectTruck()" name="department">
                <OPTION value="<%= DepartmentSearchCriteria.ALL.getKey() %>" selected><%= DepartmentSearchCriteria.ALL.getDisplayName() %></OPTION>
                <OPTION value="<%= DepartmentSearchCriteria.BODY.getKey() %>"><%= DepartmentSearchCriteria.BODY.getDisplayName() %></OPTION>
                <OPTION value="<%= DepartmentSearchCriteria.CHASSIS.getKey() %>"><%= DepartmentSearchCriteria.CHASSIS.getDisplayName() %></OPTION>
                <OPTION value="<%= DepartmentSearchCriteria.ELECTRICAL.getKey() %>"><%= DepartmentSearchCriteria.ELECTRICAL.getDisplayName() %></OPTION>
              </SELECT>
            </TD>
            <td>
              <span id="t_dialog_info_label" style="margin-left: 8px">Ordered:</span>
            </TD>
            <td>
              <SELECT id="t_combo" onchange="javascript:selectTruck()" name="ordered">
                <OPTION value="<%= IsOrderedSearchCriteria.ALL.getKey() %>" selected><%= IsOrderedSearchCriteria.ALL.getDisplayName() %></OPTION>
                <OPTION value="<%= IsOrderedSearchCriteria.ONLY.getKey() %>"><%= IsOrderedSearchCriteria.ONLY.getDisplayName() %></OPTION>
                <OPTION value="<%= IsOrderedSearchCriteria.NOT.getKey() %>"><%= IsOrderedSearchCriteria.NOT.getDisplayName() %></OPTION>
              </SELECT>
            </td>
          </tr>
        </table>
      </TD>
			<TD></TD>
      <TD></TD>
		</TR>
    <tr>
      <td colspan="3"  style="height: 8px"></td>
    </tr>
		<TR>
			<TD><IFRAME src="modules.do" name="modules_frame"
        frameborder="0" hspace="0" vspace="0" marginheight="0" marginwidth="0"
        scrolling="no" style="width: 470; height:460;"></IFRAME></TD>
			<TD></TD>
			<TD><IFRAME src="boms.do" name="boms_frame" width="100%"
        frameborder="0" hspace="0" vspace="0" marginheight="0" marginwidth="0"
        scrolling="no" style="width: 470; height:460;"></IFRAME></TD>
    </TR>
		<TR>
			<TD><img src="progress.gif" id="modules_progress" height="13" width="200"></TD>
			<TD></TD>
			<TD><img src="progress.gif" id="boms_progress" height="13" width="200"></TD>
    </TR>
    <tr>
    	<td><iframe src="empty.html" name="empty_frame"
    		frameborder="0" hspace="0" vspace="0" 
    		marginheight="0" marginwidth="0"
    		style="width: 0; height:0;"></iframe></td>
    	<td></td>
    	<td></td>
    </tr>
  </TABLE>

</html:form>
</body>

</html>