package edu.up.cs301.cardpile;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

/**
 * FlinchPile
 *
 * Class that inherits from CardPile. Creates the set of cards a player can play from during a game
 * of Flinch.
 */
public class FlinchPile extends CardPile {
    /**
     * FlinchPile
     *
     * The FlinchPile constructor. Creates a FlinchPile object using inheritance from CardPile.
     */
    public FlinchPile() {
        // DO NOT CHANGE THIS
    }
    public FlinchPile(Deck d) {
        // initialize the cardPile ArrayList
        cardPile = new ArrayList<Card>();

        // set the maximum number of allowable cards to 10
        maxCards = 10;

        // TODO: IMPLEMENT
        /* DO NOT DELETE
        for(int i = 0; i < maxCards; i++) {

        }
        */
    }

    /**
     * FlinchPile
     *
     * The FlinchPile copy constructor. Creates a FlinchPile object that is a copy of the input.
     *
     * @param orig
     */
    public FlinchPile(FlinchPile orig) {
        // TODO: IMPLEMENT
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
        // TODO: IMPLEMENT
        /* DO NOT DELETE
        // grab the index of the last card in the pile
        int lastCard = cardPile.size() - 1;

        // if the
        if((lastCard > 0) && (lastCard < maxCards)) {
            // remove and return the bottom card
            return cardPile.remove(lastCard);
        }
        // otherwise
        else {
            // the card does not exist therefore return nothing
            return null;
        }
        */
        return null; // dummy return
    }
}
