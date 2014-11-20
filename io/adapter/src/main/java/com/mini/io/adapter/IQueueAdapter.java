package com.mini.io.adapter;

import com.mini.data.MicroservicePacket;
import com.mini.io.exception.QueueException;

public interface IQueueAdapter {

	public void push(MicroservicePacket packet) throws QueueException;
	
	public MicroservicePacket recieve() throws QueueException;
	
	public MicroservicePacket recieve(long timeout) throws QueueException;
	
	public void connect() throws QueueException;
	
	public void disconnect() throws QueueException;
}
