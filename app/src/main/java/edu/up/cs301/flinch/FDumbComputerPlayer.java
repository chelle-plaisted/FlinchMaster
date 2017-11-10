package edu.up.cs301.flinch;

import edu.up.cs301.card.Card;
import edu.up.cs301.flinch.FStateElements.FPlayerState;
import edu.up.cs301.flinch.FStateElements.FState;
import edu.up.cs301.cardpile.*;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by rae-o on 11/5/2017.
 */

public class FDumbComputerPlayer extends FComputerPlayer{
    // constants
    public static final int LOOK_FLINCH = 0;
    public static final int LOOK_HAND = 1;
    public static final int LOOK_DISCARD = 2;
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
            playCards();
        }

    }

    private void playCards() {
        // look randommly at Flinch, Hand or Discards
        FPlayerState me = savedState.getPlayerState(this.playerNum);
        int toLook = (int) (Math.random() * 3);

        if(toLook == LOOK_FLINCH) {
            // look at the Flinch Pile
            // if I can play the card, play it -- play from index 0
            int index = isCardPlayable(me.getTopFlinchCard());
            if(index != -1) {
                game.sendAction(new FPlayAction(this, 0, index, new FlinchPile()));
            }
        } else if (toLook == LOOK_HAND) {
            // pick a random hand index
            Hand h = me.getHand();
            for(int i = 0; i < h.size(); i++) {
                int index = isCardPlayable(h.getCardAt(i));
                if(index != -1) {
                    game.sendAction(new FPlayAction(this, index, i, new Hand()));
                }
            }
        } else {
            int[] d = me.getTopDiscards();
            for(int i = 0; i < d.length; i++) {
                int index = isCardPlayable(d[i]);
                if(index != -1) {
                    game.sendAction(new FPlayAction(this, index, i, new DiscardPile()));
                }
            }
        }// end play cards
        /*
        Next steps: wrap this in a while with a flag to see if anything has been played
        -have the toLook increment and wrap so you go through everything
        -think of a better way to get the order of what to look at--> scramble everything in a list?
        */

    }
}
