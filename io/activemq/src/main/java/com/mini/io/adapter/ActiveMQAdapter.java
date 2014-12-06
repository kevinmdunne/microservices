package com.mini.io.adapter;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.mini.data.MicroservicePacket;
import com.mini.io.exception.QueueException;
import com.mini.io.metadata.QueueMetaData;

public class ActiveMQAdapter extends QueueAdapter{
	
	private MessageProducer producer;
	private MessageConsumer consumer;
	private Destination destination;
	private Connection connection;
	private Session session;
	
	public ActiveMQAdapter(QueueMetaData queueMetaData){
		super(queueMetaData);
	}

	@Override
	public void connect() throws QueueException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.queueMetaData.getQueueURL() + "?jms.prefetchPolicy.queuePrefetch=0");
		try{
			this.connection = connectionFactory.createConnection();
			connection.start();
			
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			this.destination = session.createQueue(this.queueMetaData.getQueueName());
		}catch(JMSException e){
			throw new QueueException(e);
		}
	}

	@Override
	public void disconnect() throws QueueException {
		try{
			if(this.producer != null){
				this.producer.close();
			}
			if(this.consumer != null){
				this.consumer.close();
			}
			if(this.session != null){
				this.session.close();
			}
			if(this.connection != null){
				this.connection.close();
			}
		}catch(JMSException e){
			throw new QueueException(e);
		}
	}
	
	private void createMessageProducer() throws JMSException{
		this.producer = this.session.createProducer(this.destination);
		this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	}
	
	private void createMessageConsumer() throws JMSException{
		this.consumer = this.session.createConsumer(this.destination);
	}

	@Override
	public void push(MicroservicePacket packet) throws QueueException {
		try{
			if(this.producer == null){
				this.createMessageProducer();
			}
			
			ObjectMessage message = this.session.createObjectMessage(packet);
			this.producer.send(message);
		}catch(JMSException e){
			throw new QueueException(e);
		}
	}

	@Override
	public MicroservicePacket recieve() throws QueueException {
		try{
			if(this.consumer == null){
				this.createMessageConsumer();
			}
			
			Message message = consumer.receive();			
			ObjectMessage objectMessage = (ObjectMessage)message;
			return (MicroservicePacket)objectMessage.getObject();
		}catch(JMSException e){
			throw new QueueException(e);
		}
	}

	@Override
	public MicroservicePacket recieve(long timeout) throws QueueException {
		try{
			if(this.consumer == null){
				this.createMessageConsumer();
			}
			
			Message message = consumer.receive(timeout);			
			ObjectMessage objectMessage = (ObjectMessage)message;
			return (MicroservicePacket)objectMessage.getObject();
		}catch(JMSException e){
			throw new QueueException(e);
		}
	}
}
