package edu.up.cs301.flinch;

import android.content.pm.ActivityInfo;
import android.graphics.Color;

import java.util.ArrayList;

import edu.up.cs301.flinch.FComputerPlayer;
import edu.up.cs301.flinch.FHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;
import edu.up.cs301.game.title.FTitleScreen;

/**
 * Created by alexaruiz on 11/6/17.
 */

public class FMainActivity extends GameMainActivity {
    public static final int PORT_NUMBER = 4752;
    private GameConfig defaultConfig; // the default configuration

    public GameConfig createDefaultConfig() {
        //lock orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        playerTypes.add(new GamePlayerType("human player (Blue)") {
            public GamePlayer createPlayer(String name) {
                return new FHumanPlayer(name, Color.rgb(204, 229, 255));
            }});
        playerTypes.add(new GamePlayerType("human player (White)") {
            public GamePlayer createPlayer(String name) {
                return new FHumanPlayer(name, Color.WHITE);
            }
        });
        playerTypes.add(new GamePlayerType("computer player (normal)") {
            public GamePlayer createPlayer(String name) {
                return new FDumbComputerPlayer(name);
            }
        });
   /*     playerTypes.add(new GamePlayerType("computer player (fast)") {
            public GamePlayer createPlayer(String name) {
                return new FComputerPlayer(name, 0.3);
            }
        });
        playerTypes.add(new GamePlayerType("computer player (slow)") {
            public GamePlayer createPlayer(String name) {
                return new FComputerPlayer(name, 1.0);
            }
        });
        playerTypes.add(new GamePlayerType("computer player (very fast)") {
            public GamePlayer createPlayer(String name) {
                return new FComputerPlayer(name, 0.15);
            }
        });
        playerTypes.add(new GamePlayerType("computer player (very slow)") {
            public GamePlayer createPlayer(String name) {
                return new FComputerPlayer(name, 3.5);
            }
        });
        */

        // Create a game configuration class for Flinch
        defaultConfig = new GameConfig(playerTypes, 2, 4, "Flinch", PORT_NUMBER);

        // Add the default players
        defaultConfig.addPlayer("Human", 0);
        defaultConfig.addPlayer("Computer", 2);

        // Set the initial information for the remote player
        defaultConfig.setRemoteData("Guest", "", 1);

        //done!
        return defaultConfig;
    }//createDefaultConfig

    public FTitleScreen createTitleScreen() {
        return null;
    }


    public LocalGame createLocalGame() {
        //FLocalGame needs to be created
        return new FLocalGame(defaultConfig.getNumPlayers());
    }
}
