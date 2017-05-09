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
	private JMSHelper jmsHelper = null;
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
	 * initialize panel GUI
	 */
	private void init_gui(){
		this.addMouseListener(this);
		cardImageBox = new JPanel();
		cardImageBox.add( new JLabel("New game") );
		messageBox = new JTextArea("Type your move here(1 chance to submit)", 1, 50);
		scoreBox = new JLabel("Point=?");
		playerBox = new JPanel();
		playerBox.setLayout( new BoxLayout(playerBox, BoxLayout.Y_AXIS) );
		this.setLayout( new BorderLayout() );
		this.add(cardImageBox, BorderLayout.NORTH);
		this.add(messageBox, BorderLayout.SOUTH);
		this.add(playerBox, BorderLayout.WEST);
		this.add(scoreBox, BorderLayout.EAST);
		this.setVisible(true);
		
		messageBox.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage( new ChatMessage(name, "server", "Move:"+messageBox.getText() ) );
					messageBox.setText("");;
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
			cardImageBox.removeAll();
			cardImageBox.add( new JLabel("New game") );
			this.scoreBox.setText("Point=?");
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
        		if( jmsHelper == null ){
        			jmsHelper = new JMSHelper(host);
    				init_jms();
        		}
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
		this.playerBox.removeAll();
		this.scoreBox.setText("Point=?");
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void onMessage(Message jmsMessage) {
		try {
	        ChatMessage chatMessage = (ChatMessage)((ObjectMessage)jmsMessage).getObject();
	        
//	        System.out.println("Receive from server: " + chatMessage.toString() );
	        
        	if( chatMessage.message.contains("Card") ){
        		this.state = all_state[2];
        		createAndShowCardPanel( chatMessage.message.substring(5) );
	        	
        	}else if( chatMessage.message.contains("End") ){
        		
        		this.state = all_state[3]; 
        		System.out.println( chatMessage.message.substring(4) );
        		endGamePanel( chatMessage.message.substring(4) );
        	}else if( chatMessage.message.contains("Score") ){
        		
        		updateScore( chatMessage.message.substring(6) );
        	}else if( chatMessage.message.contains("Player") ){
        		
        		updatePlayer( chatMessage.message.substring(7) );
        	}else if( chatMessage.message.contains("Result") ){
        		
        		this.state = all_state[3]; 
        		System.out.println( chatMessage.message.substring(7) );
        		endGamePanel( chatMessage.message.substring(7) );
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
			this.scoreBox.setText("Points="+s);
		}catch (Exception e){
			this.scoreBox.setText("Error");
		}
		this.revalidate();
		this.repaint();
	}
	
	void updatePlayer(String s){
		String [] user = s.split(":");
		this.playerBox.removeAll();
		
		for (String u : user){
			String [] tmp = u.split("_");
			if( tmp[0].equals(name) ){
				this.playerBox.add( new JLabel("(You): "+tmp[0]+", Win Games: "+tmp[1]+", Win_rate: "+tmp[2]) );
			}else{
				this.playerBox.add( new JLabel("Name: "+tmp[0]+", Win Games: "+tmp[1]+", Win_rate: "+tmp[2]) );
			}
			
		}
		
		for ( Component c : this.playerBox.getComponents() ){
			c.setSize(50, 30);
			((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.red));
		}
		
		this.revalidate();
		this.repaint();
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
		cardImageBox.add( new JLabel( msg_from_server + ",   Click to continue ... ") );
		this.playerBox.removeAll();
		this.revalidate();
		this.repaint();
	}
	
}
