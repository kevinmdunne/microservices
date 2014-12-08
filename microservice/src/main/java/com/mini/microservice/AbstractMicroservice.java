package com.mini.microservice;

import com.mini.data.MicroservicePacket;
import com.mini.data.MicroserviceRequest;
import com.mini.exception.InfastructureException;
import com.mini.exception.ServiceExecutionException;
import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.exception.QueueException;

public abstract class AbstractMicroservice implements Microservice{

	private IQueueAdapter queueAdapter;
	private ServiceRunnable runner;
	
	public AbstractMicroservice(IQueueAdapter queueAdapter){
		this.queueAdapter = queueAdapter;
	}
	
	@Override
	public void start() throws InfastructureException {
		try{
			this.queueAdapter.connect();
		
			ServiceRunnable runnable = new ServiceRunnable();
			Thread thread = new Thread(runnable);
			thread.start();
		}catch(QueueException e){
			throw new InfastructureException(e);
		}
	}
	
	@Override
	public void stop() throws InfastructureException {
		try{
			this.runner.stop();
			this.queueAdapter.disconnect();
		}catch(QueueException e){
			throw new InfastructureException(e);
		}
	}
	
	public IQueueAdapter getQueueAdapter(){
		return this.queueAdapter;
	}
	
	private class ServiceRunnable implements Runnable{
		
		private static final long TIMEOUT = 5000;
		private boolean running;
		
		public ServiceRunnable(){
			this.running = false;
		}
		
		public void stop(){
			this.running = false;
		}
		
		@Override
		public void run() {
			try{
			this.running = true;
				while(this.running){
					MicroservicePacket packet = getQueueAdapter().recieve(TIMEOUT);
					if(packet != null){
						if(packet instanceof MicroserviceRequest){
							MicroserviceRequest request = (MicroserviceRequest)packet;
							if(request.getServiceID().equals(getID())){
								execute(request);
							}else{
								getQueueAdapter().push(packet);
							}
						}
					}
				}
			}catch(QueueException e){
				
			}catch(ServiceExecutionException e){
				
			}
		}
		
	}
}
