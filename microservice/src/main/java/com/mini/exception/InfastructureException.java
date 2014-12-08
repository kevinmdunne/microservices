package com.mini.exception;

public class InfastructureException extends Exception{

	private static final long serialVersionUID = -9064777524895990823L;

	public InfastructureException(){
		
	}
	
	public InfastructureException(Exception cause){
		super(cause);
	}
}
