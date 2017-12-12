package edu.up.cs301.flinch;

import java.io.Serializable;
import edu.up.cs301.game.GamePlayer;

/**
 * This is defines an action to discard a player's card from the hand to the discard pile
 *
 *
 * @author Chelle Plaisted
 * @version Dec. 2017
 */
public class FDiscardAction extends FMoveAction implements Serializable {

    // INSTANCE VARIABLES
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 309876544567890342L;

    private int indexFrom; //this index of the Card to be discarded from the hand
    private int indexTo; // the index fo where the card is going to in the discards

    /**
     * constructor
     * @param player
     *  plyaer who discarded
     * @param source
     *  the index of the card in the hand
     * @param destination
     *  the index of the card in the discard piels
     */
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

    /**
     *
     * @return
     *  the index in the hand the card came from
     */
    public int getIndexFrom() { return indexFrom; }

    /**
     *
     * @return
     * the index in the discard piles for the card to go to
     */
    public int getIndexTo() { return indexTo; }
}
