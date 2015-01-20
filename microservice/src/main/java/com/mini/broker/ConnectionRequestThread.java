package com.mini.broker;

import java.util.ArrayList;
import java.util.List;

import com.mini.data.MicroserviceConnectionClose;
import com.mini.data.MicroserviceConnectionRequest;
import com.mini.data.MicroservicePacket;
import com.mini.io.adapter.IQueueAdapter;
import com.mini.io.exception.QueueException;
import com.mini.listeners.ConnectionRequestListener;

public class ConnectionRequestThread implements Runnable{

	private static final long TIMEOUT = 5000;

	private IQueueAdapter queueAdapter;
	private boolean running;

	private List<ConnectionRequestListener> listeners;
	
	public ConnectionRequestThread(IQueueAdapter queueAdapter){
		this.queueAdapter = queueAdapter;
		this.listeners = new ArrayList<ConnectionRequestListener>();
	}
	
	public void addListener(ConnectionRequestListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ConnectionRequestListener listener){
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
					if (packet instanceof MicroserviceConnectionRequest) {
						for (ConnectionRequestListener listener : listeners) {
							listener.connectionRequested((MicroserviceConnectionRequest)packet);
						}
					}else if(packet instanceof MicroserviceConnectionClose){
						for (ConnectionRequestListener listener : listeners) {
							listener.connectionCloseRequested((MicroserviceConnectionClose)packet);
						}	
					}
					else{
						this.queueAdapter.push(packet);
					}
				}
			}
			this.queueAdapter.disconnect();
		} catch (QueueException e) {
			e.printStackTrace();
		}
	}
}
