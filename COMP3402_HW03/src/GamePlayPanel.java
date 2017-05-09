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
	private static JPanel cardImageBox;
	private JTextArea messageBox; 
	private JLabel scoreBox;
	private JPanel playerBox;
	private String name = "";
	private final String [] all_state = {"initial", "wait", "play", "end"};
	private String state = all_state[0];
	String host = "localhost";
	private JMSHelper jmsHelper;
	private MessageProducer queueSender;
	private MessageConsumer topicReceiver;
	
	/**
	 * GamePlayPanel constructor
	 */
	public GamePlayPanel(String _host) {
		init_gui();
		init(_host);
	}

	
	/**
	 * initialize panel gui
	 */
	private void init_gui(){
		this.addMouseListener(this);
		cardImageBox = new JPanel();
		cardImageBox.add( new JLabel("New game") );
		messageBox = new JTextArea("Type your move here", 1, 50);
		scoreBox = new JLabel("n/a");
		playerBox = new JPanel();
		this.setLayout( new GridLayout(2, 2) );
		this.add(cardImageBox);
		this.add(messageBox);
		this.add(playerBox);
		this.add(scoreBox);
		this.setVisible(true);
		
		messageBox.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage( new ChatMessage(name, "server", "Move:"+messageBox.getText() ) );
					messageBox.setText("");
				}
			}
		});
	}
	
	/**
	 * initialize 
	 */
	void init(String _host){
		this.host = _host;
	}
	
	/**
	 * MouseListener interface
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if ( state == all_state[0] ){
			System.out.println("Connecting to server ... ");
			new InformWorker().execute();
			cardImageBox.removeAll();
			cardImageBox.add(new JLabel("Waiting for players"));
        	state = all_state[1];
		}else if ( state == all_state[3] ){
			state = all_state[0];
			cardImageBox = new JPanel();
			cardImageBox.add( new JLabel("New game") );
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
		    	
		    	if( !get() ){
		    		JPanel p = GamePlayPanel.cardImageBox;
			    	p.removeAll();
					p.add(new JLabel("Fail to connect to server"));
					System.err.println("Fail to connect to server");
					p.revalidate();
				    p.repaint();
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
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
		this.state = all_state[0];
		cardImageBox.removeAll();
		cardImageBox.add( new JLabel("New game") );
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void onMessage(Message jmsMessage) {
		try {
	        ChatMessage chatMessage = (ChatMessage)((ObjectMessage)jmsMessage).getObject();
	        
	        System.out.println("Receive from server: " + chatMessage.toString() );
	        
        	if( chatMessage.message.contains("Card") ){
        		this.state = all_state[2];
        		createAndShowCardPanel( chatMessage.message.substring(5) );
	        	
        	}else if( chatMessage.message.contains("End") ){
        		this.state = all_state[3]; 
        		System.out.println( chatMessage.message.substring(5) );
        		endGamePanel( chatMessage.message.substring(5) );
        	}else if( chatMessage.message.contains("Score") ){
        		updateScore( chatMessage.message.substring(6) );
        	}else if( chatMessage.message.contains("Player") ){
        		updatePlayer( chatMessage.message.substring(7) );
        	}else{
        		System.out.println("Unknown command from server: " + chatMessage);
        	}
	    } catch (JMSException e) {
	        System.err.println("Failed to receive message");
	    }
	}
	
	void updateScore(String s){
		System.out.println("Update score"+s);
		try{
			this.scoreBox.setText("Score="+s);
		}catch (Exception e){
			this.scoreBox.setText("Error");
		}
		this.revalidate();
		this.repaint();
	}
	
	void updatePlayer(String s){
		
	}
	
	void createAndShowCardPanel(String s ){
		cardImageBox.removeAll();
		String [] tmp = s.split(":");

		for (int i = 0 ; i < tmp.length ; i = i + 2){
			cardImageBox.add( new CardPanel( Integer.parseInt(tmp[i]), Integer.parseInt(tmp[i+1]), 80, 100 ) );

		this.revalidate();
		this.repaint();
		}
	}
	
	void endGamePanel( String msg_from_server ){
		cardImageBox.removeAll();
		cardImageBox.add( new JLabel( msg_from_server ) );
		this.revalidate();
		this.repaint();
	}
}
