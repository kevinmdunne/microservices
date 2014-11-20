package com.mini.io.adapter;

import com.mini.data.MicroservicePacket;
import com.mini.io.adapter.QueueAdapter;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;

public class ActiveMQAdapter extends QueueAdapter{

	public ActiveMQAdapter(QueueMetaData queueMetaData){
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
