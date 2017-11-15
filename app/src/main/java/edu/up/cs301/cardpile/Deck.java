package edu.up.cs301.cardpile;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

/**
 * Deck
 *
 * Class that inherits from CardPile. Creates the set of Card objects to be used throughout the game
 * of Flinch.
 *
 * @author Alexa Ruiz
 * @author Chelle Plaisted
 * @author Rhianna Pinkerton
 * @author Weslyn Nishimura
 *
 * @version November 2018
 */
public class Deck extends CardPile {
    /**
     * Deck()
     *
     * The Deck constructor. Creates a Deck object using inheritance from CardPile.
     */
    public Deck() {
        super();
    }

    /**
     * Deck()
     *
     * The Deck copy constructor. Creates a Deck object that is a copy of the input.
     *
     * @param orig
     */
    public Deck(Deck orig) {
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
    public Deck fillDeck() {
        // TODO: IMPLEMENT
        /* DO NOT DELETE
        // add ten sets of Card objects
        for(int i = 0; i < 10; i++) {
            // add a set of Card objects numbered 1 through 15
            for (int j = 1; j <= 15; j++) {
                // create the new Card object
                Card c = new Card(j);

                // add the new Card object to the pile
                cardPile.add(c);
            }
        }
        */

        return null; // dummy return
    }

    /**
     * shuffle()
     *
     * @return
     */
    public Deck shuffle() {
        // TODO: IMPLEMENT
        /* DO NOT DELETE
        // create a new Deck object
        Deck shuffledDeck = new Deck();

        // go through each element in a copy of the cardPile ArrayList
        for(Card temp : new ArrayList<Card>(cardPile)) {

        }
        */

        return null;
    }

    /**
     * addSet()
     */
    public void addSet() {
        // TODO: IMPLEMENT
    }
}
