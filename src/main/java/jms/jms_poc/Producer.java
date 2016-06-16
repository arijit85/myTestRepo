package jms.jms_poc;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer implements Runnable{
	MessageProducer producer;
	Connection connection;
	Session session;

	public Producer() throws JMSException, NamingException {
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
			this.producer = session.createProducer(destination);
		}
		
		public void sendMessage(String msg) throws JMSException{
			// We will send a small text message saying 'Hello World!'
			TextMessage message = session.createTextMessage(msg);
			// Here we are sending the message!
			producer.send(message);
			System.out.println("Sent message '" + message.getText() + "'");
		}
		
		public void shutdown() throws JMSException {
			connection.close();
		}

		public void run() {
			// TODO Auto-generated method stub
			while(true){
			Scanner sc = new Scanner(System.in);
			System.out.println("enter ur msg: ");
			String msg = sc.nextLine();
			try {
				this.sendMessage(msg);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}
