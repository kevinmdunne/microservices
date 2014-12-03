package com.mini.data;

public class MicroserviceRequest extends MicroservicePacket{

	private static final long serialVersionUID = -6417109377172396725L;
	
	private String serviceID;
	
	public MicroserviceRequest(String serviceID){
		super();
		
		this.serviceID = serviceID;
	}
	
	public String getServiceID(){
		return this.serviceID;
	}
}
