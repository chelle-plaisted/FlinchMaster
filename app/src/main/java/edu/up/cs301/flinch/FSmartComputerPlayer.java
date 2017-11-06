package edu.up.cs301.flinch;

import edu.up.cs301.flinch.FStateElements.FState;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * Created by rae-o on 11/5/2017.
 */

public class FSmartComputerPlayer extends FComputerPlayer {
    public FSmartComputerPlayer(String name) {
        super(name);
    }

    /**
     * Invoked whenever the player's timer has ticked. It is expected
     * that this will be overridden in smart players.
     */
    @Override
    protected void timerTicked() {
        super.timerTicked();
    }

    /**
     * callback method, called when we receive a message, typicallly from
     * the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        super.receiveInfo(info);
    }

}
