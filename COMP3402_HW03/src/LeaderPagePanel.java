import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.jms.JMSException;
import javax.swing.*;

public class LeaderPagePanel extends JPanel{
	private String name = "";
	private PokerGameApp app;
	
	public LeaderPagePanel(String _host ){
		JNDI_init(_host);
		init_gui();
	}
	/**
	 * initialize panel GUI
	 */
	private void init_gui(){
		this.setLayout( new BorderLayout() );
		this.add( new JLabel("init") , BorderLayout.NORTH);
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
    
    public void update_board( String _name ){
    	name = _name;
    	this.removeAll();
    	try {
			this.add( app.leader_board( name ) , BorderLayout.NORTH);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	this.revalidate();
    	this.repaint();
    }
    
    public void refresh(){
    	this.removeAll();
    	if( name != "" ){
    		try {
    			this.add( app.leader_board( name ) , BorderLayout.NORTH);
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
