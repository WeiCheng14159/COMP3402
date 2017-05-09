import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import javax.swing.*;

public class GamePage extends Page{
    private ActionListener return2LoginPageBtn; 
	final static String USER_PROFILE_PANEL = "User Profile";
    final static String PLAY_GAME_PANEL = "Play Game";
    final static String LEADER_BOARD_PANEL = "Leader Board";
    final static String LOGOUT_PANEL = "Logout";
  	private GamePlayPanel game_play_panel; 
  	private ProfilePagePanel profile_panel; 
  	private LeaderPagePanel leader_panel; 
    private final static int extraWindowWidth = 500;
    private final static int Height = 500;
    private final String host; 
    PokerGameApp app;

    public GamePage(ActionListener a, String _host){
    	host = _host; 
    	return2LoginPageBtn = a;
    	createGUI();
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createGUI() {
        //Create and set up the window.
        frame = new JFrame("TabDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        this.addComponentToPane(frame.getContentPane());
 
        //Display the window.
//        frame.pack();
    }
    
    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        super.frame.setSize( super.frame.getWidth() + extraWindowWidth, Height );
        
        profile_panel = new ProfilePagePanel( host, "" ); 
        
        game_play_panel = new GamePlayPanel( host );
 
        leader_panel = new LeaderPagePanel( host );
        
        JPanel card4 = new LogoutPanel( return2LoginPageBtn );
 
        tabbedPane.addTab(USER_PROFILE_PANEL, profile_panel);
        tabbedPane.addTab(PLAY_GAME_PANEL, game_play_panel);
        tabbedPane.addTab(LEADER_BOARD_PANEL, leader_panel);
        tabbedPane.addTab(LOGOUT_PANEL, card4);
        
        pane.add(tabbedPane, BorderLayout.CENTER);
    }
    
    public void reset(){
    	game_play_panel.reset();
    }
    
    public void set_user_name(String name){
    	game_play_panel.set_name(name);
    	profile_panel.update_name(host, name);
    }
}
