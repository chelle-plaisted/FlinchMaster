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
 * @author Alexa Ruiz
 * @author Chelle Plaisted
 * @author Rhianna Pinkerton
 * @author Weslyn Nishimura
 *
 * @version November 2018
 */
public class Deck extends CardPile implements Serializable {
    /**
     * Deck()
     *
     * The Deck constructor. Creates a Deck object using inheritance from CardPile.
     * Fills the deck object.
     */
    public Deck() {
        super();
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
        Collections.shuffle(cardPile);

        /* DO NOT DELETE

        Random rand = new Random();

        // if the deck is not empty
        if(cardPile.size() != 0) {
            // shuffle the deck

            // variable to store the random number
            int random;

            // variable to store temporary number
            int temp = 0;

            // hashSet to store numbers assigned
            HashSet<Integer> randHash = new HashSet<Integer>();

            // go through all Card objects
            while(temp < cardPile.size()) {
                // generate a random number between 0 and the total number of Card objects
                random = rand.nextInt(cardPile.size() - 1);

                // if the randomly generated number has NOT already been used
                if(!randHash.contains(random)) {
                    // swap the current Card object with the Card object in the new location
                    Card tempCard = cardPile.get(random);
                    cardPile.set(random, cardPile.get(temp));
                    cardPile.set(temp, tempCard);
                }

                // increment the count to move to the next Card object
                temp++;
            }
        }
        */

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
