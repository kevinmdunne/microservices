package com.mini.connection;

import com.mini.data.MicroserviceConnectionClose;
import com.mini.data.MicroservicePacket;
import com.mini.data.MicroserviceRequest;
import com.mini.data.MicroserviceResponse;
import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.adapter.QueueAdapterFactory;
import com.mini.io.exception.QueueCreationException;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;

public class MicroServiceConnection {

	private static final int TIME_OUT = 60000;

	private IQueueAdapter queueAdapter;

	public MicroServiceConnection(IQueueAdapter queueAdapter) {
		this.queueAdapter = queueAdapter;
	}

	public MicroserviceResponse request(MicroserviceRequest request) throws QueueException, ServiceExecutionException {
		this.queueAdapter.push(request);
		long starttime = System.currentTimeMillis();

		while (System.currentTimeMillis() - starttime <= TIME_OUT) {
			MicroservicePacket response = this.queueAdapter.recieve(5000);
			if (response instanceof MicroserviceResponse) {
				if (response.getCorrelationID().equals(
						request.getCorrelationID())) {
					return (MicroserviceResponse) response;
				}
			}
			// not for us so put it back on the queue
			this.queueAdapter.push(response);
		}

		throw new ServiceExecutionException("Request timed out");
	}
	
	public void close(){
		try{
			this.queueAdapter.disconnect();
			String queueName = this.queueAdapter.getQueueMetaData().getQueueName();
			IQueueAdapter q = this.createRequestAdapter(MicroServiceConnectionFactory.CONNECTION_QUEUE_NAME);
			MicroserviceConnectionClose packet = new MicroserviceConnectionClose();
			packet.setPayload(queueName);
			q.push(packet);
		}catch(QueueException e){
			e.printStackTrace();
		}catch(QueueCreationException e){
			e.printStackTrace();
		}
	}
	
	private IQueueAdapter createRequestAdapter(String queueName) throws QueueCreationException,QueueException{
		String url = this.queueAdapter.getQueueMetaData().getQueueURL();
		QueueMetaData queueData = new QueueMetaData(queueName, url);
		QueueAdapterFactory factory = QueueAdapterFactory.getInstance();
		IQueueAdapter queueAdapter = factory.createAdapter("com.mini.io.adapter.ActiveMQAdapter", queueData);
		queueAdapter.connect();
		return queueAdapter;
	}
}
