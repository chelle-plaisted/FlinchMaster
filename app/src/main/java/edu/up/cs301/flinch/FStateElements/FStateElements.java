package edu.up.cs301.flinch.FStateElements;

import java.util.ArrayList;

import edu.up.cs301.card.*;

/**
 * Created by rae-o on 10/18/2017.
 */

public class FPlayerState {
    Hand hand;
    FlinchPile flinch;
    DiscardPile[] discards;
    boolean hasFlinched;

    public FPlayerState(FPlayerState orig) {
        hand = orig.hand;
        flinch = orig.flinch;
        discards = orig.discards;
        hasFlinched = orig.hasFlinched;
    }


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
    public Card getTopFlinchCard() {
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
     */
    public ArrayList<Card> getTopDiscards() {
        ArrayList<Card> tops = new ArrayList<Card>();

        for(DiscardPile d : discards) {
            if(d.getTopCard() != null)
                tops.add(d.getTopCard());
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
}

