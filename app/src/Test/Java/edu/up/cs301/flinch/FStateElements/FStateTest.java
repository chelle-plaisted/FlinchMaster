package edu.up.cs301.flinch.FStateElements;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.cardpile.Deck;
import edu.up.cs301.cardpile.Hand;
import edu.up.cs301.flinch.FSmartComputerPlayer;

import static org.junit.Assert.*;

/**
 * Created by Flinchie on 11/9/2017.
 */
public class FStateTest {
    @org.junit.Test
    public void getDeck() throws Exception {
        FState test = new FState(2);
        Deck d = test.getDeck();
        // ensure the deck is created properly
        assertTrue(d != null);
        assertTrue(d.size() == d.testerGetMax() - (10 + 5) * test.numPlayers);
    }

    @org.junit.Test
    public void checkPlayerNum() throws Exception {
        FState test1 = new FState(2);
        assertTrue(test1.numPlayers == 2);
        FState test2 = new FState(4);
        assertTrue(test2.numPlayers == 4);
        //this test should pass because 2 - 4 players is an invariant
        FState test3 = new FState(5);
        assertTrue(test3.numPlayers == 5);
    }

    @org.junit.Test
    public void getCenterPiles() throws Exception {
        FState test = new FState(2);
        int[] centers = test.getCenterPiles();
        assertTrue(centers.length == 10);
        // all piles should be empty
        for(int i : centers) {
            assertTrue( i == -1);
        }
    }

    @org.junit.Test
    public void getPlayerState() throws Exception {
        FState test = new FState(2);
        assertTrue(test.getPlayerState(0) == test.players[0]);
        assertTrue(test.getPlayerState(1) == test.players[1]);
        assertTrue(test.getPlayerState(-1) == null);
        assertTrue(test.getPlayerState(4) == null);

    }

    @org.junit.Test
    public void getWhoseTurn() throws Exception {
        FState test = new FState(2);
        assertTrue(test.getWhoseTurn() == 0 || test.getWhoseTurn() == 1);
        test.setNextTurn(1);
        assertTrue(test.getWhoseTurn() == 1);
        test.setNextTurn(0);
        assertTrue(test.getWhoseTurn() == 0);
    }

    @org.junit.Test
    public void getStartOfGame() throws Exception {
        FState test = new FState(2);
        assertTrue(test.getStartOfGame() == true);
        test.notStartOfGame();
        assertTrue(!test.getStartOfGame());
    }

    @org.junit.Test
    public void setNextTurn() throws Exception {
        FState state = new FState(2);
        state.setNextTurn(0);
        assertTrue(state.getWhoseTurn() == 0);
        state.setNextTurn(1);
        assertTrue(state.getWhoseTurn() == 1);
    }

    @org.junit.Test
    public void replenishPlayerHand() throws Exception {
        FState state = new FState(2);
        // hand should start at 5 cards
        assertTrue(state.getPlayerState(0).getHand().size() == state.players[0].hand.testerGetMax());
        ArrayList<Card> before = (ArrayList<Card>) state.getPlayerState(0).getHand().getCardPile().clone();
        state.setNextTurn(0);
        // should not change the player's hand
        state.replenishPlayerHand();
        assertTrue(state.getPlayerState(0).getHand().getCardPile().equals(before));

        // play all the cards \
        while(state.getPlayerState(0).getHand().size() > 0) {
            state.getPlayerState(0).getHand().removeTopCard();
        }
        state.replenishPlayerHand();
        assertTrue(state.getPlayerState(0).getHand().size() == 5);
        assertTrue(state.getPlayerState(0).getHand().getCardPile() != before);
    }

    @org.junit.Test
    public void recycleFullCenterPile() throws Exception {
        FState state = new FState(2);
        state.recycleFullCenterPile(0);
        assertTrue(state.center[0].size() == 0);
    }

    @org.junit.Test
    public void playToCenter() throws Exception {
        FState state = new FState(2);
        int val = state.players[0].hand.getTopCard();
        state.setNextTurn(0);
        state.playToCenter(0, state.getPlayerState(0).getHand(), 0);
        int[] center = state.getCenterPiles();
        assertTrue(center[0] == val);
        assertTrue(state.center[0].size() == 1);
        assertTrue(state.getPlayerState(0).getHand().size() == 4);
    }

    @org.junit.Test
    public void discard() throws Exception {
        FState state = new FState(2);
        state.setNextTurn(0);
        int val = state.players[0].hand.getTopCard();
        state.discard(0, 1); // from, to
        int[] discards = state.getPlayerState(0).getTopDiscards();
        assertTrue(discards[1] == val);
        assertTrue(state.getPlayerState(0).getHand().size() == 4);
        assertTrue(state.players[0].discards[1].size() == 1);
    }

    @org.junit.Test
    public void flinchAPlayer() throws Exception {

    }

    @org.junit.Test
    public void notStartOfGame() throws Exception {
        FState state = new FState(2);
        assertTrue(state.getStartOfGame());
        state.notStartOfGame();
        assertTrue(!state.getStartOfGame());
    }

    @org.junit.Test
    public void setFlinchable() throws Exception {
        FState test = new FState(2);
        test.setFlinchable(0, false);
        assertTrue(!test.players[0].isFlinchable());
        test.setFlinchable(0, true);
        assertTrue(test.players[0].isFlinchable());

        // try invalid
        boolean before = test.players[0].hasFlinched;
        test.setFlinchable(-1, true);
        assertTrue(before == test.getPlayerState(0).isFlinchable());
        test.setFlinchable(3, false);
        assertTrue(before == test.getPlayerState(0).isFlinchable());
        test.setFlinchable(5, true);
        assertTrue(before == test.getPlayerState(0).isFlinchable());
    }

    @org.junit.Test
    public void copyConstructor() throws Exception {
        FState test = new FState(2);
        FState copy = new FState(test);
        assertTrue(test.equals(copy));
    }

}