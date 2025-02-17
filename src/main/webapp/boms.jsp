<%@ page import="java.util.*,
                 com.freightliner.pcsparsing.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ taglib uri="/jstl/core" prefix="c" %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/ftl.css" rel="stylesheet"
	type="text/css">
<TITLE>BOMs</TITLE>

  <script language="JavaScript">
    function stopProgress() {
    	parent.document.all.boms_progress.width=0;
    	parent.document.all.boms_progress.height=0;
    }
  </script>

</HEAD>
<BODY id="iframe" onLoad="stopProgress();">

    <TABLE width="462" >
        <thead>
          <tr>
            <th width="2"></th>
            <th width="94">Part Number</th>
            <th width="12"></th>
            <th width="198">Description</th>
            <th width="12"></th>
            <th width="14">T</th>
            <th width="12"></th>
            <th width="14">S</th>
            <th width="12"></th>
            <th width="18" id="title_centered">DRL</th>
            <th width="12"></th>
            <th width="18" id="title_centered" >MRL</th>
            <th width="10"></th>
            <th width="18" id="title_centered" >Qty</th>
            <th width="16"></th>
          </TR>
        </thead>
   </TABLE>   

    <div id="tbl-container">
	<TABLE id="grid" width="450" height="494" >		
        <%
          SortedSet boms = new TreeSet((Collection) request.getAttribute("boms"));
          Iterator iter = boms.iterator();
          while (iter.hasNext()) {
            BOM bom = (BOM) iter.next();
        %>
		<TR id="bom" height="9">
         	<td width="2"></td>
			<TD width="94" align="left"><%=bom.getNumber()  %></TD>
			<TD width="10"></TD>
			<TD width="192" align="left"><%= bom.getDescription()%></TD>
			<TD width="11"></TD>
            <td width="16"></td>
            <td width="14"></td>
            <td width="18"></td>
            <td width="14"></td>
            <td width="19"></td>
            <td width="12"></td>
            <td width="18"></td>
            <td width="12"></td>
            <td width="18"></td>
            
					</TR>
					<TR>
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
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
					</TR>
        <%-- Iterate with scriptlet code to make <tr> onclick
              TODO: make this a tag --%>
        <%
          SortedSet items = new TreeSet(bom.getItems());
          Iterator iter2 = items.iterator();
          while (iter2.hasNext()) {
            ComponentItem item = (ComponentItem) iter2.next();
        %>
          <% if (!item.isStatusValid()) { %>
					<tr height="9" style="background: red; color: white; vertical-align: top;">
          <% } else if (item.isRevisionLevelDifferent()) { %>
					<tr height="9" style="background: yellow; vertical-align: top;">
          <% } else {%>
          <tr height="9" style="vertical-align: top;">
          <% } %>
            <td width="2"></td>
			<TD width="94" align="left"><%=item.getNumber() %></TD>
			<TD width="10"></TD>
			<TD width="192" align="left"><%= item.getDescription()%></TD>
			<TD width="11" ></TD>
            <td width="16" align="left"><%= item.getType().getCode() %></td>
            <td width="14"></td>
            <td width="18" align="left"><%= item.getStatus() %></td>
            <td width="14"></td>
            <td width="19" align="left"><%= item.getEngRevisionLevel()%></td>
            <td width="12"></td>
            <td width="18" align="left"><%= item.getMfgRevisionLevel()%></td>
            <td width="12"></td>
            <td width="18" align="left"><%= item.getQuantity()%></td>
            
					</TR>
					<TR>
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
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
					</TR>
          <% } %>
        <% } %>
					<TR>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
						<TD></TD>
					</TR>
			</TABLE>
  </div>

</BODY>
</HTML>
