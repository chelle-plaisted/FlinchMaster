package edu.up.cs301.cardpile;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

/**
 * Created by Weslyn on 11/6/2017.
 */

public abstract class CardPile {
    // instance variables
    protected ArrayList<Card> cardPile = new ArrayList<Card>();

    /**
     * CardPile constructor
     */
    public CardPile() {
        // TODO: IMPLEMENT
    }

    /**
     * CardPile copy constructor
     *
     * @param orig
     */
    public CardPile(CardPile orig) {
        // TODO: IMPLEMENT
    }

    // CardPile Methods

    /**
     * add()
     *
     * @param c
     */
    public void add(Card c) {
        // TODO: IMPLEMENT
    }

    /**
     * addAt()
     *
     * @param c
     * @param index
     */
    public void addAt(Card c, int index) {
        // TODO: IMPLEMENT
    }

    /**
     * size()
     *
     * @return
     */
    public int size() {
        // TODO: IMPLEMENT
        return 0;
    }

    /**
     * removeTopCard()
     *
     * @return
     */
    public Card removeTopCard() {
        // TODO: IMPLEMENT
        return null;
    }

    /**
     * removeCardAt()
     *
     * @param index
     * @return
     */
    public Card removeCardAt(int index) {
        // TODO: IMPLEMENT
        return null;
    }

    /**
     * getTopCard()
     *
     * @return
     */
    public int getTopCard() {
        // TODO: IMPLEMENT
        return 0;
    }
}
