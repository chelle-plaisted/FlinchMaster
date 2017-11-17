package edu.up.cs301.flinch.FStateElements;

import edu.up.cs301.cardpile.*;
import edu.up.cs301.card.*;

/**
 * Created by rae-o on 10/18/2017.
 */

public class FPlayerState {
    Hand hand;
    FlinchPile flinch;
    DiscardPile[] discards;
    boolean hasFlinched;

    public FPlayerState() {
    }

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

