package edu.up.cs301.flinch;

import java.io.Serializable;

import edu.up.cs301.cardpile.CardPile;
import edu.up.cs301.game.GamePlayer;

/**
 * This defines an action to play a card in a flinch game
 *
 *
 * @author Chelle Plaisted
 * @version Dec. 2017
 */

public class FPlayAction extends FMoveAction implements Serializable {
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 954293110990030342L;

    /* INSTANCE VARIABLES */
    private int indexFrom; // the index of the Card played in its CardPile
    private int indexTo; // The index of where the card is going to in the center piles
    private CardPile cp; //The CardPile from which the Card was played

    public FPlayAction(GamePlayer p, int source, int destination, CardPile type) {
        super(p);
        indexFrom = source;
        indexTo = destination;
        cp = type;
    }

    /**
     * This is a play action
     * @return
     *  states that this is a play type action
     */
    @Override
    public boolean isPlay() {
        return true;
    }


    public int getIndexFrom() { return indexFrom; }

    public CardPile getCardPile() { return cp; }

    public int getIndexTo() { return indexTo; }
}
