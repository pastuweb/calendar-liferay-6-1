package net.appuntivari.calendar.model;

public class CalendarEvent {

	private long eventId;
	
	private int startDay;
	private int startMonth;
	private int startYear;
	private int startHour;
	private int startMinutes;
	
	private int endDay;
	private int endMonth;
	private int endYear;
	private int endHour;
	private int endMinutes;
	
	private String title;
	private String description;
	private String location;
	
	private int priority;
	private String tipoEvento;
	private String deviceRemind;
	
	private String repeatDescrition;
	
	public String getDeviceRemind() {
		return deviceRemind;
	}
	public void setDeviceRemind(String deviceRemind) {
		this.deviceRemind = deviceRemind;
	}
	public String getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public int getTipoRemind() {
		return tipoRemind;
	}
	public void setTipoRemind(int tipoRemind) {
		this.tipoRemind = tipoRemind;
	}
	private int tipoRemind;
	
	public int getStartDay() {
		return startDay;
	}
	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}
	public int getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}
	public int getStartYear() {
		return startYear;
	}
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	public int getStartHour() {
		return startHour;
	}
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}
	public int getStartMinutes() {
		return startMinutes;
	}
	public void setStartMinutes(int startMinutes) {
		this.startMinutes = startMinutes;
	}
	public int getEndDay() {
		return endDay;
	}
	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}
	public int getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(int endMonth) {
		this.endMonth = endMonth;
	}
	public int getEndYear() {
		return endYear;
	}
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
	public int getEndHour() {
		return endHour;
	}
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	public int getEndMinutes() {
		return endMinutes;
	}
	public void setEndMinutes(int endMinutes) {
		this.endMinutes = endMinutes;
	}
	public String getTitle() {
		return title;
	}
	public String getRepeatDescrition() {
		return repeatDescrition;
	}
	public void setRepeatDescrition(String repeatDescrition) {
		this.repeatDescrition = repeatDescrition;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	
}
