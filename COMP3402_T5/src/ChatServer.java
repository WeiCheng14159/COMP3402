import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;


public class ChatServer {
	
	public static void main(String [] args) {
		try {
			new ChatServer().start();
		} catch (NamingException | JMSException e) {
			System.err.println("Program aborted."+e);
		}
	}
	
	private JMSHelper jmsHelper;
	
	public ChatServer() throws NamingException, JMSException {
		jmsHelper = new JMSHelper();
	}
	
	public void start() throws JMSException {
		MessageConsumer queueReader = jmsHelper.createQueueReader();
		MessageProducer topicSender = jmsHelper.createTopicSender();
		
		while(true) {
			Message jmsMessage = receiveMessage(queueReader);
			
	        ChatMessage chatMessage = (ChatMessage)((ObjectMessage)jmsMessage).getObject();
	        if(chatMessage.to != null && !chatMessage.to.isEmpty()) {
	        	//Received messages are read-only, We need to create a new message
	            jmsMessage = jmsHelper.createMessage(chatMessage);
	            jmsMessage.setStringProperty("privateMessageTo", chatMessage.to);
	            jmsMessage.setStringProperty("privateMessageFrom", chatMessage.from);
	        }
			
			broadcastMessage(topicSender, jmsMessage);
		}
	}
	public Message receiveMessage(MessageConsumer queueReader) throws JMSException {
		try {
	        Message jmsMessage = queueReader.receive();
	        ChatMessage chatMessage = (ChatMessage)((ObjectMessage)jmsMessage).getObject();
	        System.out.println(chatMessage);
	        return jmsMessage;
	    } catch(JMSException e) {
	        System.err.println("Failed to receive message "+e);
	        throw e;
	    }
	}
	public void broadcastMessage(MessageProducer topicSender, Message jmsMessage) throws JMSException {
//		System.out.println("Sending out " + jmsMessage.toString() );
		try {
	        topicSender.send(jmsMessage);
	    } catch(JMSException e) {
	        System.err.println("Failed to boardcast message "+e);
	        throw e;
	    }
	}
}
