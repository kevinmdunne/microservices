package com.mini.broker;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.mini.data.MicroserviceConnectionClose;
import com.mini.data.MicroserviceConnectionRequest;
import com.mini.data.MicroserviceResponse;
import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.adapter.QueueAdapterFactory;
import com.mini.io.exception.QueueCreationException;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;
import com.mini.listeners.ConnectionRequestListener;
import com.mini.listeners.ServiceRegistrationListener;
import com.mini.microservice.Microservice;
import com.mini.utils.UUIDGenerator;

public class FrameworkBroker implements ServiceRegistrationListener, ConnectionRequestListener{

	public static final String CONNECTION_QUEUE_NAME = "connection-requests";
	
	private String serverURL;
	
	private ServiceRegistrationThread registreationThread;
	private ConnectionRequestThread connectionRequestThread;
	private Map<String,RequestHandlerThread> connections;
	private IQueueAdapter connectionRequestQueueAdapter;
	
	public FrameworkBroker(String URL){
		this.serverURL = URL;
		start();
	}
	
	public void start(){
		try{
			connections = new HashMap<String, RequestHandlerThread>();
			
			QueueMetaData queueData = new QueueMetaData(Microservice.SERVICE_REGISTRATION_QUEUE, this.serverURL);
			QueueAdapterFactory factory = QueueAdapterFactory.getInstance();
			IQueueAdapter queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
			this.registreationThread = new ServiceRegistrationThread(queueAdapter);
			registreationThread.addRegistrationListener(this);
			registreationThread.start();
			
			QueueMetaData queueData2 = new QueueMetaData(FrameworkBroker.CONNECTION_QUEUE_NAME, this.serverURL);
			this.connectionRequestQueueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData2);
			this.connectionRequestThread = new ConnectionRequestThread(connectionRequestQueueAdapter);
			connectionRequestThread.addListener(this);
			connectionRequestThread.start();
		}catch(QueueCreationException e){
			e.printStackTrace();
		}
	}
	
	public void shutdown(){
		this.registreationThread.stop();
		this.connectionRequestThread.stop();
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
	
	@Override
	public void connectionRequested(MicroserviceConnectionRequest request) {
		try{
			String queueNameAppender = request.getPayload().toString();
			String queueName = UUIDGenerator.generateID();
			
			if(!queueNameAppender.isEmpty()){
				queueName = queueName + "_" + queueNameAppender;
			}
			
			QueueMetaData queueData = new QueueMetaData(queueName, this.serverURL);
			QueueAdapterFactory factory = QueueAdapterFactory.getInstance();
			IQueueAdapter queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
			RequestHandlerThread thread = new RequestHandlerThread(queueAdapter,this);
			this.connections.put(queueName, thread);
			thread.start();

			MicroserviceResponse response = new MicroserviceResponse();
			response.setCorrelationID(request.getCorrelationID());
			response.setPayload(queueName);
			connectionRequestQueueAdapter.push(response);
			
		}catch(QueueCreationException e){
			e.printStackTrace();
		}catch(QueueException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void connectionCloseRequested(MicroserviceConnectionClose request) {
		String requestQueueName = request.getPayload().toString();
		RequestHandlerThread thread = this.connections.get(requestQueueName);
		thread.stop();
		this.connections.remove(requestQueueName);
	}
	
	public IQueueAdapter getServiceQueue(String serviceID){
		return ServiceRegistry.getInstance().getServiceQueueAdapter(serviceID);
	}
	
	public static void main(String[] args){
		FrameworkBroker broker = new FrameworkBroker("failover://tcp://localhost:61616");
		
		new Scanner(System.in).nextLine();
		System.out.println("Stopping broker");
		broker.shutdown();
		
		System.exit(0);
	}
}
