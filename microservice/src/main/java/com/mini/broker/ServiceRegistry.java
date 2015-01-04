package com.mini.broker;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {
	
	private static ServiceRegistry INSTANCE;
	
	private Map<String,ServiceRecord> registry;
	
	private ServiceRegistry(){
		registry = new HashMap<String, ServiceRecord>();
	}
	
	public static ServiceRegistry getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ServiceRegistry();
		}
		return INSTANCE;
	}
	
	public void registerService(String serviceID,String queueName){
		ServiceRecord record = registry.get(queueName);
		if(record == null){
			record = new ServiceRecord();
			registry.put(queueName, record);
		}
		record.incrementServiceCount(serviceID);
	}
	
	public void deregisterService(String serviceID,String queueName){
		ServiceRecord record = registry.get(queueName);
		if(record != null){
			record.decrementServiceCount(serviceID);
		}
	}
}
