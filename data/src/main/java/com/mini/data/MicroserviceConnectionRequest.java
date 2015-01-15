package com.mini.data;

import com.mini.utils.UUIDGenerator;

public class MicroserviceConnectionRequest extends MicroservicePacket{

	private static final long serialVersionUID = -8724236691760062885L;

	public MicroserviceConnectionRequest(){
		super();
		this.setCorrelationID(UUIDGenerator.generateID());
	}
}
