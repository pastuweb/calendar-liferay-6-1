<%@include file="/init.jsp" %>
<%@include file="/calendar-common/header.jsp" %>
	
	<%
	long remote_userid =  PrincipalThreadLocal.getUserId();
	User utente = CalendarUtil.getCalendarUser(remote_userid);
	
	if(utente != null){		
	List<CalendarEvent> lista = CalendarUtil.getEventi(remote_userid,themeDisplay.getScopeGroupId());
	%>
	
	<portlet:actionURL name="createEvent" var="createEventURL"/>
	<portlet:actionURL name="deleteEvent" var="deleteEventURL"/>
	
	
	<div style="position:relative;">
		<div style="position:relative;">
			<div id="calendar"></div>
		</div>
		<div id="addEvent" style="text-align:center;width:100%">
			<img src="<%=request.getContextPath()%>/images/add.png" alt="Inserisci Evento" style="width:70px;cursor:pointer;"/> New Event
		</div>
		<div style="position:relative;background:#FFFFCC !important;">
			<div id="legendaColori" style="width:75%;float:left;padding-left:10px"> 
				<p><strong>Legend</strong><img src="<%=request.getContextPath()%>/images/colors.png" alt="Legenda eventi" style="width:30px;"/>:</p><br>
				<div id="altaPriorita" style="width:100px;height:20px;background:#D44D45;text-align:center;padding:5px;" class="ui-corner-all">Today</div><br>
				<div id="mediaPriorita" style="width:100px;height:20px;background:#F8B356;text-align:center;padding:5px;" class="ui-corner-all">Tomorrow</div><br> 
				<div id="bassaPriorita" style="width:100px;height:20px;background:#C6CF52; text-align:center;padding:5px;" class="ui-corner-all">Others</div>
			</div>		
			<div id="exports" style="width:20%;float:left;"> 
				<p><strong>Exports</strong> in <img src="<%=request.getContextPath()%>/images/pdf.png" alt="Download Events in PDF" style="width:30px;"/>:</p><br>
				<ul>
					<li><a href="#"> &gt; TODAY and TOMORROW events</a></li> 
					<li><a href="#"> &gt; Current MONTH events</a></li>
					<li><a href="#"> &gt; NEXT events</a></li>
					<li><a href="#"> &gt; PAST events</a></li>
				</ul>
			</div>
			<div style="clear:left;"></div>
			<div style="width:100%;text-align:center;">
				<p><strong>Info</strong> <img src="<%=request.getContextPath()%>/images/info.png" alt="Info Calendar" style="width:30px;"/>:</p><br>
				If you want to <strong>Add e New Event</strong>, you can press the (+) button.<br>
				There are <strong>differents PRIORITIES</strong> (Today Events = RED, Tomorrow Events = ORANGE, Others Event = GREEN)<br>
				You can <strong>exports the events in PDF</strong> format.<br>
			</div>
			<br>
		</div>
		
		<div style="position:absolute;bottom:3px;left:3px;">
		 <a href="http://www.pastuweb.com" target="_blank"><img src="http://www.pastuweb.com/loghi_pw/icone/pastuweb.png" width="30" alt="Created By pastuweb.com" title="Created By pastuweb.com" /></a>
		</div>
	
	</div>


	<div id="dialog-formInserisciEvento" title="Crea un Nuovo Evento">
 		<p style="border: 1px solid transparent; padding: 0.3em;">Some fields are mandatories.</p>
		<form id="formCreateEvent" action="<%= createEventURL.toString() %>" method="post">
			<div id="accordion">
				<h3> Insert: <span style="color:#FF0000 !important">Start Date</span>, <span style="color:#FF0000 !important">End Date</span>, 
				<span style="color:#FF0000 !important">duartion in hours</span> o <span style="color:#FF0000 !important">daily</span>.</h3>
				<div>
				<label for="datepickerFrom">Start Date</label>
				<input type="text" id="datepickerFrom" name="datepickerFrom">
				<br><br>
				<label for="amountOraInizio">Start Hour</label><span id="amountOraInizio" style="color:#f6931f; font-weight:bold;"></span>
				<input type="hidden" id="inputOraInizio" name="inputOraInizio" value=""/>
				<div id="slider-ora-inizio"></div>
			    <br>
			    <label for="amountMinutiInizio">Start Minute</label><span id="amountMinutiInizio" style="color:#f6931f; font-weight:bold;"></span>
			    <input type="hidden" id="inputMinutiInizio" name="inputMinutiInizio" value=""/>
				<div id="slider-minuti-inizio"></div>
			    <br>	
			    <hr>
			    <br>			
				<label for="amountOre">Duration (in hours) </label><span id="amountOre" style="color:#f6931f; font-weight:bold;"></span>
				<input type="hidden" id="inputOre"  name="inputOre" value=""/>
				<div id="slider-durata"></div>
			    <br>
			    <div class="optchk">
			    	<input type="checkbox" id="allday" name="allday" value="false"><label for="allday">All Day?</label>
				</div>
				</div>
			
				<h3> Insert: <span style="color:#FF0000 !important">Title</span>, <span style="color:#FF0000 !important">Description</span>, 
				<span style="color:#FF0000 !important">Place</span> and <span style="color:#FF0000 !important">Event Type</span>. </h3>
				<div>
					<label for="titolo">Title</label><br>
					<input type="text" name="titolo" id="titolo" size="20" />
					<br>
					<label for="descrizione">Description</label>
					<textarea id="descrizione" style="resize:none;" name="descrizione" rows="3" cols="20" ></textarea>
					<br>
					<label for="luogo">Place</label>
					<input type="text" name="luogo" id="luogo" size="20" />
					<br><br>
					<label for="tipoEvento">Event Type</label>
				    <select name="tipoEvento" id="tipoEvento" name="tipoEvento">
				      <option value="call">Call</option>
				      <option value="appointment" selected="selected">Appointment</option>
				      <option value="chat" >Chat</option>
				      <option value="event">Event</option>
				      <option value="interview">Interview</option>
				      <option value="meeting">Meeting</option>
				      <option value="net-event">Net Event</option>
				      <option value="vacation">Vacation</option>
				    </select>
				</div>

				<h3> Define a possible <span style="color:#0000FF !important">Repetition</span> (optional)</h3>
				<div>
				<br>
			    <div class="optchk">
					<div id="repeat">
						<input type="radio" id="repeat_never" name="repeat" checked="checked" value="never"><label for="repeat_never">Never</label>
						<input type="radio" id="repeat_daily" name="repeat" value="daily"><label for="repeat_daily">Daily</label>
						<input type="radio" id="repeat_weekly" name="repeat" value="weekly"><label for="repeat_weekly">Weekly</label>
						<input type="radio" id="repeat_monthly" name="repeat" value="monthly"><label for="repeat_monthly">Monthly</label>
					</div>
				</div>
					<div id="repeat_never_form" class="divRepeat">
						Do not repeat NEVER the event except SELECTED DAY.<br>
					</div>
					<div id="repeat_daily_form" style="display:none;" class="divRepeat">
						Repeat EVERY DAY.<br>
					</div>
					<div id="repeat_weekly_form" style="display:none;" class="divRepeat">
						Repeat EVERY WEEK.<br>
						Choose a day of the week:<br>
			   			<div class="optchk">
						<div id="giornoSettimana">
							<input type="radio" id="DO" name="giornoSettimana" value="1"><label for="DO">SU</label>
							<input type="radio" id="LU" name="giornoSettimana" value="2"><label for="LU">MO</label>
							<input type="radio" id="MA" name="giornoSettimana" value="3"><label for="MA">TU</label>
							<input type="radio" id="ME" name="giornoSettimana" value="4"><label for="ME">WE</label>
							<input type="radio" id="GIO" name="giornoSettimana" value="5"><label for="GIO">TH</label>
							<input type="radio" id="VE" name="giornoSettimana" value="6"><label for="VE">FR</label>
							<input type="radio" id="SA" name="giornoSettimana" value="7"><label for="SA">SA</label>
						</div>
						</div>						
					</div>
					<div id="repeat_monthly_form" style="display:none;" class="divRepeat">
						Repeat monthly.<br>
						How will the SELECTED DAY in Event Start Date.<br>
					</div>
					<div id="divDataFine" style="display:none;">
						<br>
						<label for="datepickerTo">End Date</label>
						<input type="text" id="datepickerTo" name="datepickerTo"><br>
						<span style="color:#FF0000;">(Range defined by Start Date and End Date)</span>
						<br>
					</div>
				</div>


				<h3> Define the <span style="color:#0000FF !important">Remind</span> (optional) </h3>
				<div>
					<p>Your Email: <span style="color:#ff8e05"><%=utente.getEmailAddress()%></span></p><br>
					<div>
					Remind settings fixed:<br>
					<ul>
						<li>&gt; Remind <span style="color:#0000FF">10 minutes before the event</span></li>
						<li>&gt; Remind <span style="color:#0000FF">at 17:00 of the Day Before</span></li>
					</ul>
					<br>
					If you want to set the Remind with these parameters, you choose how you remind:
					<br><br>
			    	<div class="optchk">
						<div id="reminds">
						    <input type="radio" id="remind_never" name="reminds" value="never" checked="checked"><label for="remind_never">Never</label>
						    <input type="radio" id="remind_email" name="reminds" value="email" ><label for="remind_email">Email</label>
						    <input type="radio" id="remind_sms" name="reminds" value="sms" ><label for="remind_sms">SMS (Google Cloud)</label>
						 </div>
					 </div>
					</div>
				</div>
				
			</div>
		</form>
	</div>
	<br><br>

<%
String deleteEvento = request.getContextPath()+"/images/delete.png";
%>	
<script type="text/javascript" >
var events_array = new Array();
		
		<%
		for(CalendarEvent item : lista){
			/*
			startDate: new Date(2014,08, 18, 15, 50),
			endDate: new Date(2014,08, 18),
			title: "Event 1",
			description: "Description 1 ",
			priority: 2 // 1 = Low, 2 = Medium, 3 = Urgent  (Medium di default)
			*/
			String remindBy = new String("");
			if(item.getDeviceRemind() != null){
				remindBy = " - "+item.getDeviceRemind();
			}
		%>
		
		var formDeleteEvent = "<%=item.getDescription()%> <br>(<%=item.getLocation()%>) <%=remindBy%> [<%=item.getTipoEvento()%>] <br> <strong><%=item.getRepeatDescrition()%></strong> <br> <form id=\"formDeleteEvent\" action=\"<%=deleteEventURL.toString()%>\" method=\"post\">"+
								"<input type=\"hidden\" name=\"eventId\" id=\"eventId\" value=\"<%=item.getEventId()%>\" /> <input type=\"submit\" name=\"deleteEvent\" value=\"Delete\" style=\"background:url(\"<%=deleteEvento%>\");\"/> </form> ";
		events_array.push({
				startDate: new Date(<%=item.getStartYear()%>,<%=item.getStartMonth()%>,<%=item.getStartDay()%>,<%=item.getStartHour()%>,<%=item.getStartMinutes()%>),
				endDate: new Date(<%=item.getEndYear()%>,<%=item.getEndMonth()%>,<%=item.getEndDay()%>,<%=item.getEndHour()%>,<%=item.getEndMinutes()%>),
				title: "<%=item.getTitle()%>", 
				description: formDeleteEvent, 
				priority:<%=item.getPriority()%>
		});
		
		<%
		}
		%>
</script>

<%}else{ %>
	<div class="portlet-msg-alert">
			<strong style="color:#FF0000 !important;">Calendar.</strong> The user Administrator Liferay HAS NOT ALLOWED.
	</div>
<%} %>

 <%@include file="/calendar-common/footer.jsp" %> 