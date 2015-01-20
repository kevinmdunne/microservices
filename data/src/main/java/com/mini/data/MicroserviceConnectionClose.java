package com.mini.data;

import com.mini.utils.UUIDGenerator;

public class MicroserviceConnectionClose extends MicroservicePacket{

	private static final long serialVersionUID = -7039622764806663842L;

	public MicroserviceConnectionClose(){
		super();
		this.setCorrelationID(UUIDGenerator.generateID());
	}
}
