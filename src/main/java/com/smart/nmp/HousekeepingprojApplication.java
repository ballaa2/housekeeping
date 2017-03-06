package com.smart.nmp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class HousekeepingprojApplication 
{
    private static final Logger log = LoggerFactory.getLogger(HousekeepingprojApplication.class);
    private static final SimpleDateFormat sdf 		= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String startDate;
   
 
    
	public static void main(String[] args) throws ParseException
	{
		SpringApplication.run(HousekeepingprojApplication.class, args);
		 if(args!=null && args.length >0)
		 {
			 startDate = args[0];
		 }
	
	}
	

	@Bean
    HouseKeepingService houseKeepingService() {
		return new HouseKeepingService();
	}
	
	@Scheduled(initialDelayString="60000",fixedDelayString="86400000")
    public void runJob() throws Exception 
	{
		 log.info("*************************Job invoked****************************");

		Calendar calObj 		= Calendar.getInstance();
		calObj.setTime(sdf.parse(DateUtil.getCurrentDayWithTime()));
		
        houseKeepingService().deleteExpriedSubscriptionTablesData(startDate);
    }
}
