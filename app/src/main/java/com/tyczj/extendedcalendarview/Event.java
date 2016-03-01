package com.tyczj.extendedcalendarview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.text.format.Time;

public class Event {
	private int color;
	private String name;
	private String description;
	private String location;
	private long start;
	private long end;
	private Date modified;
	private long modifiedJulian;
	private Bitmap image;
	private long eventId;
	/*private String day;
	private String hour;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}*/

	public static final int DEFAULT_EVENT_ICON = 0;
	public static final int COLOR_RED = 1;
	public static final int COLOR_BLUE = 2;
	public static final int COLOR_YELLOW = 3;
	public static final int COLOR_PURPLE = 4;
	public static final int COLOR_GREEN = 5;

	public Event() {
		super();
	}

	public Event (long id) {
		eventId = id;
	}

	public Event(long eventID, long startMills, long endMills){
		this.eventId = eventID;
		this.start = startMills;
		this.end = endMills;
	}
	
	public int getColor(){
		return color;
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	/**
	 * Get the event title
	 * 
	 * @return title
	 */
	public String getTitle(){
		return name;
	}
	
	/**
	 * Get the event description
	 * 
	 * @return description
	 */
	public String getDescription(){
		return description;
	}
	
	
	public Bitmap getImage(){
		return image;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public String getLocation(){
		return location;
	}
	
	/**
	 * Set the name of the event
	 * 
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Gets the event id in the database
	 * 
	 * @return event database id
	 */
	public long getEventId(){
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	/**
	 * Get the start date of the event
	 * 
	 * @return start date
	 */
	public String getStartDate(String dateFormat){
		DateFormat df = new SimpleDateFormat(dateFormat,Locale.getDefault());
		String date = df.format(start);
		
		return date;
	}

	public void setStartDate (Date start) {
		Calendar cal = Calendar.getInstance();
		TimeZone tz = TimeZone.getDefault();
		cal.setTime(start);
		this.start = cal.getTimeInMillis();
	}

	public void setEndDate (Date end) {
		Calendar cal = Calendar.getInstance();
		TimeZone tz = TimeZone.getDefault();
		cal.setTime(end);
		this.end = cal.getTimeInMillis();
	}
	
	/**
	 * Get the end date of the event
	 * 
	 * @return end date
	 */
	public String getEndDate(String dateFormat){
		DateFormat df = new SimpleDateFormat(dateFormat,Locale.getDefault());
		String date = df.format(end);
		
		return date;
	}


	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public long getModifiedJulian() {
		return modifiedJulian;
	}

	public void setModifiedJulian(long modifiedJulian) {
		this.modifiedJulian = modifiedJulian;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

}
