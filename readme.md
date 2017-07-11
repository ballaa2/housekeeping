
Usage :

java -jar ${jarname} ${yyyy-MM-dd}

1.command : java -jar housekeeping.jar 2017-03-01
 
This command will delete the records for March 1st date (24hours of data)
from Tables 
TNT_SUBSCRIPTIONS_SMART
TNT_SUBSCRIPTIONS_TNT 
where expiry date between '2017-03-01 00:00:00' and '2017-03-02 00:00:00'

2.command : java -jar houskeeping.jar 

delete all the expired records 



*********************Production Env Command**********************

java -jar {jarname} --spring.profiles.active=prod




