package com.mini.broker;

import com.mini.io.adapter.QueueAdapter;
import com.mini.io.adapter.QueueAdapterFactory;
import com.mini.io.exception.QueueCreationException;
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
			QueueAdapter queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
			ServiceRegistrationThread registreationThread = new ServiceRegistrationThread(queueAdapter);
			registreationThread.addRegistrationListener(this);
			registreationThread.start();
		}catch(QueueCreationException e){
			e.printStackTrace();
		}
	}

	@Override
	public void serviceRegistered(String serviceID, String queue) {
		System.out.println("Registered " + serviceID);
	}

	@Override
	public void serviceDeregistered(String serviceID, String queue) {
		// TODO Auto-generated method stub
	}
	
	public static void main(String[] args){
		FrameworkBroker broker = new FrameworkBroker("failover://tcp://localhost:61616");
	}
}
