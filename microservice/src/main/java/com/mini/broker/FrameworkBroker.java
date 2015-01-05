package com.mini.broker;

import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.adapter.QueueAdapterFactory;
import com.mini.io.exception.QueueCreationException;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;
import com.mini.listeners.ServiceRegistrationListener;
import com.mini.microservice.Microservice;

public class FrameworkBroker implements ServiceRegistrationListener{

	private String serverURL;
	
	public FrameworkBroker(String URL){
		this.serverURL = URL;
		start();
	}
	
	public void start(){
		try{
			QueueMetaData queueData = new QueueMetaData(Microservice.SERVICE_REGISTRATION_QUEUE, this.serverURL);
			QueueAdapterFactory factory = QueueAdapterFactory.getInstance();
			IQueueAdapter queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
			ServiceRegistrationThread registreationThread = new ServiceRegistrationThread(queueAdapter);
			registreationThread.addRegistrationListener(this);
			registreationThread.start();
		}catch(QueueCreationException e){
			e.printStackTrace();
		}
	}

	@Override
	public void serviceRegistered(String serviceID, String queueName) {
		try{
			IQueueAdapter queueAdapter = ServiceRegistry.getInstance().getQueueAdapter(queueName);
			if(queueAdapter == null){
				QueueMetaData queueData = new QueueMetaData(queueName, this.serverURL);
				QueueAdapterFactory factory = QueueAdapterFactory.getInstance();
				queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
				queueAdapter.connect();
			}
			ServiceRegistry.getInstance().registerService(serviceID, queueName,queueAdapter);
		}catch(QueueCreationException e){
			e.printStackTrace();
		}catch(QueueException e){
			e.printStackTrace();
		}
	}

	@Override
	public void serviceDeregistered(String serviceID, String queueName) {
		ServiceRegistry.getInstance().deregisterService(serviceID, queueName);
	}
	
	public RequestConnection createRequestConnection(){
		RequestConnection connection = new RequestConnection(this);
		return connection;
	}
	
	public IQueueAdapter getServiceQueue(String serviceID){
		return ServiceRegistry.getInstance().getServiceQueueAdapter(serviceID);
	}
	
	public static void main(String[] args){
		FrameworkBroker broker = new FrameworkBroker("failover://tcp://localhost:61616");
	}
}
