package com.smart.nmp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class HousekeepingDao {

    private static final Logger log = LoggerFactory.getLogger(HousekeepingDao.class);

	 @Value("${spring.datasource.url}")
	 private String dataSourceUrl;
	 
	 @Autowired
	 private DBUtil dBUtil;
	 
	 public  int getCount(String tablename ,String startDateTime, String endDateTime) {
		
		String sql = "SELECT count(*) FROM "+tablename+"  WHERE EXPIRYDATE between '"+startDateTime+"' AND '"+endDateTime+"'";
		log.debug("sql : "+sql);
    	PreparedStatement ps;
    	int rowcount = 0;
		try {
			ps = dBUtil.getInstance().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rowcount = Integer.parseInt(rs.getString(1));
			}
			 return rowcount;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowcount;
	}
	
	public  int deleteRecords(String tablename, String startDateTime, String endDateTime) {
		String sql = "DELETE FROM "+tablename+"  WHERE EXPIRYDATE between '"+startDateTime+"' AND '"+endDateTime+"'";
		log.debug("sql : "+sql);

    	PreparedStatement ps;
    	int deletedRecords = 0;
		try {
			ps = dBUtil.getInstance().prepareStatement(sql);
			deletedRecords = ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deletedRecords;
		
	}
	
	
}
