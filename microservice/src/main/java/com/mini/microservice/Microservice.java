package com.mini.microservice;

import com.mini.data.MicroserviceRequest;
import com.mini.exception.ServiceExecutionException;
import com.mini.exception.InfastructureException;

public interface Microservice {

	public final String SERVICE_REGISTRATION_QUEUE = "service-registrar";
	
	public void start() throws InfastructureException;
	
	public void stop() throws InfastructureException;
	
	public void execute(MicroserviceRequest request) throws ServiceExecutionException;
	
	public String getID();
}
