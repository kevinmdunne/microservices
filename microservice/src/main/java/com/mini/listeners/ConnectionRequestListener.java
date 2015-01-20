package com.mini.listeners;

import com.mini.data.MicroserviceConnectionClose;
import com.mini.data.MicroserviceConnectionRequest;

public interface ConnectionRequestListener {

	public void connectionRequested(MicroserviceConnectionRequest request);
	
	public void connectionCloseRequested(MicroserviceConnectionClose request);
}
