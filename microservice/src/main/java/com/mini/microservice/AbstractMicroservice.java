package com.mini.microservice;

import com.mini.data.MicroserviceDeregistration;
import com.mini.data.MicroservicePacket;
import com.mini.data.MicroserviceRegistration;
import com.mini.data.MicroserviceRequest;
import com.mini.data.MicroserviceResponse;
import com.mini.exception.InfastructureException;
import com.mini.exception.ServiceExecutionException;
import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.adapter.QueueAdapter;
import com.mini.io.adapter.QueueAdapterFactory;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;

public abstract class AbstractMicroservice implements Microservice{

	private IQueueAdapter queueAdapter;
	private ServiceRunnable runner;
	
	public AbstractMicroservice(IQueueAdapter queueAdapter){
		this.queueAdapter = queueAdapter;
	}
	
	protected void sendResponse(MicroserviceResponse response) throws QueueException{
		this.queueAdapter.push(response);
	}
	
	@Override
	public void start() throws InfastructureException {
		try{
			this.queueAdapter.connect();
			this.register();
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
			this.deregister();
			this.runner.stop();
			this.queueAdapter.disconnect();
		}catch(QueueException e){
			throw new InfastructureException(e);
		}
	}
	
	private void register(){
		try{
			QueueMetaData queueData = new QueueMetaData(Microservice.SERVICE_REGISTRATION_QUEUE, this.queueAdapter.getQueueMetaData().getQueueURL());
			QueueAdapterFactory factory = QueueAdapterFactory.getInstance();
			QueueAdapter queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
			MicroserviceRegistration registrationPacket = new MicroserviceRegistration();
			String[] data = {getID(),this.queueAdapter.getQueueMetaData().getQueueName()};
			registrationPacket.setPayload(data);
			queueAdapter.connect();
			queueAdapter.push(registrationPacket);
			queueAdapter.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void deregister(){
		try{
			QueueMetaData queueData = new QueueMetaData(Microservice.SERVICE_REGISTRATION_QUEUE, this.queueAdapter.getQueueMetaData().getQueueURL());
			QueueAdapterFactory factory = QueueAdapterFactory.getInstance();
			QueueAdapter queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
			MicroserviceDeregistration registrationPacket = new MicroserviceDeregistration();
			String[] data = {getID(),this.queueAdapter.getQueueMetaData().getQueueName()};
			registrationPacket.setPayload(data);
			queueAdapter.connect();
			queueAdapter.push(registrationPacket);
			queueAdapter.disconnect();
		}catch(Exception e){
			e.printStackTrace();
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
