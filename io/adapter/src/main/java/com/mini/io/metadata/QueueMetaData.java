package com.mini.io.metadata;

public class QueueMetaData {
	
	private String queueName;
	private String queueURL;
	
	public QueueMetaData(){
		
	}
	
	public QueueMetaData(String queueName,String queueURL){
		this.queueName = queueName;
		this.queueURL = queueURL;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getQueueURL() {
		return queueURL;
	}

	public void setQueueURL(String queueURL) {
		this.queueURL = queueURL;
	}
}
