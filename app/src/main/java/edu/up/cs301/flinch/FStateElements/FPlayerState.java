package edu.up.cs301.flinch.FStateElements;

import java.io.Serializable;

import edu.up.cs301.cardpile.*;

/**
 * Contains the state of an individual Flinch player. Used by FState.Slapjack game.
 *
 * @author Chelle Plaisted
 * @version Nov. 2017
 */
public class FPlayerState implements Serializable {
    // INSTANCE VARIABLES: all package-access only
    Hand hand; // the cards of the player's hand
    public FlinchPile flinch; // flinch pile
    DiscardPile[] discards; // 5 discard piles
    boolean hasFlinched; // whether the player has flinched themselves
    boolean playedThisTurn; // whether the player has played this turn

    /**
     * default constructor
     */
    public FPlayerState() {
    }

    /**
     * copy constructor
     * @param orig
     *  FPlayerState object to copy
     */
    public FPlayerState(FPlayerState orig) {
        hand = new Hand(orig.hand);
        flinch = new FlinchPile(orig.flinch);
        discards = new DiscardPile[orig.discards.length];
        for (int i= 0; i < discards.length; i++) {
            discards[i] = new DiscardPile(orig.discards[i]);
        }
        hasFlinched = orig.hasFlinched;
        playedThisTurn = orig.playedThisTurn;
    }

    // METHODS

    /**
     * whether the player has played this turn
     * @return
     */
    public boolean hasPlayedThisTurn() {return playedThisTurn; }

    /**
     * @return
     *  the Hand of the player
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * @return
     *  the top card of the FlinchPile
     */
    public int getTopFlinchCard() {
        return flinch.getTopCard();
    }

    /**
     * @return
     * returns whether there are any remaining cards in the FlinchPile
     */
    public boolean isFlinchEmpty() {
        return flinch.size() == 0;
    }

    /**
     * @return
     * returns the top card of each of the player's discard piles
     * If the space is empty, a -1 fills the slot
     */
    public int[] getTopDiscards() {
        int[] tops = new int[5];
        int count = 0;
        for(DiscardPile d : discards) {
            //get discard pile cards--including any that are null
            tops[count] = d.getTopCard();
            count++;
        }
        return tops;
    }

    /**
     * @return
     *  whether the player has flinched
     */
    public boolean isFlinchable() {
        return hasFlinched;
    }

    @Override
    public boolean equals(Object o) {
        FPlayerState compare = (FPlayerState) o;
        // initialize the number of the players in the game
        if(this.hand != compare.hand) {
            return false;
        } else if(this.discards != compare.discards || this.flinch != compare.flinch || this.hasFlinched != compare.hasFlinched) {
            return false;
        }
        return true;
    }
}

