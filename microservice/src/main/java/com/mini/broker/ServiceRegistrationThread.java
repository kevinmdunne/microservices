package com.mini.broker;

import java.util.ArrayList;
import java.util.List;

import com.mini.data.MicroserviceDeregistration;
import com.mini.data.MicroservicePacket;
import com.mini.data.MicroserviceRegistration;
import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.exception.QueueException;
import com.mini.listeners.ServiceRegistrationListener;

public class ServiceRegistrationThread implements Runnable {

	private static final long TIMEOUT = 5000;

	private IQueueAdapter queueAdapter;
	private boolean running;

	private List<ServiceRegistrationListener> listeners;

	public ServiceRegistrationThread(IQueueAdapter queueAdapter) {
		this.queueAdapter = queueAdapter;
		this.listeners = new ArrayList<ServiceRegistrationListener>();
	}

	public void addRegistrationListener(ServiceRegistrationListener listener) {
		listeners.add(listener);
	}
	
	public void removeRegistrationListener(ServiceRegistrationListener listener){
		listeners.remove(listener);
	}
	
	public void start(){
		try{
			this.queueAdapter.connect();
			Thread thread = new Thread(this);
			thread.start();
		}catch(QueueException e){
			e.printStackTrace();
		}
	}

	public void stop() {
		this.running = false;
	}

	@Override
	public void run() {
		try {
			this.running = true;
			while (this.running) {
				MicroservicePacket packet = queueAdapter.recieve(TIMEOUT);
				if (packet != null) {
					if (packet instanceof MicroserviceRegistration) {
						String[] data = (String[]) packet.getPayload();
						for (ServiceRegistrationListener listener : listeners) {
							listener.serviceRegistered(data[0], data[1]);
						}
					}else if(packet instanceof MicroserviceDeregistration){
						String[] data = (String[]) packet.getPayload();
						for (ServiceRegistrationListener listener : listeners) {
							listener.serviceDeregistered(data[0], data[1]);
						}
					}
				}
			}
			this.queueAdapter.disconnect();
		} catch (QueueException e) {
			e.printStackTrace();
		}
	}

}
