package com.mini.io.adapter;

import com.mini.data.MicroservicePacket;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;

public class DummyQueueAdapter extends QueueAdapter{
	
	public DummyQueueAdapter(){
		super(null);
	}
	
	public DummyQueueAdapter(QueueMetaData queueMetaData){
		super(queueMetaData);
	}

	@Override
	public void connect() throws QueueException {
		
	}

	@Override
	public void disconnect() throws QueueException {

	}

	@Override
	public void push(MicroservicePacket arg0) throws QueueException {

	}

	@Override
	public MicroservicePacket recieve() throws QueueException {
		return null;
	}

	@Override
	public MicroservicePacket recieve(long arg0) throws QueueException {
		return null;
	}

}
