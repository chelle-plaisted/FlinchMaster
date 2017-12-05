package edu.up.cs301.flinch;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;

/**
 * Created by Rachel on 11/7/2017.
 */

public class FFlinchAction extends FMoveAction implements Serializable{


    private int accusedId;
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
     */
    public int getAccusedId() { return accusedId; }

}
