package com.mini.exception;

public class ServiceExecutionException extends Exception{

	private static final long serialVersionUID = 536971435963225593L;

	public ServiceExecutionException(){
		
	}
	
	public ServiceExecutionException(String cause){
		super(cause);
	}
	
	public ServiceExecutionException(Exception cause){
		super(cause);
	}
}
