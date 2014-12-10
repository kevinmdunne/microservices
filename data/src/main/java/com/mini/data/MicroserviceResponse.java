package com.mini.data;

public class MicroserviceResponse extends MicroservicePacket{

	private static final long serialVersionUID = -6500637629805011841L;
	
	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	
	private int status;
	private String statusMessage;
	
	public MicroserviceResponse(){
		super();
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public void setStatusMessage(String message){
		this.statusMessage = message;
	}
	
	public String getStatusMessage(){
		return statusMessage;
	}
}
