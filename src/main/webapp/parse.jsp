<%@ page import="java.util.Collection,
                 java.util.Iterator,
                 java.text.SimpleDateFormat,
                 com.freightliner.pcsparsing.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>
			PCS Parsing
		</title>
	<link href="theme/ftl.css" rel="stylesheet" type="text/css">
        <script language="JavaScript">
            function toggleIsParsed(serialNumber) {
              empty_frame.location.href='toggleIsParsed.do?serialNumber=' + serialNumber;
            }

            function cancelParse() {
              if (confirm("Cancel parsing?")) {
                document.location.href='parse.do?cancel=Cancel';
              }
            }

            function forceQuitParsing() {
              if (confirm("Force quit parsing?")) {
                document.location.href='parse.do?forceQuit=forceQuit';
              }
            }

        </script>
	</head>
	<body>
		<form name="parseForm">
			<div class="tabs">
			  <span class="tab"><a href="truck.do">Browse Spec</a></span><span class="tab"><a href="track.do">Track Truck</a></span><span class="tab" id="selected_tab"><a href="parse.do">Parse Truck</a></span>
			</div><div class="c_bluebar">
				&nbsp;
			</div>
			<div style="text-align: center; width: 100%;">
				<div class="table_frame">
					<table>
						<thead>
							<tr>
								<th width="8">
								</th>
								<th width="40">
									Parsed
								</th>
								<th width="12">
								</th>
								<th width="100">
									Serial #
								</th>
								<th width="12">
								</th>
								<th width="100">
									Project #
								</th>
								<th width="12">
								</th>
								<th width="100">
									Last Parsed
								</th>
								<th width="12">
								</th>
							</tr>
						</thead>
					</table>
					<div style="overflow: auto;">
						<table id="grid">
							<tbody>
        <%-- Iterate with scriptlet code to make <tr> onclick
              TODO: make this a tag --%>
        <%
          Collection serialNumbers = (Collection) request.getAttribute("serialNumbers");
          Iterator iter = serialNumbers.iterator();
          while (iter.hasNext()) {
            SerialNumber serialNumber = (SerialNumber) iter.next();
        %>
								<tr>
									<td width="8">
									</td>
									<td width="40">
                      <input type="checkbox" <% if (serialNumber.isParsed()) { %>checked<% } %>
                          onClick="toggleIsParsed('<%= serialNumber.getNumber() %>');"
                          <% if (!serialNumber.isValid()) { %> disabled="true"<% } %>>
									</td>
									<td width="12">
									</td>
									<td width="100">
										<%= serialNumber.getNumber() %>
									</td>
									<td width="12">
									</td>
									<td width="100">
										<%= serialNumber.getCmcsNumber() %>
									</td>
									<td width="12">
									</td>
									<td width="100">
										<%= serialNumber.getSpecLastUpdatedFormatted() %>
 									</td>
									<td width="12">
									</td>
								</tr>
								<tr>
									<td colspan="7">
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
			<div style="text-align: center; margin-top: 16px;">
				<%= message %>
			</div>
      <% } %>
			<div style="text-align: center; margin-top: 16px;">
        <% ParseStatus parseStatus = (ParseStatus) request.getAttribute("parseStatus");
           int parseState = parseStatus.getState();
        %>

        <% if (parseState == ParseStatus.IDLE ) { %>
          <% if (parseStatus.getCompletion() != null) { %>
            Finished Parsing: <%= (new SimpleDateFormat("hh:mm:ss, EEE, M/d/y")).format(parseStatus.getStart()) %>
            <br><br>
          <% } %>
          <% if (parseStatus.getError() != null) { %>
            Could not Complete Parse: <%= parseStatus.getError().getMessage() %>
            <br><br>
          <% } %>
  				<input type="submit" name="parse" value="Parse">
        <% } %>

        <% if (parseState == ParseStatus.PARSING) { %>
          Parsed <%= parseStatus.getParsed() %> of <%= parseStatus.getToParse() %> trucks
          <br>
          <%= parseStatus.getMessage() %>
          <br><br>
          Started <%= (new SimpleDateFormat("hh:mm:ss, EEE, M/d/y")).format(parseStatus.getStart()) %>
          <br><br>
  				<input type="button" name="cancel" value="Cancel" onclick="cancelParse()">
          &nbsp;&nbsp;&nbsp;
  				<input type="submit" name="refresh" value="Refresh">
        <%  } %>
        <% if (parseState == ParseStatus.STOPPING) { %>
          Stopping Parsing (May Take Several Minutes)
          <br><br>
  				<input type="button" name="forceQuit" value="Force Quit" onclick="forceQuitParsing()">
          &nbsp;&nbsp;&nbsp;
  				<input type="submit" name="refresh" value="Refresh">
        <%  } %>
			</div>
		</div>
		</form>
        <iframe src="empty.html" name="empty_frame"
    		frameborder="0" hspace="0" vspace="0"
    		marginheight="0" marginwidth="0"
    		style="width: 0; height:0;"></iframe>
	</body>
</html>
