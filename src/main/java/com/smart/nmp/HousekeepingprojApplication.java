package com.smart.nmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class HousekeepingprojApplication implements CommandLineRunner
{
    private static final Logger log = LoggerFactory.getLogger(HousekeepingprojApplication.class);

    private static String dateToDelRecords;
   
    @Autowired
    HouseKeepingService houseKeepingService;
       
	public  static void main(String[] args) 
	{
		SpringApplication.run(HousekeepingprojApplication.class, args);		 
	}
	
	@Override
	public void run(String... args) throws Exception {
		 if(args!=null && args.length >0)
		 {
			 dateToDelRecords = args[0];
		 }		
	}
	
	@Scheduled(fixedDelayString="86400000")
    public void runJob()  throws Exception
	{		
		log.info("*************************Housekeeping Job invoked****************************");		
		houseKeepingService.deleteExpriedSubscriptionTablesData(dateToDelRecords);		
    }
}
