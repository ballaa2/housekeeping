package com.smart.nmp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
	
	public static String convertDateToString(Date paramDate) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = sdfDate.format(paramDate);
		return strDate;
	}
	
	
	
	public static Calendar getPreviousDate(String startDate) throws ParseException{
	    Calendar c = convertStringToDate(startDate);
		c.setTime(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, -1);
		return c;
	}
	
	public static String getCurrentDay()
	{
		Calendar cal 		= Calendar.getInstance();
		return  sdf.format(cal.getTime()).toString(); 
	}
	
	public static String getPreviousDay()
	{
		Calendar cal 		= Calendar.getInstance();
		cal.add(Calendar.DATE, -1);    
		return  sdf.format(cal.getTime()).toString(); 
	}
	
	
	public static String getPreviousDayWithTime(String startTime) throws ParseException{
		
		String startDateTime = getPreviousDay()+" "+startTime;
		return startDateTime;			
	}
	
	public static Calendar convertStringToDate(String startDate) throws ParseException{
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		Date d = sdfDate.parse(startDate+" "+"00:00:00");
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
		return c;

	}
	
	
	public static String getCurrentDayWithTime() throws ParseException{
		
		String startDateTime = getCurrentDay()+" "+"00:00:00";
		return startDateTime;			
	}
	
	public static boolean isThisTimeValid(String timeToValidate) throws Exception{

		if(timeToValidate == null){
			return false;
		}

		String dateFromat = "HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {
			Date date = sdf.parse(timeToValidate);
			System.out.println(date);

		} catch (ParseException e) {
			e.printStackTrace();
			throw new Exception("Invalid Time Format , Please make sure that you give Time Format as HH:mm:ss");
		}

		return true;
	}
	
	public static boolean isThisDateValid(String dateToValidate) throws Exception{

		if(dateToValidate == null){
			return false;
		}

		String dateFromat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {
			Date date = sdf.parse(dateToValidate);
			System.out.println(date);

		} catch (ParseException e) {
			e.printStackTrace();
			throw new Exception("Invalid Time Format , Please make sure that you give Time Format as yyyy-MM-dd");
		}

		return true;
	}

}
