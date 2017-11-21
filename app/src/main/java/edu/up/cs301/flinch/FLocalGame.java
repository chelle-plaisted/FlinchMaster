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
    private FState state; //state of game
    private int numMoves; //number of moves since canBeFlinched was set to true TODO what to do with this?
    private boolean flinchPotential; //whether the current player is at risk of Flinching themselves
    private boolean alreadyFlinchedThisPlay; //whether current player has already been flinched this play of a single card
    private int numPlayers; // the number of players in the game

    /**
     * Constructor to set up the number of players
     * @param num
     *          Number of players
     *
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

     */
    protected void checkForFlinch() {
       //flinch card
        int topCard = state.getPlayerState(state.getWhoseTurn()).getTopFlinchCard();
        //compare each card in center pile
        for (int j : state.getCenterPiles()) {
            if (j+1 == topCard) {
                // they could possibly flinch themselves
                flinchPotential = true;
                numMoves = 0;
            } else {
                flinchPotential = false;
            }
        }
    }

    /**
     * canMove()
     *
     * Checks whether the current player can move
     *
     * @return
     *          true if player can move and false if they can't
     *
     * @param playerIdx
     *          Index of current player
     *
     */
    @Override
    protected boolean canMove(int playerIdx) {
        // if our player-number is out of range, return false
        if (playerIdx < 0 || playerIdx > numPlayers) {
            return false;
        } else if(playerIdx == state.getWhoseTurn()) {// if this is current player return true
            return true;
        }
        return false;
    }


    /**
     * makeMove()
     *
     * checks the validity of a move and acts accordingly
     * @param action
     * 			The move that the player has sent to the game
     * @return
     *          Return true if the move was made and false if the move can't be made
     *///TODO finish- basics done
    @Override
    protected boolean makeMove(GameAction action) {

        // check that we have a move action; if so cast it
        if (!(action instanceof FMoveAction)) {
            return false;
        }
        FMoveAction fma = (FMoveAction) action;

        // get the index of the player making the move; return false
        int thisPlayerIdx = getPlayerIdx(fma.getPlayer());
        /*if (thisPlayerIdx < 0) { // illegal player
            return false;
        }*/ //^^don't think we need this

        //play action
        if (fma.isPlay()) {
            // attempt to play when it's the other player's turn
            if (!canMove(thisPlayerIdx)) {
                return false;
            } else {
                // it's the correct player's turn
                // cast to a play action
                FPlayAction fpa = (FPlayAction) fma;
                // variables to find which pile card is coming from
                boolean flinch = false, hand = false, discard = false;
                // card from flinchPile
                if (fpa.getCardPile() instanceof FlinchPile) {
                    flinch = true;
                } else if(fpa.getCardPile() instanceof Hand) { // card from hand
                    hand = true;
                } else  { //card from discard pile
                    discard = true;
                }

                // from flinch hand
                if (flinch) {
                    //get top flinch card
                    int topCard = state.getPlayerState(state.getWhoseTurn()).getTopFlinchCard();
                    // if flinch card can be played to center pile
                    if(state.getCenterPiles()[fpa.getIndexTo()]+1 == topCard) {
                        //play to this pile
                        state.playToCenter(topCard, fpa.getCardPile(), fpa.getIndexTo());
                        return true; //move was completed
                    }

                } else if (hand || discard) { //from hand
                    // card from hand
                    int card = fpa.getIndexFrom();
                    // compare card to center piles
                    if(!state.isStartOfGame) {
                        if (state.getCenterPiles()[fpa.getIndexTo()] + 1 == card) {
                            //play to this pile
                            state.playToCenter(card, fpa.getCardPile(), fpa.getIndexTo());
                            //check if they can be flinched now
                            checkForFlinch();
                            //check if hand is now empty
                            isNeedCards();//method checks for us
                            return true;//move was completed
                        }
                    } else {//TODO check if first turn
                        //can only play a one
                        if(card != 1) {
                            //discard hand
                            /*Hand handFT = state.getPlayerState(thisPlayerIdx).getHand();
                            for (int d : handFT) {
                                //discard the card
                                state.discard(card, );
                                return true;
                            }*/ //TODO how to iterate through a hand???
                        } else {
                            //state.isStartOfGame = false; ????? TODO which class changes isStartOfGame?
                            return true;
                        }
                    }
                }

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

                    //TODO: TESTING
                    alreadyFlinchedThisPlay = false;
                }
                return false;// move wasn't made
            }
        } else if (fma.isDiscard()) {//discard action
            // attempt to play when it's the other player's turn
            if (!canMove(thisPlayerIdx)) {
                return false;
            } else {
                // make a discard action
                FDiscardAction fda = (FDiscardAction) fma;
                // card from discard pile
                int cardIdx = fda.getIndexFrom();
                // get the discards
                    // if any are blank--then we can only discard to a blank one
                boolean blanks = false;
                int[] d = state.getPlayerState(thisPlayerIdx).getTopDiscards();
                for(int card : d) {
                    if(card == -1) {
                        blanks = true;
                        break;
                    }
                }
                if(blanks) {
                    if(d[fda.getIndexTo()] == -1) {
                        // the space was blank
                        //discard the card
                        state.discard(cardIdx, fda.getIndexTo());
                        return true;//move was completed
                    } else {
                        // space not blank, invalid discard
                        return false;
                    }
                } else
                {
                    //discard the card
                    state.discard(cardIdx, fda.getIndexTo());
                    return true;//move was completed
                }
            }
        } else if (fma.isFlinch()) {//flinch action
            // attempt to play when it's the other player's turn
            if (!canMove(thisPlayerIdx)) {
                return false;
            } else {
                // it's the correct player's turn
                // cast to a play action
                FFlinchAction ffa = (FFlinchAction) fma;
                //check if player was flinched already
                if(alreadyFlinchedThisPlay) {
                    return false; //player has been flinched already
                } else {
                    //flinch the player
                    state.flinchAPlayer(thisPlayerIdx);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checkIfGameOver()
     *
     * Checks if the game is over
     *
     * @return
     *         message if game is over and null if it is not
     */
    protected String checkIfGameOver() {
        //iterate through each player
        for(int i = 0; i < numPlayers; i++) {
           //if their flinch pile is empty
            if (state.getPlayerState(i).isFlinchEmpty()) {
                return "Game Over"; //game is over
            } else return null; //game is not over
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
     * @return
     *          true if the player needs cards and false if not
     */
    protected boolean isNeedCards() {
        //get player's hand
        Hand h = state.getPlayerState(state.getWhoseTurn()).getHand();
        // if hand doesn't exist
        if (h == null) {
            return false;
        }
        //if hand is empty
        if(h.size() == 0) {
            //add cards
            state.replenishPlayerHand();
            return true;
        }
        return false;
    }
}
