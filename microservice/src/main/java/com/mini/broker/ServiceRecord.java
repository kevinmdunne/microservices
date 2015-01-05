package com.mini.broker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServiceRecord {

	private Map<String,Integer> map;

	public ServiceRecord(){
		map = new HashMap<String, Integer>();
	}
	
	public boolean hasService(String serviceID){
		return map.get(serviceID) != null;
	}
	
	public void incrementServiceCount(String serviceID){
		Integer count = map.get(serviceID);
		if(count == null){
			count = new Integer(0);	
		}
		count = new Integer(count.intValue() + 1);
		map.put(serviceID, count);
	}

	public void decrementServiceCount(String serviceID){
		Integer count = map.get(serviceID);
		if(count == null){
			count = new Integer(0);	
		}
		count = new Integer(count.intValue() - 1);
		if(count > 0){
			map.put(serviceID, count);
		}else{
			map.remove(serviceID);
		}
	}
	
	public int getServiceCount(String serviceID){
		Integer count = map.get(serviceID);
		if(count != null){
			return count.intValue();
		}
		return 0;
	}
	
	public int getTotalServiceCount(){
		int result = 0;
		Iterator<Integer> values = map.values().iterator();
		
		while(values.hasNext()){
			result = result + values.next().intValue();
		}
		return result;
	}
}
