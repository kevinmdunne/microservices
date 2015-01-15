package com.mini.io.adapter;

import com.mini.data.MicroservicePacket;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;

public interface IQueueAdapter {

	public void push(MicroservicePacket packet) throws QueueException;
	
	public MicroservicePacket recieve() throws QueueException;
	
	public MicroservicePacket recieve(long timeout) throws QueueException;
	
	public void deleteQueue();
	
	public void connect() throws QueueException;
	
	public void disconnect() throws QueueException;
	
	public QueueMetaData getQueueMetaData();
}
