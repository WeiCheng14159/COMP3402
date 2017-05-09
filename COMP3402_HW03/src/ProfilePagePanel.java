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
	private String name = "";
	private PokerGameApp app;
	
	public ProfilePagePanel(String _host, String _name ){
		this.name = _name;
		JNDI_init(_host);
		init_gui();
	}
	/**
	 * initialize panel GUI
	 */
	private void init_gui(){
		this.setLayout( new BorderLayout() );
		try {
			this.add( app.profile( name ) , BorderLayout.NORTH);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
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
			System.out.println( app.toString() );
		} catch(Exception ex) {
		    System.err.println("Failed accessing RMI:"+ex);
		    return;
		}
    }
    
    public void update_name(String _host, String _name){
    	this.removeAll();
    	try {
			this.add( app.profile( _name ) , BorderLayout.NORTH);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	this.revalidate();
    	this.repaint();
    }
}
