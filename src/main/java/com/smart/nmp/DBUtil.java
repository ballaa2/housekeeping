package com.smart.nmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBUtil {
	
	
	 @Value("${spring.datasource.url}")
	 private  String dataSourceUrl ;
	 
	 @Value("${spring.datasource.username}")
	 private  String userName ;
	 
	 @Value("${spring.datasource.password}")
	 private  String password ;
		 
	public  Connection getInstance()
	{
		Connection conn = null;
		if(conn == null)
		{
			try {
				conn = DriverManager.getConnection(dataSourceUrl,userName,password);
			} catch (SQLException e) {
				e.printStackTrace();
			}   
		}
		
		return conn;
	}
	


}
