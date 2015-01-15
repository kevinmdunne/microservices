package com.mini.listeners;

import com.mini.data.MicroserviceConnectionRequest;

public interface ConnectionRequestListener {

	public void connectionRequested(MicroserviceConnectionRequest request);
	
}
