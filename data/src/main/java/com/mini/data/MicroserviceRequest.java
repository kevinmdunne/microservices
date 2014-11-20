package com.mini.data;

public class MicroserviceRequest extends MicroservicePacket{

	private String serviceID;
	
	public MicroserviceRequest(String serviceID){
		super();
		
		this.serviceID = serviceID;
	}
	
	public String getServiceID(){
		return this.serviceID;
	}
}
