import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This class is used to represent a player in general card games.
 * 
 * @author Kenneth Wong
 *
 */
public class CardGamePlayer {
	private static int playerId = 0;
	private boolean done = false; 
	private double score = 0;
	private String name = "";
	private CardList cardsInHand = new CardList();
//	private int played = 0;
//	private int win = 0;
	private String sol = "";
	
	/**
	 * Creates and returns an instance of the Player class.
	 */
	public CardGamePlayer() {
		this.name = "Player " + playerId;
		playerId++;
	}

	public void setSolution(String s){
		this.sol = s;
	}
	
	public String getSolution(){
		return this.sol;
	}
	
	public void done(){
		this.done = true;
	}
	
	public boolean donna(){
		return this.done;
	}
	
	/**
	 * Creates and returns an instance of the Player class.
	 * 
	 * @param name
	 *            the name of the player
	 */
	public CardGamePlayer(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of this player.
	 * 
	 * @return the name of this player
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of this player.
	 * 
	 * @param name
	 *            the name of this player
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the score of this player.
	 * 
	 * @return the score of this player
	 */
	public double getScore() {
		return this.score;
	}

	/**
	 * Sets the score of this player.
	 * 
	 * @param score
	 *            the score of this player
	 */
	public void setScore(double score) {
		this.score = score;
	}

	public void resetScore(double score){
		this.score = 0.0f;
	}
	
	/**
	 * Adds the specified card to this player.
	 * 
	 * @param card
	 *            the specified card to be added to this player
	 */
	public void addCard(Card card) {
		if (card != null) {
			cardsInHand.addCard(card);
		}
	}

	/**
	 * Removes the list of cards from this player, if they are held by this
	 * player.
	 * 
	 * @param cards
	 *            the list of cards to be removed from this player
	 */
	public void removeCards(CardList cards) {
		for (int i = 0; i < cards.size(); i++) {
			cardsInHand.removeCard(cards.getCard(i));
		}
	}

	/**
	 * Removes all cards from this player.
	 */
	public void removeAllCards() {
		cardsInHand = new CardList();
	}

	/**
	 * Returns the number of cards held by this player.
	 * 
	 * @return the number of cards held by this player
	 */
	public int getNumOfCards() {
		return cardsInHand.size();
	}

	/**
	 * Sorts the list of cards held by this player.
	 */
	public void sortCardsInHand() {
		cardsInHand.sort();
	}

	/**
	 * Returns the list of cards held by this player.
	 * 
	 * @return the list of cards held by this player
	 */
	public CardList getCardsInHand() {
		return cardsInHand;
	}

	/**
	 * Returns the list of cards played by this player. Player can select the
	 * cards to be played by specifying the indices (space-separated) of the
	 * cards via the console.
	 * 
	 * @param lastHandOnTable
	 *            the last hand of cards placed on the table
	 * @return the list of cards player by this player
	 */
	public CardList play(CardList lastHandOnTable) {
		CardList cards = new CardList();

		Scanner scanner = new Scanner(System.in);
		System.out.print(getName() + "'s turn: ");

		String input = scanner.nextLine();

		StringTokenizer st = new StringTokenizer(input);
		while (st.hasMoreTokens()) {
			try {
				int idx = Integer.parseInt(st.nextToken());
				if (idx >= 0 && idx < cardsInHand.size()) {
					cards.addCard(cardsInHand.getCard(idx));
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

		if (cards.isEmpty()) {
			return null;
		} else {
			return cards;
		}
	}
	
//	void addWinGame(){
//		this.win ++;
//	}
//	
//	void addPlayedGame(){
//		this.played ++;
//	}
//	
//	float getWinRate(){
//		if(this.played == 0)
//			return 0.0f;
//		else
//			return ((float)this.win/(float)this.played);
//	}
}
