/**
 * This class is used to model a deck used in a BigTwo game
 * 
 * @author chengwei
 *
 */
public class _24Deck extends Deck {

	/**
	 * create and return an instance of BigTwoDeck class, and the deck is
	 * shuffled right after the creation
	 */
	public void initialize() {
		super.initialize();
		shuffle();
	}
}
