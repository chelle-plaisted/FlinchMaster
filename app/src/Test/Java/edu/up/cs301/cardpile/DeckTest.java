package edu.up.cs301.cardpile;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Rachel on 11/18/2017.
 */
public class DeckTest {
    @Test
    public void fillDeck() throws Exception {
        Deck test = new Deck();
        // remove all Cards
        Deck before = test;
        for(int i = 0; i < test.maxCards; i++) {
            test.removeTopCard();
        }

        assertTrue(test.size() == 0);
        test.fillDeck();
        assertTrue(test.size() == test.maxCards);
        assertTrue(test.equals(before));
    }

    @Test
    public void shuffle() throws Exception {
        Deck test = new Deck();

        Deck before = new Deck();

        test.shuffle();
        assertTrue(!test.equals(before));
    }

    @Test
    public void addSet() throws Exception {

    }


}