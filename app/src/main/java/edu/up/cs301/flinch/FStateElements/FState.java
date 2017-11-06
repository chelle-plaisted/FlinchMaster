package edu.up.cs301.flinch.FStateElements;

import edu.up.cs301.card.*;
import edu.up.cs301.flinch.Deck;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Contains the state of a Slapjack game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 *
 * @author Steven R. Vegdahl 
 * @version July 2013
 */
public class FState extends GameState
{
	private static final long serialVersionUID = -8269749892027578792L;

	///////////////////////////////////////////////////
	// ************** instance variables ************
	///////////////////////////////////////////////////

	// the Deck the game will be played with
	Deck deck;

	// whose turn is it to turn a card?
	int toPlay;
	// how many players are there
	int numPlayers;
	// the individual player info
	FPlayerState[] players;
	// the contents of the center piles
	CenterPile[] center;

	// info on game stage: is it the start of the game?
	private boolean isStartOfGame;

	/**
	 * Constructor for objects of class SJState. Initializes for the beginning of the
	 * game, with a random player as the first to turn card
	 *
	 */
	public SJState(int num) {
		// initialize the number of the players in the game
		numPlayers = num;
		// randomly pick the player who starts
		toPlay = (int)(num * Math.random());

		// start a new Deck and shuffle it
		deck = new Deck();
		deck.shuffle();

		//this is the start of the game
		isStartOfGame = true;

		// initialize center piles to empty
		center = new CenterPile[10];
		for(CenterPile c : center){
			c = new CenterPile();
		}
		// initialize the players
		initPlayers(num);
	}

	/**
	 * Copy constructor for objects of class SJState. Makes a copy of the given state
	 *
	 * @param orig  the state to be copied
	 */
	public SJState(SJState orig) {
		// initialize the number of the players in the game
		numPlayers = orig.numPlayers;
		// pick the player who starts
		toPlay = orig.toPlay;

		// update the Deck
		deck = new Deck(orig.deck);

		//is this the start of the game
		isStartOfGame = orig.isStartOfGame;

		//center piles
		center = new CenterPile[10];
		for(int i = 0; i < 10; i++) {
			center[i] = new CenterPile(orig.center[i]);
		}

		// initialize the players
		players = new FPlayerState[numPlayers];
		for(int i = 0; i < numPlayers; i++) {
			players[i] = new FPlayerState(orig.players[i]);
		}
	}

	/* HELPER METHODS */

	/**
	 * initializes the information for all of the players for the start of the game
	 */
	private void initPlayers() {
		players = new FPlayerState[numPlayers];
		for (FPlayerState p : players) {
			p = new FPlayerState();
			// initialize Hand
			p.hand = new Hand(deck);
			// initialize Discard piles
			p.discards = new DiscardPile[5];
			for (DiscardPile d : p.discards) {
				// initialize each pile to empty
				d = new DiscardPile();
			}
			// initialize Flinch pile
			p.flinch = new FlinchPile(deck);
			p.hasFlinched = false;
		}
	}

	/* ACCESSORS */

	/**
	 * Gives the given deck.
	 *
	 * @return  the main deck of the game
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 *
	 * @return
	 * 	the tops of the center piles
	 */
	public Card[] getCenterPiles() {
		Card[] tops = new Card[10];
		// get all of the top cards
		for(int i = 0; i < 10; i++) {
			tops[i] = center[i].getTopCard();
		}
		return tops;
	}

	/**
	 * gets info about a certain player
	 * @param playerID
	 * 	index of the player desired
	 * @return
	 * 	the player's publicly available info
	 */
	public FPlayerState getPlayerState(int playerID) {
		return players[playerID];
	}

	/**
	 *
	 * @return
	 * 	whose turn it is
	 */
	public int getWhoseTurn() {
		return toPlay;
	}

	/**
	 *
	 * @return
	 * 	whether it is the start of the game
	 */
	public boolean getStartOfGame() {
		return isStartOfGame;
	}


	/* MUTATORS */
	/**
	 * Tells which player's turn it is.
	 *
	 * @return the index (0 through numPlayers - 1) of the player whose turn it is.
	 */
	public int toPlay() {
		return toPlay;
	}

	/**
	 * change whose move it is
	 *
	 * @param idx
	 * 		the index of the player whose move it now is
	 */
	public void setNextTurn(int idx) {
		toPlay = idx;
	}

	/**
	 * Generates a new hand for a certain player
	 * Assumes that the player is whomever's turn it is.
	 * 	the id of the player who needs a new hand
	 */
	public void replenishPlayerHand() {
		players[toPlay].hand.fillHand();
	}

	/**
	 *
	 * @param index
	 * 	index of the center pile that's full
	 */
	public void recycleFullCenterPile(int index){
		center[index].empty();
	}

	/**
	 * removes a card from a player's CardPile ( flinch, discard, hand) and plays it a centerPile
	 * 	Assumes that the player is whomever's turn it is.
	 * @param indexFrom
	 * 	the index they played the card from
	 * 		FlinchPile : assumes to be top card
	 * 		DiscardPile : index refers to which discard pile (0 - 4), assumed to be top card
	 * 		Hand : index of the card in the hand to play from
	 * @param p
	 * 	the CardPile played from (FlinchPile, DiscardPile, Hand)
	 * @param indexTo
	 * 	the index of the centerPile being played to (0 -9)
	 */
	public void playToCenter(int indexFrom, CardPile p, int indexTo) {
		// what cardPile was played from?
		Card c;
		if(p instanceof FlinchPile) {
			c = players[toPlay].flinch.removeTopCard();
		}else if (p instanceof DiscardPile) {
			c = players[toPlay].discards[indexFrom].removeTopCard();
		} else if (p instanceof Hand){
			c = players[toPlay].hand.removeCardAt(indexFrom);
		} else {
			// no valid CardPile found
			return;
		}
		if(c != null) {
			center[indexTo].add(c);
		}
	}

	/**
	 * removes a card from the correct player's ahnd and puts it into a certain CenterPile of the center array
	 * 	Assumes that the player is whomever's turn it is.
	 * @param indexFrom
	 * 	the index in the hand to discard
	 * @param indexTo
	 * 	the index being played to
	 */
	public void discard(int indexFrom, int indexTo) {
		Card c = players[toPlay].hand.removeCardAt(indexFrom);
		players[toPlay].discards[indexTo].add();
	}

	/**
	 * removes the bottom card from the accusing player's flinch pile and puts it to the bottom of
	 * the current player's flinch pile
	 * @param accusingPlayerId
	 * 	the player who pressed the flinch button
	 */
	public void flinchAPlayer(int accusingPlayerId) {
		Card c = players[accusingPlayerId].flinch.removeBottomCard();
		players[toPlay].flinch.addAt(c, players[toPlay].flinch.size() - 1);
	}

	public void notStartOfGame() {
		isStartOfGame = false;
	}

	/**
	 * sets whether a player could be flinched
	 * @param playerId
	 * 	the player in question
	 * @param flinchable
	 * 	whether or not the said player is flinchable
	 */
	public void setFlinchable(int playerId, boolean flinchable) {
		players[playerId].hasFlinched = flinchable;
	}

	// THE REST OF THESE ARE SEPARTATE
	/**
	 * Replaces all cards with null, except for the top card of deck 2
	 */
	public void nullAllButTopOf2() {
		// see if the middle deck is empty; remove top card from middle deck
		boolean empty2 = piles[2].size() == 0;
		Card c = piles[2].removeTopCard();

		// set all cards in deck to null
		for (Deck d : piles) {
			d.nullifyDeck();
		}

		// if middle deck had not been empty, add back the top (non-null) card
		if (!empty2) {
			piles[2].add(c);
		}
	}
}