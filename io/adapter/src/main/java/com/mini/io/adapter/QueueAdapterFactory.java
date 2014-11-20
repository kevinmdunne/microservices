package com.mini.io.adapter;

import java.lang.reflect.Constructor;

import com.mini.io.exception.QueueCreationException;
import com.mini.io.metadata.QueueMetaData;

public class QueueAdapterFactory {

	private static QueueAdapterFactory INSTANCE;
	
	public static QueueAdapterFactory getInstance(){
		if(INSTANCE == null){
			INSTANCE  = new QueueAdapterFactory();
		}
		return INSTANCE;
	}
	
	private QueueAdapterFactory(){
		
	}
	
	public QueueAdapter createAdapter(String className,QueueMetaData queueMetaData)throws QueueCreationException{
		try{
			Class<?> clazz = Class.forName(className);			
			Constructor<?> cons = clazz.getConstructor(QueueMetaData.class);
			QueueAdapter adapter = (QueueAdapter) cons.newInstance(queueMetaData);
			return adapter;
		}catch(Exception e){
			QueueCreationException ex = new QueueCreationException(e);
			throw ex;
		}
	}
}
