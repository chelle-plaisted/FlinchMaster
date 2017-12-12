package edu.up.cs301.flinch;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;

/**
 * This is an action to flinch another player
 *
 *
 * @author Chelle Plaisted
 * @version Dec. 2017
 */

public class FFlinchAction extends FMoveAction implements Serializable{
    // INSTANCE VARIABLES
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 983542865434931852L;
    private int accusedId; // playerID of the player actor is accusing of flinching

    /**
     * constructor
     * @param p
     *  player who called flinch
     * @param accused
     *  player they called flinch on
     */
    public FFlinchAction(GamePlayer p, int accused) {
        super(p);
        accusedId = accused;
    }

    /**
     * This is a Flinch action
     * @return
     *  states that this is a play type action
     */
    @Override
    public boolean isFlinch() {
        return true;
    }

    /**
     *
     * @return
     *  the ID of the accused player
     */
    public int getAccusedId() { return accusedId; }

}
