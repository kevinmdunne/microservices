package com.mini.io.data;

import com.mini.io.utils.UUIDGenerator;

public class MicroservicePacket {

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
