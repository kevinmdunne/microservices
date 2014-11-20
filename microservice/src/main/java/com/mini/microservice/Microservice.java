package com.mini.microservice;

import com.mini.exception.ServiceExecutionException;
import com.mini.exception.InfastructureException;

public interface Microservice {

	public void start() throws InfastructureException;
	
	public void stop() throws InfastructureException;
	
	public void execute() throws ServiceExecutionException;
	
	public String getID();
}
