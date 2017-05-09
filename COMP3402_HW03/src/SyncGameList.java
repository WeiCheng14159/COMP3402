import java.util.ArrayList;



public class SyncGameList {

	private Object lock = new Object();
	private static final ArrayList<CardGame> game_list = new ArrayList<CardGame>();


	public synchronized void add( CardGame c){
		synchronized(lock){
			this.game_list.add(c);
		}
	}


	public synchronized CardGame get( int i ){
		synchronized(lock){
			return this.game_list.get(i);
		}
	}
	
	
	public synchronized void remove( int i){
		synchronized(lock){
			this.game_list.remove(i);
		}
	}
	
	
	public synchronized void remove( CardGame i){
		synchronized(lock){
			this.game_list.remove(i);
		}
	}
	
	
	public synchronized int size(){
		synchronized(lock){
			return this.game_list.size();
		}
	}
	
	public synchronized ArrayList<CardGame> getGameList(){
		synchronized(lock){
			return this.game_list;
		}
	} 
}

