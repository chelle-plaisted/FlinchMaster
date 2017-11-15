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
     *///TODO finish this
    protected boolean checkForFlinch() {
        /*if(potentialFlinch && alreadyFlinchedThisPlay == false) {
            alreadyFlinchedThisPlay = true;
            numMoves = 0;
            return true;
        }
        alreadyFlinchedThisPlay = false;*/
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
     *///TODO finish this
    @Override
    protected boolean canMove(int playerIdx) {
        if (playerIdx < 0 || playerIdx > 1) {
            // if our player-number is out of range, return false
            return false;
        }

        return false;
    }


    /**
     * makeMove()
     *
     * checks the validity of a move and acts accoridngly
     * @param action
     * 			The move that the player has sent to the game
     * @return
     *///TODO finish this
    @Override
    protected boolean makeMove(GameAction action) {

        // check that we have a move action; if so cast it
        if (!(action instanceof FMoveAction)) {
            return false;
        }
        FMoveAction fma = (FMoveAction) action;

        // get the index of the player making the move; return false
        int thisPlayerIdx = getPlayerIdx(fma.getPlayer());
        if (thisPlayerIdx < 0) { // illegal player
            return false;
        }

        //play action
        if (fma.isPlay()) {
            if (thisPlayerIdx != state.toPlay()) {
                // attempt to play when it's the other player's turn
                return false;
            } else {
                // it's the correct player's turn
                // HOW TO GET WHICH DECK IT'S COMING FROM??
                return false;//change later
            }

        } else if (fma.isDiscard()) {//discard action
            return false; //change later
        } else if (fma.isFlinch()) {//flinch action
            return false;//change later
        }



        return false;
    }

    /**
     * checkIfGameOver()
     *
     * Checks if the game is over
     *
     * Returns: true if game is over and false if it is not
     *///TODO finish this
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
        // if there is no state to send, ignore
        if (state == null) {
            return;
        }

        FState stateForPlayer = new FState(state); // copy of state
        // send the modified copy of the state to the player
        p.sendInfo(stateForPlayer);
    }

    /**
     * isNeedCards()
     *
     * Checks if the players hand is empty and gives them new cards
     *
     * Returns: true if the player needs cards and false if not
     *///TODO finish this
    protected boolean isNeedCards(int playerIdx) {
        if(state.getPlayerState(playerIdx).getHand().size() == 0) {
            //state.getPlayerState(playerIdx).getHand().fillHand(d);
            return false;//remove later
        }

        return false;
    }
}
