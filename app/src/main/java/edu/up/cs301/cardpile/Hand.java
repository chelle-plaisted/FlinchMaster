package edu.up.cs301.cardpile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import edu.up.cs301.card.Card;

/**
 * Hand
 *
 * Class that inherits from CardPile. Creates the set of cards a player can play from during a game
 * of Flinch.
 *
 * @author Weslyn Nishimura
 *
 * @version November 2018
 */
public class Hand extends CardPile implements Serializable{
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 4973540099988776655L;

    /**
     * Hand()
     *
     * The Hand constructor. Creates a Hand object using inheritance from CardPile.
     */
    public Hand() {
        // this has intentionally been left empty
    }

    /**
     * Hand()
     *
     * The hand constructor. Creates a Hand object using inheritance from CardPile.
     *
     * @param d
     */
    public Hand(Deck d) {
        // inheritance from CardPile
        super();

        // set the maximum number of allowable cards to 5
        maxCards = 5;

        // grab the top ten cards from the deck to make one Flinch pile
        for(int i = 0; i < maxCards; i++) {
            // remove the top card from the deck and add it to the Flinch pile
            // this assumes that the deck has already been shuffled
            cardPile.add(d.removeTopCard());
        }

        // by default, automatically sort the hand when it is made
        arrangeHand();
    }

    /**
     * Hand()
     *
     * The Hand copy constructor. Creates a Hand object that is a copy of the input.
     *
     * @param orig
     */
    public Hand(Hand orig) {
        // inheritance from CardPile
        super(orig);

        // set the maximum number of allowable cards to 5
        maxCards = 5;
    }

    // Hand Methods

    /**
     * arrangeHand()
     *
     * Method to sort the Card objects in the Hand object. Uses selection sort.
     */
    public void arrangeHand() {
        // go through each Card objects in the hand
        for(int i = 0; i < cardPile.size() - 1; i++) {
            // save the location of the current card
            int index = i;

            // go through the rest of the Card objects in the hand
            for(int j = i+1; j < cardPile.size(); j++) {
                // find the smallest value Card object
                if (cardPile.get(j).getNum() < cardPile.get(index).getNum()) {
                    // save the location of the smallest value Card object
                    index = j;
                }
            }

            // swap the current Card object with the Card object with the smallest value
            Card tempCard = cardPile.get(index);
            cardPile.set(index, cardPile.get(i));
            cardPile.set(i, tempCard);
        }

    }

    /**
     * fillHand()
     *
     * Method to refill the cards in the Hand.
     *
     * @param d the deck
     */
    public void fillHand(Deck d) {
        // if the hand is not full
        while(cardPile.size() != maxCards) {
            // add a card to the hand from the top of the deck
            cardPile.add(d.removeTopCard());
        }
    }

    /**
     * getCardAt()
     *
     * Method to get the number of the card at the given index
     *
     * @param index location of the Card object
     * @return the value of the Card object at the given location
     */
    public int getCardAt(int index) {
        // get the value of the the Card object at the given index
        if(index < 0 || index >= size()) {
            return -1;
        }
        // if card is null
        if(cardPile.get(index) == null) {
            return -1;
        }
        return cardPile.get(index).getNum();
    }
}
