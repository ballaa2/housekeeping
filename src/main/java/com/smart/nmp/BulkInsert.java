//package com.smart.nmp;
//
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class BulkInsert {
//
//	
//	public  void insertRecords() throws ParseException
//	{
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		String insertTableSQL  = "INSERT INTO APP.PNP_SUBSCRIPTIONS_SMART"
//		+ " (ID,MSISDN,BRAND,WALLETNAME,TRANSACTIONDATETIME,EXPIRYDATE,BALANCE,CAUSEID,EVICTIONDATETIME,EVICTTAG,STATUS,LAST_EVENT_TS,SUBSCRIPTION_TYPE) VALUES "	
//		+ " (?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		
//
//		PreparedStatement ps;
//		Calendar startCalObj 	= Calendar.getInstance();
//		startCalObj.setTime(sdf.parse(DateUtil.getPreviousDayWithTime("00:00:00")));
//		
//		
//		Calendar endCalObj 	= Calendar.getInstance();
//		endCalObj.setTime(startCalObj.getTime());
//		endCalObj.set(Calendar.HOUR,24);	
//		
//		String startTime = sdf.format(startCalObj.getTime());
//		System.out.println("startTime : "+startTime);
//
//		try
//		{
//			
//			ps = DBUtil.getConnection().prepareStatement(insertTableSQL);
//
//			Timestamp startDateTime = 	Timestamp.valueOf(startTime);
//			System.out.println("startDateTime"+startTime);
//			int primayrecordno = 0;
//			while (startCalObj.compareTo(endCalObj) <=0) 
//			{
//				startCalObj.add(Calendar.MILLISECOND, 1);
//				for(int j=0;j<3;j++)
//				{
//					ps.setString(1,""+primayrecordno++);
//					ps.setString(2, "1");
//					ps.setString(3, "TNT");
//					ps.setString(4, "walletname");
//					ps.setTimestamp(5, startDateTime);
//					ps.setTimestamp(6, Timestamp.valueOf(sdf.format(startCalObj.getTime())));
//					ps.setString(7, "0");
//					ps.setString(8, "1");
//					ps.setTimestamp(9, startDateTime);
//					ps.setInt(10, 0);
//					ps.setInt(11, 0);
//					ps.setTimestamp(12, startDateTime);
//					ps.setString(13, "");
//					ps.executeUpdate();
//				}
//
//			}
//
//		} 
//		catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//	}
//
//	
//}
