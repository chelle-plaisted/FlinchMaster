package edu.up.cs301.flinch;

import edu.up.cs301.cardpile.DiscardPile;
import edu.up.cs301.cardpile.FlinchPile;
import edu.up.cs301.cardpile.Hand;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * This is a smart computer player that plays with strategy and discards at random
 *
 * @author Chelle Plaisted
 * @version Dec. 2017
 */
public class FSmartComputerPlayer extends FComputerPlayer {
    // INSTANCE VARIABLES
    private int goalIndex;
    public FSmartComputerPlayer(String name) {
        super(name);
        goalIndex = -1;
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
            // did the previous player flinch themselves by discarding?
            int prevPlayer = this.playerNum - 1;
            if(prevPlayer < 0) {
                prevPlayer = savedState.getNumPlayers() - 1;
            }
            if (savedState.getPlayerState(prevPlayer).isFlinchable()) {
                game.sendAction(new FFlinchAction(this, 0));
            }
            // play cards
            if(playFlinch()) {
                return;
            } else if(playHand()) {
                return;
            } else if (playDiscard()) {
                return;
            }
            discard();
        } else {
            if (Math.random() < 1) {// what percent of the time should the computer player catch flinches?
                if(savedState.getPlayerState(savedState.getWhoseTurn()).isFlinchable()) {
                    game.sendAction(new FFlinchAction(this, savedState.getWhoseTurn()));
                }
                // TEST
                /*if (savedState.getPlayerState(0).isFlinchable()) {
                    game.sendAction(new FFlinchAction(this, 0));
                } */
            }
        }

    }

    /**
     * play from the flinch pile, building to the flinch pile if necessary
     * @return
     *  whether or not a card was played
     */
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
            sleep(1500);
            // can I play the next Flinch card too?
            return true;
        }
        // I could not directly play the card-- can we build to the card?
        goalIndex = potentiallyPlayable();

        if(goalIndex != -1) {
            // see if the hand can play the next card
            if (playHand()) {
                // the hand played the next card, return true to update state
                return true;
            } // see if the discard can play the next card
            if (playDiscard()) {
                return true;
            }
        }

        goalIndex = -1;
        return false;
    }

    /**
     * play from the hand
     * @return
     */
    private boolean playHand() {
        // look at the Hand---every card
        // if I can play the card, play it -- and send index
        Hand h = me.getHand();
        for(int i = 0; i < h.size(); i++) {
            // This is for building cards, ignore if we are playing regularly from the the Hand
            if (goalIndex != -1) {
                // can the card in the hand be played on the goal index
                if (h.getCardAt(i) == savedState.getCenterPiles()[goalIndex] + 1) {
                    // this Card is a good building card-- play
                    game.sendAction(new FPlayAction(this, i, goalIndex, new Hand()));
                    sleep(1500);
                    return true;
                } // other wise continue
                else {
                    continue;
                }
            } else {
                boolean dontPlay = false;
                // but don't play the card if it will aid my opponent
                for (int scan = 0; scan < savedState.getNumPlayers(); scan++) {
                    if (scan == this.playerNum) continue;
                    if (h.getCardAt(i) == savedState.getPlayerState(scan).getTopFlinchCard() - 1 && h.getCardAt(i) != 1) {
                        dontPlay = true;
                    }
                }
                if (dontPlay) continue;

                int index = isCardPlayable(h.getCardAt(i));
                if (index != -1) {
                    // I can play the card

                    game.sendAction(new FPlayAction(this, i, index, new Hand()));
                    sleep(1500);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * play from the discard pile
     * @return
     */
    private boolean playDiscard() {
        // look at the Discard---every card
        // if I can play the card, play it -- and send index
        int[] d = me.getTopDiscards();
        for(int i = 0; i < d.length; i++) {
            // This is for building cards, ignore if we are playing regularly from the the Hand
            if (goalIndex != -1) {
                if (d[i] == savedState.getCenterPiles()[goalIndex] + 1) {
                    // this Card is a good building card-- play
                    game.sendAction(new FPlayAction(this, i, goalIndex, new DiscardPile()));
                    sleep(1500);
                    return true;
                } else {
                    continue;
                }
            } else {
                boolean dontPlay = false;
                // but don't play the card if it will aid my opponent
                for (int scan = 0; scan < savedState.getNumPlayers(); scan++) {
                    if (scan == this.playerNum) continue;
                    if (d[i] == savedState.getPlayerState(scan).getTopFlinchCard() - 1 && d[i] != 1) {
                        dontPlay = true;
                    }
                }
                if (dontPlay) continue;
                int index = isCardPlayable(d[i]);
                if (index != -1) {
                    // I can play the card
                    game.sendAction(new FPlayAction(this, i, index, new DiscardPile()));
                    sleep(2000);
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * tells whether a card could potentially be played from the center
     * @return
     */
    private int potentiallyPlayable() {
        int cardVal = me.getTopFlinchCard();
        int[] centers = savedState.getCenterPiles();
        int cardDifference = 16;
        int closest = -1;
        for(int i = 0; i < centers.length; i++) {
            if(centers[i] == -1 && cardVal != 1) {
                continue;
            }
            if (cardVal - centers[i] < cardDifference && cardVal - centers[i] >= 0) {
                cardDifference = cardVal - centers[i];
                closest = i;
            }
        }
        return closest;
    }
}
