package edu.up.cs301.flinch.FStateElements;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Rachel on 11/12/2017.
 */
public class FStateTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

        public void getDeck() throws Exception {

        }

        @org.junit.Test
        public void getCenterPiles() throws Exception {

        }

        @org.junit.Test
        public void getPlayerState() throws Exception {

        }

        @org.junit.Test
        public void getWhoseTurn() throws Exception {

        }

        @org.junit.Test
        public void getStartOfGame() throws Exception {

        }

        @org.junit.Test
        public void toPlay() throws Exception {

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

        }

    }

