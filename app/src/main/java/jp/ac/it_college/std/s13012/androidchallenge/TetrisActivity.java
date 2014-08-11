package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;


public class TetrisActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tetris);
//        setContentView(new Board(this));
        setContentView(new Block(this));

        Log.v("PlayGame", getIntent().getStringExtra("Difficulty"));
    }


}
