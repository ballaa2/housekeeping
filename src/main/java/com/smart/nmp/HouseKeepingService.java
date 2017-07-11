package com.smart.nmp;

import org.springframework.stereotype.Service;

@Service
public interface HouseKeepingService {

	public void deleteExpriedSubscriptionTablesData(String startDate)throws Exception ;

	
}