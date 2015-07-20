package net.appuntivari.calendar.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import net.appuntivari.calendar.model.CalendarEvent;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.calendar.model.CalEvent;
import com.liferay.portlet.calendar.service.CalEventLocalServiceUtil;

public class CalendarUtil {
	
	private static Logger log = Logger.getLogger(CalendarUtil.class);
	
	public static List<CalendarEvent> getEvents(long userId, long groupId){
			Long companyId = CompanyThreadLocal.getCompanyId();
			log.info("getEvents of companyId "+companyId + " and groupId "+groupId + " userId "+userId);
			
			Calendar calStart = Calendar.getInstance();
			Calendar calEnd = Calendar.getInstance();
			Calendar calEndDate = Calendar.getInstance();
			CalendarEvent item = null;
			
			String repeatDescritpion;
			List<CalendarEvent> result = new ArrayList<CalendarEvent>(); 
			try {
				List<CalEvent> listaEventi = CalEventLocalServiceUtil.getCompanyEvents(companyId, 0, CalEventLocalServiceUtil.getCompanyEventsCount(companyId));
				for (CalEvent calEvent : listaEventi) {
					if(calEvent.getUserId() == userId && calEvent.getGroupId() == groupId){

						calStart.setTime(calEvent.getStartDate());
						calEnd.setTime(calStart.getTime());
						calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
				
						if(calEvent.getRepeating()){
							
							//exist an ENDDATE
							if(calEvent.getRecurrenceObj() != null){
							Date endDate = calEvent.getEndDate();
							calEndDate.setTime(endDate);
							calEndDate.add(Calendar.DATE, 1);
							
								while(calStart.getTime().before(calEndDate.getTime())){
										if(calEvent.getRecurrenceObj().getFrequency() == 3){ //daily
											
											repeatDescritpion = "repeat daily until "+(calEndDate.get(Calendar.DATE)-1)+"/"+calEndDate.get(Calendar.MONTH)+"/"+calEndDate.get(Calendar.YEAR);
											
											item = createCalendarEvent(calEvent.getEventId(), calEvent.getTitle(),calEvent.getDescription(),calEvent.getLocation(),
													calStart.get(Calendar.DATE),calStart.get(Calendar.MONTH),calStart.get(Calendar.YEAR),calStart.get(Calendar.HOUR_OF_DAY),calStart.get(Calendar.MINUTE),
													calEnd.get(Calendar.DATE),calEnd.get(Calendar.MONTH),calEnd.get(Calendar.YEAR),calEnd.get(Calendar.HOUR_OF_DAY),calEnd.get(Calendar.MINUTE),
													calEvent.getRemindBy(), calEvent.getType(), repeatDescritpion
													);
											
											//aggiungo evento alla lista in Output
											result.add(item);
											
											calStart.add(Calendar.DATE, 1);
											calEnd.setTime(calStart.getTime());
											calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
										
										}else if(calEvent.getRecurrenceObj().getFrequency() == 4){ //weekly
											
											int dayOfWeek = calEvent.getRecurrenceObj().getByDay()[0].getDayOfWeek(); //1=Sunday, --- , 7=Saturday
											if(calStart.get(Calendar.DAY_OF_WEEK) == dayOfWeek){
												
												repeatDescritpion = "repeat weekly "+getDayOfWeekName(dayOfWeek)+" until "+(calEndDate.get(Calendar.DATE)-1)+"/"+calEndDate.get(Calendar.MONTH)+"/"+calEndDate.get(Calendar.YEAR);
												
												calEnd.setTime(calStart.getTime());
												calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
												
												item = createCalendarEvent(calEvent.getEventId(), calEvent.getTitle(),calEvent.getDescription(),calEvent.getLocation(),
														calStart.get(Calendar.DATE),calStart.get(Calendar.MONTH),calStart.get(Calendar.YEAR),calStart.get(Calendar.HOUR_OF_DAY),calStart.get(Calendar.MINUTE),
														calEnd.get(Calendar.DATE),calEnd.get(Calendar.MONTH),calEnd.get(Calendar.YEAR),calEnd.get(Calendar.HOUR_OF_DAY),calEnd.get(Calendar.MINUTE),
														calEvent.getRemindBy(), calEvent.getType(), repeatDescritpion
														);
											
												//aggiungo evento alla lista in Output
												result.add(item);
													
												calStart.add(Calendar.DATE, 7);
												calEnd.setTime(calStart.getTime());
												calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
												
											}else{
												
												calStart.add(Calendar.DATE, 1);
												calEnd.setTime(calStart.getTime());
												calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
												
												continue;
											}

											
										}else if(calEvent.getRecurrenceObj().getFrequency() == 5){ //monthly
											
											calStart.add(Calendar.MONTH,1);
											calEnd.setTime(calStart.getTime());
											calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
											
											repeatDescritpion = "repeat monthly at "+calStart.get(Calendar.DATE)+" until "+(calEndDate.get(Calendar.DATE)-1)+"/"+calEndDate.get(Calendar.MONTH)+"/"+calEndDate.get(Calendar.YEAR);
											
											item = createCalendarEvent(calEvent.getEventId(), calEvent.getTitle(),calEvent.getDescription(),calEvent.getLocation(),
													calStart.get(Calendar.DATE),calStart.get(Calendar.MONTH),calStart.get(Calendar.YEAR),calStart.get(Calendar.HOUR_OF_DAY),calStart.get(Calendar.MINUTE),
													calEnd.get(Calendar.DATE),calEnd.get(Calendar.MONTH),calEnd.get(Calendar.YEAR),calEnd.get(Calendar.HOUR_OF_DAY),calEnd.get(Calendar.MINUTE),
													calEvent.getRemindBy(), calEvent.getType(), repeatDescritpion
													);
											
											//aggiungo evento alla lista in Output
											result.add(item);
											
										}
										
										
								}
								
							}
						}else{
							//aggiungo evento alla lista in Output
							item = createCalendarEvent(calEvent.getEventId(), calEvent.getTitle(),calEvent.getDescription(),calEvent.getLocation(),
									calStart.get(Calendar.DATE),calStart.get(Calendar.MONTH),calStart.get(Calendar.YEAR),calStart.get(Calendar.HOUR_OF_DAY),calStart.get(Calendar.MINUTE),
									calEnd.get(Calendar.DATE),calEnd.get(Calendar.MONTH),calEnd.get(Calendar.YEAR),calEnd.get(Calendar.HOUR_OF_DAY),calEnd.get(Calendar.MINUTE),
									calEvent.getRemindBy(), calEvent.getType(), "no repeat"
									);
							result.add(item);
						}
						
						
					}
				}
			} catch (SystemException e) {
				e.printStackTrace();
			}

			return result;
	}
	
	public static List<CalendarEvent> getEventsForExports(long userId, long groupId, String exportType){
		Long companyId = CompanyThreadLocal.getCompanyId();
		log.info("getEventiForExports of companyId "+companyId + " and groupId "+groupId + " userId "+userId+" exportType "+exportType);		
		
		List<CalendarEvent> eventsList = getEvents(userId, groupId);
		log.info("eventsList "+eventsList.size());
		List<CalendarEvent> finalResult = new ArrayList<CalendarEvent>(); 
		
		//Today
		Calendar calToday = Calendar.getInstance();
		calToday.setTime(new Date());	
		int ddToday=calToday.get(Calendar.DATE);
		int MMToday=calToday.get(Calendar.MONTH)+1;
		int yyyyToday=calToday.get(Calendar.YEAR);
		log.info("calToday "+ddToday+"-"+MMToday+"-"+yyyyToday);
		
		for(CalendarEvent item : eventsList){
			//log.info("item "+item.getStartDay()+"-"+item.getStartMonth()+"-"+item.getStartYear());
			if(exportType.equals("today_tomorrow")){
				//log.info("today_tomorrow");
				Calendar calTomorrow = Calendar.getInstance();
				calTomorrow.setTime(calToday.getTime());
				calTomorrow.add(Calendar.DATE,1);
				int ddTomorrow=calTomorrow.get(Calendar.DATE);
				int MMTomorrow=calTomorrow.get(Calendar.MONTH)+1;
				int yyyyTomorrow=calTomorrow.get(Calendar.YEAR);
				
				if( (ddToday == item.getStartDay() && MMToday == (item.getStartMonth()+1) && yyyyToday == item.getStartYear()) || 
						(ddTomorrow == item.getStartDay() && MMTomorrow == (item.getStartMonth()+1) && yyyyTomorrow == item.getStartYear())  ){
					//OK
					log.info("event title added : "+item.getTitle());
					finalResult.add(item);
				}else{
					continue;
				}
				
			}else if(exportType.equals("current_month")){
				//log.info("current_month");
				if( MMToday == (item.getStartMonth()+1) && yyyyToday == item.getStartYear()){
					//OK
					log.info("event title added : "+item.getTitle());
					finalResult.add(item);
				}else{
					continue;
				}
			
			}else if(exportType.equals("next_events")){
				//log.info("next_events");
				Calendar calItem = Calendar.getInstance();
				calItem.set(Calendar.DATE, item.getStartDay());
				calItem.set(Calendar.MONTH, item.getStartMonth()-1);
				calItem.set(Calendar.YEAR, item.getStartYear());
				
				log.info("calItem "+ calItem.getTime());
				log.info("calToday "+ calToday.getTime());
				if(  calItem.getTime().after(calToday.getTime())  ){
					//OK
					log.info("event title added : "+item.getTitle());
					finalResult.add(item);
				}else{
					continue;
				}

			}else if(exportType.equals("past_events")){
				//log.info("past_events");
				Calendar calItem = Calendar.getInstance();
				calItem.set(Calendar.DATE, item.getStartDay());
				calItem.set(Calendar.MONTH, item.getStartMonth()-1);
				calItem.set(Calendar.YEAR, item.getStartYear());
							
				log.info("calItem "+ calItem.getTime());
				log.info("calToday "+ calToday.getTime());
				if(  calItem.getTime().before(calToday.getTime())  ){
					//OK
					log.info("event title added : "+item.getTitle());
					finalResult.add(item);
				}else{
					continue;
				}
			}
			
		}

		
		return finalResult;
	}
	
	public static List<CalendarEvent> getEventsByType(long userId, long groupId, String type){
		Long companyId = CompanyThreadLocal.getCompanyId();
		log.info("getEventi of companyId "+companyId + " and groupId "+groupId + " userId "+userId+" type "+type);

		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		Calendar calEndDate = Calendar.getInstance();
		CalendarEvent item = null;
		
		String repeatDescritpion;
		List<CalendarEvent> result = new ArrayList<CalendarEvent>(); 
		try {
			List<CalEvent> listaEventi = CalEventLocalServiceUtil.getCompanyEvents(companyId, 0, CalEventLocalServiceUtil.getCompanyEventsCount(companyId));
			for (CalEvent calEvent : listaEventi) {
				if(calEvent.getUserId() == userId && calEvent.getGroupId() == groupId && calEvent.getType().toLowerCase().contains(type)){

					calStart.setTime(calEvent.getStartDate());
					calEnd.setTime(calStart.getTime());
					calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
			
					if(calEvent.getRepeating()){
						
						//exist an ENDDATE
						if(calEvent.getRecurrenceObj() != null){
						Date endDate = calEvent.getEndDate();
						calEndDate.setTime(endDate);
						calEndDate.add(Calendar.DATE, 1);
						
							while(calStart.getTime().before(calEndDate.getTime())){
									if(calEvent.getRecurrenceObj().getFrequency() == 3){ //daily
										
										repeatDescritpion = "repeat daily until "+(calEndDate.get(Calendar.DATE)-1)+"/"+(calEndDate.get(Calendar.MONTH)+1)+"/"+calEndDate.get(Calendar.YEAR);
										
										item = createCalendarEvent(calEvent.getEventId(), calEvent.getTitle(),calEvent.getDescription(),calEvent.getLocation(),
												calStart.get(Calendar.DATE),calStart.get(Calendar.MONTH),calStart.get(Calendar.YEAR),calStart.get(Calendar.HOUR_OF_DAY),calStart.get(Calendar.MINUTE),
												calEnd.get(Calendar.DATE),calEnd.get(Calendar.MONTH),calEnd.get(Calendar.YEAR),calEnd.get(Calendar.HOUR_OF_DAY),calEnd.get(Calendar.MINUTE),
												calEvent.getRemindBy(), calEvent.getType(), repeatDescritpion
												);
										
										//aggiungo evento alla lista in Output
										result.add(item);
										
										calStart.add(Calendar.DATE, 1);
										calEnd.setTime(calStart.getTime());
										calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
									
									}else if(calEvent.getRecurrenceObj().getFrequency() == 4){ //weekly
										
										int dayOfWeek = calEvent.getRecurrenceObj().getByDay()[0].getDayOfWeek(); //1=Sunday, --- , 7=Saturday
										if(calStart.get(Calendar.DAY_OF_WEEK) == dayOfWeek){
											
											repeatDescritpion = "repeat weekly "+getDayOfWeekName(dayOfWeek)+" until "+(calEndDate.get(Calendar.DATE)-1)+"/"+calEndDate.get(Calendar.MONTH)+"/"+calEndDate.get(Calendar.YEAR);
											
											calEnd.setTime(calStart.getTime());
											calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
											
											item = createCalendarEvent(calEvent.getEventId(), calEvent.getTitle(),calEvent.getDescription(),calEvent.getLocation(),
													calStart.get(Calendar.DATE),calStart.get(Calendar.MONTH),calStart.get(Calendar.YEAR),calStart.get(Calendar.HOUR_OF_DAY),calStart.get(Calendar.MINUTE),
													calEnd.get(Calendar.DATE),calEnd.get(Calendar.MONTH),calEnd.get(Calendar.YEAR),calEnd.get(Calendar.HOUR_OF_DAY),calEnd.get(Calendar.MINUTE),
													calEvent.getRemindBy(), calEvent.getType(), repeatDescritpion
													);
										
											//aggiungo evento alla lista in Output
											result.add(item);
												
											calStart.add(Calendar.DATE, 7);
											calEnd.setTime(calStart.getTime());
											calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
											
										}else{
											
											calStart.add(Calendar.DATE, 1);
											calEnd.setTime(calStart.getTime());
											calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
											
											continue;
										}

										
									}else if(calEvent.getRecurrenceObj().getFrequency() == 5){ //monthly
										
										calStart.add(Calendar.MONTH,1);
										calEnd.setTime(calStart.getTime());
										calEnd.add(Calendar.HOUR_OF_DAY, calEvent.getDurationHour());
										
										repeatDescritpion = "repeat monthly at "+calStart.get(Calendar.DATE)+" until "+(calEndDate.get(Calendar.DATE)-1)+"/"+calEndDate.get(Calendar.MONTH)+"/"+calEndDate.get(Calendar.YEAR);
										
										item = createCalendarEvent(calEvent.getEventId(), calEvent.getTitle(),calEvent.getDescription(),calEvent.getLocation(),
												calStart.get(Calendar.DATE),calStart.get(Calendar.MONTH),calStart.get(Calendar.YEAR),calStart.get(Calendar.HOUR_OF_DAY),calStart.get(Calendar.MINUTE),
												calEnd.get(Calendar.DATE),calEnd.get(Calendar.MONTH),calEnd.get(Calendar.YEAR),calEnd.get(Calendar.HOUR_OF_DAY),calEnd.get(Calendar.MINUTE),
												calEvent.getRemindBy(), calEvent.getType(), repeatDescritpion
												);
										
										//aggiungo evento alla lista in Output
										result.add(item);
										
									}
									
									
							}
							
						}
					}else{
						//aggiungo evento alla lista in Output
						item = createCalendarEvent(calEvent.getEventId(), calEvent.getTitle(),calEvent.getDescription(),calEvent.getLocation(),
								calStart.get(Calendar.DATE),calStart.get(Calendar.MONTH),calStart.get(Calendar.YEAR),calStart.get(Calendar.HOUR_OF_DAY),calStart.get(Calendar.MINUTE),
								calEnd.get(Calendar.DATE),calEnd.get(Calendar.MONTH),calEnd.get(Calendar.YEAR),calEnd.get(Calendar.HOUR_OF_DAY),calEnd.get(Calendar.MINUTE),
								calEvent.getRemindBy(), calEvent.getType(), "no repeat"
								);
						result.add(item);
					}
					
					
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static CalendarEvent createCalendarEvent(long eventId, String title, String descr, String location, 
			int startDay, int startMonth, int startYear, int startHour, int startMinutes,
			int endDay, int endMonth, int endYear, int endHour, int endMinutes,
			int reminBy, String tipoEvento, String repeatDescription){
		
		
		CalendarEvent item = new CalendarEvent();
		item.setEventId(eventId);
		item.setTitle(title);
		item.setDescription(descr);
		item.setLocation(location);
		
		item.setStartDay(startDay);
		item.setStartMonth(startMonth);
		item.setStartYear(startYear);
		item.setStartHour(startHour);
		item.setStartMinutes(startMinutes);
		
		item.setEndDay(endDay);
		item.setEndMonth(endMonth);
		item.setEndYear(endYear);
		item.setEndHour(endHour);
		item.setEndMinutes(endMinutes);
		
		item.setRepeatDescrition(repeatDescription);
		
		Date today = new Date();
		Date tomorrow = new Date();
		Calendar calToday = Calendar.getInstance();
		calToday.setTime(today);
		Calendar calTomorrow = Calendar.getInstance();
		calTomorrow.setTime(tomorrow);
		calTomorrow.add(Calendar.DATE, 1);
		int priority = 1;
		if(calToday.get(Calendar.DATE) == startDay && calToday.get(Calendar.MONTH) == startMonth &&  calToday.get(Calendar.YEAR) == startYear){
			priority = 3;
		}else if(calTomorrow.get(Calendar.DATE) == startDay && calTomorrow.get(Calendar.MONTH) == startMonth &&  calTomorrow.get(Calendar.YEAR) == startYear){
			priority = 2;
		}else{
			priority = 1;
		}
		
		item.setPriority(priority);
		
		if(reminBy == 1){
			item.setDeviceRemind("Email");
		}else{
			item.setDeviceRemind(null);
		}
		
		
		if(tipoEvento.equals("appointment")){
			item.setTipoEvento("Appuntamento");
		}else if(tipoEvento.equals("call")){
			item.setTipoEvento("Call");
		}else if(tipoEvento.equals("chat")){
			item.setTipoEvento("Chat");
		}else if(tipoEvento.equals("event")){
			item.setTipoEvento("Event");
		}else if(tipoEvento.equals("interview")){
			item.setTipoEvento("Interview");
		}else if(tipoEvento.equals("meeting")){
			item.setTipoEvento("Meeting");
		}else if(tipoEvento.equals("vacation")){
			item.setTipoEvento("Vacation");
		}

		return item;
	}
	
	public static User getCalendarUser(long userId){
		 User utente;
		try {
			utente = UserLocalServiceUtil.getUser(userId);
			return utente;
		} catch (PortalException e) {
			e.printStackTrace();
			return null;
		} catch (SystemException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getDayOfWeekName(int dayOfWeek){
		if(dayOfWeek == 1){
			return "Sunday";
		}else if(dayOfWeek == 2){
			return "Monday";
		}else if(dayOfWeek == 3){
			return "Tuesday";
		}else if(dayOfWeek == 4){
			return "Wednesday";
		}else if(dayOfWeek == 5){
			return "Thursday";
		}else if(dayOfWeek == 6){
			return "Friday";
		}else if(dayOfWeek == 7){
			return "Saturday";
		}
		return null;
	}
	
	
	
}
