package edu.up.cs301.cardpile;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.up.cs301.card.Card;

import static org.junit.Assert.*;

/**
 * Created by Rachel on 11/17/2017.
 */
public class CenterPileTest {
    @Test
    public void isEmpty() throws Exception {

    }

    @Test
    public void empty() throws Exception {

    }

    @Test
    public void isFull() throws Exception {

    }

    @Test
    public void getCardPile() throws Exception {
        // empty centerPile
        CenterPile test = new CenterPile();
        ArrayList<Card> cards = test.getCardPile();
        assertTrue(cards.size() == 0);
    }

    @Test
    public void add() throws Exception {
        CenterPile test = new CenterPile();
        // add a single card
        test.add(new Card(1));
        ArrayList<Card> cards = test.getCardPile();
        assertTrue(cards.size() == 1);
        assertTrue(cards.get(0).getNum() == 1);

        // try adding over the max
        for(int i = 0; i < 20; i++) {
            test.add(new Card(2));
        }
        cards = test.getCardPile();
        // have we exceeded max size?
        assertTrue(cards.size() == test.testerGetMax());

        // are they all the correct value? --i.e. the top card is the one and the rest are twos
        for(int i = 0; i < cards.size(); i++) {
            if(i == 0) {
                assertTrue(cards.get(i).getNum() == 1);
            } else {
                assertTrue(cards.get(i).getNum() == 2);
            }
        }
    }

    @Test
    public void addAt() throws Exception {
        CenterPile test = new CenterPile();
        // add a series of ones
        for(int i = 0; i < 5; i++) {
            test.add(new Card(1));
        }
        // add a 2 at the top of the pile
        test.addAt(new Card(2), 0);
        ArrayList<Card> cards = test.getCardPile();
        // check
        assertTrue(cards.get(0).getNum() == 2);

        // add at a later index
        test.addAt(new Card(5), 4);
        cards = test.getCardPile();
        assertTrue(cards.get(4).getNum() == 5);

        // try adding at illegal index
        test.addAt(new Card(6), cards.size());
        // ensure nothing changes
        ArrayList<Card> illegal = test.getCardPile();
        assertTrue(cards.equals(illegal));
    }

    @Test
    public void size() throws Exception {
        CenterPile test = new CenterPile();
        // there are no cards
        assertTrue(test.size() == test.getCardPile().size());
        // add 5 cards
        for(int i = 0; i < 5; i++) {
            test.add(new Card (1));
        }
        assertTrue(test.size() == test.getCardPile().size());
        // add cards over the max
        for(int i = 0; i < test.testerGetMax(); i++) {
            test.add(new Card (1));
        }
        assertTrue(test.size() == test.getCardPile().size());
    }

    @Test
    public void removeTopCard() throws Exception {
        CenterPile test = new CenterPile();
        // there are no cards
        ArrayList<Card> before = test.getCardPile();
        test.removeTopCard();
        ArrayList<Card> after = test.getCardPile();
        assertTrue(before.equals(after));

        // there is one card
        test.add(new Card(1));
        test.removeTopCard();
        assertTrue(test.size() == 0);

        // add three cards
        test.add(new Card(1));
        test.add(new Card(2));
        test.add(new Card(3));
        Card c = test.removeTopCard();
        before = test.getCardPile();
        assertTrue(test.size() == 2);
        assertTrue(before.get(0).getNum() == 2);
        // we removed the correct Card
        assertTrue(c.getNum() == 1);
    }

    @Test
    public void removeCardAt() throws Exception {
        CenterPile test = new CenterPile();
        // there are no cards
        ArrayList<Card> before = test.getCardPile();
        test.removeCardAt(4);
        ArrayList<Card> after = test.getCardPile();
        assertTrue(before.equals(after));

        // there is one card
        test.add(new Card(1));
        test.removeCardAt(0);
        assertTrue(test.size() == 0);

        // add three cards
        test.add(new Card(1));
        test.add(new Card(2));
        test.add(new Card(1));
        Card removed = test.removeCardAt(1);
        before = test.getCardPile();
        assertTrue(test.size() == 2);
        assertTrue(!before.contains(new Card(2)));
        assertTrue(removed.getNum() == 2);
        for(Card c : before) {
            assertTrue(c.getNum() == 1);
        }
        // try a negative index
        test.removeCardAt(-1);
        after = test.getCardPile();
        assertTrue(before.equals(after));
    }

    @Test
    public void getTopCard() throws Exception {
        CenterPile test = new CenterPile();
        // there are no cards
        int c = test.getTopCard();
        assertTrue(c == -1);

        // add a card
        test.add(new Card(8));
        assertTrue(test.getTopCard() == 8);

        // add a card in front
        test.addAt(new Card(10), 0);
        assertTrue(test.getTopCard() == 10);
        test.add(new Card(4));
        assertTrue(test.getTopCard() == 10);

        // remove top card
        test.removeTopCard();
        assertTrue(test.getTopCard() == 8);
    }

}