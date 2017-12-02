package edu.up.cs301.flinch;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;

/**
 * Created by Rachel on 11/7/2017.
 */

public class FFlinchAction extends FMoveAction implements Serializable{


    public FFlinchAction(GamePlayer p) {
        super(p);
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

}
