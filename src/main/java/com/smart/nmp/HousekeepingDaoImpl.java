package com.smart.nmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HousekeepingDaoImpl implements HousekeepingDao {

    private static final Logger log = LoggerFactory.getLogger(HousekeepingDaoImpl.class);
	 
	 @Autowired
	 JdbcTemplate jdbcTemplate;
	
	 @Override
	 public  int getCount(String tablename ,String startDateTime, String endDateTime) {		
		String sql = "SELECT count(*) FROM "+tablename+"  WHERE EXPIRYDATE between '"+startDateTime+"' AND '"+endDateTime+"'";
		log.debug("sql : "+sql);
    	int rowcount = jdbcTemplate.queryForObject(sql,Integer.class);		
		 return rowcount;
	 }
	 
	 @Override
	 public  String getStartDate(String tablename,String todaysDate) {		
		String sql = "SELECT MIN(EXPIRYDATE) FROM "+tablename+"  WHERE EXPIRYDATE <= '"+todaysDate+"' ";
		log.debug("sql : "+sql);
    	String rowcount = jdbcTemplate.queryForObject(sql,String.class);		
		return rowcount;
	 }
	 
	 @Override
	 public  int deleteRecords(String tablename, String startDateTime, String endDateTime) {
		String sql = "DELETE FROM "+tablename+"  WHERE EXPIRYDATE between '"+startDateTime+"' AND '"+endDateTime+"'";
		log.debug("sql : "+sql);
		int deletedRecords = jdbcTemplate.update(sql);
		return deletedRecords;		
	}



}
