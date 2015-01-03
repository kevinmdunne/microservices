package com.mini.listeners;

public interface ServiceRegistrationListener {

	public void serviceRegistered(String serviceID,String queue);
	
	public void serviceDeregistered(String serviceID,String queue);
}
