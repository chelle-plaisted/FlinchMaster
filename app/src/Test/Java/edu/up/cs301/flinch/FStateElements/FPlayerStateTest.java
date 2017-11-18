package edu.up.cs301.flinch.FStateElements;

import org.junit.Test;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.cardpile.FlinchPile;
import edu.up.cs301.cardpile.Hand;

import static org.junit.Assert.*;

/**
 * Created by Rachel on 11/18/2017.
 */
public class FPlayerStateTest {
    @Test
    public void getHand() throws Exception {
        FState state = new FState(2);
        Hand h = state.getPlayerState(0).getHand();
        assertTrue(h.equals(state.players[0].hand));
    }

    @Test
    public void getTopFlinchCard() throws Exception {
        FState state = new FState(2);
        int val = state.getPlayerState(0).getTopFlinchCard();
        assertTrue(val == state.players[0].flinch.getTopCard());
    }

    @Test
    public void isFlinchEmpty() throws Exception {
        FState state = new FState(2);
        assertTrue(!state.getPlayerState(0).isFlinchEmpty());
        state.setNextTurn(0);
        // play all the cards
        FlinchPile f = new FlinchPile();
        for(int i = 0; i < 10; i++) {
            state.playToCenter(0, f, 0);
        }
        assertTrue(state.getPlayerState(0).isFlinchEmpty());
    }

    @Test
    public void getTopDiscards() throws Exception {
        FState state = new FState(2);
        int[] d = state.getPlayerState(0).getTopDiscards();
        assertTrue(d.length == 5);

        // these should all start at -1
        for (int i = 0; i < d.length; i++) {
            assertTrue(d[i] == -1);
        }

        // discard all my hand cards
        ArrayList<Card> h = (ArrayList<Card>) state.players[0].hand.getCardPile().clone();
        state.setNextTurn(0);
        for(int i = 0; i < 5; i++) {
            state.discard(0, i);
        }

        // check
        d = state.getPlayerState(0).getTopDiscards();
        for(int i = 0; i < 5; i++) {
            assertTrue(h.get(i).getNum() == d[i]);
        }
    }

    @Test
    public void isFlinchable() throws Exception {
        FState state = new FState(2);
        assertTrue(!state.getPlayerState(0).isFlinchable());
        state.setFlinchable(0, true);
        assertTrue(state.getPlayerState(0).isFlinchable());
    }

    @Test
    public void equals() throws Exception {

    }

}