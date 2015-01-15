package com.mini.broker;

import com.mini.data.MicroservicePacket;
import com.mini.data.MicroserviceRequest;
import com.mini.data.MicroserviceResponse;
import com.mini.exception.ServiceExecutionException;
import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.exception.QueueException;

public class RequestHandlerThread implements Runnable{

	private static final long TIMEOUT = 5000;

	private IQueueAdapter requestQueueAdapter;
	private boolean running;
	private FrameworkBroker broker;
	
	public RequestHandlerThread(IQueueAdapter requestQueueAdapter,FrameworkBroker broker){
		this.requestQueueAdapter = requestQueueAdapter;
		this.broker = broker;
	}
	
	public void start(){
		try{
			this.requestQueueAdapter.connect();
			Thread thread = new Thread(this);
			thread.start();
		}catch(QueueException e){
			e.printStackTrace();
		}
	}
	
	public void stop() {
		this.running = false;
	}
	
	@Override
	public void run() {
		try {
			this.running = true;

			while (this.running) {
				MicroservicePacket packet = requestQueueAdapter.recieve(TIMEOUT);

				if (packet != null) {
					if(packet instanceof MicroserviceRequest){
						MicroserviceResponse response = request((MicroserviceRequest)packet);
						requestQueueAdapter.push(response);
					}else{
						this.requestQueueAdapter.push(packet);
					}
				}
			}
			this.requestQueueAdapter.disconnect();
		} catch (QueueException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public MicroserviceResponse request(MicroserviceRequest request) throws QueueException,ServiceExecutionException{
		String serviceID = request.getServiceID();
		IQueueAdapter queue = broker.getServiceQueue(serviceID);
		if(queue != null){
			queue.push(request);
			long starttime = System.currentTimeMillis();
			
			while(System.currentTimeMillis() - starttime <= TIMEOUT){
				MicroservicePacket response = queue.recieve(TIMEOUT);
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

}
