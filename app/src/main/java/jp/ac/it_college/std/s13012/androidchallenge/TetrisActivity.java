package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class TetrisActivity extends Activity {
    int fallSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        /*String difficulty = getIntent().getStringExtra("Difficulty");
        switch (difficulty){
            case "EASY":
                fallSpeed = 3;
                break;
            case "NORMAL":
                fallSpeed = 5;
                break;
            case "HARD":
                fallSpeed = 8;
                break;
        }*/

        Log.v("Difficulty", getIntent().getStringExtra("Difficulty"));
        Log.v("FallSpeed", Integer.toString(fallSpeed));
    }


}