package net.appuntivari.calendar.portlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import net.appuntivari.calendar.model.CalendarEvent;
import net.appuntivari.calendar.utils.CalendarUtil;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.cal.DayAndPosition;
import com.liferay.portal.kernel.cal.Duration;
import com.liferay.portal.kernel.cal.TZSRecurrence;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.calendar.model.CalEvent;
import com.liferay.portlet.calendar.service.CalEventLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class CalendarViewPortlet extends MVCPortlet {
		 
	private static Logger log = Logger.getLogger(CalendarViewPortlet.class);
	
	protected String calendarJSP = "/calendar-view/calendar.jsp";
	SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
	
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		String exportPdf = ParamUtil.getString(request, "exportPdf");
		String username = ParamUtil.getString(request, "username");
		long userId = ParamUtil.getLong(request, "userId");
		long groupId = ParamUtil.getLong(request, "groupId");
		log.info("exportPdf - "+exportPdf);
		
		//Font
		Font fontRedBold18 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.RED);
		Font fontRedBold16 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.RED);
		Font fontBlackBold16 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
		Font fontBlueBold16 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLUE);
		Font fontBlack16 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.BLACK);
		Font fontBlack12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream portletOutputStream=null;
		Document document=null;
		
		List<CalendarEvent> eventsList = new ArrayList<CalendarEvent>();
		
		try{
			
			
			document = new Document();
			PdfWriter.getInstance(document, baos);
			 
			
			document.open();
			Paragraph p1 = new Paragraph("Calendar", fontRedBold18);
			p1.setAlignment(Element.ALIGN_CENTER);
			document.add(p1);
			Paragraph p11 = new Paragraph(username, fontBlackBold16);
			p11.setAlignment(Element.ALIGN_CENTER);
			document.add(p11);
			Paragraph p2 =  new Paragraph("Created by: Pasturenzi Francesco", fontBlack12);
			p2.setAlignment(Element.ALIGN_RIGHT);
			document.add(p2);
			document.add( Chunk.NEWLINE );
			Paragraph p3 = new Paragraph("Export type", fontBlueBold16);
			p3.setAlignment(Element.ALIGN_CENTER);
			document.add(p3);
			
			if(exportPdf.equals("today_tomorrow")){
				Paragraph p4 = new Paragraph("TODAY and TOMORROW events", fontBlackBold16);
				p4.setAlignment(Element.ALIGN_CENTER);
				document.add(p4);
				eventsList = CalendarUtil.getEventsForExports(userId, groupId, "today_tomorrow");
			}else if(exportPdf.equals("current_month")){
				Paragraph p4 = new Paragraph("Current MONTH events", fontBlackBold16);
				p4.setAlignment(Element.ALIGN_CENTER);
				document.add(p4);
				eventsList = CalendarUtil.getEventsForExports(userId, groupId, "current_month");
			}else if(exportPdf.equals("next_events")){
				Paragraph p4 = new Paragraph("All NEXT events", fontBlackBold16);
				p4.setAlignment(Element.ALIGN_CENTER);
				document.add(p4);
				eventsList = CalendarUtil.getEventsForExports(userId, groupId, "next_events");
			}else if(exportPdf.equals("past_events")){
				Paragraph p4 = new Paragraph("All PAST events", fontBlackBold16);
				p4.setAlignment(Element.ALIGN_CENTER);
				document.add(p4);
				eventsList = CalendarUtil.getEventsForExports(userId, groupId, "past_events");
			}
			log.info("eventsList SIZE - "+eventsList.size()+" items");
			
			document.add( Chunk.NEWLINE );
			document.add( Chunk.NEWLINE );
			//create Header Table
			PdfPTable table = new PdfPTable(7); 
			table.setWidthPercentage(100);
			PdfPCell cell_eventID = new PdfPCell();
			cell_eventID.addElement(new Paragraph("Event ID", fontRedBold16));
			cell_eventID.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell_eventID);
			
			PdfPCell cell_title = new PdfPCell();
			cell_title.addElement(new Paragraph("Title", fontBlueBold16));
			cell_title.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell_title);
			
			PdfPCell cell_location = new PdfPCell();
			cell_location.addElement(new Paragraph("Location", fontRedBold16));
			cell_location.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell_location);
			
			PdfPCell cell_type = new PdfPCell();
			cell_type.addElement(new Paragraph("Type", fontRedBold16));
			cell_type.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell_type);
			
			PdfPCell cell_remind = new PdfPCell();
			cell_remind.addElement(new Paragraph("Device Remind", fontRedBold16));
			cell_remind.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell_remind);
			
			PdfPCell cell_startDate = new PdfPCell();
			cell_startDate.addElement(new Paragraph("StartTime", fontBlueBold16));
			cell_startDate.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell_startDate);
			PdfPCell cell_endDate = new PdfPCell();
			cell_endDate.addElement(new Paragraph("EndTime", fontBlueBold16));
			cell_endDate.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell_endDate);
			 
			//recupero dati dal db
			for(CalendarEvent item : eventsList){
				
				table.addCell(new Paragraph(String.valueOf(item.getEventId()), fontBlack12));
				table.addCell(new Paragraph(item.getTitle(), fontBlack12));
				table.addCell(new Paragraph(item.getLocation(), fontBlack12));
				table.addCell(new Paragraph(item.getTipoEvento(), fontBlack12));
				table.addCell(new Paragraph(item.getDeviceRemind(), fontBlack12));
				
				table.addCell(new Paragraph(item.getStartDay()+"/"+(item.getStartMonth()+1)+"/"+item.getStartYear()+" "+item.getStartHour()+":"+item.getStartMinutes(), fontBlack12));
				table.addCell(new Paragraph(item.getEndDay()+"/"+(item.getEndMonth()+1)+"/"+item.getEndYear()+" "+item.getEndHour()+":"+item.getEndMinutes(), fontBlack12));

			}
			
			document.add(table);
			document.close();
			 			
			
			response.setContentType("application/pdf");
			response.setProperty(HttpHeaders.CONTENT_DISPOSITION,"attachement;filename="+username+"_events.pdf");
			response.addProperty(HttpHeaders.CACHE_CONTROL,"max-age=3600, must-revalidate");
			response.setContentLength(baos.size());
			 
			log.info("PDF SIZE - "+baos.size()+" byte");
			
			portletOutputStream = (OutputStream) response.getPortletOutputStream();
			baos.writeTo(portletOutputStream);
			 
			portletOutputStream.flush();
			baos.close();
			portletOutputStream.close();

		}catch (Exception e) {
			log.info( e.getMessage());
		}

	}
	
	public void createEvent(ActionRequest request, ActionResponse response)
		        throws Exception {
		long remote_userid =  PrincipalThreadLocal.getUserId();
		
		Date today = new Date();
		SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.ENGLISH);
		
		//recupero parametri
		String dataInizio = ParamUtil.getString(request, "datepickerFrom");
		String oraInizio = ParamUtil.getString(request, "inputOraInizio");
		if(Integer.parseInt(oraInizio) >= 0 && Integer.parseInt(oraInizio) <=9){
			oraInizio = "0"+oraInizio;
		}
		String minutiInizio = ParamUtil.getString(request, "inputMinutiInizio");
		if(Integer.parseInt(minutiInizio) >= 0 && Integer.parseInt(minutiInizio) <=9){
			minutiInizio = "0"+minutiInizio;
		}
		String[] arrayDataInizio = dataInizio.split("-");
		Date dInizio = targetFormat.parse(arrayDataInizio[2]+arrayDataInizio[1]+arrayDataInizio[0]+""+oraInizio+minutiInizio+"00");
		Calendar calInizio = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calInizio.setTime(dInizio);
		
		String durataOre = ParamUtil.getString(request, "inputOre");
		String allday = ParamUtil.getString(request, "allday");
		String titolo = ParamUtil.getString(request, "titolo");
		String descrizione = ParamUtil.getString(request, "descrizione");
		String luogo = ParamUtil.getString(request, "luogo");
		String tipoEvento = ParamUtil.getString(request, "tipoEvento");
		String repeat = ParamUtil.getString(request, "repeat");
		String giornoSettimanaInRepeat = ParamUtil.getString(request, "giornoSettimana");
		String dataFineRepeat = ParamUtil.getString(request, "datepickerTo");
		String[] arrayDataFineRepeat = dataFineRepeat.split("-");
		Date dFine = targetFormat.parse(arrayDataFineRepeat[2]+arrayDataFineRepeat[1]+arrayDataFineRepeat[0]+""+oraInizio+minutiInizio+"00");
		
		String reminds = ParamUtil.getString(request, "reminds");
		
		ArrayList<String> errors = new ArrayList<String>();
		
		if (dataInizio.length() > 0 && 
				oraInizio.length() > 0 && minutiInizio.length() > 0 && durataOre.length() > 0 && 
				titolo.length() > 0 && descrizione.length() > 0 && tipoEvento.length() > 0) {

			ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
			long groupId= themeDisplay.getScopeGroupId();

			long newEventId = CounterLocalServiceUtil.increment(CalEvent.class.getName());
			CalEvent newCalEvent = CalEventLocalServiceUtil.createCalEvent(newEventId);
			log.info("Nuovo Evento. EventId = "+newCalEvent.getEventId());
			
			newCalEvent.setTitle(titolo);
			newCalEvent.setDescription(descrizione);
			newCalEvent.setCreateDate(today);
			newCalEvent.setModifiedDate(today);
			newCalEvent.setStartDate(dInizio);
			newCalEvent.setTimeZoneSensitive(true);
			newCalEvent.setUserId(remote_userid);
			newCalEvent.setType(tipoEvento);
			newCalEvent.setUserName(UserLocalServiceUtil.getUser(remote_userid).getFullName());
			newCalEvent.setCompanyId(UserLocalServiceUtil.getUser(remote_userid).getCompanyId());
			if(allday.equals("allday")){
				newCalEvent.setAllDay(true);
			}else{
				newCalEvent.setAllDay(false);
			}
			newCalEvent.setDurationHour(Integer.parseInt(durataOre));
			newCalEvent.setDurationMinute(0);
			if(reminds.endsWith("email")){
				newCalEvent.setRemindBy(1);
				newCalEvent.setFirstReminder(604800000); //un giorno prima
				newCalEvent.setSecondReminder(7200000);
			}else if(reminds.endsWith("sms")){
				newCalEvent.setRemindBy(2);
				newCalEvent.setFirstReminder(604800000); //un giorno prima
				newCalEvent.setSecondReminder(7200000);
			}else{
				newCalEvent.setRemindBy(0);
				newCalEvent.setFirstReminder(900000);
				newCalEvent.setSecondReminder(300000);
			}
			newCalEvent.setLocation(luogo);
			
			if(!repeat.equals("never")){
				newCalEvent.setRepeating(true);
				
				Calendar calFine = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				calFine.setTime(dFine);
				
					TZSRecurrence occurre = new TZSRecurrence();
					if(repeat.equals("daily")){
						occurre.setFrequency(3);
						occurre.setByDay(null);
						occurre.setByMonth(null);
					}else if(repeat.equals("weekly")){
						occurre.setFrequency(4);
						DayAndPosition[] dayAndPos = new DayAndPosition[1];
						if(giornoSettimanaInRepeat.length() == 0){
							giornoSettimanaInRepeat = "1";
						}
						dayAndPos[0] = new DayAndPosition(Integer.parseInt(giornoSettimanaInRepeat),0);
						occurre.setByDay(dayAndPos);
						occurre.setByMonth(null);
					}else if(repeat.equals("monthly")){
						occurre.setFrequency(5);
						occurre.setByDay(null);
						int[] dayMonth = new int[1];
						dayMonth[0] = calInizio.get(Calendar.DAY_OF_MONTH);
						occurre.setByMonth(dayMonth);
					}
					
					occurre.setByWeekNo(null);
					occurre.setByYearDay(null);
					occurre.setInterval(1);
					occurre.setOccurrence(0);
					occurre.setWeekStart(1);
					occurre.setDtStart(calInizio);
					occurre.setDtEnd(calFine);
					occurre.setDuration(new Duration(1, 0, 0, 0));
					occurre.setUntil(calFine);
					occurre.setTimeZone(TimeZone.getTimeZone("GMT"));
					
					newCalEvent.setRecurrenceObj(occurre);
					newCalEvent.setEndDate(dFine);
			}else{
				newCalEvent.setRepeating(false);
				newCalEvent.setRecurrenceObj(null);
				newCalEvent.setEndDate(null);
			}

			newCalEvent.setGroupId(groupId);

			//testCalEvent(newEventId, newCalEvent);
			
			CalEventLocalServiceUtil.updateCalEvent(newCalEvent);
			CalEventLocalServiceUtil.addEventResources(newCalEvent.getEventId(), false, false);//"false,false" so only the Owner can view the event
			CalEventLocalServiceUtil.updateAsset(remote_userid, newCalEvent, null, null, null);
			
			CalEvent event = CalEventLocalServiceUtil.getCalEvent(newCalEvent.getEventId());
			CalEventLocalServiceUtil.updateCalEvent(event);
			
			
					
			SessionMessages.add(request, "evento-creato");
			response.setRenderParameter("jspPage", calendarJSP);
			log.info("### Evento Creato ###");
		}else{
			SessionErrors.add(request, "campi-richiesti");
			for (String error : errors) {
                SessionErrors.add(request, error);
            }
        	response.setRenderParameter("jspPage", calendarJSP);
        	log.info("### Errore Creazione dell'Evento ###");
		}
		
		 
	}
	
	public void deleteEvent(ActionRequest request, ActionResponse response)
	        throws Exception {
		
		long eventId = ParamUtil.getLong(request, "eventId");
		
		CalEventLocalServiceUtil.deleteEvent(eventId);
		SessionMessages.add(request, "evento-eliminato");
		response.setRenderParameter("jspPage", calendarJSP);
		log.info("### Evento Eliminato "+eventId+" ###");
		
	}
	
	public void testCalEvent(long eventId, CalEvent event) throws PortalException, SystemException{
		
		log.info("\n\n### Get Info One Event "+eventId+" ###");
		log.info("Titolo "+event.getTitle());
		log.info("Descrizione "+event.getDescription());
		log.info("Start Date "+event.getStartDate());
		log.info("Durata Ora "+event.getDurationHour());
		log.info("Durata Minuti "+event.getDurationMinute());
		log.info("Tipo "+event.getType());
		
		log.info("CompanyId "+event.getCompanyId());
		log.info("GroupId "+event.getGroupId());
		
		log.info("UserId "+event.getUserId());
		log.info("Username "+event.getUserName());
		
		log.info("Remind "+event.getRemindBy()); //0= never, 1=email, 2=sms
		log.info("FirstRemind "+event.getFirstReminder());
		log.info("SecondRemind "+event.getSecondReminder());
		
		log.info("Repeat "+event.getRepeating()); //true o false
		if(event.getRecurrenceObj() != null){
			log.info("Frequency "+event.getRecurrenceObj().getFrequency());
			log.info("Interval "+event.getRecurrenceObj().getInterval());
			log.info("Occurence "+event.getRecurrenceObj().getOccurrence());
			log.info("WeekStart "+event.getRecurrenceObj().getWeekStart());
			if(event.getRecurrenceObj().getByDay() != null){
				log.info("length ByDay "+event.getRecurrenceObj().getByDay().length);
				for(int i=0; i < event.getRecurrenceObj().getByDay().length ; i++ ){
					log.info("ByDayPosition "+event.getRecurrenceObj().getByDay()[i].getDayPosition());//0
					log.info("ByDayOfWeek "+event.getRecurrenceObj().getByDay()[i].getDayOfWeek());//1=Sunday, 1 ,2 3, 4, 5, 7=Saturday
				}
			}			
			log.info("ByMonth "+event.getRecurrenceObj().getByMonth());
			if(event.getRecurrenceObj().getByMonthDay() != null){
				log.info("length ByMonthDay "+event.getRecurrenceObj().getByMonthDay().length);
				for(int i=0; i < event.getRecurrenceObj().getByMonthDay().length ; i++ ){
					log.info("ByMonthDay "+event.getRecurrenceObj().getByMonthDay()[i]);//sempre il Giorno Selezionato in Data Inizio
				}
			}
			log.info("ByWeekNo "+event.getRecurrenceObj().getByWeekNo());
			log.info("ByYearDay "+event.getRecurrenceObj().getByYearDay());
			log.info("DtStart "+event.getRecurrenceObj().getDtStart());
			log.info("DtEnd "+event.getRecurrenceObj().getDtEnd());
			log.info("Duration "+event.getRecurrenceObj().getDuration());//1 days
			log.info("Until "+event.getRecurrenceObj().getUntil());
			log.info("TimeZone "+event.getRecurrenceObj().getTimeZone());
		}
		
		log.info("### Fine Get Info One Event ###");
	}
	
	public void executeFilterTypeAgenda(ActionRequest request, ActionResponse response)
	        throws Exception {
		
		
		log.info("### executeFilterTypeAgenda ###");
		String items = ParamUtil.getString(request, "checkedTypeAgendaItems");
		long remote_userid =  PrincipalThreadLocal.getUserId();
		ThemeDisplay themeDisplay= (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		long groupId= themeDisplay.getScopeGroupId();
		
		List<CalendarEvent> lista = new ArrayList<CalendarEvent>();
		log.info(items);
		if(!items.contains("all")){
			for(String item : items.trim().split("#")){
				if(!item.trim().equals("")){
					log.info("remote_userid "+remote_userid+" groupId "+groupId+" "+item);
					List<CalendarEvent> tempList = CalendarUtil.getEventsByType(remote_userid,groupId,item);
					lista.addAll(tempList);
				}
			} 
		}else{
			log.info("all events");
			lista = CalendarUtil.getEvents(remote_userid,groupId);

		}
		
		SessionMessages.add(request, "eventi-filtrati-per-tipo-agenda");
		request.setAttribute("isEnableFilterAgenda",true);
		request.setAttribute("listEventsFiltered",lista);
		request.setAttribute("itemsSelected",items);
		response.setRenderParameter("jspPage", calendarJSP);
		
	}
	
}
