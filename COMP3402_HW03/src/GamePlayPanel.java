import java.awt.*;
import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.swing.*;
import javax.naming.NamingException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

public class GamePlayPanel extends JPanel implements MouseListener, MessageListener{
	private static JPanel cardImage;
	private JTextArea input; 
	private String name = "";
	private String state = "initial";
	String host = "localhost";
	private JMSHelper jmsHelper;
	private MessageProducer queueSender;
	private MessageConsumer topicReceiver;
	/**
	 * 	state = 0 initial
	 	state = 1 wait 
		state = 2 play 
		state = 3 end
	 */
	
	/**
	 * GamePlayPanel constructor
	 */
	public GamePlayPanel() {
		init_gui();
//		init();
	}
	
	/**
	 * initialize panel gui
	 */
	private void init_gui(){
		this.addMouseListener(this);
		cardImage = new JPanel();
		cardImage.add( new JLabel("New game") );
		input = new JTextArea(1, 50);
		this.setLayout( new GridLayout(2, 1) );
		this.add(cardImage);
		this.add(input);
		this.setVisible(true);
	}

	/**
	 * MouseListener interface
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (state == "initial"){
			System.out.println("Connecting to server");
			new InformWorker().execute();
			cardImage.removeAll();
			cardImage.add(new JLabel("Waiting for players"));
        	state = "wait";
		}
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	public void set_name(String s ){
		name = s;
	}
	
	private class InformWorker extends SwingWorker<Boolean, Boolean>{
		
        @Override  
        protected Boolean doInBackground() throws JMSException{  

        	try {
				jmsHelper = new JMSHelper(host);
				init_jms();
				sendMessage( new ChatMessage(name, "server", ("Add:" + name) ) );
				return true;
			} catch (NamingException e) {
				e.printStackTrace();
				return false;
			}
        }  
        
        @Override  
        protected void done() {  
        	try {
        		JPanel p = GamePlayPanel.cardImage;
			    p.removeAll();
				if( !get() ){
					p.add(new JLabel("Fail to inform server"));
					System.err.println("Fail to connect to server");
				}
				p.revalidate();
			    p.repaint();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }  
	}
	
	/**
	 * create JMS instances 
	 * @throws JMSException
	 */
	private void init_jms() throws JMSException{
		// queueSender sends message to server 
		queueSender = jmsHelper.createQueueSender();
		
		// topicReceiver receives messages from server
		topicReceiver = jmsHelper.createTopicReader(name);
	    topicReceiver.setMessageListener(this);
	}

	private void sendMessage(ChatMessage msg) {
		if(msg != null) {
			System.out.println("Send message: "+msg);
			Message message = null;
			try {
				message = jmsHelper.createMessage(msg);
				message.setStringProperty("privateMessageTo", "server");
				message.setStringProperty("privateMessageFrom", name);
			} catch (JMSException e) {
			}
			if(message != null) {
				try {
					queueSender.send(message);
				} catch (JMSException e) {
					System.err.println("Failed to send message");
				}
			}
		}
	}
	
	/**
	 * reset the game play panel when logout
	 */
	public void reset(){
		this.state = "initial";
		cardImage.removeAll();
		cardImage.add( new JLabel("New game") );
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void onMessage(Message jmsMessage) {
		try {
	        ChatMessage chatMessage = (ChatMessage)((ObjectMessage)jmsMessage).getObject();
	        
	        System.out.println("Receive from server: " + chatMessage.toString() );
	        
        	//this message if for you 
        	if( chatMessage.message.contains("Card") ){
        		String msg = chatMessage.message.substring(5); 
        		System.out.println(msg);
        		this.state = "play";
        		createAndShowCardPanel( msg );
	        	
        	}else if( chatMessage.message.contains("End") ){
        		//end of the game 
        	}else{
        		//unknown command from server
        		System.out.println("Unknown command from server: " + chatMessage);
        	}
	        
	    } catch (JMSException e) {
	        System.err.println("Failed to receive message");
	    }
	}
	
	void createAndShowCardPanel(String s ){
		cardImage.removeAll();
		String [] tmp = s.split(":");

		for (int i = 0 ; i < tmp.length ; i = i + 2){
			cardImage.add( new CardPanel( Integer.parseInt(tmp[i]), Integer.parseInt(tmp[i+1]), 80, 100 ) );
		}
		this.revalidate();
		this.repaint();
	}
}
