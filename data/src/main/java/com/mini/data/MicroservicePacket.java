package com.mini.data;

import java.io.Serializable;

import com.mini.utils.UUIDGenerator;

public class MicroservicePacket implements Serializable{

	private static final long serialVersionUID = -3078668681474484520L;
	
	private Object payload;
	private String packetID;
	private String correlationID;
	
	public MicroservicePacket(){
		this.packetID = UUIDGenerator.generateID();
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getCorrelationID() {
		return correlationID;
	}

	public void setCorrelationID(String correlationID) {
		this.correlationID = correlationID;
	}

	public String getPacketID() {
		return packetID;
	}
	
	
}
