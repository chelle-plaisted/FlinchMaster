package edu.up.cs301.flinch;

import edu.up.cs301.flinch.FStateElements.* ;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by pinkertr20 on 11/7/2017.
 */

public class FLocalGame extends LocalGame{
    //instance variables
    FState state; //state of game UNCOMMENT ONCE FSTATE IS MADE
    int numMoves; //number of moves since canBeFlinched was set to true
    boolean potentialFlinch; //whether the current player is at risk of Flinching themselves
    boolean alreadyFlinchedThisPlay; //whether current player has already been flinched this play of a single card

    /**
     * checkForFlinch()
     *
     * Checks whether the current player can be Flinched
     *
     * Returns: true if player can be Flinched, else false.
     */
    protected boolean checkForFlinch() {
        return false;
    }

    /**
     * canMove()
     *
     * Checks whether the current player can move
     *
     * Returns: true if player can move and false if they can't
     *
     * @param playerIdx
     *          Index of current player
     *
     */
    @Override
    protected boolean canMove(int playerIdx) {
        return false;
    }


    /**
     * makeMove()
     *
     * checks the validity of a move and acts accoridngly
     * @param action
     * 			The move that the player has sent to the game
     * @return
     */
    @Override
    protected boolean makeMove(GameAction action) {
        return false;
    }

    /**
     * checkIfGameOver()
     *
     * Checks if the game is over
     *
     * Returns: true if game is over and false if it is not
     */
    protected String checkIfGameOver() {
        return null;
    }

    /**
     * sendUpdatedStateTo()
     *
     * Sends the updated state to the given player
     *
     * @param p
     *          Given player
     */
    protected void sendUpdatedStateTo(GamePlayer p) {

    }

    /**
     * isNeedCards()
     *
     * Checks if the players hand is empty and gives them new cards
     *
     * Returns: true if the player needs cards and false if not
     */
    protected boolean isNeedCards() {
        return false;
    }
}
