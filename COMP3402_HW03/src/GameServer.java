import java.rmi.*;
import java.rmi.server.*;
import java.sql.SQLException;
import java.io.*;
import java.util.*;
import java.util.Timer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import javax.swing.*;

public class GameServer extends UnicastRemoteObject implements PokerGameApp
{
	
	private ArrayList<CardGamePlayer> wait_list; 
	private GameList game_list;
	
	private DBManager db; 
	
	private static final Object mutex = new Object();
	
	MessageConsumer queueReader;
	MessageProducer topicSender;
	
	/**
	 * PUBLIC GAMESERVER CONSTRUCTOR
	 * @throws RemoteException
	 */
	public GameServer() throws RemoteException, NamingException, JMSException{
		jmsHelper = new JMSHelper();
		wait_list = new ArrayList<CardGamePlayer>();
		game_list = new GameList();
		init_db();
	}

	/**
	 * SERVER MAIN METHOD
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			GameServer app = new GameServer();
			System.setSecurityManager(new SecurityManager());
			Naming.rebind("PokerGameApp", app );
			System.out.println("Service registered with name PokerGameApp");
			app.start();
		} catch(Exception e) {
			System.err.println("Exception thrown: "+e);
		}
	}
	
	/**
	 * INITIALIZE DATA BASE
	 */
	private void init_db(){
		try {
			db = new DBManager();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			System.out.printf("DB connection fail");
			e.printStackTrace();
		}
	}
	
	/**
	 * LOGIN METHOD: HANDLE CLIENT LOGIN
	 * @param _user_name
	 * @param _user_password
	 * @return
	 */
	private boolean login(String _user_name, String _user_password){
		if( db.login_user(_user_name, _user_password) ){
			db.online_user(_user_name);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * REGISTER METHOD: HANDLE NEW REGISTER CLIENT 
	 * @param _user_name
	 * @param _password
	 * @return
	 */
	private boolean register(String _user_name, String _user_password){
		return db.add_user(_user_name, _user_password);
	}
	
	/**
	 * PLAY_GAME METHOD: PROVIDE CLIENT GAME PANEL
	 * @param u_name
	 * @param pw
	 * @return
	 */
	private String get_cards(String u_name) {
		CardGamePlayer tmp = null;
		synchronized(mutex){
			for (CardGame g : game_list.getGameList() ){
				for(CardGamePlayer p : g.getPlayerList() ){
					if(p.getName() == u_name ){
						tmp = p;
						for(Card card : g.getDeck().pop4() ){
							p.addCard(card);
							System.out.println( "user has card: " + card.toString() );
						}
					}
				}
			}
		}

		if (tmp != null ){
			return tmp.getCardsInHand().toString();
		}else{
			return "";
		}
		
	}
	
	/**
	 * LOGOUT METHOD: HANDLE CLIENT LOGOUT
	 * @param _user_name
	 * @return
	 */
	private boolean logout(String _user_name){
		if( db.logout_user(_user_name) ){
			db.offline_user(_user_name);
			return true;
		}else{
			return false;
		}
	}
	
	private JMSHelper jmsHelper;
	private void start() throws JMSException {
		queueReader = jmsHelper.createQueueReader();
		topicSender = jmsHelper.createTopicSender();
		
		Thread dispatch_thd = new Thread( new Dispath_thd() );
		dispatch_thd.start();
		
		Thread onlinePlayer_thd = new Thread( new OnlinePlayer_thd() );
		onlinePlayer_thd.start();
		
		Thread gameOver_thd = new Thread( new GameOver_thd() );
		gameOver_thd.start();
		
		while(true) {
			Message jmsMessage = receiveMessage(queueReader);
			ChatMessage message = (ChatMessage)((ObjectMessage)jmsMessage).getObject();
			
	        if(message != null && message.to.equals("server")) {
	        	
	        	if(message.message.contains("Add:")){
	        		System.out.println("New user: " + message);
	        		add_wait( new CardGamePlayer(message.message.substring(4)) );
	        		
	        	}else if(message.message.contains("Move:")){
	        		System.out.println("Msg: " + message);
	        		String result = makeMove( message.message.substring(5), message.from );
	        		
	        		if( result == "offline"){
	        			jmsMessage = jmsHelper.createMessage( new ChatMessage("server", message.from, "End:Other players go offline") );
	        		}else if( result != null ){
	        			jmsMessage = jmsHelper.createMessage( new ChatMessage("server", message.from, "Score:" + result.substring(0, 4)  ) );
	        		}else{
	        			jmsMessage = jmsHelper.createMessage( new ChatMessage("server", message.from, "Score:Wrong format" ) );
	        		}
	        		jmsMessage.setStringProperty("privateMessageTo", message.from);
		            jmsMessage.setStringProperty("privateMessageFrom", "server");

					broadcastMessage(topicSender, jmsMessage);	
	        	}else{
	        		System.out.println("Unkown command "+ message);
	        	}
	    
	        }
		}
	}
	
	private String makeMove(String token, String _name){
		System.out.printf("Token: %s, name: %s", token, _name);
		CardGamePlayer player = null;
		synchronized(mutex){	
			for( CardGame g : game_list.getGameList() ){
				for(CardGamePlayer p : g.getPlayerList() ){
					if( p.getName().equals(_name) )
						player = p;
				}
			}
		}
		
		if( player != null){
			double r = 0;
			try {
				r = new InfixPostfixEvaluator().evalInfix(token);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			if( player.donna() ){
				System.out.printf("User: %s has made your move", _name );
				return player.getScore() + "(Answer submitted)";
			}else{
				System.out.printf("User: %s Move: %s Total: %f", _name, token.toString(), r );
				player.setScore( Math.abs(24.0f-r) );
				player.setSolution(token);
				player.done();
				return String.valueOf(r);
			}
			
		}else{
			System.out.println("No such user" );
			return "offline";
		}
	}
	
	/**
	 * add new user to game pairing list 
	 * @param u
	 */
	private synchronized void add_wait ( CardGamePlayer u ){
		wait_list.add(u);
	}
	
	private synchronized void del_wait(CardGamePlayer s){
		if(wait_list.contains(s)){
			wait_list.remove(s);
		}else{
			System.err.println("No such user");
		}
	}
	
	private class OnlinePlayer_thd implements Runnable{

		@Override
		public void run() {
			while(true){
				synchronized(mutex){	
					for( CardGame g : game_list.getGameList() ){
						String msg = "";
						for(CardGamePlayer p : g.getPlayerList()){
							msg += ( GameServer.this.db.get_info( p.getName() ).toString() ) +":"; 
						}
	//					System.out.println("Users in this game: " + msg);
						for(CardGamePlayer p : g.getPlayerList()){
							ChatMessage PlayerMsg = new ChatMessage("server", p.getName(), "Player:"+msg);
							try {
								Message jmsMessage = jmsHelper.createMessage( PlayerMsg );
								jmsMessage.setStringProperty("privateMessageTo", p.getName());
					            jmsMessage.setStringProperty("privateMessageFrom", "server");
								broadcastMessage(GameServer.this.topicSender, jmsMessage);	
							} catch (JMSException e) {
								e.printStackTrace();
							}
						}
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class GameOver_thd implements Runnable{

		@Override
		public void run() {
			while(true){
				//end of game ? 
				boolean game_over = true; 
				CardGame tmp_game = null;
				
				synchronized(mutex){
					for( CardGame g : game_list.getGameList() ){
						game_over = true;
						for(CardGamePlayer p : g.getPlayerList()){
							game_over = game_over && p.donna();
						}
						
						if (game_over && game_list.size() != 0){
							tmp_game = g;
							//find winner
							ArrayList<Double> list = new ArrayList<Double>();
							double min = 999.0f;
							for(int i = 0; i < tmp_game.getPlayerList().size() ; i++ ){
								double tmp = tmp_game.getPlayerList().get(i).getScore();
								if( tmp < min){
									min = tmp;
									tmp_game.set_best_sol( tmp_game.getPlayerList().get(i).getSolution() );
								}
								list.add(tmp);
							}
							//send result to user
							list.sort( new doubleComparator() );
							System.out.println("Sorted list: " + list);
							
							for(CardGamePlayer p : tmp_game.getPlayerList()){
								ChatMessage tmp_msg;
								GameServer.this.db.add_played_game( p.getName() );	
								if( list.indexOf(min) != list.lastIndexOf(min) ){//match game
									if( p.getScore() == min ){
										GameServer.this.db.add_win( p.getName() );
										tmp_msg = new ChatMessage("server", p.getName(), String.format("Result:Match,You win!, your win rate %3.2f", GameServer.this.db.get_win_rate( p.getName() ) ) );
									}else{
										tmp_msg = new ChatMessage("server", p.getName(), ( "Result:Lose, winner's sol is "+tmp_game.get_best_sol() ) );
									}
								}else{//someone win
									if( p.getScore() == min ){
										GameServer.this.db.add_win( p.getName() );
										tmp_msg = new ChatMessage("server", p.getName(), String.format("Result:You win!, your win rate %3.2f", GameServer.this.db.get_win_rate( p.getName() ) ) );
									}else{
										tmp_msg = new ChatMessage("server", p.getName(), ( "Result:Lose, winner's sol is "+tmp_game.get_best_sol() ) );
									}
								}
								try {
									Message jmsMessage = jmsHelper.createMessage( tmp_msg );
									jmsMessage.setStringProperty("privateMessageTo", p.getName());
						            jmsMessage.setStringProperty("privateMessageFrom", "server");
									broadcastMessage(GameServer.this.topicSender, jmsMessage);	
								} catch (JMSException e) {
									e.printStackTrace();
								}
							}
//							 rank function GameServer.this.db;
							break;
						}
					}
					game_list.remove(tmp_game);
				}
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		private class doubleComparator implements Comparator<Double>{
			@Override
			public int compare(Double o1, Double o2) {
				return (int)(o1-o2);
			}	
		}
	}
	
	private class Dispath_thd implements Runnable {
		
		@Override
		 public void run() {
			while(true){
				if(wait_list.size() > 0)
					System.out.println("Wait list users: " + wait_list.toString());
				if( wait_list.size() >= 4 ){
					
					synchronized(mutex){
						//create new card game
						game_list.add( new _24Game(wait_list.get(0), wait_list.get(1), wait_list.get(2), wait_list.get(3)));
					}
					//send cards to user  
					for (int i = 0 ; i < 4 ; i ++ ){
						ChatMessage tmp = new ChatMessage("server", wait_list.get(i).getName(), ( "Card:" + get_cards(wait_list.get(i).getName() ) ) );
						try {	
							Message jmsMessage = jmsHelper.createMessage( tmp );
							jmsMessage.setStringProperty("privateMessageTo", tmp.to);
				            jmsMessage.setStringProperty("privateMessageFrom", tmp.from);

							broadcastMessage(GameServer.this.topicSender, jmsMessage);	
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
					
					synchronized(mutex){
						//shuffle the deck 
						game_list.get(game_list.size()-1 ).getDeck().shuffle();
					}
					//delete from wait list
					for(int i = 0 ; i < 4 ; i ++)
						del_wait(wait_list.get(0));

				}else if ( wait_list.size() == 2 ){
					
					synchronized(mutex){
						//create new card game
						game_list.add( new _24Game( wait_list.get(0), wait_list.get(1) ) );
					}
					//send cards to user  
					for (int i = 0 ; i < 2 ; i ++ ){
						ChatMessage tmp = new ChatMessage("server", wait_list.get(i).getName(), ( "Card:" + get_cards(wait_list.get(i).getName() ) ) );
						try {
							Message jmsMessage = jmsHelper.createMessage( tmp );
							jmsMessage.setStringProperty("privateMessageTo", tmp.to);
				            jmsMessage.setStringProperty("privateMessageFrom", tmp.from);

							broadcastMessage(GameServer.this.topicSender, jmsMessage);	
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
					
					synchronized(mutex){
						//shuffle the deck 
						game_list.get( game_list.size()-1).getDeck().shuffle();
					}
					//delete from wait list
					for(int i = 0 ; i < 2 ; i ++)
						del_wait(wait_list.get(0));
				}
				
				try{
					for( int i = 0 ; i < 10 ; i ++){
						synchronized(mutex){
							if( game_list.size() >= 4){
								break;
							}
						}
						Thread.sleep(1000);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}	
		}
	}
	
	/**
	 * 
	 * @param queueReader
	 * @return
	 * @throws JMSException
	 */
	private Message receiveMessage(MessageConsumer queueReader) throws JMSException {
		try {
	        Message jmsMessage = queueReader.receive();
	        ChatMessage chatMessage = (ChatMessage)((ObjectMessage)jmsMessage).getObject();
	        System.out.println("Receive message from user: " + chatMessage);
	        return jmsMessage;
	    } catch(JMSException e) {
	        System.err.println("Failed to receive message "+e);
	        throw e;
	    }
	}
	
	private void broadcastMessage(MessageProducer topicSender, Message jmsMessage) throws JMSException {
//		System.out.println("Sendint message to everyone: " + jmsMessage.toString() );	
		try {
	        topicSender.send(jmsMessage);
	    } catch(JMSException e) {
	        System.err.println("Failed to boardcast message "+e);
	        throw e;
	    }
	}

	@Override
	public boolean authenticate(String cmd_code, String u_name, String pw) throws RemoteException {
		if ( cmd_code.equals("login") || cmd_code.equals("profile") || cmd_code.equals("leader_board") || cmd_code.equals("play_game")){
			return login(u_name, pw);
		}else if ( cmd_code.equals("register") ){
			return register(u_name, pw);
		}else if (cmd_code.equals("logout")){
			return logout(u_name);
		}else{
			System.out.println("Wrong command code from client, I got " + cmd_code );
			return false; 
		}
	}

	@Override
	public String request(String cmd_code, String u_name, String pw) throws RemoteException {
		if( !login(u_name, pw)){
			System.out.println("Request invalid from unknown client");
			return ("Please login again");
		}
		if (cmd_code.equals("profile") ){
			return ("User name: " + u_name + "\n Number of wins: 10 \n Number of games: 10 \n Average time to win: 2 min");
		}else if(cmd_code.equals("leader_board") ){
			return ("First Place: " + u_name + "\n Second Place: Apple \n Third Place: Banana " );
		}else if (cmd_code.equals("play_game") ){
			return ("Hi, " + u_name + " ! Game time ! ");
		}else{
			System.out.println("Unknow command code");
			return ("Unknown command code");
		}
	}

	@Override
	public JLabel profile(String u_name) throws RemoteException {
		return new JLabel( db.get_info(u_name).to_profile() );
	}

	@Override
	public JLabel leader_board(String u_name) throws RemoteException {
		String m = "<html><table><tr><th>Name</th><th>Played</th><th>Wins</th><th>Rate</th><th>Rank#</th></tr>";
		for( PlayerInfoMessage msg : db.get_full_rank() )
		{
			m += ( msg.to_board() ) ;
		}
		m += "</table></html>";
		return new JLabel( m );
	}
}
