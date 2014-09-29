<%@include file="/init.jsp" %>

<link href="<%=request.getContextPath()%>/css/reset.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/css/dp_calendar.css" type="text/css" rel="stylesheet" />
<link href="<%=request.getContextPath()%>/css/demo.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/date.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dp_calendar.min.js"></script>


 <style>
    .optchk .ui-state-active.ui-state-default {
      background: #aaff89 !important;
      color:#000000 !important;
    }
    .optchk .ui-state-hover {
      background: #EFEFEF !important;
      color:#000000 !important;
    }
    .optchk .ui-state-default {
     background: #f76767 !important;
     color:#000000 !important;
    }
 </style>
  
<div class="stileCalendarPortlet">
	<liferay-ui:error key="campi-richiesti" message="Alcuni campi sono Obbligatori. Ri-Controllare!" />
	<liferay-ui:success key="evento-creato" message="Evento Creato con Successo" />
	<liferay-ui:success key="evento-elimato" message="Evento Eliminato con Successo" />
	
	<div style="text-align:center;background:#FFFFCC !important;">
		
		<h2 style="color:#FF0000;">
			<img src="<%=request.getContextPath()%>/images/calendar.png" alt="Calendario" style="width:80px;"/>
			Your <span style="color:#000000;"> Calendar</span>
		</h2>

	</div>