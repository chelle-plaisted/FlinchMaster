package edu.up.cs301.flinch;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.up.cs301.game.R;

/**
 * This is an activity to control the learn to play screen
 *
 * @author Chelle Plaisted
 * @version Dec. 2017
 */

public class FUserManualActivity extends Activity implements View.OnClickListener {

    private Button back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_to_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        back = (Button) findViewById(R.id.goBackButton);
        back.setOnClickListener(this);

    }


    public void onClick(View v) {
        Intent switchLayout;
        switch(v.getId()){

            case R.id.goBackButton:
                switchLayout = new Intent(this, FMainActivity.class);
                startActivity(switchLayout);
                break;
        }
    }
}
