package com.smart.nmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="housekeepingSchedulerTime")
public class CronScheduler implements CommandLineRunner{
	private static final Logger log = LoggerFactory.getLogger(CronScheduler.class);
    private static String dateToDelRecords;

	@Autowired
	HouseKeepingService houseKeepingService;
	
	@Scheduled(cron ="${housekeepingSchedulerTime}")
	public void deleteExpiredRecords() {
		try {
			// bulkInsert.insertRecords();
			 log.info("*************************Cron Job invoked****************************");		
			 houseKeepingService.deleteExpriedSubscriptionTablesData(dateToDelRecords);
		} catch (Exception e) {
			log.error("Error occured while deleting recrods:",e);
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
