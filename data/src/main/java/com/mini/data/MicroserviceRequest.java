package com.mini.data;

import com.mini.utils.UUIDGenerator;

public class MicroserviceRequest extends MicroservicePacket{

	private static final long serialVersionUID = -6417109377172396725L;
	
	private String serviceID;
	
	public MicroserviceRequest(String serviceID){
		super();
		this.setCorrelationID(UUIDGenerator.generateID());
		this.serviceID = serviceID;
	}
	
	public String getServiceID(){
		return this.serviceID;
	}
}
