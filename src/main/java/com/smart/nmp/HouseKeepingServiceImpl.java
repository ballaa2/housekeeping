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
public class HouseKeepingServiceImpl implements HouseKeepingService{
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final Logger log = LoggerFactory.getLogger(HouseKeepingServiceImpl.class);

	@Value("${thresholdRowsLimit}")
	private int thresholdRowsLimit;

	@Value("${timespanInSeconds}")
	private int timespan;

	@Value("${tablenames}")
	private String tablenames;

	@Autowired
	HousekeepingDao houseKeepingDao;

	@Override
	public void deleteExpriedSubscriptionTablesData(String dateToDelRecords)
			throws Exception {
		SimpleDateFormat sdf 		= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BigDecimal timeSpanInMin 	= new BigDecimal(timespan).divide(new BigDecimal(60));

		log.info("Thresholdlimit	:	" + thresholdRowsLimit);
		log.info("timeSpanInMin		:	" + timeSpanInMin);

		List<String> timeslots = new ArrayList<String>();

		String[] tables = tablenames.split(",");
		String startDate = "";
		for (String tableName : tables) {
			Calendar startCalObj 	= null;
			Calendar tempEndCalObj 	= Calendar.getInstance();
			Calendar jobEndCalObj 	= Calendar.getInstance();

			if (dateToDelRecords != null && dateToDelRecords.length() > 7) {
				DateUtil.isThisDateValid(dateToDelRecords);

				startCalObj = DateUtil.convertStringToDate(dateToDelRecords);
				tempEndCalObj.setTime(startCalObj.getTime());
				tempEndCalObj.set(Calendar.HOUR, 0);
				tempEndCalObj.set(Calendar.MINUTE, timeSpanInMin.intValue());
				tempEndCalObj.set(Calendar.SECOND, 0);

				jobEndCalObj.setTime(startCalObj.getTime());
				jobEndCalObj.add(Calendar.HOUR, 24);

			} else {
				//delete all expired records expirydate <= todaysdate 
				startDate = houseKeepingDao.getStartDate(tableName, DateUtil.getCurrentDayWithTime());
				if(startDate!=null && startDate.trim().length()>0 ){
					startCalObj = DateUtil.convertStringToCalendar(startDate);				
					tempEndCalObj.setTime(startCalObj.getTime());
					tempEndCalObj.add(Calendar.MINUTE, timeSpanInMin.intValue());					
					jobEndCalObj.setTime(sdf.parse(DateUtil.getCurrentDayWithTime()));
				}
			}

			if(startDate!=null && startDate.trim().length()>0){
				log.info("startTime			:	" + sdf.format(startCalObj.getTime())
						+ " for table " + tableName);
				log.info("tempEndCalObj			:	" + sdf.format(tempEndCalObj.getTime())
						+ " for table " + tableName);
				log.info("jobEndCalObj		:	" + sdf.format(jobEndCalObj.getTime()));
				
				
				while (tempEndCalObj.compareTo(jobEndCalObj) <= 0) {
					getTimeFrames(startCalObj, tempEndCalObj, tableName, timeslots);
					startCalObj.setTime(tempEndCalObj.getTime());
					tempEndCalObj.add(Calendar.MINUTE, timeSpanInMin.intValue());
				}
				
				if(tempEndCalObj.compareTo(jobEndCalObj)>0){
					tempEndCalObj.setTime(jobEndCalObj.getTime()); 
					startCalObj.setTime(tempEndCalObj.getTime());
					startCalObj.add(Calendar.MINUTE, -(timeSpanInMin.intValue()));
					getTimeFrames(startCalObj, tempEndCalObj, tableName, timeslots);
				}
				deleteRecords(timeslots, tableName);
				timeslots = new ArrayList<String>();
				
			}else{
				log.info("There were no expiry records in the database for the table "+tableName);
			}
		}

	}

	public void getTimeFrames(Calendar startCalObj, Calendar endCalObj,
			String tableName, List<String> timeslots) throws ParseException {
		try {
			if (startCalObj.compareTo(endCalObj) <= 0) {
				// Fetch Records
				int rowsCount = houseKeepingDao.getCount(tableName,
						sdf.format(startCalObj.getTime()),
						sdf.format(endCalObj.getTime()));

				int minutesDiff = (int) minsBetween(startCalObj, endCalObj);

				// Checking for Threshhold
				if (rowsCount > thresholdRowsLimit && minutesDiff > 1) {
					Calendar end = (Calendar) startCalObj.clone();
					end.add(Calendar.MINUTE, minutesDiff / 2);
				// This date act as 1st half "end date" and 2nd half "start date"

					getTimeFrames(startCalObj, end, tableName, timeslots);
				// Ist Half  Recursive call start,end

					getTimeFrames((Calendar) end.clone(), endCalObj, tableName,timeslots);
				// 2nd Half Recursive call start,end
				} else if (rowsCount > 0) {
					timeslots.add(sdf.format(startCalObj.getTime()) + "#"
							+ sdf.format(endCalObj.getTime()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	private void deleteRecords(List<String> timeslots, String tableName) {

		log.info("timeslots			:	" + timeslots);
		try {
			String ts = "";
			String tsArray[];
			long starttime = System.currentTimeMillis();
			for (int i = 0; i < timeslots.size(); i++) {
				ts = timeslots.get(i);
				tsArray = ts.split("#");

				if (tsArray.length > 1) {
					log.debug("DELETE FROM " + tableName
							+ " WHERE EXPIRYDATE between " + tsArray[0]
							+ " AND " + tsArray[1]);
					log.debug("No of Records Deleted :"
							+ houseKeepingDao.deleteRecords(tableName,
									tsArray[0], tsArray[1]));
				}
			}
			long end = System.currentTimeMillis() - starttime;
			log.info("Total time taken to delete records from table "
					+ tableName + " in ms	:	" + end);
			System.out.println("Total time taken to delete records from table "
					+ tableName + " in ms	:	" + end);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	public static long minsBetween(Calendar startDate, Calendar endDate) {
		long end 		= endDate.getTimeInMillis();
		long start 		= startDate.getTimeInMillis();
		return TimeUnit.MILLISECONDS.toMinutes(Math.abs(end - start));
	}
}