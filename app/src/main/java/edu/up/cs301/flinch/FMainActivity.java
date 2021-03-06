package edu.up.cs301.flinch;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.View;
import java.util.ArrayList;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;
import edu.up.cs301.game.title.FTitleScreen;

/**
 * Main activty of flinch game
 *
 * @author Alexa Ruiz, Chelle Plaisted, Rhianna Pinkerton
 * @version Dec. 2017
 */

public class FMainActivity extends GameMainActivity {
    public static final int PORT_NUMBER = 4752;
    private GameConfig defaultConfig; // the default configuration
    private FTitleScreen titleScreen;

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
        playerTypes.add(new GamePlayerType("computer player (smart)") {
            public GamePlayer createPlayer(String name) {
                return new FSmartComputerPlayer(name);
            }
        });

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


    public FTitleScreen createTitleScreen(View v) {

        //lock orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        titleScreen = new FTitleScreen(v);

        return titleScreen;

    }



    public LocalGame createLocalGame() {
        //FLocalGame needs to be created
        return new FLocalGame();
    }
}
