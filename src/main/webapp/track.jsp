<%@ page import="java.util.Collection,
                 java.util.Iterator,
                 java.lang.String,
                 com.freightliner.pcsparsing.impl.TrackTruckImpl,                 
                 com.freightliner.pcsparsing.SerialNumber"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>                 
<html>

	<head>
		<title>
			PCS Parsing
		</title>
  	<link href="theme/ftl.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" language="javascript" src="js/validation_fns.js"></script>

    <script language="JavaScript">

        function doDelete(serialNumber) {
          if (confirm("Really mark " + serialNumber + " for deletion?")) {
            mainForm.action='deleteSerialNumber.do';
            mainForm.serialNumber.value=serialNumber;
            mainForm.submit();
          }
        }

        function toggleIsTracked(serialNumber) {
          empty_frame.location.href='toggleIsTracked.do?serialNumber=' + serialNumber;
        }

        function toggleMTCreate(serialNumber) {
          empty_frame.location.href='toggleMTCreate.do?serialNumber=' + serialNumber;
        }        
       
        function updateDateNeeded(serialNumber,date) {
        
          if (!isDate(date)) {
	          if(alert("Date format is invalid for serial " + serialNumber)) {
	            mainForm.action='track.do';
    	        mainForm.serialNumber.value=serialNumber;
        	    mainForm.submit();
	          }
          }
          else {
            mainForm.action=('updateDateNeeded.do?serialNumber=' + serialNumber + ' &date=' + date + ' &actionCode=updateDateNeeded');
            mainForm.submit();
          }
        }         

		
    </script>
	</head>
        <div style="margin-top: 16px;text-align: right;">
   	      <input type="submit" name="Date Update" value="Upd Dates">          
        </div>
	  <body>        	

       <form action="addSerialNumber.do" method="post" name="mainForm">

      <div class="tabs">
  			<span class="tab"><a href="truck.do">Browse Spec</a></span><span class="tab" id="selected_tab"><a href="track.do">Track Truck</a></span><span class="tab"><a href="parse.do">Parse Truck</a></span>
			</div><div class="c_bluebar">
				&nbsp;
			</div>
			<div style="text-align: center; width: 100%;">
				<div class="table_frame" style="height: 264px">
					<table>
						<thead>
							<tr>
								<th width="2">
								</th>
								<th width="24" nowrap>
									Valid
								</th>
								<th width="2">
								<th width="20" nowrap>
									Track&nbsp;It
								</th>
								<th width="2">
								</th>
								<th width="60">
									Serial #
								</th>
								<th width="2">
								</th>
								<th width="60">
									Project #
								</th>
								<th width="2">
								</th>
								<th width="100">
									Description
								</th>
								<th width="20">
								</th>
								<th width="10">
								  Delete
								</th>
								<th width="2">
								</th>								
								<th width="10">
								  MT Create
								</th>
								<th width="12">
								</th>																
								<th width="10">
								  Date Needed
								</th>								
							</tr>
						</thead>
					</table>
					<div style="overflow: auto;">
						<table id="grid">
							<tbody>
        <%
          Collection serialNumbers = (Collection) request.getAttribute("serialNumbers");
          Iterator iter = serialNumbers.iterator();
          while (iter.hasNext()) {
            SerialNumber serialNumber = (SerialNumber) iter.next();
        %>
								<tr>
									<td width="2">
									</td>
                  <TD width="24" id="icon">
                    <% if (!serialNumber.isValid()) { %>
                      <img src="warn.gif">
                    <% } %>
                  </TD>
									<td width="30">
									</td>
                  <td width="80">
                    <input type="checkbox" <% if (serialNumber.isTracked()) { %>checked<% } %>
                            onClick="toggleIsTracked('<%= serialNumber.getNumber() %>');"
                            <% if (!serialNumber.isValid()) { %> disabled="true"<% } %> >
									</td>
									<td width="12">
									</td>
									<td width="80">

										<%= serialNumber.getNumber() %>
									</td>
									<td width="12">
									</td>
									<td width="80">
										<%= serialNumber.getCmcsNumber() %>
									</td>
									<td width="12">
									</td>
									<td width="200">
										<%= serialNumber.getDescription() %>
									</td>
									<td width="2">
									</td>
									<td width="30">
										<input type="button" value="X" onClick="doDelete('<%= serialNumber.getNumber() %>');">
									</td>
									<td width="20">
									</td>									
									<td width="30">
										<input type="checkbox" name="mtCreate" <% if (TrackTruckImpl.getMTCreate(serialNumber.getNumber()) > 0) { %>checked<% } %>
											onClick="toggleMTCreate('<%= serialNumber.getNumber() %>');">
									</td>									
									<td width="12">
									</td>																		
									<td width="30">
										<input type="text" name="dateNeeded" size="8" maxlength="8" value="<%= TrackTruckImpl.getDateNeeded(serialNumber.getNumber()) %>"
											onChange="updateDateNeeded('<%= serialNumber.getNumber() %>',this.value);"/>
									</td>																		
								</tr>
								<tr>
									<td colspan="10">
									</td>
								</tr>
       <% } %>
							</tbody>
						</table>
					</div>
			</div>
	      <% String message = (String) request.getAttribute("message");
    	    if (message != null && !message.equals("")) {
	      %>
    	  <div>
	        <b><%= message %></b>
    	  </div>
	      <% } %> 	      
	              

    	  
        <div style="text-align: center; margin-top: 16px;">
          <span style="font-weight: bold;">New Truck</span>
        <div style="margin-top: 16px;">
          <span style="font-weight: bold; margin-right: 8px; text-align: right; width: 200px;">
            Serial #:
          </span>
          <span style="margin-right: 8px; text-align: left;">
            <input type="text" name="serialNumber">
          </span>
        </div>
        <div style="margin-top: 16px;">
          <span style="font-weight: bold; margin-right: 8px; text-align: right; width: 200px;">
            Project #:
          </span>
          <span style="margin-right: 8px; text-align: left;">
            <input type="text" name="cmcsNumber">
          </span>
        </div>
        <div style="margin-top: 16px;">
          <span style="font-weight: bold; margin-right: 8px; text-align: right; width: 200px;">
            Description:
          </span>
          <span style="margin-right: 8px; text-align: left;">
            <input type="text" name="description">
          </span>
        </div>
        
        <div style="margin-top: 16px;">
          <input type="submit" name="Add Serial" value="Add">
        </div>
        
			</div>
		</div>
		</form>
        <iframe src="empty.html" name="empty_frame"
    		frameborder="0" hspace="0" vspace="0"
    		marginheight="0" marginwidth="0"
    		style="width: 0; height:0;"></iframe>

 	</body>
</html>
