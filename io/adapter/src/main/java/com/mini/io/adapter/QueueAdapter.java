package com.mini.io.adapter;

import com.mini.io.metadata.QueueMetaData;


public abstract class QueueAdapter implements IQueueAdapter{

	protected QueueMetaData queueMetaData;
	
	public QueueAdapter(QueueMetaData queueMetaData){
		this.queueMetaData = queueMetaData;
	}
	
	@Override
	public QueueMetaData getQueueMetaData(){
		return this.queueMetaData;
	}
}
