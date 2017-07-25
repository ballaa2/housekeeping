package com.smart.nmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cronSchedulerTime")
public class CronScheduler implements CommandLineRunner{
	private static final Logger log = LoggerFactory.getLogger(CronScheduler.class);
    private static String dateToDelRecords;

	@Autowired
	HouseKeepingService houseKeepingService;
	
	@Scheduled(cron ="${cronSchedulerTime}")
	public void deleteExpiredRecords() {
		try {
			 log.info("*************************Housekeeping Cron Job invoked****************************");		
			 houseKeepingService.deleteExpriedSubscriptionTablesData(dateToDelRecords);
			 log.info("*************************Housekeeping Cron Job Completed****************************");		

		} catch (Exception e) {
			log.error("Error occured while deleting records:",e);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		 if(args!=null && args.length >0)
		 {
			 dateToDelRecords = args[0];
		 }		
	}
}
