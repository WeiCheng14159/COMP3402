import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.swing.*;
import javax.naming.NamingException;
import javax.jms.JMSException;

public class GamePlayPanel extends JPanel implements MouseListener{
	private String name = "";
	private String state = "initial";
	//state = 0 initial state 
	//state = 1 waiting for players 
	//state = 2 game playing 
	//state = 3 end of game
	
	public GamePlayPanel() {
		this.addMouseListener(this);
		this.add( new JLabel("New game") );
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("state: " + state);
		this.removeAll();
		if (state == "initial"){
			this.add(new JLabel("Waiting for players"));
			
			state = "wait";
		}
		this.revalidate();
		this.repaint();
	}

	public void start(){
		String prev_state = "";
		while(true){
			if(prev_state != state){
				this.removeAll();
				if( state == "initial"){
					this.add(new JLabel("New Game"));
				}else if( state == "wait"){
					this.add( new JLabel("Waiting"));
					inform_server( name );
				}else if( state == "play"){
					this.add( new JLabel("Playing"));
				}else if( state == "end"){
					
				}
				prev_state = state;
			}
			
			try{
				Thread.sleep(100);
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
			this.revalidate();
			this.repaint();
		}
	}
	
	private void inform_server( String msg ){
		String host = "localhost";
		QueueSender sender = null;
		try {
			sender = new QueueSender(host);
			sender.sendMessages( msg );
		} catch (NamingException | JMSException e) {
			System.err.println("Program aborted");
		} finally {
			if(sender != null) {
				try {
					sender.close();
				} catch (Exception e) { }
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		inform_server("Michael");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void set_name(String s ){
		name = s;
	}
}
