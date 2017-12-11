package edu.up.cs301.flinch;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import edu.up.cs301.card.Card;
import edu.up.cs301.flinch.FStateElements.FPlayerState;
import edu.up.cs301.flinch.FStateElements.FState;
import edu.up.cs301.cardpile.*;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by rae-o on 11/5/2017.
 */

public class FDumbComputerPlayer extends FComputerPlayer{

    public FDumbComputerPlayer(String name) {
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
            if(playCards()) {
                return;
            }
            sleep(1500);
            super.discard();
        }

    }

    /**
     * Method to generate plays by playing as many cards as possible
     * Will rest 3 seconds after playing a card
     */
    private boolean playCards() {

        // look randommly at Flinch, Hand or Discards
        int[] toLook = {0,1,2};
        Collections.shuffle(Arrays.asList(toLook));
        for(int idx = 0; idx < toLook.length; idx++) {
            if(toLook[idx] == LOOK_FLINCH) {
                // look at the Flinch Pile
                // if I can play the card, play it -- play from index 0
                int index = isCardPlayable(me.getTopFlinchCard());
                if(index != -1) {
                    // I can play the card

                    game.sendAction(new FPlayAction(this, 0, index, new FlinchPile()));
                    sleep(1500);
                    return true;
                }
            } else if (toLook[idx] == LOOK_HAND) {
                // look at the Hand---every card
                // if I can play the card, play it -- and send index
                Hand h = me.getHand();
                for(int i = 0; i < h.size(); i++) {
                    int index = isCardPlayable(h.getCardAt(i));
                    if(index != -1) {
                        // I can play the card
                        game.sendAction(new FPlayAction(this, i, index, new Hand()));
                        sleep(1500);
                        return true;
                    }
                }
            } else {
                // look at the Discard---every card
                // if I can play the card, play it -- and send index
                int[] d = me.getTopDiscards();
                for(int i = 0; i < d.length; i++) {
                    int index = isCardPlayable(d[i]);
                    if(index != -1) {
                        // I can play the card
                        game.sendAction(new FPlayAction(this, i, index, new DiscardPile()));
                        sleep(1500);
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
