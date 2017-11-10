package edu.up.cs301.flinch;

import java.util.Arrays;
import java.util.Collections;

import edu.up.cs301.cardpile.DiscardPile;
import edu.up.cs301.cardpile.FlinchPile;
import edu.up.cs301.cardpile.Hand;
import edu.up.cs301.flinch.FStateElements.FPlayerState;
import edu.up.cs301.flinch.FStateElements.FState;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by rae-o on 11/5/2017.
 */

public class FSmartComputerPlayer extends FComputerPlayer {
    public FSmartComputerPlayer(String name) {
        super(name);
    }

    /**
     * Invoked whenever the player's timer has ticked. It is expected
     * that this will be overridden in smart players.
     */
    @Override
    protected void timerTicked() {
        super.timerTicked();
    }

    /**
     * callback method, called when we receive a message, typicallly from
     * the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        super.receiveInfo(info);

        if(this.playerNum == savedState.getWhoseTurn()) {
            playCards();
            discard();
        }

    }

    private void playCards() {
        boolean cardPlayed;
        do{
           cardPlayed = false;
            if(playFlinch() || playHand() || playDiscard())
                cardPlayed = true;
        }while(cardPlayed);
    }

    private boolean playFlinch() {
        // look at the flinch pile
        // if there is a card in the center higher than the flinch pile, hunt to see if we can build to it
        // if I have no building cards or can play the flinch card--then I'm done hunting
        // look at the Flinch, then hand, then discards
        // if there is a non Flinch pile card I can play, add it to the list of toBePlayed cards and call the method recursively with a one lower card goal

        int index = isCardPlayable(me.getTopFlinchCard());
        if(index != -1) {
            // I can play the card
            game.sendAction(new FPlayAction(this, 0, index, new FlinchPile()));
            return true;
        }
        // I could not directly play the card-- can we build to the card?
        index = potentiallyPlayable();

        if(index != -1) {
            int targetVal =savedState.getCenterPiles()[index];
            // look at hand -- can I play the next card
            playHand();
        }


        return false;
    }

    private boolean playHand() {
        // look at the Hand---every card
        // if I can play the card, play it -- and send index
        boolean played = false;
        Hand h = me.getHand();
        for(int i = 0; i < h.size(); i++) {
            int index = isCardPlayable(h.getCardAt(i));
            if(index != -1) {
                // I can play the card
                game.sendAction(new FPlayAction(this, index, i, new Hand()));
                played = true;
            }
        }
        return played;
    }

    private boolean playDiscard() {
        // look at the Discard---every card
        // if I can play the card, play it -- and send index
        boolean played = false;
        int[] d = me.getTopDiscards();
        for(int i = 0; i < d.length; i++) {
            int index = isCardPlayable(d[i]);
            if(index != -1) {
                // I can play the card
                game.sendAction(new FPlayAction(this, index, i, new DiscardPile()));
                played = true;
            }
        }
        return played;
    }

    private int potentiallyPlayable() {
        int cardVal = me.getTopFlinchCard();
        int[] centers = savedState.getCenterPiles();
        int cardDifference = 16;
        int closest = -1;
        for(int i = 0; i < centers.length; i++) {
            if (cardVal - centers[i] < cardDifference && cardVal - centers[i] >= 0) {
                cardDifference = cardVal - centers[i];
                closest = i;
            }
        }
        return closest;
    }

}
