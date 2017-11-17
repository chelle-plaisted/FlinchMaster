package edu.up.cs301.flinch.FStateElements;

import edu.up.cs301.cardpile.Deck;
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
        assertTrue(d.size() == 150);
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
        assertTrue(state.getPlayerState(0).getHand().size() == 0);
        state.setNextTurn(0);
        state.replenishPlayerHand();
        assertTrue(state.getPlayerState(0).getHand().size() == 5);
    }

    @org.junit.Test
    public void recycleFullCenterPile() throws Exception {
        FState state = new FState(2);
        state.recycleFullCenterPile(0);
        assertTrue(state.getCenterPiles()[0] == 0);
    }

    @org.junit.Test
    public void playToCenter() throws Exception {
        FState state = new FState(2);
        state.givePlayerCard(0, 1);
        state.setNextTurn(0);
        state.playToCenter(0, state.getPlayerState(0).getHand(), 1);
        assertTrue(state.getCenterPiles()[0] == 1);
        assertTrue(state.getPlayerState(0).getHand().size() == 0);
    }

    @org.junit.Test
    public void discard() throws Exception {
        FState state = new FState(2);
        state.givePlayerCard(0, 1);
        state.setNextTurn(0);
        state.discard(0, 1);
        assertTrue(state.getCenterPiles()[0] == 1);
        assertTrue(state.getPlayerState(0).getHand().size() == 0);
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
        FState before = new FState(test);
        test.setFlinchable(-1, true);
        assertTrue(before.equals(test));
        test.setFlinchable(3, false);
        assertTrue(before.equals(test));
        test.setFlinchable(5, true);
        assertTrue(before.equals(test));
    }

    @org.junit.Test
    public void copyConstructor() throws Exception {
        FState test = new FState(2);
        FState copy = new FState(test);
        assertTrue(test.equals(copy));
    }

}