package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class TetrisActivity extends Activity{
    int fallSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Block(this));

        String difficulty = getIntent().getStringExtra("Difficulty");
        if (difficulty.equals("EASY")) {
            fallSpeed = 3;

        } else if (difficulty.equals("NORMAL")) {
            fallSpeed = 5;

        } else if (difficulty.equals("HARD")) {
            fallSpeed = 8;

        }

        Log.v("Difficulty", getIntent().getStringExtra("Difficulty"));
        Log.v("FallSpeed", Integer.toString(fallSpeed));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Block.finishLoop();
    }
}