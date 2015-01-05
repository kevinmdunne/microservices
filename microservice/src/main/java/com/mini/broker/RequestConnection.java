package com.mini.broker;

import com.mini.data.MicroservicePacket;
import com.mini.data.MicroserviceRequest;
import com.mini.data.MicroserviceResponse;
import com.mini.exception.ServiceExecutionException;
import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.exception.QueueException;

public class RequestConnection {
	
	private static final int TIME_OUT = 60000;
	
	private FrameworkBroker broker;
	
	public RequestConnection(FrameworkBroker broker){
		this.broker = broker;
	}
	
	public MicroserviceResponse request(MicroserviceRequest request) throws QueueException,ServiceExecutionException{
		String serviceID = request.getServiceID();
		IQueueAdapter queue = broker.getServiceQueue(serviceID);
		if(queue != null){
			queue.push(request);
			long starttime = System.currentTimeMillis();
			
			while(System.currentTimeMillis() - starttime <= TIME_OUT){
				MicroservicePacket response = queue.recieve(TIME_OUT);
				if(response instanceof MicroserviceResponse){
					if(response.getCorrelationID().equals(request.getCorrelationID())){
						return (MicroserviceResponse)response;
					}
				}
				//not for us so put it back on the queue
				queue.push(response);
			}
			
			throw new ServiceExecutionException("Request timed out");
		}else{
			throw new ServiceExecutionException("Not such service registered");
		}
	}
	
	public void dispose(){

	}
	

}
