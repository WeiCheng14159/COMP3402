import java.util.ArrayList;
/**
 * BigTwo class implementation  
 * @author m2-4790k
 * 
 */
public class _24Game implements CardGame {

	private ArrayList<CardGamePlayer> player_list;
	private Deck deck = new _24Deck();
	private int idx = 0;
	
	public _24Game(CardGamePlayer a, CardGamePlayer b){
		player_list = new ArrayList<CardGamePlayer>();
		player_list.add(a);
		player_list.add(b);
		deck.initialize();
	}
	
	public _24Game(CardGamePlayer a, CardGamePlayer b,  CardGamePlayer c, CardGamePlayer d){
		player_list = new ArrayList<CardGamePlayer>();
		player_list.add(a);
		player_list.add(b);
		player_list.add(c);
		player_list.add(d);
		deck.initialize();
	}
	
	@Override
	public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return player_list.size();
	}

	@Override
	public Deck getDeck() {
		// TODO Auto-generated method stub
		return this.deck;
	}

	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		// TODO Auto-generated method stub
		return this.player_list;
	}

	@Override
	public int getCurrentIdx() {
		// TODO Auto-generated method stub
		return this.idx;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkMove(int[] cardIdx) {
		
	}

	@Override
	public boolean endOfGame() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
