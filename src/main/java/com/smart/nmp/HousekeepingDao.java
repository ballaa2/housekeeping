package com.smart.nmp;

import org.springframework.stereotype.Repository;

@Repository
public interface HousekeepingDao {
	 
	public  int getCount(String tablename ,String startDateTime, String endDateTime) ;	
	public  int deleteRecords(String tablename, String startDateTime, String endDateTime);
	public String getStartDate(String tablename,String todaysDate,String oldDate);
	public  int deleteOldRecords(String tablename, String oldDate);

	
}
