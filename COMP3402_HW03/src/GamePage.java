import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;

public class GamePage extends Page{
	private String u_name;
    private ActionListener return2LoginPageBtn; 
    private MouseListener inforServerBtn;
	final static String USER_PROFILE_PANEL = "User Profile";
    final static String PLAY_GAME_PANEL = "Play Game";
    final static String LEADER_BOARD_PANEL = "Leader Board";
    final static String LOGOUT_PANEL = "Logout";
  	private GamePlayPanel game_play_panel; 
    final static int extraWindowWidth = 500;
 
//    public GamePage(ActionListener a, MouseListener m){
//    	return2LoginPageBtn = a;
//    	inforServerBtn = m;
//    	createGUI();
//    }
    public GamePage(ActionListener a){
    	return2LoginPageBtn = a;
//    	inforServerBtn = m;
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
        frame.pack();
    }
    
    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
 
        //Create the "cards".
        JPanel card1 = new JPanel() {
            //Make the panel wider than it really needs, so
            //the window's wide enough for the tabs to stay
            //in one row.
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        card1.add(new JButton("New Game"));
        
        game_play_panel = new GamePlayPanel();
 
        JPanel card3 = new LeaderBoardPanel();
 
        JPanel card4 = new LogoutPanel( return2LoginPageBtn );
 
        tabbedPane.addTab(USER_PROFILE_PANEL, card1);
        tabbedPane.addTab(PLAY_GAME_PANEL, game_play_panel);
        tabbedPane.addTab(LEADER_BOARD_PANEL, card3);
        tabbedPane.addTab(LOGOUT_PANEL, card4);
        
        pane.add(tabbedPane, BorderLayout.CENTER);
    }
 
   
    
    public void showGUI(){
    	frame.setVisible(true);
    }

    public void hideGUI(){
    	frame.setVisible(false);
    }
    
    public void reset(){
    	game_play_panel.reset();
    }
    
    public void set_user_name(String name){
    	game_play_panel.set_name(name);;
    }

}
