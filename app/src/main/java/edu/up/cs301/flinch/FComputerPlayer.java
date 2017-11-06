package edu.up.cs301.flinch;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.flinch.FStateElements.FPlayerState;
import edu.up.cs301.flinch.FStateElements.FState;
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

    /**
     * Constructor for the FComputerPlayer class; creates an "average"
     * player.
     *
     * @param name
     * 		the player's name
     */
    public FComputerPlayer(String name) {
        // invoke general constructor to create player whose average reaction
    	// time is half a second.
        this(name, 0.5);
    }

    /*
     * Constructor for the FComputerPlayer class
     */
    public FComputerPlayer(String name, double avgReactionTime) {
        // invoke superclass constructor
        super(name);

        // set the minimim reaction time, which is half the average reaction
        // time, converted to milliseconds (0.5 * 1000 = 500)
        minReactionTimeInMillis = 500*avgReactionTime;
    }

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

    	// if we don't have a game-state, ignore
    	if (!(info instanceof FState)) {
    		return;
    	}
    	
    	// update our state variable
    	savedState = (FState)info;

		// if it is our turn, we will play cards
		// otherwise, monitor for Flinches if able to


		/*
		// discard to end turn--assume I am the current player
		FPlayerState me = savedState.getPlayerState(savedState.getWhoseTurn());
		int toDiscard = (int) (Math.random() * me.getHand().size());
		int destination = (int) (Math.random() * 5);
		game.sendAction(new DiscardAction(toDiscard, destination));
		*/
	}

	/**
	 * tells whether or not a card is playable to the center piles
	 * @param c
	 * 	the card to check
	 * @return
	 * 	the index of a pile to play on (-1 if none available)
	 */
	public int isCardPlayable(Card c) {
		// a card is playable if it is a one OR the next lowest denomination is in the center pile
		Card[] center = savedState.getCenterPiles();
		for(int i = 0; i < center.length; i++) {
			if(center[i] == null && c.getNum() == 1) {
				return i;
			} else if (center[i].getNum() == c.getNum() - 1) {
				return i;
			}
		}
		return -1;
	}
}
