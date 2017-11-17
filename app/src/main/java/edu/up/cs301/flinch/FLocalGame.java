package edu.up.cs301.flinch;

import edu.up.cs301.cardpile.FlinchPile;
import edu.up.cs301.cardpile.Hand;
import edu.up.cs301.flinch.FStateElements.* ;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by pinkertr20 on 11/7/2017.
 */

/*
NOTE: TEST CASE THAT MIGHT FAIL WITH CURRENT DESIGN
    A player fails to play from their flinch pile (flinches themselves) but plays that Flinch card NEXT
        Since only two plays have gone by, if someone calls a Flinch action--it SHOULD be valid...but it might accidentally be reset

 */

public class FLocalGame extends LocalGame{
    //instance variables
    private FState state; //state of game UNCOMMENT ONCE FSTATE IS MADE
    private int numMoves; //number of moves since canBeFlinched was set to true
    private boolean flinchPotential; //whether the current player is at risk of Flinching themselves
    private boolean alreadyFlinchedThisPlay; //whether current player has already been flinched this play of a single card
    private int numPlayers; // the number of players in the game

    /**
     * Constructor to set up the number of players
     * @param num
     */
    public FLocalGame(int num) {
        numPlayers = num;
        numMoves = 0;
        flinchPotential = false;
        state = new FState(numPlayers);
        alreadyFlinchedThisPlay = false;
    }
     /**
     * checkForFlinch()
     *
     * Checks whether the current player can be Flinched

     *///TODO finish this
    protected void checkForFlinch() {
       /* if(flinchPotential && alreadyFlinchedThisPlay == false) {
            alreadyFlinchedThisPlay = true;
            numMoves = 0;
            return true;
        }
        alreadyFlinchedThisPlay = false;
        return false;
        */
       // is the top card of flinch pile playable? (see FComputerPlayer isPlayable for <code></code>
            /* if( ... ) {
                    // they could possibly flinch themselves
                    flinchPotential = true;
                    numMoves = 0;
               } else {
                    flinchPotential = false;
               }
             */
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
     *///TODO comment stuff
    @Override
    protected boolean canMove(int playerIdx) {
        if (playerIdx < 0 || playerIdx > numPlayers) {
            // if our player-number is out of range, return false
            return false;
        }
        if(playerIdx == state.getWhoseTurn()) {
            return true;
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
                FPlayAction fpa = (FPlayAction) fma;
                boolean flinch = false, hand = false, discard = false;
                //HOW YOU KNOW WHICH DECK ITS COMING FROM
                if (fpa.getCardPile() instanceof FlinchPile) {
                    flinch = true;
                } else if(fpa.getCardPile() instanceof Hand) {
                    hand = true;
                } else  {
                    discard = true;
                }
                // is the play legal?
                    // get the card from the correct location to check
                    // check if first turn rules apply

                    // it is the first turn--only ones are valid
                        // a One was played, it is no longer the first turn




                 // this is a legal move--remove the card from the players correct cardPile and play to center

                // did the player Flinch themselves?
                if(!flinchPotential) {
                    // check
                    checkForFlinch();
                } else {
                    // they already had the potential to Flinch themselves
                    if(flinch) {
                        // they played from their Flinch pile, so might not have Flinched
                        checkForFlinch();
                    } else {
                        // they have officially Flinched themselves
                        numMoves++;
                        // too many moves have gone by: the flinch is invalid
                        if(numMoves > 2) {
                            state.setFlinchable(state.getWhoseTurn(), false );
                            checkForFlinch();
                        } else {
                            state.setFlinchable(state.getWhoseTurn(), true);
                        }
                    }

                    //TODO: CHANGE RETURN VALUE, CHECK THE PLAYER'S HAND FOR BEING EMPTY, ALREADY FLINCHED THIS PLAY STUFF, TESTING
                }

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
        // for loop
        // check all player's flinch piles
        // if one is empty.. return true
        // else return false
        for(int i = 0; i < state.numPlayers; i++) {
            return null;
        }

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
     *///TODO comment stuff
    protected boolean isNeedCards() {
        if(state.getPlayerState(state.getWhoseTurn()).getHand().size() == 0) {
            state.getPlayerState(state.getWhoseTurn()).getHand().fillHand(state.getDeck());
            return true;
        }

        return false;
    }
}
