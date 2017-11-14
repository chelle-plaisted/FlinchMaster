package edu.up.cs301.cardpile;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

/**
 * CardPile
 *
 * Abstract class from which all card piles inherit. A card pile is a collection of card object with
 * methods unique to the particular pile.
 */
public abstract class CardPile {
    // instance variables
    protected ArrayList<Card> cardPile;

    /**
     * CardPile()
     *
     * The CardPile constructor. Creates a CardPile object with an empty ArrayList for Card objects.
     */
    public CardPile() {
        // initialize the ArrayList
        cardPile = new ArrayList<Card>();
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

        // grab the cardPile ArrayList from orig and copy it
        cardPile = orig.getCardPile();
    }

    // CardPile Methods

    /**
     * ************************************************************NEW METHOD NOT IN DESIGN DOCUMENT
     *
     * getCardPile()
     *
     * Method for returning the entire cardPile ArrayList.
     *
     * @return cardPile
     */
    public ArrayList<Card> getCardPile() {
        // return the entire pile
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
        // add the card object to the bottom of the cardPile ArrayList
        cardPile.add(c);
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
        // add the Card object (c) to the given position (index)
        cardPile.add(index, c);
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
     */
    public Card removeTopCard() {
        // position zero represents the top card of the pile, therefore remove it
        return cardPile.remove(0);
    }

    /**
     * removeCardAt()
     *
     * Method to get rid of a particular Card object of the cardPile ArrayList
     *
     * @param index
     * @return the removed Card object
     */
    public Card removeCardAt(int index) {
        // remove the Card object at the given index
        return cardPile.remove(index);
    }

    /**
     * getTopCard()
     *
     * Method to get the value of the top Card object in the cardPile ArrayLisst
     *
     * @return the value of the top Card object in the pile
     */
    public int getTopCard() {
        // return the value of the card in position zero
        return cardPile.get(0).getNum();
    }
}
