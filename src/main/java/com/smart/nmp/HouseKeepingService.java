package com.smart.nmp;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HouseKeepingService 
{
	private static final SimpleDateFormat sdf 	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final Logger log = LoggerFactory.getLogger(HouseKeepingService.class);


	@Value("${spring.datasource.url}")
	private String dataSourceUrl;

	@Value("${startTime}")
	private String startTime;

	@Value("${thresholdRowsLimit}")
	private int thresholdRowsLimit;

	@Value("${timespanInSeconds}")
	private int timespan;
	
	@Value("${tablenames}")
	private String tablenames;

	@Autowired
	HousekeepingDao houseKeepingDao;

	public void deleteExpriedSubscriptionTablesData(String startDate) throws Exception
	{
		SimpleDateFormat sdf 		= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BigDecimal timeSpanInMin 	= new BigDecimal(timespan).divide(new BigDecimal(60));
		
		DateUtil.isThisTimeValid(startTime);


		log.info("****Thresholdlimit*****" + thresholdRowsLimit);

		List<String> timeslots = new ArrayList<String>();
		
		String[] tables = tablenames.split(",");
		
		for (String tableName : tables) 
		{
			Calendar startCalObj 		= null;
			Calendar endCalObj  		= Calendar.getInstance();
			
			Calendar jobEndCalObj 		= Calendar.getInstance();

			if(startDate !=null && startDate.length()>7)
			{
				DateUtil.isThisDateValid(startDate);

				 startCalObj = DateUtil.convertStringToDate(startDate);
				 endCalObj.setTime(startCalObj.getTime());
				 endCalObj.set(Calendar.HOUR,0);
				 endCalObj.set(Calendar.MINUTE, timeSpanInMin.intValue());
				 endCalObj.set(Calendar.SECOND, 0);		
			
				jobEndCalObj.setTime(startCalObj.getTime());
				jobEndCalObj.add(Calendar.HOUR, 24);				
				
			}else{
				 startCalObj 		= Calendar.getInstance();
				 startCalObj.setTime(sdf.parse(DateUtil.getPreviousDayWithTime(startTime)));
				 
			 	 endCalObj = Calendar.getInstance();
				 endCalObj.setTime(sdf.parse(DateUtil.getPreviousDayWithTime(startTime)));
				 endCalObj.set(Calendar.HOUR,0);
				 endCalObj.set(Calendar.MINUTE, timeSpanInMin.intValue());
				 endCalObj.set(Calendar.SECOND, 0);
				 
				 jobEndCalObj.setTime(sdf.parse(DateUtil.getCurrentDayWithTime()));
			}
			
			log.info("****startTime****"+sdf.format(startCalObj.getTime()) + " for table " +tableName);
			log.info("****endTime****"+sdf.format(endCalObj.getTime()));
			log.info("****jobEndCalObj****"+sdf.format(jobEndCalObj.getTime()));


			
			while(endCalObj.compareTo(jobEndCalObj) <=0)
			{
				getTimeFrames(startCalObj, endCalObj,tableName,timeslots);
				startCalObj.setTime(endCalObj.getTime());
				endCalObj.add(Calendar.MINUTE, timeSpanInMin.intValue());
			}
			deleteRecords(timeslots,tableName);
			timeslots = new ArrayList<String>();
		}

	}
	
	

	public  void getTimeFrames(Calendar startCalObj,Calendar endCalObj,String tableName,List<String> timeslots) throws ParseException {
		try{
			if (startCalObj.compareTo(endCalObj) <= 0){
				//Fetch Records
				int rowsCount = houseKeepingDao.getCount(tableName,sdf.format(startCalObj.getTime()), sdf.format(endCalObj.getTime()));;
				int minutesDiff = (int)minsBetween(startCalObj, endCalObj);
								
				//Checking for Threshhold
				if (rowsCount > thresholdRowsLimit && minutesDiff>1 ) 
				{
					Calendar end = (Calendar) startCalObj.clone();
					end.add(Calendar.MINUTE,minutesDiff/2 );//This date act as 1st half "end date" and 2nd half "start date"
					
					getTimeFrames(startCalObj,end,tableName,timeslots);//Ist Half Recursive call start,end
					
					getTimeFrames((Calendar)end.clone(),endCalObj,tableName,timeslots);//2nd Half Recursive call start,end					
				} 
				else if(rowsCount >0)
				{
					timeslots.add(sdf.format(startCalObj.getTime()) + "#"+ sdf.format(endCalObj.getTime()));
				}		
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} 

	}
	
	private void deleteRecords(List<String> timeslots,String tableName)
	{
		log.info("****timeslots****"+timeslots);
		try{
		String ts = "";
		String tsArray[];
		long starttime = System.currentTimeMillis();
		for(int i=0;i<timeslots.size();i++)
		{
			 ts 		= timeslots.get(i);
			 tsArray 	=  ts.split("#");
			 if(tsArray.length>1)
			 {
				log.debug("DELETE FROM "+tableName+" WHERE EXPIRYDATE between "+tsArray[0]+" AND "+tsArray[1]);
			 	log.debug("No of Records Deleted :"+houseKeepingDao.deleteRecords(tableName,tsArray[0], tsArray[1]));
			 }
		}
		long end = System.currentTimeMillis() - starttime;
		log.info("****Total time Taken to delete records from table****"+tableName+" in ms: "+end);

		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	public static long minsBetween(Calendar startDate, Calendar endDate) {
	    long end = endDate.getTimeInMillis();
	    long start = startDate.getTimeInMillis();
	    return TimeUnit.MILLISECONDS.toMinutes(Math.abs(end - start));
	}
}