import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.swing.*;

public class ProfilePagePanel extends JPanel{
	private String _user_name = "";
	private PokerGameApp app;
	private String host = "";
	
	public ProfilePagePanel(String _host){
		this.host = _host;
		JNDI_init( host );
		init_gui();
	}
	
	/**
	 * initialize panel GUI
	 */
	private void init_gui(){
		this.setLayout( new BorderLayout() );
		this.add( new JLabel("Init") , BorderLayout.NORTH);
		this.setVisible(true);
	}
	
	/**
	 * create JMS instances 
	 * @throws JMSException
	 */
    private void JNDI_init(String _host ){
    	try {
			Registry registry = LocateRegistry.getRegistry( _host );
			app = (PokerGameApp)registry.lookup("PokerGameApp");
		} catch(Exception ex) {
		    System.err.println("Failed accessing RMI:"+ex);
		    return;
		}
    }
    
    public void update_profile( String _name ){
    	_user_name = _name;
    	this.removeAll();
    	try {
			this.add( app.profile( _user_name ) , BorderLayout.NORTH);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	this.revalidate();
    	this.repaint();
    }
    
    public void refresh(){
    	this.removeAll();
    	if( _user_name != "" ){
    		try {
    			this.add( app.profile( _user_name ) , BorderLayout.NORTH);
    		} catch (RemoteException e) {
    			e.printStackTrace();
    		}
    	}else{
    		this.add( new JLabel("") );
    	}
    	this.revalidate();
    	this.repaint();
    }
}
