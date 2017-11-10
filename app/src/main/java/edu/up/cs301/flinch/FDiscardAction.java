package edu.up.cs301.flinch;

import edu.up.cs301.cardpile.CardPile;
import edu.up.cs301.game.GamePlayer;

/**
 * Created by Rachel on 11/7/2017.
 */

public class FDiscardAction extends FMoveAction {

    private int indexFrom; //this index of the Card to be discarded from the hand
    private int indexTo; // the index fo where the card is going to in the discards
    public FDiscardAction(GamePlayer player, int source, int destination) {

        super(player);
        indexFrom = source;
        indexTo = destination;
    }

    /**
     * This is a discard action
     * @return
     *  states that this is a discard type action
     */
    @Override
    public boolean isDiscard() {
        return true;
    }


    public int getIndexFrom() { return indexTo; }

    public int getIndexTo() { return indexTo; }
}
