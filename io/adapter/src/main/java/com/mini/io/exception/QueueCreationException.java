package com.mini.io.exception;

public class QueueCreationException extends Exception{

	private static final long serialVersionUID = 1705640889568066629L;

	public QueueCreationException(Exception e){
		super(e);
	}
}
