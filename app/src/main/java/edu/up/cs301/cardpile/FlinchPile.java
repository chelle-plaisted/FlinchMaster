package edu.up.cs301.cardpile;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.card.Card;

/**
 * FlinchPile
 *
 * Class that inherits from CardPile. Creates the set of cards a player can play from during a game
 * of Flinch.
 *
 * @author Weslyn Nishimura
 *
 * @version November 2018
 */
public class FlinchPile extends CardPile implements Serializable{
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 4009874333455670342L;

    /**
     * FlinchPile()
     *
     * The FlinchPile constructor. Creates a FlinchPile object using inheritance from CardPile.
     */
    public FlinchPile() {
        // this has intentionally been left empty
    }

    /**
     * FlinchPile()
     *
     * Another FlinchPile constructor. Creates a FlinchPile object.
     *
     * @param d
     */
    public FlinchPile(Deck d) {
        // initialize the cardPile ArrayList
        cardPile = new ArrayList<Card>();

        // set the maximum number of allowable cards to 10
        maxCards = 10;

        // grab the top ten cards from the deck to make one Flinch pile
        for(int i = 0; i < maxCards; i++) {
            // remove the top card from the deck and add it to the Flinch pile
            // this assumes that the deck has already been shuffled
            cardPile.add(d.removeTopCard());
        }
    }

    /**
     * FlinchPile
     *
     * The FlinchPile copy constructor. Creates a FlinchPile object that is a copy of the input.
     *
     * @param orig
     */
    public FlinchPile(FlinchPile orig) {
        // inheritance from CardPile
        super(orig);

        // set the maximum number of allowable cards to 10
        maxCards = 10;
    }

    // FlinchPile Methods

    /**
     * removeBottomCard()
     *
     * Method to remove the bottom card from the pile
     *
     * @return the removed Card object
     */
    public Card removeBottomCard() {
        // grab the index of the last card in the pile
        int lastCard = cardPile.size() - 1;

        // if the last card exists
        if((lastCard >= 0)) {
            // remove and return the bottom card
            return cardPile.remove(lastCard);
        }
        // otherwise
        else {
            // the card does not exist therefore return nothing
            return null;
        }
    }

    /**
     * addAt()
     *
     * Method to add a Card object in a particular position in the CardPile object.
     *
     * @param c
     * @param index
     */
    @Override
    public void addAt(Card c, int index) {
        // if the card pile is not full, Card object is valid, and the index is valid
        // a Card object with an invalid number should not be added
        if((c != null) && (index >= 0) && (c.getNum() != -1)) {
            // add the Card object (c) to the given position (index)
            cardPile.add(index, c);
        }
    }
}
