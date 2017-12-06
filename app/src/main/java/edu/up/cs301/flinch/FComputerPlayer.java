package edu.up.cs301.flinch;

import edu.up.cs301.card.*;
import edu.up.cs301.cardpile.Hand;
import edu.up.cs301.flinch.FStateElements.*;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * This is a computer player that slaps at an average rate given
 * by the constructor parameter.
 *
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class FComputerPlayer extends GameComputerPlayer
{
	// the minimum reaction time for this player, in milliseconds
	protected double minReactionTimeInMillis;

	// the most recent state of the game
	protected FState savedState;
	protected FPlayerState me;

	// constants
	protected static final int LOOK_FLINCH = 0;
	protected static final int LOOK_HAND = 1;
	protected static final int LOOK_DISCARD = 2;

	/**
	 * Constructor for the FComputerPlayer class; creates an "average"
	 * player.
	 *
	 * @param name
	 * 		the player's name
	 */
	public FComputerPlayer(String name) {
		// invoke general constructor to create player whose average reaction

		super(name);
	}

	/*
     * Constructor for the FComputerPlayer class
     */
	/*
	public FComputerPlayer(String name, double avgReactionTime) {
		// invoke superclass constructor
		super(name);

		// set the minimim reaction time, which is half the average reaction
		// time, converted to milliseconds (0.5 * 1000 = 500)
		minReactionTimeInMillis = 500*avgReactionTime;
	}
	*/

	/**
	 * Invoked whenever the player's timer has ticked. It is expected
	 * that this will be overridden in smart players.
	 */
	@Override
	protected void timerTicked() {
		// stop the timer, since we don't want another timer-tick until it
		// again is explicitly started
		getTimer().stop();
	}

	/**
	 * callback method, called when we receive a message, typicallly from
	 * the game
	 */
	@Override
	protected void receiveInfo(GameInfo info) {
		// if it is a flinch action pending--wait by returning
		// if we don't have a game-state, ignore
	//	synchronized (syncObj) {
			if (!(info instanceof FState)) {
				return;
			}

			// update our state variable
			savedState = (FState) info;
			me = savedState.getPlayerState(this.playerNum);

			// if it is our turn, we will play cards
			// otherwise, monitor for Flinches if able to

			// rest 3 seconds so play isn't immediate
			sleep(3000);
	//	}
	}

	/**
	 * tells whether or not a card is playable to the center piles
	 * @param cardVal
	 * 	the card value to check
	 * @return
	 * 	the index of a pile to play on (-1 if none available)
	 */
	public int isCardPlayable(int cardVal) {
		// a card is playable if it is a one OR the next lowest denomination is in the center pile
		// if the card is "empty" (meaning it was a blank space), this is not playable
		if (cardVal == 0) return -1;
		int[] center = savedState.getCenterPiles();
		for(int i = 0; i < center.length; i++) {
			if(center[i] == -1 && cardVal == 1) {
				return i;
			} else if (center[i] == cardVal - 1) {
				return i;
			}
		}
		return -1;
	}


	/**
	 * method to discard a card
	 */
	protected void discard() {
		// select a random card from the hand
		Hand h = savedState.getPlayerState(this.playerNum).getHand();
		int handIndex = (int) (Math.random() * h.size());
		// select a random index to discard to
		int d[] = savedState.getPlayerState(this.playerNum).getTopDiscards();
		int discardIndex = (int) (Math.random() * d.length);
		boolean blanks = false;
		for(int card : d) {
			if(card == -1) {
				blanks = true;
				break;
			}
		}
		if(blanks) {
			while (d[discardIndex] != -1) {
				discardIndex = (int) (Math.random() * d.length);
			}
		}

		game.sendAction(new FDiscardAction(this,handIndex,discardIndex));
	}
}
