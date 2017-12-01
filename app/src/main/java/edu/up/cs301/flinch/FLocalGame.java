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
    private int numMoves; //number of moves since canBeFlinched was set to true
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
     * note--will it let us do a Flinch action since its not this player's turn
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
                // NOTE: I think the below if(flinch) and if(hand || discard) structures could be consolidated TODO: fix
                // from flinch hand
                if (flinch) {
                    //get top flinch card
                    int topCard = state.getPlayerState(state.getWhoseTurn()).getTopFlinchCard();
                    // if flinch card can be played to center pile
                    if(state.getCenterPiles()[fpa.getIndexTo()]+1 == topCard || (topCard == 1 && state.getCenterPiles()[fpa.getIndexTo()] == -1)) {
                        //play to this pile
                        state.playToCenter(topCard, fpa.getCardPile(), fpa.getIndexTo());

                        // NOTE THE BELOW CODE DOES NOT WORK--IT WON'T BE REACHED CORRECTLY TODO: FIX
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
                            alreadyFlinchedThisPlay = false;
                        }

                        //check if center pile is full
                        recycleCards();
                        return true; //move was completed
                    }

                } else if (hand || discard) { //from hand
                    int card;
                    // card from hand
                    if(hand) {
                        card = state.getPlayerState(thisPlayerIdx).getHand().getCardAt(fpa.getIndexFrom());
                    } else  {
                        // card from discard
                        card = state.getPlayerState(thisPlayerIdx).getTopDiscards()[fpa.getIndexFrom()];
                    }
                    // compare card to center piles
                   if(!state.isStartOfGame) {
                       // is the move valid
                       int[] center = state.getCenterPiles();
                        if (center[fpa.getIndexTo()] + 1 == card || (card == 1 && state.getCenterPiles()[fpa.getIndexTo()] == -1)) {
                            //play to this pile
                            state.playToCenter(fpa.getIndexFrom(), fpa.getCardPile(), fpa.getIndexTo());

                            // NOTE THE BELOW CODE DOES NOT WORK--IT WON'T BE REACHED CORRECTLY TODO: FIX
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
                                alreadyFlinchedThisPlay = false;
                            }

                            //check if they can be flinched now
                            checkForFlinch();
                            //check if hand is now empty
                            isNeedCards();//method checks for us
                            //check if center pile is full
                            recycleCards();
                            return true;//move was completed
                        }
                    } else {
                        //can only play a one
                        if( card != 1) {
                            return false;

                        } else {
                            // no longer the start of the game
                            //state.notStartOfGame();
                            state.isStartOfGame = false;
                            // play the one
                            state.playToCenter(fpa.getIndexFrom(), fpa.getCardPile(), fpa.getIndexTo());

                            // NOTE THE BELOW CODE DOES NOT WORK--IT WON'T BE REACHED CORRECTLY TODO: FIX
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
                                alreadyFlinchedThisPlay = false;
                            }
                            
                            //check if center pile is full
                            recycleCards();
                            return true;
                        }
                    }
                }

                //check if center pile is full
                recycleCards();
                return false;// move wasn't made
            }
        } else if (fma.isDiscard()) {//discard action
            boolean invalidDis = false;
            // attempt to play when it's the other player's turn
            if (!canMove(thisPlayerIdx)) {
                return false;
            } else {
                //if this is the start of the game
                if(state.isStartOfGame) {
                    //go through center piles
                    for (int i = 0; i < state.getPlayerState(thisPlayerIdx).getHand().size(); i++) {
                        //go through cards in hand and search for 1's
                        if(state.getPlayerState(thisPlayerIdx).getHand().getCardAt(i) == 1) {
                            //if there is a one then this is an invalid discard action
                            invalidDis = true;
                            //check if center pile is full
                            recycleCards();
                            return false;
                        }
                    }
                }
                if (invalidDis == false) {
                    // make a discard action
                    FDiscardAction fda = (FDiscardAction) fma;
                    // card from hand
                    int cardIdx = fda.getIndexFrom();
                    // get the discards
                    // if any are blank--then we can only discard to a blank one
                    boolean blanks = false;
                    int[] d = state.getPlayerState(thisPlayerIdx).getTopDiscards();
                    for (int card : d) {
                        if (card == -1) {
                            blanks = true;
                            break;
                        }
                    }
                    if (blanks) {
                        if (d[fda.getIndexTo()] == -1) {
                            // the space was blank
                            //discard the card
                            discard(cardIdx, thisPlayerIdx, fda);
                            //check if center pile is full
                            recycleCards();
                            return true;//move was completed
                        } else {
                            // space not blank, invalid discard
                            //check if center pile is full
                            recycleCards();
                            return false;
                        }
                    } else {
                        //discard the card
                        discard(cardIdx, thisPlayerIdx, fda);
                        //check if center pile is full
                        recycleCards();
                        return true;//move was completed
                    }
                }
                invalidDis = false; //
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
        //check if center pile is full
        recycleCards();

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

    /**
     * discard()
     *
     * Discards a card
     *
     * @param cardIdx
     * @param thisPlayerIdx
     * @param fda
     */
    private void discard(int cardIdx, int thisPlayerIdx, FDiscardAction fda) {
        //get the index to
        int idxTo = fda.getIndexTo();
        //discard card
        state.discard(cardIdx, idxTo);
        //if it is the first turn
        if(state.isStartOfGame) {
            // discard all cards
            while(state.getPlayerState(thisPlayerIdx).getHand().size() != 0) {
                idxTo++;
                if(idxTo >= 5) {
                    idxTo = 0;
                }
                state.discard(0, idxTo);
            }
        }
        // do we need more cards?
        isNeedCards();
        // increment the play?
        thisPlayerIdx++;
        if(thisPlayerIdx >= numPlayers) {
            thisPlayerIdx = 0;
        }
        state.setNextTurn(thisPlayerIdx);
    }

    /**
     * recycleCards()
     *
     * Checks if a center pile is full and recyles that pile
     *
     */
    private void recycleCards() {
        //put center piles in array
        int arr[] = state.getCenterPiles();
        //go through center piles
        for(int i = 0; i < state.getCenterPiles().length; i++) {
            //if the top card is 15
            if(arr[i] == 15) {
                //recycle pile
                state.recycleFullCenterPile(i);
            }
        }
    }
}
