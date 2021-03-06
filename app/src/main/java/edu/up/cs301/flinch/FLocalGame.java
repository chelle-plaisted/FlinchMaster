package edu.up.cs301.flinch;

import edu.up.cs301.cardpile.FlinchPile;
import edu.up.cs301.cardpile.Hand;
import edu.up.cs301.flinch.FStateElements.* ;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.util.MessageBox;

/**
 * The LocalGame class for a Flinch game.  Defines and enforces
 * the game rules; handles interactions between players.
 *
 * @author Rhianna Pinkerton
 * @version Dec. 2017
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
     *
     */
    public FLocalGame() {
     //   numPlayers = this.players.length;
        numMoves = 0;
        flinchPotential = false;
       // state = new FState(numPlayers);
        alreadyFlinchedThisPlay = false;
    }

    /**
     * start()
     *
     * Starts  the game
     *
     * @param players
     */
    @Override
    public void start(GamePlayer[] players) {
        super.start(players);
        numPlayers = this.players.length;
        state = new FState(numPlayers);
    }

     /**
     * setFlinchPotential()
     *
     * Checks whether the current player has a card in the flinch pile that could be played

     */
    protected void setFlinchPotential() {
       //flinch card
        int topCard = state.getPlayerState(state.getWhoseTurn()).getTopFlinchCard();
        //compare each card in center pile
        flinchPotential = false;
        //iterate through center piles
        for (int j : state.getCenterPiles()) {
            if (j+1 == topCard || topCard == 1) {
                // they could possibly flinch themselves
                flinchPotential = true;
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
     * Checks the validity of a move and acts accordingly
     *
     * @param action
     * 			The move that the player has sent to the game
     * @return
     *          Return true if the move was made and false if the move can't be made
     */
    @Override
    protected synchronized boolean makeMove(GameAction action) {

        // check that we have a move action; if so cast it
        if (!(action instanceof FMoveAction)) {
            return false;
        }
        FMoveAction fma = (FMoveAction) action;

        // retire any old flinch messages
        state.retireFlinchMessage();
        // get the index of the player making the move; return false
        int thisPlayerIdx = getPlayerIdx(fma.getPlayer());


        if (fma.isFlinch()) {//flinch action
            // it's the correct player's turn
            // cast to a play action
            FFlinchAction ffa = (FFlinchAction) fma;

            //check if player was flinched already
            if(alreadyFlinchedThisPlay) {
                return false; //player has been flinched already
            } else {
                // is the flinch correct?
                //has the player played this turn
                if(state.getPlayerState(thisPlayerIdx).hasPlayedThisTurn()) {
                    // the player has played this turn, the flinch action applies to the current player
                    return makeFlinch(ffa.getAccusedId(), thisPlayerIdx, false);
               } // the player has not palyed this turn, the flinch action applies to the previous player
                else {
                    return makeFlinch(ffa.getAccusedId(), thisPlayerIdx,true);
                }
            }
        }
        //play action
        else if (fma.isPlay()) {
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
            } // end card searching
            // from flinch hand
            if (flinch) {
                //get top flinch card
                int topCard = state.getPlayerState(thisPlayerIdx).getTopFlinchCard();

                //check if index in in bounds
                if(fpa.getIndexTo() >= 0 && fpa.getIndexTo() <= 9) {
                    // if flinch card can be played to center pile
                    if (state.getCenterPiles()[fpa.getIndexTo()] + 1 == topCard ||
                            (topCard == 1 && state.getCenterPiles()[fpa.getIndexTo()] == -1)) {
                        //play to this pile
                        state.playToCenter(topCard, fpa.getCardPile(), fpa.getIndexTo());
                        // if the player played a one at the start of the game: no longer start of game
                        if (state.isStartOfGame && topCard == 1) {
                            state.notStartOfGame();
                        }
                        // if I have flinched myself but correct it by playing the flinch card, that flinch is now invalid
                        if (state.getPlayerState(thisPlayerIdx).isFlinchable()) {
                            state.setFlinchable(thisPlayerIdx, false);
                        }
                        // Now that the player has played a card, check if the player has the risk for flinch
                        setFlinchPotential();
                        // this player has played this turn
                        state.playedThisTurn(thisPlayerIdx, true);

                        //check if center pile is full
                        recycleCards();
                        alreadyFlinchedThisPlay = false;
                        return true; //move was completed
                    }
                }

            } else if (hand || discard) {
                int card;
                // card from hand
                if (hand) {
                    card = state.getPlayerState(thisPlayerIdx).getHand().getCardAt(fpa.getIndexFrom());
                } else {
                    // card from discard
                    card = state.getPlayerState(thisPlayerIdx).getTopDiscards()[fpa.getIndexFrom()];
                }
                // compare card to center piles
                if (!state.isStartOfGame) {
                    // is the move valid
                    int[] center = state.getCenterPiles();
                    if (center[fpa.getIndexTo()] + 1 == card || (card == 1 && state.getCenterPiles()[fpa.getIndexTo()] == -1)) {
                        //play to this pile
                        state.playToCenter(fpa.getIndexFrom(), fpa.getCardPile(), fpa.getIndexTo());

                         // do I have an active flinch?
                        if(state.getPlayerState(thisPlayerIdx).isFlinchable()) {
                            if(numMoves >= 4) {
                                // this Flinch is too old
                                numMoves = 0;
                                state.setFlinchable(thisPlayerIdx, false);
                            } else {
                                numMoves++;
                            }
                        } // otherwise : did I flinch myself that turn?
                        else if(flinchPotential) {
                            // I flinched myself: notify state and start tracking my moves
                            numMoves = 1;
                            state.setFlinchable(thisPlayerIdx, true);

                        }
                        // Now that the player has played a card, check if the player has the risk for flinch
                        setFlinchPotential();

                        // this player has played this turn
                        state.playedThisTurn(thisPlayerIdx, true);


                        //check if hand is now empty
                        isNeedCards();//method checks for us
                        //check if center pile is full
                        recycleCards();
                        alreadyFlinchedThisPlay = false;
                        return true;//move was completed
                    }
                } else {
                    //can only play a one
                    if (card != 1) {
                        return false;

                    } else {
                        // no longer the start of the game
                        state.isStartOfGame = false;
                        // play the one
                        state.playToCenter(fpa.getIndexFrom(), fpa.getCardPile(), fpa.getIndexTo());

                        // did I flinch myself that turn?
                        if(flinchPotential) {
                            // I flinched myself: notify state and start tracking my moves
                            numMoves = 0;
                            state.setFlinchable(thisPlayerIdx, true);
                        }
                        // Now that the player has played a card, check if the player has the risk for flinch
                        setFlinchPotential();

                        // this player has played this turn
                        state.playedThisTurn(thisPlayerIdx, true);

                        //check if center pile is full
                        recycleCards();
                        alreadyFlinchedThisPlay = false;
                        return true;
                    }
                }
            }

                //check if center pile is full
                recycleCards();
                return false;// move wasn't made
        } else if (fma.isDiscard()) {//discard action
            boolean invalidDis = false;
            //if this is the start of the game
            if(state.isStartOfGame) {
                //go through center piles
                for (int i = 0; i < state.getPlayerState(thisPlayerIdx).getHand().size(); i++) {
                    //go through cards in hand and search for 1's
                    if(state.getPlayerState(thisPlayerIdx).getHand().getCardAt(i) == 1) {
                        //if there is a one then this is an invalid discard action
                        //check if center pile is full
                        recycleCards();
                        return false;
                    }
                }
            }
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
        //check if center pile is full
        recycleCards();

        return false;
    }

    /**
     * makeFlinch()
     *
     * Flinches a player
     *
     * @param id_current
     *      Current player's ID
     * @param id_accuse
     *      ID of the player accused
     * @param discardFlinch
     *      Boolean to tell if the flinch card was discarded
     * @return
     *      Return true if the player was flinched
     */
    private boolean makeFlinch(int id_current, int id_accuse, boolean discardFlinch) {

        if(state.getPlayerState(id_current).isFlinchable()) {
            //flinch the player
            state.flinchAPlayer(id_current, id_accuse, 0);
            alreadyFlinchedThisPlay = true;
            state.setFlinchable(id_current, false);
            if(!discardFlinch) {
                // make it the next player's turn
                setUpNextTurn(id_current);
            }

            return true;
        } else {
            // invalid flinch attempt
            state.flinchAPlayer(id_accuse, id_current, 1);
            return true;
        }
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
                return "Game Over: " + playerNames[i] +" won the game."; //game is over
            }
        }

        // or is the deck empty?
        if(state.getDeck().size() <= 0) {
            return "Game Over: Ran out of cards.";
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
//        state.retireFlinchMessage();
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
     * Discards a card & handles the start of the next player's turn
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

        // DID THE PLAYER FLINCH SELF BY DISCARDING
        if(flinchPotential) {
            // player flinched self
            state.setFlinchable(thisPlayerIdx, true);
        }

        setUpNextTurn(thisPlayerIdx);

    }

    /**
     * setUpNextTurn()
     *
     * Sets up the next player for their turn
     *
     * @param thisPlayerIdx
     *      Current player's index
     */
    private void setUpNextTurn(int thisPlayerIdx) {

        // NEXT PLAYER'S TURN
        // increment the play

        thisPlayerIdx++;
        //if the player idx is invalid
        if(thisPlayerIdx >= numPlayers) {
            thisPlayerIdx = 0;
        }
        state.setNextTurn(thisPlayerIdx);
        // give the player more cards if needed
        isNeedCards();
        // does the player start out with a potential Flinch risk
        setFlinchPotential();
        // this player cannot flinch selves yet
        state.setFlinchable(thisPlayerIdx, false);
        // this player hasn't played yet this turn
        state.playedThisTurn(thisPlayerIdx, false);
        alreadyFlinchedThisPlay = false;
    }

    /**
     * recycleCards()
     *
     * Checks if a center pile is full and recycles that pile
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
    } // end recycle cards
}
