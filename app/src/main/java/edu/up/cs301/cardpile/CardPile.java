package edu.up.cs301.cardpile;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.card.Card;

/**
 * CardPile
 *
 * Abstract class from which all card piles inherit. A card pile is a collection of card object with
 * methods unique to the particular pile.
 *
 * @author Weslyn Nishimura
 *
 * @version November 2018
 */
public abstract class CardPile implements Serializable {
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 4973546318190030342L;

    // instance variables
    protected ArrayList<Card> cardPile; // the ArrayList to hold all card objects
    protected int maxCards; // maximum number of allowable cards in a pile

    /**
     * CardPile()
     *
     * The CardPile constructor. Creates a CardPile object with an empty ArrayList for Card objects.
     */
    public CardPile() {
        // initialize the cardPile ArrayList
        cardPile = new ArrayList<Card>();

        // set the max number of cards to 150 by default
        maxCards = 150;
    }

    /**
     * CardPile()
     *
     * The CardPile copy constructor. Creates a CardPile object that is a copy of the input.
     *
     * @param orig
     */
    public CardPile(CardPile orig) {
        // set the cardPile ArrayList
        cardPile = new ArrayList<Card>();

        // set the max number of card to 150 by default
        maxCards = 150;

        // if the CardPile is valid
        if(orig != null) {
            // grab the cardPile ArrayList from orig and copy it
            for(int i = 0; i < orig.cardPile.size(); i++) {
                this.cardPile.add(orig.cardPile.get(i));
            }
            //cardPile = orig.getCardPile();
        }
    }

    // CardPile Methods

    /**
     * getCardPile()
     *
     * Method for returning the entire cardPile ArrayList.
     *
     * @return cardPile
     */
    public ArrayList<Card> getCardPile() {
        // return the entire pile (or null if empty)
        return cardPile;
    }

    /**
     * add()
     *
     * Method to add a Card object to the bottom of the CardPile object
     *
     * @param c
     */
    public void add(Card c) {
        // if the Card object is valid and the card pile is not full
        // a Card object with an invalid number should not be added
        if((c != null) && (cardPile.size() < maxCards) && c.getNum() != -1) {
            // add the card object to the bottom of the cardPile ArrayList
            cardPile.add(c);
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
    public void addAt(Card c, int index) {
        // if the card pile is not full, Card object is valid, and the index is valid
        // a Card object with an invalid number should not be added
        if((c != null) && (index >= 0) && (cardPile.size() < maxCards && c.getNum() != -1)) {
            // add the Card object (c) to the given position (index)
            cardPile.add(index, c);
        }
    }

    /**
     * size()
     *
     * Method to count the number of Card objects in the cardPile ArrayList
     *
     * @return the number of Card Objects
     */
    public int size() {
        // use ArrayList method size() to return the number of Card Objects in cardPile
        return cardPile.size();
    }

    /**
     * removeTopCard()
     *
     * Method to get rid of the top Card object (position zero) of the cardPile ArrayList
     *
     * @return the removed Card object
     *         null if no Card object was removed
     */
    public Card removeTopCard() {
        // if the top card exists
        //if(cardPile.get(0) != null) {
        if(size() >0) {
            // position zero represents the top card of the pile, therefore remove it
            return cardPile.remove(0);
        }
        // if the top card does not exist
        else {
            // return nothing since nothing was removed
            return null;
        }
    }

    /**
     * removeCardAt()
     *
     * Method to get rid of a particular Card object of the cardPile ArrayList
     *
     * @param index
     * @return the removed Card object
     *         null if no Card object was removed
     */
    public Card removeCardAt(int index) {
        // if the index number is valid and not above the number of cards in the pile
        if((index >= 0) && (index < size())) {
            // remove the Card object at the given index
            return cardPile.remove(index);
        }
        // otherwise
        else {
            // return nothing since nothing was removed
            return null;
        }
    }

    /**
     * getTopCard()
     *
     * Method to get the value of the top Card object in the cardPile ArrayList
     *
     * @return the value of the top Card object in the pile
     *         -1 if the top Card does not exist
     */
    public int getTopCard() {
        // if the top card exists
        if(cardPile == null || cardPile.size() == 0) {
            return -1;
        }
        if(cardPile.get(0) != null) {
            // return the value of the card in position zero
            return cardPile.get(0).getNum();
        }
        // if the top card does not exist
        else {
            // return a negative number to indicate that the card does not exist
            return -1;
        }
    }


    // TESTING HOOKS BELOW HERE

    /**
     * testerGetMax()
     *
     * Method to be used as a testing hook
     *
     * @return the maximum allowable number of cards
     */
    public int testerGetMax() {
        return maxCards;
    }
}
