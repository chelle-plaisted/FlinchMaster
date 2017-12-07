package edu.up.cs301.flinch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.View;

import edu.up.cs301.game.R;

/**
 * Created by Rachel on 12/7/2017.
 */

public class FTitleScreenActivity extends Activity implements View.OnClickListener {

    private Button start;
    private Button learn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_title_screen);

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
