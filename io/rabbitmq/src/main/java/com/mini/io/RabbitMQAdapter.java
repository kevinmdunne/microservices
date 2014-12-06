package com.mini.io;

import com.mini.data.MicroservicePacket;
import com.mini.io.adapter.QueueAdapter;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;

public class RabbitMQAdapter extends QueueAdapter{

	public RabbitMQAdapter(QueueMetaData queueMetaData){
		super(queueMetaData);
	}
	
	@Override
	public void connect() throws QueueException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() throws QueueException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void push(MicroservicePacket arg0) throws QueueException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MicroservicePacket recieve() throws QueueException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MicroservicePacket recieve(long arg0) throws QueueException {
		// TODO Auto-generated method stub
		return null;
	}

}
