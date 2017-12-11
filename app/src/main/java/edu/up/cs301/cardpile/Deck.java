package edu.up.cs301.cardpile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import edu.up.cs301.card.Card;

/**
 * Deck
 *
 * Class that inherits from CardPile. Creates the set of Card objects to be used throughout the game
 * of Flinch.
 *
 * @author Weslyn Nishimura
 *
 * @version November 2018
 */
public class Deck extends CardPile implements Serializable {
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 490318190009998888L;

    /**
     * Deck()
     *
     * The Deck constructor. Creates a Deck object using inheritance from CardPile.
     * Fills the deck object.
     */
    public Deck() {
        // inheritance from CardPile
        super();

        // By default, when a new deck is made, fill it
        fillDeck();
    }

    /**
     * Deck()
     *
     * The Deck copy constructor. Creates a Deck object that is a copy of the input.
     *
     * @param orig
     */
    public Deck(Deck orig) {
        // inheritance from CardPile
        super(orig);
    }

    // Deck Methods

    /**
     * fillDeck()
     *
     * Method to completely fill the deck with Card objects.
     *
     * @return the Deck object full of cards
     */
    public void fillDeck() {
        // add ten sets of Card objects
        for(int i = 0; i < 10; i++) {
            // add a set of Card objects numbered 1 through 15
            for (int j = 1; j <= 15; j++) {
                // create the new Card object
                Card c = new Card(j);

                // add the new Card object to the pile
                add(c);
            }
        }
    }

    /**
     * shuffle()
     *
     * Method to randomly sort the Card objects in the deck
     */
    public void shuffle() {
        // Randomly sort the card objects in the ArrayList
        Collections.shuffle(cardPile);
    }

    /**
     * addSet()
     *
     * Method to add a set of Card objects to the deck (numbered one through fifteen)
     */
    public void addSet() {
        // add a set of Card objects numbered 1 through 15
        for (int j = 1; j <= 15; j++) {
            // create the new Card object
            Card c = new Card(j);

            // add the new Card object to the pile
            cardPile.add(c);
        }
        shuffle();
    }
}
