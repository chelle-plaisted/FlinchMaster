package edu.up.cs301.flinch;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * This outlines a general flinch game action
 *
 *
 * @author Chelle Plaisted
 * @version Dec. 2017
 */

public abstract class FMoveAction extends GameAction implements Serializable{

    private static final long serialVersionUID = -3107100271012188849L;

    /**
     * Constructor for SJMoveAction
     *
     * @param player the player making the move
     */
    public FMoveAction(GamePlayer player)
    {
        // invoke superclass constructor to set source
        super(player);
    }

    /**
     * @return
     * 		whether the move was a discard
     */
    public boolean isDiscard() {
        return false;
    }

    /**
     * @return
     * 		whether the move was a "play"
     */
    public boolean isPlay() {
        return false;
    }

    /**
     * @return
     * 		whether the move was a Flinch
     */
    public boolean isFlinch() {
        return false;
    }
}