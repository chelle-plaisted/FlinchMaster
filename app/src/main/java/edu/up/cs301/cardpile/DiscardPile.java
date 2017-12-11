package edu.up.cs301.cardpile;

import java.io.Serializable;

/**
 * DiscardPile
 *
 * Class that inherits from CardPile. Creates the set of cards a player can discard to at the end of
 * their turn during a game of Flinch.
 *
 * @author Weslyn Nishimura
 *
 * @version November 2018
 */
public class DiscardPile extends CardPile implements Serializable{

    // to satisfy the Serializable interface
    private static final long serialVersionUID = 497556631125888742L;

    /**
     * DiscardPile
     *
     * The DiscardPile constructor. Creates a DiscardPile object using inheritance from CardPile.
     */
    public DiscardPile() {
        // inheritance from CardPile
        super();
    }

    /**
     * DiscardPile
     *
     * The DiscardPile copy constructor. Create a DiscardPile object that is a copy of the input.
     *
     * @param orig
     */
    public DiscardPile(DiscardPile orig) {
        // inheritance from CardPile
        super(orig);
    }
}
