package edu.up.cs301.flinch;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import edu.up.cs301.game.R;

/**
 * This is an activity to control the title screen
 *
 *
 * @author Chelle Plaisted
 * @version Dec. 2017
 */

public class FTitleScreenActivity extends Activity implements View.OnClickListener {

    private Button start;
    private Button learn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_title_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(this);

        learn = (Button) findViewById(R.id.learnToPlayButton);
        learn.setOnClickListener(this);
    }


    public void onClick(View v) {
        Intent switchLayout;
        switch(v.getId()){

            case R.id.startButton:
                 switchLayout = new Intent(this, FMainActivity.class);
                startActivity(switchLayout);
                break;
            case R.id.learnToPlayButton:
                switchLayout = new Intent(this, FUserManualActivity.class);
                startActivity(switchLayout);
                break;
        }
    }
}
