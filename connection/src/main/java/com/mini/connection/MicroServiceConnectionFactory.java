package com.mini.connection;

import com.mini.data.MicroserviceConnectionRequest;
import com.mini.data.MicroservicePacket;
import com.mini.data.MicroserviceResponse;
import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.adapter.QueueAdapterFactory;
import com.mini.io.exception.QueueCreationException;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;

public class MicroServiceConnectionFactory {
	
	private static final long TIME_OUT = 60000;
	
	public static final String CONNECTION_QUEUE_NAME = "connection-requests";
	
	public String serverURL;
	
	
	public MicroServiceConnectionFactory(String serverURL){
		this.serverURL = serverURL;
	}
	
	public MicroServiceConnection createConnection(){
		IQueueAdapter queueAdapter = null;
		try{
			QueueMetaData queueData = new QueueMetaData(CONNECTION_QUEUE_NAME, this.serverURL);
			QueueAdapterFactory factory = QueueAdapterFactory.getInstance();
			queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
			queueAdapter.connect();
			MicroserviceConnectionRequest connectionRequest = new MicroserviceConnectionRequest();
			queueAdapter.push(connectionRequest);
			
			long starttime = System.currentTimeMillis();
			
			while (System.currentTimeMillis() - starttime <= TIME_OUT) {
				MicroservicePacket response = queueAdapter.recieve(5000);
				if(response instanceof MicroserviceResponse){
					if(response.getCorrelationID().equals(connectionRequest.getCorrelationID())){
						String queueName = response.getPayload().toString();
						IQueueAdapter requestQueue = this.createRequestAdapter(queueName);
						return new MicroServiceConnection(requestQueue);
					}
				}
				//not for us so put it back on the queue
				queueAdapter.push(response);
			}
			queueAdapter.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private IQueueAdapter createRequestAdapter(String queueName) throws QueueCreationException,QueueException{
		QueueMetaData queueData = new QueueMetaData(queueName, this.serverURL);
		QueueAdapterFactory factory = QueueAdapterFactory.getInstance();
		IQueueAdapter queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
		queueAdapter.connect();
		return queueAdapter;
	}
}
