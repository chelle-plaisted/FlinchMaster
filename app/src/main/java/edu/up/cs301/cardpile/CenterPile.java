package edu.up.cs301.cardpile;

import java.io.Serializable;

/**
 * CenterPile
 *
 * Class that inherits from CardPile. Creates piles of cards for the center of the board in the
 * Flinch game.
 *
 * @author Alexa Ruiz
 * @author Chelle Plaisted
 * @author Rhianna Pinkerton
 * @author Weslyn Nishimura
 *
 * @version November 2018
 */
public class CenterPile extends CardPile implements Serializable {

    // to satisfy the Serializable interface
    private static final long serialVersionUID = 6789876567896546765L;

    /**
     * CenterPile()
     *
     * The CenterPile constructor. Creates a CenterPile object using inheritance from CardPile.
     */
    public CenterPile() {
        super();

        // set the maximum number of allowable cards to 15
        maxCards = 15;
    }

    /**
     * CenterPile
     *
     * The CenterPile copy constructor. Creates a CenterPile object that is a copy of the input.
     *
     * @param orig
     */
    public CenterPile(CenterPile orig) {
        super(orig);

        // set the maximum number of allowable cards to 15
        maxCards = 15;
    }

    // CenterPile Methods

    /**
     * isEmpty()
     *
     * Method to determine if the CenterPile object is empty
     *
     * @return a boolean telling whether or not the card pile is empty
     */
    public boolean isEmpty() {
        // if the cardPile ArrayList has no card objects
        if(!cardPile.isEmpty()) {
            // the cardPile ArrayList is empty
            return true;
        }
        // otherwise
        else {
            // the cardPile ArrayList is not empty
            return false;
        }
    }

    /**
     * empty()
     *
     * Method to remove all card objects in the cardPile ArrayList
     */
    public void empty() {
        // continuously iterate through the cardPile ArrayList if it is not empty
        while(!cardPile.isEmpty()) {
            // remove the top element from the ArrayList
            cardPile.remove(0);
        }
    }

    /**
     * isFull()
     *
     * Method to determine if the cardPile ArrayList is full or not
     *
     * @return a boolean telling whether or not the card pile is empty
     */
    public boolean isFull() {
        // if the number of cards in the pile is 15
        if(cardPile.size() == maxCards) {
            // it is full
            return true;
        }
        // otherwise
        else {
            // it is not full
            return false;
        }
    }
}
