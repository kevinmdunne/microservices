package com.mini.broker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mini.io.adapter.IQueueAdapter;

public class ServiceRegistry {
	
	private static ServiceRegistry INSTANCE;
	
	private Map<String,ServiceRecord> registry;
	private Map<String,IQueueAdapter> serviceQueues;
	
	private ServiceRegistry(){
		registry = new HashMap<String, ServiceRecord>();
		serviceQueues = new HashMap<String, IQueueAdapter>();
	}
	
	public static ServiceRegistry getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ServiceRegistry();
		}
		return INSTANCE;
	}
	
	public void registerService(String serviceID,String queueName,IQueueAdapter queue){
		ServiceRecord record = registry.get(queueName);
		if(record == null){
			record = new ServiceRecord();
			registry.put(queueName, record);
		}
		record.incrementServiceCount(serviceID);
		serviceQueues.put(queueName, queue);
	}
	
	public void deregisterService(String serviceID,String queueName){
		ServiceRecord record = registry.get(queueName);
		if(record != null){
			record.decrementServiceCount(serviceID);
			if(record.getTotalServiceCount() <= 0){
				try{
					this.serviceQueues.get(queueName).disconnect();
					this.serviceQueues.remove(queueName);
					this.registry.remove(queueName);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public IQueueAdapter getQueueAdapter(String queueName){
		return this.serviceQueues.get(queueName);
	}
	
	public IQueueAdapter getServiceQueueAdapter(String serviceID){
		Iterator<String> queueNames = this.registry.keySet().iterator();

		while(queueNames.hasNext()){
			String queueName = queueNames.next();
			ServiceRecord record = this.registry.get(queueName);
			if(record.hasService(serviceID)){
				return this.serviceQueues.get(queueName);
			}
		}
		return null;
	}
	
	public int getServiceCount(String queueName){
		ServiceRecord record = registry.get(queueName);
		if(record != null){
			return record.getTotalServiceCount();
		}
		return 0;
	}
}
