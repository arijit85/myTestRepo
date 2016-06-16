package jms.jms_poc;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Consumer implements Runnable{
	
	MessageConsumer consumer;
	Connection connection;
	Session session;

	public Consumer() throws JMSException, NamingException {
		// Obtain a JNDI connection
		InitialContext jndi = new InitialContext();
		// Look up a JMS connection factory
		ConnectionFactory conFactory = (ConnectionFactory) jndi
				.lookup("connectionFactory");
		
		// Getting JMS connection from the server and starting it
		connection = conFactory.createConnection();
			connection.start();
			// JMS messages are sent and received using a Session. We will
			// create here a non-transactional session object. If you want
			// to use transactions you should set the first parameter to 'true'
			session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Destination destination = (Destination) jndi.lookup("MyQueue");
			// MessageProducer is used for sending messages (as opposed
			// to MessageConsumer which is used for receiving them)
			this.consumer = session.createConsumer(destination);
		}
	
	public String receiveMessage() throws JMSException{
					// Here we receive the message.
			// By default this call is blocking, which means it will wait
			// for a message to arrive on the queue.
			Message message = consumer.receive();
			// There are many types of Message and TextMessage
			// is just one of them. Producer sent us a TextMessage
			// so we must cast to it to get access to its .getText()
			// method.
			TextMessage textMessage=null;
			if (message instanceof TextMessage) {
				textMessage = (TextMessage) message;
				
			}
			return textMessage.getText();
	}
		public void shutdown() throws JMSException {
			connection.close();
		}

		public void run() {
			while(true){
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				System.out.println("Received message '" + this.receiveMessage()
						+ "'");
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}

