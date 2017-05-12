import java.util.ArrayList;



public class GameList {

	private Object lock = new Object();
	private final ArrayList<CardGame> game_list = new ArrayList<CardGame>();

	public  void add( CardGame c){
		this.game_list.add(c);
	}

	public  CardGame get( int i ){
		return this.game_list.get(i);
	}
	
	public  void remove( int i){
		this.game_list.remove(i);
	}
	
	public  void remove( CardGame i){
		this.game_list.remove(i);
	}
	
	public  int size(){
		return this.game_list.size();
	}
	
	public  Object get_mutex(){
		return lock;
	}

	public  ArrayList<CardGame> getGameList(){
		return this.game_list;
	}
}

